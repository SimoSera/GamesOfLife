package com.simosera.gamesoflife;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private ComboBox<String> shapeComboBox;

    @FXML
    private Slider heightSlider;
    private static final int maxHeightSquare=150;
    private static final int maxHeightHexagon=50;
    public void initialize(){
        shapeComboBox.getItems().clear();
        shapeComboBox.getItems().setAll("Standard (Square)","Hexagonal");
        shapeComboBox.valueProperty().addListener(comboBoxValueChanged());
        shapeComboBox.getSelectionModel().select(0);
    }

    private ChangeListener<String> comboBoxValueChanged(){
        return ((observable, oldValue, newValue) -> {
            if(shapeComboBox.getSelectionModel().getSelectedIndex()==0){
                heightSlider.setMax(maxHeightSquare);
            }else{
                heightSlider.setMax(maxHeightHexagon);
            }
        });
    }
    @FXML
    void startGame(ActionEvent event) throws IOException {
        int ruleIndex=shapeComboBox.getSelectionModel().getSelectedIndex();
        int width;
        int height=(int)heightSlider.getValue();
        Stage stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader;
        fxmlLoader = new FXMLLoader(GameOfLifeApplication.class.getResource("game-view.fxml"));
        GameController gameController;
        height=(int)heightSlider.getValue();
        if(ruleIndex==0)
            gameController=new GameController();
        else
            gameController=new HexGameController();

        fxmlLoader.setController(gameController);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Game Of Life");
        stage.setResizable(false);
        gameController.initData(35);//hex width/height has to be 3/2  and height lower than 40
        stage.setScene(scene);
        stage.show();
    }


}
