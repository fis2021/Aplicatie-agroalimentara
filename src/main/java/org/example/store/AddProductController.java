package org.example.store;

import org.example.exceptions.ProductAlreadyExistsException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.Product;
import org.example.services.StoreService;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;

public class AddProductController {
    @FXML
    private TextField nameInput;
    @FXML
    private TextField priceInput;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;

    public static void openAddProdPanel() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AddProductController.class.getResource("/org/example/addProduct.fxml"));
        Parent addProdWindow = loader.load();
        Scene addProdScene = new Scene(addProdWindow);

        Stage window = new Stage();

        window.setScene(addProdScene);
        window.setTitle("Add Product Panel");
        window.show();
    }
    public void addNewProd() throws ProductAlreadyExistsException, IOException {
        try {
            // TODO: Make sure priceInput has valid data before parsing.
            final Product product = new Product(nameInput.getText(), Double.parseDouble(priceInput.getText()));
            StoreService.addData(usernameInput.getText(), passwordInput.getText(), product);
            nameInput.clear();
            passwordInput.clear();
            priceInput.clear();
            usernameInput.clear();
        }catch(ProductAlreadyExistsException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            nameInput.clear();
            passwordInput.clear();
            priceInput.clear();
            usernameInput.clear();
        }
    }
}
