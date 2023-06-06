package com.simosera.gamesoflife;

public class HexGame extends Game{


    public HexGame(int height, int width, Rule evenRule) {
        super(height, width, evenRule);
        Rule oddRule=evenRule.getHexOddRuleFromEven();
        for(int i=1;i<height;i+=2){
            for(int j=0;j<width;j++){
                cells[i][j].setRule(oddRule);
            }
        }
    }

    public void setCellAtIndex(int i, int j, Cell c){
        cells[i][j]=c;
        if(i%2==1)
            cells[i][j].setRule(c.getRule().getHexOddRuleFromEven());

    }
}
