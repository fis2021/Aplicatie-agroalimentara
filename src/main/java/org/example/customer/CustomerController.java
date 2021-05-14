package org.example.customer;

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
import org.dizitart.no2.objects.ObjectRepository;
import org.example.model.User;
import org.example.services.FileSystemService;
import org.example.services.UserService;
import org.example.store.EditProductController;

import java.util.List;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class CustomerController {

    public final ObservableList<User> stores = FXCollections.observableArrayList();

    private static ObjectRepository<User> users = UserService.getUsers();

    @FXML
    private TableView<User> tableview;
    @FXML
    private TableColumn<User,String> username;
    @FXML
    private TableColumn viewproducts;
    @FXML
    private TextField filterField;

    public static void openCustomerPanel() throws IOException {
        Parent customerWindow = FXMLLoader.load(CustomerController.class.getResource("/customer.fxml"));
        Scene customerScene = new Scene(customerWindow);

        Stage window = new Stage();

        window.setScene(customerScene);
        window.setTitle("Customer Panel");
        window.show();
    }

    public void viewOrderHistory() throws IOException {
        ViewPastOrdersController.viewPastOrdersPanel();
    }

    public void initialize(){
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        viewproducts.setSortable(false);

        Callback<TableColumn<User,String>,TableCell<User,String>> cellFactory = (param) -> {

            final TableCell<User,String> cell = new TableCell<User,String>(){

                @Override
                public void updateItem(String item,boolean empty){
                    super.updateItem(item,empty);

                    if(empty)
                    {
                        setGraphic(null);
                        setText(null);
                    }
                    else
                    {
                        final Button viewButton = new Button("View");
                        viewButton.setOnAction(event -> {

                            User u = getTableView().getItems().get(getIndex());
                            Path STORE_PATH = FileSystemService.getPathToFile("config", u.getUsername() + ".db");

                            try {
                                ViewProductController.openViewProductsPanel(STORE_PATH);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            //alert.setContentText("You have clicked"+u.getUsername());
                            //alert.show();
                        });

                        setGraphic(viewButton);
                        setText(null);
                    }

                };

            };
            return cell;
        };

        viewproducts.setCellFactory(cellFactory);

        for(User user : users.find())
        {
            if(Objects.equals(user.getRole(),"Store"))
                stores.add(user);
        }

        FilteredList<User> filteredData = new FilteredList<>(stores, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(store -> {


                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (store.getUsername().toLowerCase().indexOf(lowerCaseFilter) != -1 )
                    return true;
                else
                    return false;
            });
        });
        SortedList<User> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableview.comparatorProperty());

        tableview.setItems(sortedData);
    }

}
