import java.awt.Color;
class testScore{

    public static void main(String[] args){
        Color black = new Color(0,0,0);
        Color white = new Color(255,255,255);

        Color[][] board = new Color[3][3]; 

        board[0][0] = null;
        board[0][1] = null;
        board[0][2] = null;    

        board[1][0] = null;
        board[1][1] = null;
        board[1][2] = white;    

        board[2][0] = null;
        board[2][1] = null;
        board[2][2] = white;    

        PossibleMove move = new PossibleMove(board,0,0,black);

        System.out.println(move.score());
    }
}