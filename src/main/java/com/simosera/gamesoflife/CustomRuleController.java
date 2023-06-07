package com.simosera.gamesoflife;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * Controller used to control the view for
 * creating new rules
 * @author Simone Serafini
 * @version 2023.06.07
 */
public class CustomRuleController {

    /** Size of the matrix for selecting the neighbours
     * MUST be an odd number */
    public static final int MAX_NEIGHBOURHOOD_SIZE=9;

    /** Rule that will be set with the right values*/
    Rule customRule;

    /** Function to pass to this class the getShape method from GameController*/
    GetShapeFunction getShape;

    @FXML
    private Spinner<Integer> maxReproductionSpinner;

    @FXML
    private Spinner<Integer> minReproductionSpinner;

    @FXML
    private TextField nameTextField;

    @FXML
    private Pane neighbourSelectionPanel;

    @FXML
    private Spinner<Integer> overpopulationSpinner;

    @FXML
    private Spinner<Integer> underpopulationSpinner;

    IntegerSpinnerValueFactory underpopulationFactory;

    IntegerSpinnerValueFactory overpopulationFactory;

    IntegerSpinnerValueFactory minReproductionFactory;

    IntegerSpinnerValueFactory maxReproductionFactory;

    double cellWidth;

    double cellHeight;

    /**
     * Saves the parameters values and initializes the
     * {@link IntegerSpinnerValueFactory} and {@link Spinner}.
     * @param rule {@link Rule} where the values will be saved in
     * @param getShape  the getShape method of the {@link GameController} that calls
     *                  this function
     */
    public void initData(Rule rule,GetShapeFunction getShape){
        this.getShape=getShape;
        customRule=rule;
        underpopulationFactory=new IntegerSpinnerValueFactory(0,0,0);
        underpopulationFactory.valueProperty().addListener((observable, oldValue, newValue) -> updateFactories());
        overpopulationFactory=new IntegerSpinnerValueFactory(0,0,0);
        overpopulationFactory.valueProperty().addListener((observable, oldValue, newValue) -> updateFactories());
        minReproductionFactory=new IntegerSpinnerValueFactory(0,0,0);
        minReproductionFactory.valueProperty().addListener((observable, oldValue, newValue) -> updateFactories());
        maxReproductionFactory=new IntegerSpinnerValueFactory(0,0,0);
        maxReproductionFactory.valueProperty().addListener((observable, oldValue, newValue) -> updateFactories());
        underpopulationSpinner.setValueFactory(underpopulationFactory);
        overpopulationSpinner.setValueFactory(overpopulationFactory);
        minReproductionSpinner.setValueFactory(minReproductionFactory);
        maxReproductionSpinner.setValueFactory(maxReproductionFactory);
        cellWidth=neighbourSelectionPanel.getPrefWidth()/ MAX_NEIGHBOURHOOD_SIZE;
        cellHeight=neighbourSelectionPanel.getPrefHeight()/ MAX_NEIGHBOURHOOD_SIZE;
        drawNeighboursOnPane();
    }

    /**
     * Updates the {@link IntegerSpinnerValueFactory} so that the user can't select
     * invalid numbers,like numbers bigger than the number of neighbours.
     */
    public void updateFactories(){
        underpopulationFactory.setMax(overpopulationFactory.getValue());
        overpopulationFactory.setMin(underpopulationFactory.getValue());
        overpopulationFactory.setMax(customRule.getNeighbours().size());
        minReproductionFactory.setMax(maxReproductionFactory.getValue());
        maxReproductionFactory.setMax(customRule.getNeighbours().size());
        maxReproductionFactory.setMin(minReproductionFactory.getValue());
        underpopulationFactory.setValue(Math.min(underpopulationFactory.getValue(),customRule.getNeighbours().size()));
        overpopulationFactory.setValue(Math.min(overpopulationFactory.getValue(),customRule.getNeighbours().size()));
        underpopulationFactory.setValue(Math.min(underpopulationFactory.getValue(),customRule.getNeighbours().size()));
        underpopulationFactory.setValue(Math.min(underpopulationFactory.getValue(),customRule.getNeighbours().size()));
    }

    /**
     * Handler for the Listener onMouseClicked set on the cells.
     * When a {@link Shape} is clicked this method gets triggered, it
     * adds/removes the neighbour from the rule and  updates the
     * {@link IntegerSpinnerValueFactory} and the neighbourSelectionPanel.
     * @param row
     * @param column
     */
    public void cellClicked(int row,int column){
        Coordinate coordinate=new Coordinate(column-(MAX_NEIGHBOURHOOD_SIZE /2),row-(MAX_NEIGHBOURHOOD_SIZE /2));
        if(customRule.isNeighbour(coordinate)){
            customRule.removeNeighbour(coordinate);
        }else{
            customRule.addNeighbour(coordinate);
        }
        updateFactories();
        drawNeighboursOnPane();
    }

    /**
     * Draws the cells on the neighbourSelectionPane using the
     * {@link GetShapeFunction} getShape passed by the controller.
     * that opened this stage.
     * Also sets a onMouseClicked Listener on the cells
     */
    private void drawNeighboursOnPane(){
        neighbourSelectionPanel.getChildren().clear();
        for(int row = 0; row< MAX_NEIGHBOURHOOD_SIZE; row++){
            for(int column = 0; column< MAX_NEIGHBOURHOOD_SIZE; column++){
                Shape sh;
                final int finalI=row,finalJ=column;
                sh=getShape.apply(row,column,cellWidth,cellHeight,Color.WHITE);
                sh.setOnMouseClicked(event->cellClicked(finalI,finalJ));
                neighbourSelectionPanel.getChildren().add(sh);
            }
        }
        for(Coordinate c : customRule.neighbours){
            ((Shape)neighbourSelectionPanel.getChildren().get(c.x+(MAX_NEIGHBOURHOOD_SIZE /2)+(c.y+ MAX_NEIGHBOURHOOD_SIZE /2)* MAX_NEIGHBOURHOOD_SIZE)).setFill(Color.BLACK);
        }
        ((Shape)neighbourSelectionPanel.getChildren().get(MAX_NEIGHBOURHOOD_SIZE /2* MAX_NEIGHBOURHOOD_SIZE + MAX_NEIGHBOURHOOD_SIZE /2)).setFill(Color.RED);
    }

    /**
     * Handler for the save button pressed listener.
     * When the save button in pressed it puts all the values
     * and the name in the rule passed by the controller that sets
     * the Stage.
     * @param event {@link Event} object passed by the event listener, needed
     *             for closing (hiding) the stage
     * @see Button
     */
    @FXML
    public void saveButtonPressed(Event event){
        customRule.setOverpopulation(overpopulationFactory.getValue());
        customRule.setUnderpopulation(underpopulationFactory.getValue());
        customRule.setReliveMaximum(maxReproductionFactory.getValue());
        customRule.setReliveMinimum(minReproductionFactory.getValue());
        String name=nameTextField.getText();
        if(name.length()<1)
            name="Custom rule";
        customRule.setName(name);
        //the hide method is equivalent to the close method
        ((Node) event.getSource()).getScene().getWindow().hide();
    }



}
