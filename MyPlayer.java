import java.awt.Color;
import java.util.LinkedList;
import java.util.HashSet;
//import Math.max;

/** This is me testing the chooseMove method
 * 
 * 
 * SO---
 *      look at last move and see if it creates an open 3 or 4. 
 *      if so: 
 *          
 * 
 **/

class MyPlayer extends GomokuPlayer {
    
    final static int boardsize = 8;
    final static Color black = new Color(0,0,0);
    final static Color white = new Color(255,255,255);

    int lastx = 0;
    int lasty = 0;

    Color[][] lastState = {new Color[8],
                           new Color[8],
                           new Color[8],
                           new Color[8],
                           new Color[8],
                           new Color[8],
                           new Color[8],
                           new Color[8]};

    PossibleMove thisLastMove;
    
    int turn = -1;
    public Move chooseMove(Color[][] board, Color me){
        turn++;
        
        //starting game
        if(turn==0 && me.equals(white)){
            PossibleMove move = new PossibleMove(copyMatrix(board),4,4,me);
            lastState = copyMatrix(move.board);
            thisLastMove = move;
            return new Move(move.x,move.y);
        }
        //respond to start
        if(turn<3){
            //System.out.println("turn=="+turn+" laststate[4][4]"+lastState[4][4]);
            PossibleMove move = startingGame(board,lastState, me);
            lastState = copyMatrix(move.board);
            //System.out.println("turn==0 laststate[4][4]"+lastState[4][4]);
            thisLastMove = move;
            return new Move(move.x,move.y);
        }

        //PossibleMove bestMove = bestMove(board, me,4);
        PossibleMove bestMove = alphaBeta(board, me,3);
        int x = bestMove.x;
        int y = bestMove.y;
        int lastx = x;
        int lasty = y;
        System.out.println("x:"+x+" y:"+y);
        System.out.println("Score:"+bestMove.score());
        return new Move(x,y);
    }

    private PossibleMove startingGame(Color[][] currentState, Color[][] lastState, Color me){            
        PossibleMove oponentMove = findLastMove(currentState, lastState);
        //if turn 
        if(turn==0){
            //Your color is black
            //play diaganol
            if(oponentMove.x<4&&oponentMove.y<4)
                return new PossibleMove(currentState,oponentMove.x+1,oponentMove.y+1,null);
            if(oponentMove.x<4&&oponentMove.y>3)
                return new PossibleMove(currentState,oponentMove.x+1,oponentMove.y-1,null);
            if(oponentMove.x>3&&oponentMove.y<4)
                return new PossibleMove(currentState,oponentMove.x-1,oponentMove.y+1,null);
            if(oponentMove.x>3&&oponentMove.y>3)
                return new PossibleMove(currentState,oponentMove.x-1,oponentMove.y-1,null);
        }
    
        //search moves around thisLastMove
        //and there last move
        //  radius of 2
        LinkedList<PossibleMove> movesList = getPossibleMovesRadius(currentState, me, thisLastMove.x, thisLastMove.y, 2);
        PossibleMove posMove = movesList.getLast();
        PossibleMove negMove = movesList.getLast();

        HashSet<PossibleMove> moves = new HashSet<PossibleMove>(movesList);
        moves.addAll(getPossibleMovesRadius(currentState, me, oponentMove.x, oponentMove.y, 2));

        int posScore = Integer.MIN_VALUE;
        int negScore = Integer.MAX_VALUE;
        for(PossibleMove move:moves){
            int score = miniMaxRadius(move, me, 4, 2);
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
    }

    private PossibleMove findLastMove(Color[][] currentState, Color[][] lastState){
        for(int i=0;i<boardsize;i++)
            for(int j=0;j<boardsize;j++)
                if(currentState[i][j]!=lastState[i][j])
                    return new PossibleMove(currentState,i,j,null);
        return null;
    }
    private PossibleMove alphaBeta(Color[][] board, Color me, int n){
        LinkedList<PossibleMove> moves = getAllPossibleMoves(board, me);
        PossibleMove posMove = moves.getLast();
        PossibleMove negMove = moves.getLast();

        int posScore = Integer.MIN_VALUE;
        int negScore = Integer.MAX_VALUE;
        for(PossibleMove move:moves){
            int score = maxValue(move, me, n, Integer.MIN_VALUE, Integer.MAX_VALUE);
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
        
    }

    private int maxValue(PossibleMove move, Color peble, int n, int alpha, int beta){
        n--;
        peble = swapColor(peble);
        if(n<0){
            //return low/highest score
            return move.score();
        }
        LinkedList<PossibleMove> nextMoves = getAllPossibleMoves(move.board, peble);
        for(PossibleMove nextMove : nextMoves){
            alpha = Math.max(alpha, minValue(nextMove, peble, n, alpha, beta));
            if(alpha>=beta)
                return beta;
        }
        return alpha;
    }

    private int minValue(PossibleMove move, Color peble, int n, int alpha, int beta){
        n--;
        peble = swapColor(peble);
        if(n<0){
            //return low/highest score
            return move.score();
        }
        LinkedList<PossibleMove> nextMoves = getAllPossibleMoves(move.board, peble);
        for(PossibleMove nextMove : nextMoves){
            beta = Math.min(beta, minValue(nextMove, peble, n, alpha, beta));
            if(beta<=alpha)
                return alpha;
        }
        return beta;
    }

    private  PossibleMove bestMove(Color[][] board, Color me, int n){
        //white is pos
        //black is neg
        /*
            for each move
                get possible moves - recursive
                    pos - neg

                    get best score from score 
        */

        LinkedList<PossibleMove> moves = getAllPossibleMoves(board, me);
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

    }

    private  int miniMax(PossibleMove move, Color peble, int n){
        n--;
        peble = swapColor(peble);
        if(n<0){
            //return low/highest score
            return move.score();
        }

        else{
            //return max if peble white
            //return min if peble black

            LinkedList<PossibleMove> nextMoves = getAllPossibleMoves(move.board, peble);

            int posScore = Integer.MIN_VALUE;
            int negScore = Integer.MAX_VALUE;
            for(PossibleMove nextMove : nextMoves){
                int score = miniMax(move, peble, n);
                //int score = nextMove.score();
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

    private  int miniMaxRadius(PossibleMove move, Color peble, int n, int r){
        n--;
        peble = swapColor(peble);
        if(n<0){
            //return low/highest score
            return move.singleDigit();
        }

        else{
            //return max if peble white
            //return min if peble black
            LinkedList<PossibleMove> nextMoves = getPossibleMovesRadius(move.board, peble, move.x, move.y, r);

            int posScore = Integer.MIN_VALUE;
            int negScore = Integer.MAX_VALUE;
            for(PossibleMove nextMove : nextMoves){
                int score = miniMaxRadius(move, peble, n, r);
                //int score = nextMove.score();
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

    private  Color swapColor(Color peble){
        if(peble.equals(new Color(0,0,0))){
            return new Color(255,255,255);
        }
        else{
            return new Color(0,0,0);
        }
    }

    private LinkedList<PossibleMove> getAllPossibleMoves(Color[][] board, Color me){
        LinkedList<PossibleMove> moves = new LinkedList<PossibleMove>();
        for(int i=0; i<boardsize; i++)
            for(int j=0; j<boardsize; j++)
                //System.out.println(board[i][j]);
                if(board[i][j] == null)
                    moves.push(new PossibleMove(copyMatrix(board),i,j,me));    
        return moves;
    }

    private LinkedList<PossibleMove> getPossibleMovesRadius(Color[][] board, Color me, int x, int y, int r){
        LinkedList<PossibleMove> moves = new LinkedList<PossibleMove>();
        x-=r;
        y-=r;
        while(x<0){
            x++;
            r++;
        }
        while(y<0){
            y++;
            r++;
        }
        r=r*2;
        while(x>boardsize-r)
            x--;
        while(y>boardsize-r)
            y--;
        
        for(int i=x; i<x+r; i++)
            for(int j=y; j<x+r; j++){
                if(isValidMove(board, i, j))
                    moves.push(new PossibleMove(copyMatrix(board),i,j,me));
            }        
        return moves;
    }

    private  boolean isValidMove(Color[][] board, int x, int y){
        if(x>-1 && x<boardsize && y>-1 && y<boardsize)
            if(board[x][y] == null)
                return true;
        return false;

    }

    private  <T>T[][] copyMatrix(T[][] input) {
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