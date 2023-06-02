package com.simosera.gamesoflife;

public class HexGame extends Game{
    HexCell[][] cells;
    public HexGame(int height, int width, HexCell[][] cells) {
        super(height, width, cells);
    }

    public HexGame(int height, int width) {
        super(height, width);
    }
    public HexCell[][] getCells() {
        return cells;
    }
    public HexCell getCellFromIndex(int i,int j){
        return cells[i][j];
    }
    public void setCellAtIndex(int i,int j,HexCell c){
        cells[i][j]=new HexCell(c);
    }
    public void countNeighbours(){
        int sum;
        int y;
        int x;
        for(int i=0;i<cells.length;i++){
            for(int j=0;j<cells[i].length;j++){
                sum=0;
                for(Coordinate e : cells[i][j].neighboursRelativeCordsToCount()){
                    y=(i+e.y+height+((i+e.y)%2))%height;
                    x=(j+e.x+width)%width;
                    sum+=cells[y][x].isLive() ? 1 : 0 ;
                }
                cells[i][j].setNeighboursCount(sum);
            }
        }
    }
    private void countNeighboursRow(int i){
        int sum;
        int x;
        int y;
        for(int j=0;j<cells[i].length;j++){
            sum=0;
            for(Coordinate e : cells[i][j].neighboursRelativeCordsToCount()){
                y=(i+e.y+height+((i+e.y)%2))%height;
                x=(j+e.x+width)%width;
                sum+=cells[y][x].isLive() ? 1 : 0 ;
            }
            cells[i][j].setNeighboursCount(sum);
        }

    }
}
