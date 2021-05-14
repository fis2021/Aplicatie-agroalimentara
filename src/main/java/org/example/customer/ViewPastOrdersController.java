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
import org.example.model.OrderStatus;
import org.example.services.FileSystemService;
import org.example.services.StoreService;
import org.example.store.EditProductController;
import java.io.IOException;
import java.util.List;
public class ViewPastOrdersController {

    @FXML
    private TableView<OrderStatus> tableview;
    @FXML
    private TableColumn<OrderStatus,String> orders;
    @FXML
    private TextField filterField;
    private final ObservableList<OrderStatus> statList= FXCollections.observableArrayList();
    private static List<OrderStatus> stats;
    public static void viewPastOrdersPanel() throws IOException {

        stats = StoreService.loadStatusFromFile(FileSystemService.getPathToFile("config", "status.db"));
        Parent viewPastOrdersWindow = FXMLLoader.load(EditProductController.class.getResource("/userOrder.fxml"));
        Scene viewPastOrdersScene = new Scene(viewPastOrdersWindow);
        Stage window = new Stage();
        window.setScene(viewPastOrdersScene);
        window.setTitle("Orders History");
        window.show();
    }

    public void initialize()
    {
        orders.setCellValueFactory(new PropertyValueFactory<OrderStatus,String>("Orders"));
        for(OrderStatus or:stats){
            statList.add(or);
        }

        FilteredList<OrderStatus> filteredData = new FilteredList<>(statList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(info -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (info.getO().getShopname().toLowerCase().indexOf(lowerCaseFilter) != -1 )
                    return true;
                else
                    return false;
            });
        });
        SortedList<OrderStatus> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableview.comparatorProperty());
        tableview.setItems(sortedData);
    }
}