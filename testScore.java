import java.awt.Color;
class testScore{

    public static void main(String[] args){
        Color black = new Color(0,0,0);
        Color white = new Color(255,255,255);

        Color[][] board = new Color[8][8]; 

        board[0][0] = null;
        board[0][1] = null;
        board[0][2] = null;    
        board[0][3] = null;
        board[0][4] = black;
        board[0][5] = null; 
        board[0][6] = null;
        board[0][7] = null;

        board[1][0] = null;
        board[1][1] = null;
        board[1][2] = null;    
        board[1][3] = black;
        board[1][4] = null;
        board[1][5] = null; 
        board[1][6] = null;
        board[1][7] = null;

        board[2][0] = null;
        board[2][1] = null;
        board[2][2] = black;    
        board[2][3] = null;
        board[2][4] = null;
        board[2][5] = null; 
        board[2][6] = null;
        board[2][7] = null;

        board[3][0] = null;
        board[3][1] = black;
        board[3][2] = null;    
        board[3][3] = white;
        board[3][4] = null;
        board[3][5] = null; 
        board[3][6] = null;
        board[3][7] = null;

        board[4][0] = black;
        board[4][1] = null;
        board[4][2] = null;    
        board[4][3] = null;
        board[4][4] = white;
        board[4][5] = null; 
        board[4][6] = null;
        board[4][7] = null;

        board[5][0] = black;
        board[5][1] = null;
        board[5][2] = black;    
        board[5][3] = null;
        board[5][4] = null;
        board[5][5] = white; 
        board[5][6] = null;
        board[5][7] = null;

        board[6][0] = null;
        board[6][1] = black;
        board[6][2] = null;    
        board[6][3] = null;
        board[6][4] = null;
        board[6][5] = null; 
        board[6][6] = white;
        board[6][7] = null;

        board[7][0] = black;
        board[7][1] = null;
        board[7][2] = null;    
        board[7][3] = null;
        board[7][4] = null;
        board[7][5] = null; 
        board[7][6] = null;
        board[7][7] = white;

    

        PossibleMove move = new PossibleMove(board);

        System.out.println(move.score(1,1));
    }
}