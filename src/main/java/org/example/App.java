package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.database.Database;
import org.example.exceptions.UsernameAlreadyExistsException;
import org.example.services.FileSystemService;
import org.example.services.UserService;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException, UsernameAlreadyExistsException {
        UserService.initUserDatabase();
        scene = new Scene(loadFXML("login"));
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop() throws Exception {
        Database.db.close();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }

}
