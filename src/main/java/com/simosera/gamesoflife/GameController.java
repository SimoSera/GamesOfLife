package com.simosera.gamesoflife;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.action.Action;

import static java.lang.Math.min;

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
    private int speedMs;
    private double cellWidth;
    private double cellHeight;
    private int width;
    private int height;
    private String cellShape;
    private String rules;
    private Color bgColor;
    private Color cellColor;
    private AnimationTimer timer;
    private Color[] colors;
    private long lastFrame;

    @FXML
    void playTButtonPressed(ActionEvent event) throws InterruptedException {
        if(started){
            stopGame();
            started=false;
        }else{
            startGame();
            started=true;
        }
    }

    public void initializeAll(){
        started=false;
        game=new Game(height,width);
        speedMs=(int)(1000/speedSlider.getValue());
        lastFrame=0;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime=now-lastFrame;
                if(elapsedTime/1000000>speedMs){
                    game.multiThreadNextStep();updateMatrix();
                    lastFrame=now;
                }
            }
        };

        cellWidth=1000/(double)width;
        cellHeight=500/(double)height;
        updateMatrix();
    }

    public void clickAddCell(MouseEvent event){
        Cell c=new Cell(true);
        game.setCellAtIndex((int)(event.getY()/cellHeight),(int) (event.getX()/cellWidth),c);
        updateMatrix();
    }

    public void speedSetter(ActionEvent e){
        speedMs=(int)(1000/speedSlider.getValue());
    }


    public void updateMatrix(){
        matrixPane.getChildren().clear();
        for(int i=0;i< game.getHeight();i++){
            for(int j=0;j<game.getWidth();j++){
                Rectangle rect;
                if(game.getCellFromIndex(i,j).isLive()){
                    rect= new Rectangle(cellWidth,cellHeight,colors[min(3,game.getCellFromIndex(i,j).getNeighboursCount())]);

                }else{
                    rect= new Rectangle(cellWidth,cellHeight,Color.WHITE);
                }
                rect.setStroke(Color.GREY);
                rect.setX(j*cellWidth);
                rect.setY(i*cellHeight);
                matrixPane.getChildren().add(rect);
            }
        }
    }

    public void initData(int width, int height, Color cellColor, Color bgColor,String cellShape,String rules){
        this.width=width;
        this.height=height;
        this.cellColor=cellColor;
        this.bgColor=bgColor;
        this.rules=rules;
        this.cellShape=cellShape;
        colors=new Color[5];
        colors[0]=Color.GREEN;
        colors[1]=Color.YELLOW;
        colors[2]=Color.ORANGE;
        colors[3]=Color.RED;
        initializeAll();
    }


    public void startGame() throws InterruptedException {
        matrixPane.removeEventHandler(MouseEvent.MOUSE_CLICKED,this::clickAddCell);
        timer.start();
    }
    public void stopGame(){
        matrixPane.addEventHandler(MouseEvent.MOUSE_CLICKED,this::clickAddCell);
        timer.stop();
    }


}
