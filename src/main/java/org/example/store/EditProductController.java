package org.example.store;

import org.example.exceptions.ProductAlreadyExistsException;
import org.example.exceptions.ProductDoesNotExist;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.model.Product;
import org.example.services.StoreService;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javax.swing.*;
import java.io.IOException;

public class EditProductController {
    @FXML
    private TextField curentNameInput;
    @FXML
    private TextField newNameInput;
    @FXML
    private TextField newPriceInput;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    public static void EditProdPanel() throws IOException {
        Parent editProdWindow = FXMLLoader.load(EditProductController.class.getResource("/org/example/edit.fxml"));
        Scene editProdScene = new Scene(editProdWindow);

        Stage window = new Stage();

        window.setScene(editProdScene);
        window.setTitle("Edit Product Panel");
        window.show();
    }
    public void editProduct() {
        try {
            final Product product = new Product(newNameInput.getText(), Double.parseDouble(newPriceInput.getText()));
            StoreService.editData(curentNameInput.getText(), product, usernameInput.getText(), passwordInput.getText());
            curentNameInput.clear();
            newNameInput.clear();
            newPriceInput.clear();
            usernameInput.clear();
            passwordInput.clear();
        }catch (ProductAlreadyExistsException | ProductDoesNotExist e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            curentNameInput.clear();
            newNameInput.clear();
            newPriceInput.clear();
            usernameInput.clear();
            passwordInput.clear();
        }
    }
}
