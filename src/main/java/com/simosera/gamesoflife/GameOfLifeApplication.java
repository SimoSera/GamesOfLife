package com.simosera.gamesoflife;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * {@link Application} class that starts the app
 * and shows the menu-view.fxml
 * @author Simone Serafini
 * @version 2023.06.07
 */
public class GameOfLifeApplication extends Application {

    /**
     *  Starts the application by showing the primary menu {@link Scene}
     *  controlled by the {@link MenuController}
     * @param stage the primary {@link Stage} for this application.
     * @throws IOException {@link IOException} thrown by the {@link Class} getResource method
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameOfLifeApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setResizable(false);
        stage.setTitle("Game Of Life");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Main method that launches the app
     * @param args Arguments ot he main method
     */
    public static void main(String[] args) {
        launch();
    }
}