import java.awt.Color;
import java.util.LinkedList;

/** This is me testing the chooseMove method
 * 
 **/

class MyPlayer extends GomokuPlayer {
    
    final int boardsize = 8;
    final Color black = new Color(0,0,0);
    final Color white = new Color(255,255,255);

    boolean firstGo = true;
    public Move chooseMove(Color[][] board, Color me){

        //System.out.println(board[7][7]);
        //linked list of all possible moves 

        //starting game
        if(firstGo && me.equals(white)){
            firstGo = false;
            return new Move(4,4);
        }

        PossibleMove bestMove = bestMove(board, me,10);
        int x = bestMove.x;
        int y = bestMove.y;
        System.out.println("x:"+x+" y:"+y);
        System.out.println("Score:"+bestMove.score());
        return new Move(x,y);
    }

    private PossibleMove bestMove(Color[][] board, Color me, int n){
        //white is pos
        //black is neg
        /*
            for each move
                get possible moves - recursive
                    pos - neg

                    get best score from score 
        */

        LinkedList<PossibleMove> moves = getPossibleMoves(board, me);
        PossibleMove posMove = moves.getLast();
        PossibleMove negMove = moves.getLast();

        int posScore = Integer.MIN_VALUE;
        int negScore = Integer.MAX_VALUE;
        for(PossibleMove move:moves){
            int score = miniMax(move, me, n);
            //int score = move.score();
            if(score>posScore){
                posScore = score;
                posMove = move;
            }
            if(score<negScore){
                negScore = score;
                negMove = move;
            }
        }

        if(me.equals(white))
            return posMove;
        else
            return negMove;

        // if(me.equals(new Color(255,255,255))){
        //     //look for positive
        //     //White
        //     int bestScore = Integer.MIN_VALUE;
        //     for(PossibleMove move : moves){
        //         LinkedList<PossibleMove> nextMove = getPossibleMoves(board, new Color(0,0,0));
        //         int score = bestPosMove(nextMove, new Color(0,0,0), n);
        //         System.out.println("--"+score);
        //         if(score>bestScore){
        //             bestScore = move.score;
        //             bestMove = move;
        //             System.out.println("Score so far:"+bestScore);
        //         }
        //     }
        // }
        // else{
        //     //look for negative
        // }

    }

    private int miniMax(PossibleMove move, Color peble, int n){
        n--;
        peble = swapColor(peble);
        if(n<0){
            //return low/highest score
            return move.score();
        }

        else{
            //return max if peble white
            //return min if peble black

            LinkedList<PossibleMove> nextMoves = getPossibleMoves(move.board, peble);

            int posScore = Integer.MIN_VALUE;
            int negScore = Integer.MAX_VALUE;
            for(PossibleMove nextMove : nextMoves){
                //int score = miniMax(move, me, n);
                int score = nextMove.score();
                if(score>posScore){
                    posScore = score;
                }
                if(score<negScore){
                    negScore = score;
                }
            }

            if(peble.equals(white))
                return posScore;
            else
                return negScore;

        }
    }

    private int bestPosMove(LinkedList<PossibleMove> moves, Color go, int n){
        int bestScore = Integer.MIN_VALUE;
        if(n<0){
            for(PossibleMove move : moves){
                if(move.score()>bestScore){
                    bestScore = move.score;
                }
            }
        }
        else{
            n--;
            go = swapColor(go);
            for(PossibleMove move : moves){
                LinkedList<PossibleMove> nextMoves = getPossibleMoves(move.board, go);
                int score = bestNegMove(nextMoves, go, n);
                if(score>bestScore){
                    bestScore = score;
                }
            }
        }
        //System.out.println("here");
        return bestScore;
    
    }

    private int bestNegMove(LinkedList<PossibleMove> moves, Color go, int n){
        //System.out.println("n:"+n);
        int bestScore = Integer.MIN_VALUE;
        if(n<=0){
            for(PossibleMove move : moves){
                if(move.score()>bestScore){
                    bestScore = move.score;
                }
            }
        }
        else{
            bestScore = Integer.MAX_VALUE;
            n--;
            go = swapColor(go);
            for(PossibleMove move : moves){
                LinkedList<PossibleMove> nextMoves = getPossibleMoves(move.board, go);
                int score = bestPosMove(nextMoves, go, n);
                if(score<bestScore){
                    bestScore = score;
                }
            }
        }
        //System.out.println("here");
        return bestScore;
    }

    private Color swapColor(Color peble){
        if(peble.equals(new Color(0,0,0))){
            return new Color(255,255,255);
        }
        else{
            return new Color(0,0,0);
        }
    }

    private LinkedList<PossibleMove> getPossibleMoves(Color[][] board, Color me){

        LinkedList<PossibleMove> moves = new LinkedList<PossibleMove>();
        
        for(int i=0; i<boardsize; i++){
            for(int j=0; j<boardsize; j++){
                //System.out.println(board[i][j]);
                if(board[i][j] == null){
                    moves.push(new PossibleMove(copyMatrix(board),i,j,me));
                }
                else{
                    //System.out.println("("+i+","+j+") is occupied");
                }
            }
        }
        return moves;
        
    }

    private boolean isValidMove(Color[][] board, PossibleMove move){

        if(board[move.x][move.y] == null){
            return true;
        }
        return false;

    }

    private static <T>T[][] copyMatrix(T[][] input) {
        if (input == null)
            return null;
        T[][] result = input.clone();
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
}


}

/*
    move object

    java.awt.Color[r=0,g=0,b=0]


    tree structure
        holds PossibleMove in linked list
            Can change to a differnt stucture - think of as an API

    isValidMove()

    getValidMoves()

    class PossibleMove

    when picking move look for one near the last move

*/