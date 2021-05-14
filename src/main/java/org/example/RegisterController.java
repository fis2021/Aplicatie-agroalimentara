package org.example;

import org.example.exceptions.UsernameAlreadyExistsException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.example.services.UserService;

public class RegisterController {
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private Text registerMessage;

    public RegisterController() {
    }

    @FXML
    public void handleRegisterAction() {
        try {
            System.out.println(this.usernameInput.getText());
            UserService.addUser(this.usernameInput.getText(), this.passwordInput.getText(), "Customer");
            this.registerMessage.setText("Account created successfully!");
        } catch (UsernameAlreadyExistsException var2) {
            this.registerMessage.setText(var2.getMessage());
        }

    }
}
