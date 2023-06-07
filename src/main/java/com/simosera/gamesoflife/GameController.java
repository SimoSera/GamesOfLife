package com.simosera.gamesoflife;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.random.RandomGenerator;
import static java.lang.Math.min;

/**
 * Controller for the main game view, it manages the {@link Game}
 * and the timing and many other things
 * @author Simone Serafini
 * @version 2023.06.07
 */
public class GameController {
    /** For nanosecond and millisecond conversions to second */
    static final double NANOSECONDS_IN_A_SECOND = 1.0E9;
    static final double MILLISECOND_IN_A_SECOND = 1000;

    /** {@link Color} That will be used for the cells if rainbow
     * {@link CheckBox} is not selected
     */
    static final Color defaultColor=Color.BLACK;

    /** {@link Color}s That will be used for the cells if rainbow
     * {@link CheckBox} is selected
     */
    static final List<Color> rainbowColors = List.of(Color.RED, Color.ORANGE, new Color(0.97,0.85,0,1),
            new Color(0.1,0.7,0.1,0.8),  new Color(0.1,0.35,1,0.8), new Color(0.29,0,0.51,0.8), new Color(0.4,0.1,0.7,0.8));

    @FXML
    VBox chooseCellVBox;

    @FXML
    Label stepsPerSecondLbl;

    @FXML
    Pane matrixPane;

    @FXML
    CheckBox colorsCheckBox;

    @FXML
    ToggleButton playTButton;

    @FXML
    Slider speedSlider;

    @FXML
    Slider densitySlider;

    Game game;

    Rule selectedRule;

    AnimationTimer timer;

    List<Color> colors;

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

    /**
     * Handler for the onAction Listener of the
     * play {@link Button}
     */
    @FXML
    void playTButtonPressed() {
        if(started)
            stopGame();
        else
            startGame();
    }

    /**
     *


    /**
     *  Method called from the {@link MenuController} to pass the
     *  parameters set in the menu view and
     *  initializes all the attributes of this controller and of the view
     * @param height height of the matrix, width will be calculated based on this parameter
     * @throws IOException {@link IOException} that may be thrown by
     *                      {@link Class} getResource
     */
    public void initializeAll(int height) throws IOException {
        this.numberOfRows = height;
        colors=new ArrayList<>();
        colors.add(Color.BLACK);
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

    /**
     * Initializes some data that depends on the {@link Shape}
     * of the game. This one is for the Square version
     */
    public void initShapeData() {
        availableRules.putAll(Map.of(Rule.getConwayRule().getName(), Rule.getConwayRule()
                , Rule.getNeumannRule().getName(), Rule.getNeumannRule()));
        this.cellsPerRow = (int)(numberOfRows * (matrixPane.getPrefWidth() / matrixPane.getPrefHeight()));
        cellWidth = matrixPane.getPrefWidth() / cellsPerRow;
        cellHeight = matrixPane.getPrefHeight() / numberOfRows;
    }

    /**
     * Listener that defines the Handler for when the {@link RadioButton}
     * that is selected has changed in the {@link ToggleGroup}.
     * @return ChangeListener to set the toggle value Listener to.
     * @see ChangeListener
     */
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

    /**
     * Handler for the mouseClick event of the {@link Shape}s of the matrixPane.
     * Sets the cell at row,column live/dead to dead/live
     * and updates the matrixPane.
     * @param row Row of the clicked {@link Shape}
     * @param column  Column of the clicked {@link Shape}
     */
    public void cellClicked(int row,int column) {
        Cell cell = game.getCellFromIndex(row, column);
        cell.setRule(selectedRule);
        game.setCellAtIndex(row, column, cell);
        game.getCellFromIndex(row, column).setLive( !game.getCellFromIndex(row, column).isLive());
        game.countNeighbours();
        updateMatrix();
    }

    /**
     * Handler of the {@link ChangeListener} of the value of the speedSlider.
     * Sets the speedInMilliseconds to the current value of the speedSlider
     * @return ChangeListener to set to the speedSlider
     * @see ChangeListener
     */
    public ChangeListener<Number> speedChanged() {
        return (observable, oldValue, newValue) -> speedInMilliseconds = (int)(MILLISECOND_IN_A_SECOND / speedSlider.getValue());
    }

    /**
     * Handler for the Listener of the random generate {@link Button}.
     * Sets to live some {@link Cell} randomly with a density based
     * on the densitySlider. Doesn't reset the matrixPane.
     */
    @FXML
    public void randomCellsGenerator() {
        //100 is because it needs a percentage
        double density=densitySlider.getValue() / 100;
        Cell cell;
        RandomGenerator rnd = RandomGenerator.getDefault();
        for(int row = 0; row < numberOfRows; row++){
            for(int column = 0; column < cellsPerRow; column++){
                if(!game.getCellFromIndex(row, column).isLive()){
                    cell=new Cell(selectedRule);
                    cell.setLive(rnd.nextDouble() < density);
                    game.setCellAtIndex(row, column, cell);
                }
            }
        }
        game.countNeighbours();
        updateMatrix();
    }

    /**
     * Handler for the Listener of the clear {@link Button}
     * Resets the game by instancing anew {@link Game} and
     * updates the matrixPane.
     */
    @FXML
    public void resetGame() {
        stopGame();
        playTButton.setSelected(false);
        initGame();
        updateMatrix();
    }

    /**
     * Handler for the Listener of the "create new rule" {@link Button}.
     * Opens a new {@link Stage} for creating a new {@link Rule},
     * the {@link Stage} will be over the current one and will have
     * the window focus blocking the current {@link javafx.stage.Window}
     * @throws IOException {@link IOException} that can be thrown by {@link Class} getResource method
     */
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

    /**
     * Updates the matrixPane {@link Pane} based on the
     * {@link Game} game cells state and number of neighbours.
     */
    public void updateMatrix() {
        matrixPane.getChildren().clear();
        for(int row = 0; row < game.getHeight(); row++){
            for(int column = 0; column < game.getWidth(); column++){
                Shape shape;
                if(game.getCellFromIndex(row, column).isLive()){
                    shape = getShape(row, column, cellWidth, cellHeight, colors.get(min(colors.size() - 1, game.getCellFromIndex(row, column).getNeighboursCount())));
                }else{
                    shape = getShape(row, column,cellWidth, cellHeight, Color.WHITE);
                }
                final int final1 = row, final2 = column;
                shape.setOnMouseClicked(event->cellClicked(final1, final2));
                matrixPane.getChildren().add(shape);
            }
        }
    }

    /**
     * Returns the {@link Shape} to draw after instancing it
     * based on the parameters.
     * This is for the standard game so the {@link Shape}
     * is upcast from a  {@link Rectangle}.
     * @param y the y position of hte {@link Shape} to draw
     * @param x the x position of hte {@link Shape} to draw
     * @param width the width of the {@link Shape} to draw
     * @param height the height of the {@link Shape} to draw
     * @param color  the {@link Color} of the {@link Shape} to draw
     * @return Returns the {@link Shape} for the caller to add in the {@link Node}
     */
    public Shape getShape(int y, int x, double width, double height, Color color) {
        Rectangle rect;
        rect = new Rectangle(width, height);
        rect.setFill(color);
        rect.setStroke(Color.GREY);
        rect.setX(x * width);
        rect.setY(y * height);
        return rect;
    }

    /**
     * Adds all the availableRules to the right side {@link ScrollPane}.
     * For each availableRules {@link Rule} calls
     *  addRuleToChoosePane.
     * @throws IOException {@link IOException} can be thrown by addRuleToChoosePane
     */
    public void initializeCellChoosePane() throws IOException {
        for(Map.Entry<String,Rule> entry : availableRules.entrySet()){
            addRuleToChoosePane(entry.getValue());
        }
    }

    /**
     * Loads the fxml file "create-cell-rule-view" and puts
     * it in the option {@link Pane} and initializes the attributes
     * with the {@link Rule} values.
     * @param rule {@link Rule} you want to add
     * @throws IOException  {@link IOException} that can be thrown
     *                      by {@link Class} getResource method
     */
    public void addRuleToChoosePane(Rule rule) throws IOException {
        FXMLLoader fxmlLoader;
        fxmlLoader = new FXMLLoader(GameOfLifeApplication.class.getResource("radio-selector-element.fxml"));
        Pane option = fxmlLoader.load();
        option.getChildren().stream().filter(v -> v instanceof RadioButton).forEach(r -> {((RadioButton) r).setToggleGroup(toggleGroup); r.setId(rule.getName());});
        option.getChildren().stream().filter(v -> v instanceof Label).forEach(l -> ((Label) l).setText(rule.getName()));
        option.getChildren().stream().filter(v -> v instanceof Pane).forEach(panel -> drawNeighboursOnPane((Pane) panel, rule));
        chooseCellVBox.getChildren().add(option);
    }

    /**
     * Draws the neighbour cells for the {@link Rule} in
     * the {@link Pane}.
     * Empty cells are white and cells which {@link Coordinate}
     * are in the {@link Rule} neighbourhood are black
     * @param panel {@link Pane} to draw the cells in
     * @param rule {@link Rule} to draw
     */
    private void drawNeighboursOnPane(Pane panel, Rule rule) {
        List<Coordinate> neighbours = rule.getNeighbours();
        int size = CustomRuleController.MAX_NEIGHBOURHOOD_SIZE;
        double cWidth = panel.getPrefWidth() / size;
        double cHeight = panel.getPrefHeight() / size;
        panel.getChildren().clear();
        for(int row = 0; row < size; row++){
            for(int column = 0; column < size; column++){
                Shape s = getShape(row, column, cWidth, cHeight, Color.WHITE);
                panel.getChildren().add(s);
            }
        }
        for(Coordinate coordinate : neighbours){
            ((Shape) panel.getChildren().get(coordinate.x + (size/2) + (coordinate.y + size/2) * size)).setFill(Color.BLACK);
        }
        ((Shape) panel.getChildren().get(size/2 * size + size/2)).setFill(Color.RED);
    }
    /**
     * Starts the game, starts the {@link AnimationTimer}
     * and changes the button to the pause icon.
     */
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

    /**
     * Stops the game, stops the {@link AnimationTimer}
     * and changes the button to the play icon.
     */
    public void stopGame() {
        started = false;
        playTButton.setBackground(new Background(
                new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResource("Icons/play-icon.png")).toString()),
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        new BackgroundSize(100, 100, true, true, true, false)))
        );
        timer.stop();
    }

    /**
     * Handler of the onAction Listener of the back {@link Button}.
     * Shows the menu {@link Scene} and closes the current {@link Scene}.
     * @param event {@link Event} object needed to get the current {@link Stage}
     * @throws IOException {@link IOException} that can be thrown
     *                      by {@link Class} getResource method
     */
    public void backButtonPressed(Event event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(GameOfLifeApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Game Of Life");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handler for the onAction of the {@link CheckBox} colorsCheckBox.
     * Sets the colors based on if the {@link CheckBox} is selected or
     * not.
     */
    public void colorsCheckBoxChanged() {
        if(colorsCheckBox.isSelected())
            colors = new ArrayList<>(rainbowColors);
        else
            colors = new ArrayList<>(List.of(defaultColor));
    }


}
