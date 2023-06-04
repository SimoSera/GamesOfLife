package com.simosera.gamesoflife;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private ColorPicker backgroundColorChooser;

    @FXML
    private ColorPicker cellColorPicker;

    @FXML
    private ComboBox<String> rulesetComboBox;

    @FXML
    private Button startGameButton;

    public void initialize(){
        rulesetComboBox.getItems().clear();
        rulesetComboBox.getItems().setAll("Conway's","Hexagonal Conway's");
        rulesetComboBox.getSelectionModel().select(0);

    }
    @FXML
    void startGame(ActionEvent event) throws IOException {
        Color cellColor=cellColorPicker.getValue();
        Color bgColor=backgroundColorChooser.getValue();
        int selectedRules=rulesetComboBox.getSelectionModel().getSelectedIndex();
        Stage stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader;
        if(selectedRules==1)
            fxmlLoader = new FXMLLoader(GameOfLifeApplication.class.getResource("game-hex-view.fxml"));
        else
            fxmlLoader = new FXMLLoader(GameOfLifeApplication.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Game Of Life");
        GameController gameController = fxmlLoader.getController();
        gameController.initData(15,10,cellColor,bgColor,selectedRules);//modify when you will have width and height
        stage.setScene(scene);
        stage.show();
    }


}
