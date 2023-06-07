package com.simosera.gamesoflife;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.random.RandomGenerator;
import static java.lang.Math.min;

/**
 * Controller for the main view, it manages the game
 * and the timing and many other things
 * @author Simone Serafini
 * @version 2023.06.07
 */
public class GameController {
    /** For nanosecond and millisecond conversions to second */
    static final double NANOSECONDS_IN_A_SECOND = 1.0E9;
    static final double MILLISECOND_IN_A_SECOND = 1000;

    @FXML
    VBox chooseCellVBox;

    @FXML
    Label stepsPerSecondLbl;

    @FXML
    Pane matrixPane;

    @FXML
    ToggleButton playTButton;

    @FXML
    Slider speedSlider;

    @FXML
    Slider densitySlider;

    Game game;

    Rule selectedRule;

    AnimationTimer timer;

    Color[] colors;

    ExecutorService executorService;

    /** Rules showed on the right side */
    HashMap<String,Rule>  availableRules;

    ToggleGroup toggleGroup;

    boolean started;

    /** nanosecond in which the last step was made*/
    long lastFrame;

    int speedInMilliseconds;

    double cellWidth;

    double cellHeight;

    int cellsPerRow;

    int numberOfRows;

    @FXML
    void playTButtonPressed() {
        if(started){
            stopGame();
        }else{
            startGame();
        }
    }

    public void initData(int height) throws IOException {
        this.numberOfRows = height;
        colors = new Color[5];
        colors[0] = Color.GREEN;
        colors[1]=Color.YELLOWGREEN;
        colors[2]=Color.ORANGE;
        colors[3]=Color.RED;
        initializeAll();
    }
    public void initializeAll() throws IOException {
        started = false;
        playTButton.setBackground(new Background(
                new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResource("Icons/play-icon.png")).toString()),
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,
                        new BackgroundSize(100, 100, true, true, true, false)))
        );
        toggleGroup = new ToggleGroup();
        availableRules = new HashMap<>();

        speedInMilliseconds = (int) (MILLISECOND_IN_A_SECOND / speedSlider.getValue());
        lastFrame = 0;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime = now - lastFrame;
                if(elapsedTime / NANOSECONDS_IN_A_SECOND > speedInMilliseconds / MILLISECOND_IN_A_SECOND && (executorService==null || executorService.isTerminated())){
                    executorService = game.multiThreadNextStep();
                    updateMatrix();
                    lastFrame = now;
                    stepsPerSecondLbl.setText(String.valueOf((int) Math.ceil(NANOSECONDS_IN_A_SECOND / elapsedTime)));
                }
            }
        };
        initShapeData();
        initializeCellChoosePane();
        toggleGroup.selectedToggleProperty().addListener(toggleGroupChangedSelection());
        toggleGroup.getToggles().get(availableRules.size() - 1).setSelected(true);
        speedSlider.valueProperty().addListener(speedChanged());
        initGame();
        updateMatrix();
    }

    public void initShapeData() {
        availableRules.putAll(Map.of(Rule.getConwayRule().getName(), Rule.getConwayRule()
                , Rule.getNeumannRule().getName(), Rule.getNeumannRule()));
        this.cellsPerRow = (int)(numberOfRows * (matrixPane.getPrefWidth() / matrixPane.getPrefHeight()));
        cellWidth = matrixPane.getPrefWidth() / cellsPerRow;
        cellHeight = matrixPane.getPrefHeight() / numberOfRows;
    }
    public ChangeListener<Toggle> toggleGroupChangedSelection() {
        return (observable, oldValue, newValue) -> {
            RadioButton button;
            if((button = (RadioButton) toggleGroup.getSelectedToggle()) != null){
                if(availableRules.containsKey(button.getId())){
                    selectedRule=availableRules.get(button.getId());
                }
            }
            resetGame();
        };
    }
    public void initGame() {
        game=new Game(numberOfRows, cellsPerRow,selectedRule);
    }

    public void cellClicked(int i,int j) {
        Cell c = game.getCellFromIndex(i, j);
        c.setRule(selectedRule);
        game.setCellAtIndex(i, j, c);
        game.getCellFromIndex(i, j).setLive( !game.getCellFromIndex(i, j).isLive());
        game.countNeighbours();
        updateMatrix();
    }
    public ChangeListener<Number> speedChanged() {
        return (observable, oldValue, newValue) -> speedInMilliseconds = (int)(MILLISECOND_IN_A_SECOND / speedSlider.getValue());
    }

    @FXML
    public void randomCellsGenerator() {
        //100 is because it needs a percentage
        double density=densitySlider.getValue() / 100;
        Cell c;
        RandomGenerator rnd = RandomGenerator.getDefault();
        for(int i = 0; i < numberOfRows; i++){
            for(int j = 0; j < cellsPerRow; j++){
                if(!game.getCellFromIndex(i, j).isLive()){
                    c=new Cell(selectedRule);
                    c.setLive(rnd.nextDouble() < density);
                    game.setCellAtIndex(i, j, c);
                }
            }
        }
        game.countNeighbours();
        updateMatrix();
    }

    @FXML
    public void resetGame() {
        stopGame();
        playTButton.setSelected(false);
        initGame();
        updateMatrix();
    }
    @FXML
    public void openCreateNewRuleStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameOfLifeApplication.class.getResource("create-cell-rule-view.fxml"));
        fxmlLoader.load();
        Stage stage=fxmlLoader.getRoot();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        Rule rule = new Rule();
        ((CustomRuleController)fxmlLoader.getController()).initData(rule, this::getShape);
        stage.showAndWait();
        if(rule.name.length() > 0){
            availableRules.put(rule.name, rule);
            addRuleToChoosePane(rule);
        }
        toggleGroup.getToggles().get(availableRules.size() - 1).setSelected(true);
    }

    public void updateMatrix() {
        matrixPane.getChildren().clear();
        for(int i = 0; i < game.getHeight(); i++){
            for(int j = 0; j < game.getWidth(); j++){
                Shape shape;
                if(game.getCellFromIndex(i, j).isLive()){
                    shape = getShape(i, j, cellWidth, cellHeight, colors[min(colors.length - 1, game.getCellFromIndex(i, j).getNeighboursCount())]);
                }else{
                    shape = getShape(i, j,cellWidth, cellHeight, Color.WHITE);
                }
                final int final1 = i, final2 = j;
                shape.setOnMouseClicked(event->cellClicked(final1, final2));
                matrixPane.getChildren().add(shape);
            }
        }
    }

    public Shape getShape(int i, int j, double width, double height, Color color) {
        Rectangle rect;
        rect = new Rectangle(width, height);
        rect.setFill(color);
        rect.setStroke(Color.GREY);
        rect.setX(j * width);
        rect.setY(i * height);
        return rect;
    }

    public void initializeCellChoosePane() throws IOException {
        for(Map.Entry<String,Rule> entry : availableRules.entrySet()){
            addRuleToChoosePane(entry.getValue());
        }
    }
    public void addRuleToChoosePane(Rule rule) throws IOException {
        FXMLLoader fxmlLoader;
        fxmlLoader = new FXMLLoader(GameOfLifeApplication.class.getResource("radio-selector-element.fxml"));
        Pane option = fxmlLoader.load();
        option.getChildren().stream().filter(v -> v instanceof RadioButton).forEach(r -> {((RadioButton) r).setToggleGroup(toggleGroup); r.setId(rule.getName());});
        option.getChildren().stream().filter(v -> v instanceof Label).forEach(l -> ((Label) l).setText(rule.getName()));
        option.getChildren().stream().filter(v -> v instanceof Pane).forEach(p -> drawNeighboursOnPane((Pane) p, rule));
        chooseCellVBox.getChildren().add(option);
    }

    private void drawNeighboursOnPane(Pane p, Rule r) {
        List<Coordinate> neighbours = r.getNeighbours();
        int size = CustomRuleController.MAX_NEIGHBOURHOOD_SIZE;
        double cWidth = p.getPrefWidth() / size;
        double cHeight = p.getPrefHeight() / size;
        p.getChildren().clear();
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Shape s = getShape(i, j, cWidth, cHeight, Color.WHITE);
                p.getChildren().add(s);
            }
        }
        for(Coordinate c : neighbours){
            ((Shape) p.getChildren().get(c.x + (size/2) + (c.y + size/2) * size)).setFill(Color.BLACK);
        }
        ((Shape) p.getChildren().get(size/2 * size + size/2)).setFill(Color.RED);
    }

    public void startGame() {
        started = true;
        playTButton.setBackground(new Background(
                new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResource("Icons/pause-icon.png")).toString()),
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        //100 is because those are percentages
                        new BackgroundSize(100, 100, true, true, true, false)))
        );
        timer.start();

    }
    public void stopGame() {
        started = false;
        playTButton.setBackground(new Background(
                new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResource("Icons/play-icon.png")).toString()),
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        new BackgroundSize(100, 100, true, true, true, false)))
        );
        timer.stop();
    }


}
