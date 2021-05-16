package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.example.admin.AdminController;
import org.example.customer.CustomerController;
import org.example.exceptions.UnauthorizedException;
import org.example.model.UserType;
import org.example.services.UserService;
import org.example.store.StoreController;

import javax.swing.*;

public class LoginController {
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private ChoiceBox roleInput;

    public void initialize() {

        roleInput.getItems().setAll("Customer", "Store", "Admin");
    }

    public void openRegister() throws Exception {

        Parent registerWindow = FXMLLoader.load(getClass().getResource("register.fxml"));
        Scene registerScene = new Scene(registerWindow);

        Stage window = new Stage();

        window.setScene(registerScene);
        window.setTitle("Register");
        window.show();
    }
    public void login() throws Exception {
        final UserType userType = UserType.valueOf(((String)roleInput.getValue()).toUpperCase());
        final String userName = usernameInput.getText();

        UserService.checkUser(userName,passwordInput.getText(),userType);

        switch (userType) {
            case CUSTOMER: CustomerController.openCustomerPanel(userName); break;
            case STORE: StoreController.openStorePanel(userName); break;
            case ADMIN: AdminController.openAdminPanel(); break;
        }
    }
}
