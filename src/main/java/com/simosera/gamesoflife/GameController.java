package com.simosera.gamesoflife;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

public class GameController {

    @FXML
    private Pane matrixPane;

    @FXML
    private ToggleButton playTButton;

    @FXML
    private Button setBut;

    @FXML
    private Slider speedSlider;

    private boolean started;
    private Game game;

    @FXML
    void playTButtonPressed(ActionEvent event) {
        if(started){
            stopGame();
            started=false;
        }else{
            startGame();
            started=true;
        }
    }

    public void initialize(){
        started=false;
        game=new Game(100,100);

    }

    public void updateMatrix(){
        for(int i=0;i< game.getHeight();i++){
            for(int j=0;j<game.getWidth();j++){
                if(game.getCellFromIndex(i,j).isLive()){
                   // draw colour
                }else{
                    //draw background
                }
            }
        }
    }

    public void startGame(){

    }
    public void stopGame(){

    }

}
