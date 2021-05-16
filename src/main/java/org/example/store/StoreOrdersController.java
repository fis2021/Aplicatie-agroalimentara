package org.example.store;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.database.Database;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.services.StoreService;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.util.Objects;

public class StoreOrdersController {
    @FXML
    private TableView<OrderStatus> ordSt;
    @FXML
    private TableColumn<OrderStatus, String> order;
    @FXML
    private TableColumn<OrderStatus,String> accRej;  //accept/reject
    @FXML
    private TextField  filterField;
    private final ObservableList<OrderStatus> statList= FXCollections.observableArrayList();

    public void initialize(){
        order.setCellValueFactory(new PropertyValueFactory<>("Order"));
        // TODO: Filter by store.
        for(OrderStatus or: Database.statuses.find()){
            if(Objects.equals(or.getStatus(),"Pending")) {
                statList.add(or);
            }
        }

        FilteredList<OrderStatus> filteredData = new FilteredList<>(statList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(info -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return info.getOrder().getShopName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<OrderStatus> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(ordSt.comparatorProperty());

        ordSt.setItems(sortedData);

        Callback<TableColumn<OrderStatus,String>,TableCell<OrderStatus,String>> cellFactory2=(param) -> {
            final TableCell<OrderStatus,String> cell2 = new TableCell<OrderStatus,String>(){

                @Override
                public void updateItem(String item,boolean empty){
                    super.updateItem(item,empty);

                    if(empty)
                    {
                        setGraphic(null);
                        setText(null);
                    }else
                    {
                        final ChoiceBox selectStatus = new ChoiceBox();
                        selectStatus.getItems().add("Accept");
                        selectStatus.getItems().add("Reject");
                        selectStatus.setOnAction(event ->{
                            final String acceptReject=(String)selectStatus.getValue();
                            final Order order = ordSt.getItems().get(getIndex()).getOrder();
                            StoreService.updateOrderStatus(order, acceptReject);
                        });

                        setGraphic(selectStatus);
                        setText(null);

                    }
                }
            };

            return cell2;

        };
        accRej.setCellFactory(cellFactory2);
    }
    public static void viewOrdersPanel() throws IOException {
        Parent viewOrdersWindow = FXMLLoader.load(EditProductController.class.getResource("/org/example/storeOrder.fxml"));
        Scene viewOrdersScene = new Scene(viewOrdersWindow);

        Stage window = new Stage();

        window.setScene(viewOrdersScene);
        window.setTitle("View Orders Panel");
        window.show();
    }
}
