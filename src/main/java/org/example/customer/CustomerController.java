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
import org.example.model.User;
import org.example.services.FileSystemService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import static org.example.database.Database.users;

public class CustomerController {

    private final ObservableList<User> stores = FXCollections.observableArrayList();
    private static String userName;

    @FXML
    private TableView<User> tableview;
    @FXML
    private TableColumn<User, String> username;
    @FXML
    private TableColumn viewproducts;
    @FXML
    private TextField filterField;

    public static void openCustomerPanel(String username) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(CustomerController.class.getResource("/org/example/customer.fxml"));
        Parent customerWindow = loader.load();
        Scene customerScene = new Scene(customerWindow);

        Stage window = new Stage();

        window.setScene(customerScene);
        window.setTitle("Customer Panel");
        window.show();
        userName = username;
    }

    public void viewOrderHistory() throws IOException {
        ViewPastOrdersController.viewPastOrdersPanel(userName);
    }

    public void initialize() {
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        viewproducts.setSortable(false);

        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory = (param) -> {

            final TableCell<User, String> cell = new TableCell<User, String>() {

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button viewButton = new Button("View");
                        viewButton.setOnAction(event -> {

                            User u = getTableView().getItems().get(getIndex());
                            try {
                                ViewProductController.openViewProductsPanel(u.getUsername());
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

                }

                ;

            };
            return cell;
        };

        viewproducts.setCellFactory(cellFactory);

        for (User user : users.find()) {
            if (Objects.equals(user.getRole(), "Store"))
                stores.add(user);
        }

        FilteredList<User> filteredData = new FilteredList<>(stores, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(store -> {


                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (store.getUsername().toLowerCase().indexOf(lowerCaseFilter) != -1)
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
