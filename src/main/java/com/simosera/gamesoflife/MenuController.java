package com.simosera.gamesoflife;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Menu view where
 * the user select the matrix size and the
 * type of game
 * @author Simone Serafini
 * @version 2023.06.07
 */
public class MenuController {

    /**  Maximum heights for square games and Hexagonal games*/
    private static final int MAX_HEIGHT_SQUARE = 100;

    private static final int MAX_HEIGHT_HEXAGON = 50;

    @FXML
    private ComboBox<String> shapeComboBox;

    @FXML
    private Slider heightSlider;

    public void initialize() {
        shapeComboBox.getItems().clear();
        shapeComboBox.getItems().setAll("Standard (Square)", "Hexagonal");
        shapeComboBox.valueProperty().addListener(comboBoxValueChanged());
        shapeComboBox.getSelectionModel().select(0);
    }

    private ChangeListener<String> comboBoxValueChanged() {
        return ((observable, oldValue, newValue) -> {
            if(shapeComboBox.getSelectionModel().getSelectedIndex() == 0){
                heightSlider.setMax(MAX_HEIGHT_SQUARE);
            }else{
                heightSlider.setMax(MAX_HEIGHT_HEXAGON);
            }
        });
    }
    @FXML
    void startGame(ActionEvent event) throws IOException {
        int ruleIndex = shapeComboBox.getSelectionModel().getSelectedIndex();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader;
        fxmlLoader = new FXMLLoader(GameOfLifeApplication.class.getResource("game-view.fxml"));
        GameController gameController;
        int height = (int) heightSlider.getValue();
        if(ruleIndex == 0)
            gameController = new GameController();
        else
            gameController = new HexGameController();
        fxmlLoader.setController(gameController);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Game Of Life");
        stage.setResizable(false);
        gameController.initData(height);
        stage.setScene(scene);
        stage.show();
    }


}
