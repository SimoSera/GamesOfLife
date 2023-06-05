package com.simosera.gamesoflife;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.random.RandomGenerator;

import static java.lang.Math.min;

public class GameController {
    ToggleGroup tg;
    @FXML
    private VBox chooseCellVBox;
    @FXML
    Label stepsPerSecondLbl;
    @FXML
     Pane matrixPane;

    @FXML
     ToggleButton playTButton;

    @FXML
     Button setBut;

    @FXML
     Slider speedSlider;

    @FXML
     Button randomGenerateButton;

    @FXML
     Slider densitySlider;

    @FXML
     Button clearButton;

     boolean started;
     Game game;
     int speedMs;
     double cellWidth;
     double cellHeight;
     int cellsPerRow;
     int numberOfRows;
     Color bgColor;
     Color cellColor;
     Rule rule;
     AnimationTimer timer;
     Color[] colors;
     long lastFrame;
     ExecutorService executorService;

    @FXML
    void playTButtonPressed() {
        if(started){
            stopGame();
        }else{
            startGame();
        }
    }

    public void initializeAll(){
        started=false;
        game=new Game(numberOfRows, cellsPerRow,rule);
        speedMs=(int)(1000/speedSlider.getValue());
        lastFrame=0;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime=now-lastFrame;
                if(elapsedTime/1000000>speedMs && (executorService==null || executorService.isTerminated())){
                    executorService=game.multiThreadNextStep();
                    updateMatrix();
                    lastFrame=now;
                    stepsPerSecondLbl.setText(String.valueOf(1000/(elapsedTime/1000000)));
                }
            }
        };

        cellWidth=1000/(double) cellsPerRow;
        cellHeight=500/(double) numberOfRows;
        updateMatrix();
    }

    @FXML
    public void clickAddCell(MouseEvent event) {
        int i,j;
        i=(int)(event.getY()/cellHeight);
        j=(int) (event.getX()/cellWidth);
        Cell c=new Cell(rule);
        c.setLive(!game.getCellFromIndex(i,j).isLive());
        game.setCellAtIndex(i, j, c);
        game.countNeighbours();
        updateMatrix();
    }
    @FXML
    public void speedSetter(){
        speedMs=(int)(1000/speedSlider.getValue());
    }

    @FXML
    public void randomCellsGenerator() {
        double density=densitySlider.getValue()/100;
        Cell c;
        RandomGenerator rnd=RandomGenerator.getDefault();
        for(int i = 0; i< numberOfRows; i++){
            for(int j = 0; j< cellsPerRow; j++){
                c=new Cell(rule);
                c.setLive(rnd.nextDouble() < density);
                game.setCellAtIndex(i,j,c);
            }
        }
        game.countNeighbours();
        updateMatrix();
    }

    @FXML
    public void resetGame(){
        stopGame();
        game=new Game(numberOfRows, cellsPerRow,rule);
        updateMatrix();
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

    public void initializeCellChoosePane(ArrayList<Rule> cellRules) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(GameOfLifeApplication.class.getResource("radio-selector-element.fxml"));
        for(Rule c : cellRules){
            Pane option = fxmlLoader.load();
            option.getChildren().stream().filter(v-> v instanceof RadioButton).forEach(r->{((RadioButton) r).setToggleGroup(tg);r.setId(c.getName());});
            option.getChildren().stream().filter(v-> v instanceof Label).forEach(l->{((Label) l).setText(c.getName());});
            option.getChildren().stream().filter(v-> v instanceof Pane).forEach(p->{drawNeighboursOnPane((Pane) p);});
            chooseCellVBox.getChildren().add(option);
        }

    }

    private void drawNeighboursOnPane(Pane p,Rule r){
        int maxNeighbourHeight=3;
        int maxNeighbourWidth=3;
        List<Coordinate> neighbours=r.getNeighbours();
        p.getChildren().clear();
        for(int i=0;i<maxNeighbourHeight ;i++){
            for(int j=0;j<maxNeighbourWidth;j++){
                Rectangle rect;
                if(game.getCellFromIndex(i,j).isLive()){
                    rect= new Rectangle(cellWidth,cellHeight,colors[min(3,game.getCellFromIndex(i,j).getNeighboursCount())]);

                }else{

                }
                rect= new Rectangle(cellWidth,cellHeight,Color.WHITE);
                rect.setStroke(Color.GREY);
                rect.setX(j*cellWidth);
                rect.setY(i*cellHeight);
                p.getChildren().add(rect);
            }
        }
        for(Coordinate c : neighbours){
            ((Rectangle)p.getChildren().get(c.x+c.y*maxNeighbourWidth)).setFill(Color.);
        }


    }

    public void initData(int width, int height, Color cellColor, Color bgColor,int rules){
        this.cellsPerRow =width;
        this.numberOfRows =height;
        this.cellColor=cellColor;
        this.bgColor=bgColor;
        colors=new Color[5];
        colors[0]=Color.GREEN;
        colors[1]=Color.YELLOWGREEN;
        colors[2]=Color.ORANGE;
        colors[3]=Color.RED;
        this.rule=Rule.getConwayRule();// to replace with actual rules int
        initializeAll();
    }


    public void startGame() {
        started=true;
        timer.start();
    }
    public void stopGame(){
        started=false;
        timer.stop();
    }


}
