package com.simosera.gamesoflife;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    public void cellClicked(int i,int j){
        Coordinate coordinate=new Coordinate(j-(MAX_NEIGHBOURHOOD_SIZE /2),i-(MAX_NEIGHBOURHOOD_SIZE /2));
        if(customRule.isNeighbour(coordinate)){
            customRule.removeNeighbour(coordinate);
        }else{
            customRule.addNeighbour(coordinate);
        }
        updateFactories();
        drawNeighboursOnPane();
    }
    private void drawNeighboursOnPane(){
        neighbourSelectionPanel.getChildren().clear();
        for(int i = 0; i< MAX_NEIGHBOURHOOD_SIZE; i++){
            for(int j = 0; j< MAX_NEIGHBOURHOOD_SIZE; j++){
                Shape sh;
                final int finalI=i,finalJ=j;
                sh=getShape.apply(i,j,cellWidth,cellHeight,Color.WHITE);
                sh.setOnMouseClicked(event->cellClicked(finalI,finalJ));
                neighbourSelectionPanel.getChildren().add(sh);
            }
        }
        for(Coordinate c : customRule.neighbours){
            ((Shape)neighbourSelectionPanel.getChildren().get(c.x+(MAX_NEIGHBOURHOOD_SIZE /2)+(c.y+ MAX_NEIGHBOURHOOD_SIZE /2)* MAX_NEIGHBOURHOOD_SIZE)).setFill(Color.BLACK);
        }
        ((Shape)neighbourSelectionPanel.getChildren().get(MAX_NEIGHBOURHOOD_SIZE /2* MAX_NEIGHBOURHOOD_SIZE + MAX_NEIGHBOURHOOD_SIZE /2)).setFill(Color.RED);
    }
    @FXML
    public void saveButtonPressed(Event eevent){
        customRule.setOverpopulation(overpopulationFactory.getValue());
        customRule.setUnderpopulation(underpopulationFactory.getValue());
        customRule.setReliveMaximum(maxReproductionFactory.getValue());
        customRule.setReliveMinimum(minReproductionFactory.getValue());
        String name=nameTextField.getText();
        if(name.length()<1)
            name="Custom rule";
        customRule.setName(name);
        ((Node) eevent.getSource()).getScene().getWindow().hide(); //close
    }



}
