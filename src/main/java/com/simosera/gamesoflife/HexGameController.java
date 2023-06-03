package com.simosera.gamesoflife;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.ExecutorService;
import java.util.random.RandomGenerator;

import static java.lang.Math.min;

public class HexGameController {
    public Label stepsPerSecondLbl;
    @FXML
    private Pane matrixPane;

    @FXML
    private ToggleButton playTButton;

    @FXML
    private Button setBut;

    @FXML
    private Slider speedSlider;

    @FXML
    private Button randomGenerateButton;

    @FXML
    private Slider densitySlider;

    @FXML
    private Button clearButton;

    private boolean started;
    private Game game;
    private int speedMs;
    private double cellWidth;
    private double cellHeight;
    private int width;
    private int height;
    private Color bgColor;
    private Color cellColor;
    private Cell rules;
    private AnimationTimer timer;
    private Color[] colors;
    private long lastFrame;
    private ExecutorService executorService;

    @FXML
    void playTButtonPressed() throws InterruptedException {
        if(started){
            stopGame();
        }else{
            startGame();
        }
    }

    public void initializeAll(){
        started=false;
        game=new Game(height,width,rules);
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

        cellWidth=1000/(double)width;
        cellHeight=500/(double)height;
        updateMatrix();
    }

    @FXML
    public void clickAddCell(MouseEvent event) {
        int i,j;
        i=(int)(event.getY()/cellHeight);
        j=(int) (event.getX()/cellWidth);
        Cell c=rules.getDefault();
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
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                c= new ConwayCell(rnd.nextDouble() < density);
                game.setCellAtIndex(i,j,c);
            }
        }
        game.countNeighbours();
        updateMatrix();
    }

    @FXML
    public void resetGame(){
        stopGame();
        game=new Game(height,width,new ConwayCell());
        updateMatrix();
    }

    public void updateMatrix(){
       if(rules.getClass().getName().equals("com.simosera.gamesoflife.HexCell")){
           updateMatrixHexagons();
       }else{
           updateMatrixSquares();
       }
    }
    public void updateMatrixHexagons(){
        matrixPane.getChildren().clear();
        double r = cellHeight/2* 1.28; // the inner radius from hexagon center to outer corner
        double n = r; // the inner radius from hexagon center to middle of the axis
        for(int i=0;i< game.getHeight();i++){
            for(int j=0;j<game.getWidth();j++){
                Polygon poly;
                poly=new Polygon();
                double x =j* 2 * n + (i % 2) * n ;
                double y=i * 2 * r * 0.75;
                poly.getPoints().addAll(x, y,
                        x, y + r,
                        x + n, y + r * 1.5,
                        x + 2 * n, y + r,
                        x + 2 * r, y,
                        x + n, y - r * 0.5);
                if(game.getCellFromIndex(i,j).isLive()){
                    poly.setFill(colors[min(3,game.getCellFromIndex(i,j).getNeighboursCount())]);
                }else{
                    poly.setFill(Color.WHITE);
                }
                poly.setStroke(Color.GREY);
                matrixPane.getChildren().add(poly);
            }
        }
    }
    public void updateMatrixSquares(){
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
    public void initData(int width, int height, Color cellColor, Color bgColor,int rules){
        this.width=width;
        this.height=height;
        this.cellColor=cellColor;
        this.bgColor=bgColor;
        colors=new Color[5];
        colors[0]=Color.GREEN;
        colors[1]=Color.YELLOWGREEN;
        colors[2]=Color.ORANGE;
        colors[3]=Color.RED;
        this.rules=comboIndexToCell(rules);
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

    private Cell comboIndexToCell(int i){
        Cell c;
        switch (i){
            case 0:
                c= new ConwayCell();
                break;
            case 1:
                c=new HexCell();
                break;
            case 2:
                c=new ConwayCell();
                break;
            default:
                c= new ConwayCell();
        }
        return c;
    }

}
