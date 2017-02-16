import java.awt.Color;

/** This is me testing the chooseMove method
 * 
 **/

class MyPlayer extends GomokuPlayer {
    int row = -1;
    int col = -1;
    public Move chooseMove(Color[][] board, Color me){
        row++;
        col++;
        return new Move(row,col);
    }
}