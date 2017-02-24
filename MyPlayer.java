import java.awt.Color;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.stream.IntStream;
import java.util.Comparator;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;


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
    Color me;
    long startTime;
    Color[][] lastState = {new Color[8],new Color[8],new Color[8],new Color[8],new Color[8],new Color[8],new Color[8],new Color[8]};
    int lastx = 0;
    int lasty = 0;
    int whiteFactor = 1;
    int blackFactor = 1;
    int turn = -1;
    HashMap<PossibleMove,Integer> bestMoves = new HashMap<PossibleMove,Integer>();
    LinkedList<PossibleMove> startGame = new LinkedList<PossibleMove>();

    static Color[] wOpen4 = {null,white,white,white,white,null};
    static Color[] bOpen4 = {null,black,black,black,black,null};
    static Color[] wWin = {white,white,white,white,white,};
    static Color[] bWin = {black,black,black,black,black,};

    public Move chooseMove(Color[][] board, Color me){
        turn++;
        if(me.equals(white))
            System.out.println("--WHITE--");
        if(me.equals(black))
            System.out.println("--BLACK--");
        if(turn==0 && me.equals(black)){
            whiteFactor = 2;
            this.me = me;
        }
        if(turn==0 && me.equals(white)){
            blackFactor = 2;
            this.me = me;
            System.out.println("white startgame");
            lastState = board;
            startGame.push(new PossibleMove(copyMatrix(board),4,4,me));
            return new Move(4,4);
        }
        if(turn<1){
            //start gamce
            System.out.println("startgame");
            pushLastMove(board, lastState);
            PossibleMove bestMove = null;
            LinkedList<PossibleMove> nextMoves = getStartMoves(board);
            if(turn==0){
                PossibleMove closestToCentre = nextMoves.pop();
                bestMove = closestToCentre;
            }
            else{
                bestMove = startAlphaBetaIterative(board, me, 3);
            }
            startGame.push(bestMove);
            lastState = board;
            return new Move(bestMove.x,bestMove.y);
        }

        System.out.println("alpha-beta");
        PossibleMove bestMove = alphaBetaIterative(board,me);
        int x = bestMove.x;
        int y = bestMove.y;
        int lastx = x;
        int lasty = y;
        System.out.println("x:"+x+" y:"+y);
        System.out.println("Score:"+bestMove.score(whiteFactor,blackFactor));
        return new Move(x,y);
    }

    private LinkedList<PossibleMove> getStartMoves(Color[][] board){
        LinkedList<PossibleMove> nextMoves = new LinkedList<PossibleMove>();
        for(PossibleMove move: startGame){
            int x=move.x+1;
            int y=move.y+1;
            if(isValidMove(board, x, y))
                nextMoves.push(new PossibleMove(copyMatrix(board),x,y,me));
            x=move.x+1;
            y=move.y-1;
            if(isValidMove(board, x, y))
                nextMoves.push(new PossibleMove(copyMatrix(board),x,y,me));
            x=move.x-1;
            y=move.y-1;
            if(isValidMove(board, x, y))
                nextMoves.push(new PossibleMove(copyMatrix(board),x,y,me));
            x=move.x-1;
            y=move.y+1;
            if(isValidMove(board, x, y))
                nextMoves.push(new PossibleMove(copyMatrix(board),x,y,me));
        }
        return nextMoves;
    }
    private void pushLastMove(Color[][] currentState, Color[][] lastState){
        for(int i=0;i<boardsize;i++)
            for(int j=0;j<boardsize;j++)
                if(currentState[i][j]!=lastState[i][j])
                    startGame.push(new PossibleMove(copyMatrix(currentState),i,j,swapColor(me)));
    }
    private PossibleMove getLastMove(Color[][] currentState, Color[][] lastState){
        for(int i=0;i<boardsize;i++)
            for(int j=0;j<boardsize;j++)
                if(currentState[i][j]!=lastState[i][j])
                    return new PossibleMove(copyMatrix(currentState),i,j,swapColor(me));
        return null;
    }


    private PossibleMove startAlphaBetaIterative(Color[][] board, Color me, int depth){
        PossibleMove bestMove = startGame.getLast();
        startTime = System.nanoTime();
        long estimatedTime = 0;
        while(estimatedTime < 9500){
            bestMove = startAlphaBeta(board,me,depth);
            depth++;
            estimatedTime = (System.nanoTime() - startTime) / 1000000;
        }
        System.out.println("depth:"+depth);
        return bestMove;
    }
    private PossibleMove startAlphaBeta(Color[][] board, Color me, int n){
        LinkedList<PossibleMove> moves = getStartMoves(board); 
        PossibleMove posMove = moves.getLast();
        PossibleMove negMove = moves.getLast();

        int posScore = Integer.MIN_VALUE;
        int negScore = Integer.MAX_VALUE;
        for(PossibleMove move:moves){
            if(me.equals(white)){
                int score = startMaxValue(move, me, n, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if(score>posScore){
                    posScore = score;
                    posMove = move;
                }
            }
            else{
                int score = startMinValue(move, me, n, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if(score<negScore){
                    negScore = score;
                    negMove = move;
                }
            }
        }
        if(me.equals(white))
            return posMove;
        else
            return negMove;
    
    }

    private int startMaxValue(PossibleMove move, Color peble, int n, int alpha, int beta){
        long estimatedTime = (System.nanoTime() - startTime) / 1000000;
        if(estimatedTime > 10000){
            return move.score(whiteFactor,blackFactor);
        }
        n--;
        peble = swapColor(peble);
        if(n<0 || move.score(whiteFactor,blackFactor) == Integer.MAX_VALUE || move.score(whiteFactor,blackFactor) == Integer.MIN_VALUE)
            return move.score(whiteFactor,blackFactor);
        LinkedList<PossibleMove> nextMoves = getStartMoves(move.board);
        PossibleMove bestMove = nextMoves.getLast();
        int bestScore = Integer.MIN_VALUE;
        int score = Integer.MIN_VALUE;
        for(PossibleMove nextMove : nextMoves){
            score = Math.max(score, startMinValue(nextMove, peble, n, alpha, beta));
            if(score<bestScore){
                bestScore=score;
                bestMove = nextMove;
            }
            alpha = Math.max(alpha,score);
            if(alpha>=beta)
                break;
        }
        return score;
    }

    private int startMinValue(PossibleMove move, Color peble, int n, int alpha, int beta){
        long estimatedTime = (System.nanoTime() - startTime) / 1000000;
        if(estimatedTime > 9500){
            return move.score(whiteFactor,blackFactor);
        }
        n--;
        peble = swapColor(peble);
        if(n<0)
            return move.score(whiteFactor,blackFactor);
        
        LinkedList<PossibleMove> nextMoves = getStartMoves(move.board);
        PossibleMove bestMove = nextMoves.getLast();
        int bestScore = Integer.MAX_VALUE;
        int score = Integer.MAX_VALUE;
        for(PossibleMove nextMove : nextMoves){
            score = Math.min(score, maxValue(nextMove, peble, n, alpha, beta)); 
            beta = Math.min(beta, score);
            if(score>bestScore){
                bestScore = score;
                bestMove = nextMove;
            }
            if(beta<=alpha)
                break;
        }
        return score;
    }

    private PossibleMove alphaBetaIterative(Color[][] board, Color me){
        int depth = 2;
        PossibleMove bestMove = new PossibleMove(copyMatrix(board),0,0,me);
        startTime = System.nanoTime();
        long estimatedTime = 0;
        while(estimatedTime < 9500){
            bestMove = alphaBeta(board,me,depth);
            depth++;
            depth++;
            estimatedTime = (System.nanoTime() - startTime) / 1000000;
        }
        System.out.println("depth:"+depth);
        return bestMove;
    }
    private PossibleMove alphaBeta(Color[][] board, Color me, int n){
        PossibleMove[] moves = getOrderedMoves(board, me); 
        PossibleMove posMove = moves[0];
        PossibleMove negMove = moves[0];

        int posScore = Integer.MIN_VALUE;
        int negScore = Integer.MAX_VALUE;
        for(PossibleMove move:moves){
            if(me.equals(white)){
                int score = maxValue(move, me, n, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if(score>posScore){
                    posScore = score;
                    posMove = move;
                }
            }
            else{
                int score = minValue(move, me, n, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if(score<negScore){
                    negScore = score;
                    negMove = move;
                }
            }
        }
        if(me.equals(white))
            return posMove;
        else
            return negMove;
    
    }

    private int maxValue(PossibleMove move, Color peble, int n, int alpha, int beta){
        long estimatedTime = (System.nanoTime() - startTime) / 1000000;
        if(estimatedTime > 9500){
            return move.score(whiteFactor,blackFactor);
        }
        int tState = isTerminalState(move);
        if(tState == -2)
            return Integer.MIN_VALUE;
        else if(tState == +2)
            return Integer.MAX_VALUE;
        else if(tState == -1)
            return -100000;
        else if(tState == +1)
            return +100000;
        
        n--;
        peble = swapColor(peble);
        
        if(n<0)
            return move.score(whiteFactor,blackFactor);
        PossibleMove[] nextMoves = getOrderedMoves(move.board, peble);
        PossibleMove bestMove = nextMoves[0];
        int bestScore = Integer.MIN_VALUE;
        int score = Integer.MIN_VALUE;
        for(PossibleMove nextMove : nextMoves){
            score = Math.max(score, minValue(nextMove, peble, n, alpha, beta));
            if(score<bestScore){
                bestScore=score;
                bestMove = nextMove;
            }
            alpha = Math.max(alpha,score);
            if(alpha>=beta)
                break;
        }
       if(me.equals(white)){
            if(bestMoves.containsKey(bestMove)){
                int rating = bestMoves.get(bestMove)+1;
                bestMoves.replace(bestMove, rating);
            }
            else
                bestMoves.put(bestMove,1);
        }
        return score;
    }

    private int minValue(PossibleMove move, Color peble, int n, int alpha, int beta){
        long estimatedTime = (System.nanoTime() - startTime) / 1000000;
        if(estimatedTime > 9500){
            return move.score(whiteFactor,blackFactor);
        }
        int tState = isTerminalState(move);
        if(tState == -2)
            return Integer.MIN_VALUE;
        else if(tState == +2)
            return Integer.MAX_VALUE;
        else if(tState == -1)
            return -100000;
        else if(tState == +1)
            return +100000;
        n--;
        peble = swapColor(peble);
        if(n<0 || move.score(whiteFactor,blackFactor) == Integer.MAX_VALUE || move.score(whiteFactor,blackFactor) == Integer.MIN_VALUE)
            return move.score(whiteFactor,blackFactor);        
        PossibleMove[] nextMoves = getOrderedMoves(move.board, peble);
        PossibleMove bestMove = nextMoves[0];
        int bestScore = Integer.MAX_VALUE;
        int score = Integer.MAX_VALUE;
        for(PossibleMove nextMove : nextMoves){
            score = Math.min(score, maxValue(nextMove, peble, n, alpha, beta)); 
            beta = Math.min(beta, score);
            if(score>bestScore){
                bestScore = score;
                bestMove = nextMove;
            }
            if(beta<=alpha)
                break;
        }
        if(me.equals(black)){
            if(bestMoves.containsKey(bestMove)){
                int rating = bestMoves.get(bestMove)+1;
                bestMoves.replace(bestMove, rating);
            }
            else
                bestMoves.put(bestMove,1);
        }

        return score;
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
                if(board[i][j] == null)
                    moves.push(new PossibleMove(copyMatrix(board),i,j,me));
        return moves;
    }
    private PossibleMove[] getOrderedMoves(Color[][] board, Color me){
        LinkedList<PossibleMove> moves = getAllPossibleMoves(board, me);
        PossibleMove[] movesArr = moves.toArray(new PossibleMove[moves.size()]);
        int[] rating = new int[movesArr.length];
        for(int i=0;i<movesArr.length;i++){
            if(bestMoves.containsKey(movesArr[i]))
                rating[i] = bestMoves.get(movesArr[i]);
            else
                rating[i] = -1;
        }
        final PossibleMove[] sortedMoves = IntStream.range(0,rating.length)
                        .mapToObj(i -> new OrderMove(movesArr[i], rating[i]))
                        .sorted(Comparator.comparingInt(o -> o.rating))
                        .map(o -> o.move)
                        .toArray(PossibleMove[]::new);
        return sortedMoves;
    }

    private int isTerminalState(PossibleMove move){

        //TODO
        //get horizontal, vertical, diaganol
        int x=move.x;
        int y=move.y;
        Color[][] rows = new Color[4][];
        //horizontal
        rows[0] = move.board[y];
        //vertical
        rows[1] = new Color[8];
        for(int i=0;i<boardsize;i++){
            rows[1][i] = move.board[i][x];
        }
        //daiganol Top Left to Bottom Right
        int xD = x;
        int yD = y;
        while(xD>0 && yD>0){
            xD--;
            yD--;
        }
        LinkedList<Color> diaganol = new LinkedList<Color>(); 
        while(xD<boardsize && yD<boardsize){
            diaganol.push(move.board[xD][yD]);
            xD++;
            yD++;    
        }
        rows[2] = diaganol.toArray(new Color[diaganol.size()]);
        //diaganol Bottom Left to Top Right
        xD = x;
        yD = y;
        while(xD>0 && yD>7){
            xD--;
            yD++;
        }
        diaganol = new LinkedList<Color>(); 
        while(xD<boardsize && yD<0){
            diaganol.push(move.board[xD][yD]);
            xD++;
            yD--;
        }
        rows[3] = diaganol.toArray(new Color[diaganol.size()]);
        
        for(Color[] row: rows){
            List<Color> rowList = Arrays.asList(row);
            if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(wOpen4)))
                return +1;
            if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(bOpen4)))
                return -1;
            if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(wWin)))
                return +2;
            if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(bWin)))
                return -2;
        }
        return 0;
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
