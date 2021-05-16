package org.example.customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.example.database.Database;
import org.example.model.OrderStatus;
import org.example.store.EditProductController;
import java.io.IOException;

public class ViewPastOrdersController {

    private static String userName;

    @FXML
    private TableView<OrderStatus> tableview;
    @FXML
    private TableColumn<OrderStatus,String> orders;
    @FXML
    private TextField filterField;
    private final ObservableList<OrderStatus> statList= FXCollections.observableArrayList();

    public static void viewPastOrdersPanel(String username) throws IOException {
        Parent viewPastOrdersWindow = FXMLLoader.load(EditProductController.class.getResource("/org/example/userOrder.fxml"));
        Scene viewPastOrdersScene = new Scene(viewPastOrdersWindow);
        Stage window = new Stage();
        window.setScene(viewPastOrdersScene);
        window.setTitle("Orders History");
        window.show();
        userName = username;
    }

    public void initialize()
    {
        orders.setCellValueFactory(new PropertyValueFactory<>("order"));
        for(OrderStatus status : Database.statuses.find(ObjectFilters.eq("customerName", userName))){
            statList.add(status);
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
        sortedData.comparatorProperty().bind(tableview.comparatorProperty());
        tableview.setItems(sortedData);
    }
}
