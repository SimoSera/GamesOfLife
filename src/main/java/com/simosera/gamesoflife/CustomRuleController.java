package com.simosera.gamesoflife;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class CustomRuleController {
    public static final int maxNeighbourhoodSize=9; //Use only odd values
    double cellWidth;
    double cellHeight;

    Rule customRule;
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

    GetShapeFunction getShape;

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
        cellWidth=neighbourSelectionPanel.getPrefWidth()/maxNeighbourhoodSize;
        cellHeight=neighbourSelectionPanel.getPrefHeight()/maxNeighbourhoodSize;
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
        Coordinate coordinate=new Coordinate(j-(maxNeighbourhoodSize/2),i-(maxNeighbourhoodSize/2));
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
        for(int i=0;i<maxNeighbourhoodSize ;i++){
            for(int j=0;j<maxNeighbourhoodSize;j++){
                Shape sh;
                final int finalI=i,finalJ=j;
                sh=getShape.apply(i,j,cellWidth,cellHeight,Color.WHITE);
                sh.setOnMouseClicked(event->cellClicked(finalI,finalJ));
                neighbourSelectionPanel.getChildren().add(sh);
            }
        }
        for(Coordinate c : customRule.neighbours){
            ((Shape)neighbourSelectionPanel.getChildren().get(c.x+(maxNeighbourhoodSize/2)+(c.y+maxNeighbourhoodSize/2)*maxNeighbourhoodSize)).setFill(Color.BLACK);
        }
        ((Shape)neighbourSelectionPanel.getChildren().get(maxNeighbourhoodSize/2*maxNeighbourhoodSize+maxNeighbourhoodSize/2)).setFill(Color.RED);
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
