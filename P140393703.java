import java.awt.Color;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.stream.IntStream;
import java.util.Comparator;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;


//import Math.max;

/** 
 *  if many with same score choose move closest to centre?       
 * 
 **/

class P140393703 extends GomokuPlayer {
    
    final static int boardsize = 8;
    final static Color black = Color.BLACK;
    final static Color white = Color.WHITE;
    Color me;
    long startTime;
    Color[][] lastState = {new Color[8],new Color[8],new Color[8],new Color[8],new Color[8],new Color[8],new Color[8],new Color[8]};
    int whiteFactor = 1;
    int blackFactor = 1;
    int turn = -1;
    HashMap<PossibleMove140393703,Integer> bestMoves = new HashMap<PossibleMove140393703,Integer>();

    static Color[] wOpen4 = {null,white,white,white,white,null};
    static Color[] bOpen4 = {null,black,black,black,black,null};
    static Color[] wWin = {white,white,white,white,white};
    static Color[] bWin = {black,black,black,black,black};

    public Move chooseMove(Color[][] board, Color me){
        turn++;
        if(turn==0 && me == Color.BLACK){
            whiteFactor = 3;
            this.me = me;
        }
        if(turn==0 && me == Color.WHITE){
            blackFactor = 3;
            this.me = me;
            lastState = board;
            return new Move(4,4);
        }
        if(turn<1){
            //start gamce
            if(turn==0){
                PossibleMove140393703 bestMove = firstBlackMove(board, lastState);
                return new Move(bestMove.x,bestMove.y);
            }
        }

        //Alpha-Beta
        PossibleMove140393703 bestMove = alphaBetaIterative(board,me);
        int x = bestMove.x;
        int y = bestMove.y;
        return new Move(x,y);
    }
    /***********************************************************
     * THE START GAME
     * 
     */
    private PossibleMove140393703 findLastMove(Color[][] currentState, Color[][] lastState){
        for(int i=0;i<boardsize;i++)
            for(int j=0;j<boardsize;j++)
                if(currentState[i][j]!=lastState[i][j])
                    return new PossibleMove140393703(currentState,i,j,null);
        return null;
    }

    private PossibleMove140393703 firstBlackMove(Color[][] currentState,Color[][] lastState){
        PossibleMove140393703 oponentMove = findLastMove(currentState, lastState);
        if(oponentMove.x<4&&oponentMove.y<4)
            return new PossibleMove140393703(currentState,oponentMove.x+1,oponentMove.y+1,null);
        if(oponentMove.x<4&&oponentMove.y>3)
            return new PossibleMove140393703(currentState,oponentMove.x+1,oponentMove.y-1,null);
        if(oponentMove.x>3&&oponentMove.y<4)
            return new PossibleMove140393703(currentState,oponentMove.x-1,oponentMove.y+1,null);
        if(oponentMove.x>3&&oponentMove.y>3)
            return new PossibleMove140393703(currentState,oponentMove.x-1,oponentMove.y-1,null);
        return null;
    }

    /***********************************************************
     * ALPHA-BETA
     * 
     */
    private PossibleMove140393703 alphaBetaIterative(Color[][] board, Color me){
        int depth = 0;
        PossibleMove140393703 bestMove = new PossibleMove140393703(copyMatrix(board),0,0,me);
        startTime = System.nanoTime();
        long estimatedTime = 0;
        while(estimatedTime < 9500){
            bestMove = alphaBeta(board,me,depth);
            depth++;
            estimatedTime = (System.nanoTime() - startTime) / 1000000;
        }
        return bestMove;
    }
    private PossibleMove140393703 alphaBeta(Color[][] board, Color me, int n){
        PossibleMove140393703[] moves = getOrderedMoves(board, me); 
        PossibleMove140393703 posMove = moves[0];
        PossibleMove140393703 negMove = moves[0];

        int posScore = Integer.MIN_VALUE;
        int negScore = Integer.MAX_VALUE;

        for(PossibleMove140393703 move:moves){
            if(me == Color.WHITE){
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
        if(me == Color.WHITE)
            return posMove;
        else
            return negMove;
    
    }

    private int maxValue(PossibleMove140393703 move, Color peble, int n, int alpha, int beta){
        long estimatedTime = (System.nanoTime() - startTime) / 1000000;
        if(estimatedTime > 9500){
            return move.score(whiteFactor,blackFactor);
        }
        if(isTerminalState(move, peble)){
            return move.score(whiteFactor,blackFactor);
        }        
        n--;
        peble = swapColor(peble);
        
        if(n<1)
            return move.score(whiteFactor,blackFactor);
        PossibleMove140393703[] nextMoves = getOrderedMoves(move.board, peble);
        PossibleMove140393703 bestMove = nextMoves[0];
        int bestScore = Integer.MIN_VALUE;
        int score = Integer.MIN_VALUE;
        for(PossibleMove140393703 nextMove : nextMoves){
            score = Math.max(score, minValue(nextMove, peble, n, alpha, beta));
            if(score<bestScore){
                bestScore=score;
                bestMove = nextMove;
            }
            alpha = Math.max(alpha,score);
            if(alpha>=beta)
                break;
        }
       if(this.me == Color.WHITE){
            if(bestMoves.containsKey(bestMove)){
                int rating = bestMoves.get(bestMove)+1;
                bestMoves.replace(bestMove, rating);
            }
            else
                bestMoves.put(bestMove,1);
        }
        return score;
    }

    private int minValue(PossibleMove140393703 move, Color peble, int n, int alpha, int beta){
        long estimatedTime = (System.nanoTime() - startTime) / 1000000;
        if(estimatedTime > 9500){
            return move.score(whiteFactor,blackFactor);
        }
        if(isTerminalState(move, peble)){
            return move.score(whiteFactor,blackFactor);
        }

        n--;
        peble = swapColor(peble);

        if(n<0)
            return move.score(whiteFactor,blackFactor);        
        PossibleMove140393703[] nextMoves = getOrderedMoves(move.board, peble);
        PossibleMove140393703 bestMove = nextMoves[0];
        int bestScore = Integer.MAX_VALUE;
        int score = Integer.MAX_VALUE;
        for(PossibleMove140393703 nextMove : nextMoves){
            score = Math.min(score, maxValue(nextMove, peble, n, alpha, beta)); 
            beta = Math.min(beta, score);
            if(score>bestScore){
                bestScore = score;
                bestMove = nextMove;
            }
            if(beta<=alpha)
                break;
        }
        if(this.me == Color.BLACK){
            if(bestMoves.containsKey(bestMove)){
                int rating = bestMoves.get(bestMove)+1;
                bestMoves.replace(bestMove, rating);
            }
            else
                bestMoves.put(bestMove,1);
        }
        return score;
    }
    private Color swapColor(Color peble){
        if(peble.equals(Color.BLACK)){
            return Color.WHITE;
        }
        else{
            return Color.BLACK;
        }
    }

    private LinkedList<PossibleMove140393703> getAllPossibleMove140393703s(Color[][] board, Color me){
        LinkedList<PossibleMove140393703> moves = new LinkedList<PossibleMove140393703>();
        for(int i=0; i<boardsize; i++)
            for(int j=0; j<boardsize; j++)
                if(board[i][j] == null)
                    moves.push(new PossibleMove140393703(copyMatrix(board),i,j,me));
        return moves;
    }
    private PossibleMove140393703[] getOrderedMoves(Color[][] board, Color me){
        LinkedList<PossibleMove140393703> moves = getAllPossibleMove140393703s(board, me);
        PossibleMove140393703[] movesArr = moves.toArray(new PossibleMove140393703[moves.size()]);
        int[] rating = new int[movesArr.length];
        for(int i=0;i<movesArr.length;i++){
            if(bestMoves.containsKey(movesArr[i]))
                rating[i] = bestMoves.get(movesArr[i]);
            else
                rating[i] = -1;
        }
        final PossibleMove140393703[] sortedMoves = IntStream.range(0,rating.length)
                        .mapToObj(i -> new OrderMove140393703(movesArr[i], rating[i]))
                        .sorted(Comparator.comparingInt(o -> o.rating))
                        .map(o -> o.move)
                        .toArray(PossibleMove140393703[]::new);
        return sortedMoves;
    }

    private boolean isTerminalState(PossibleMove140393703 move, Color peble){
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
        while(xD>0 && yD<7){
            xD--;
            yD++;
        }
        diaganol = new LinkedList<Color>(); 
        while(xD<boardsize && yD>0){
            diaganol.push(move.board[xD][yD]);
            xD++;
            yD--;
        }
        rows[3] = diaganol.toArray(new Color[diaganol.size()]);
        
        for(Color[] row: rows){
            List<Color> rowList = Arrays.asList(row);
            if(peble == Color.WHITE){
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(wWin)))
                    return true;
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(bWin)))
                    return true;
            }
            else{
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(bWin)))
                    //return -2;
                    return true;
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(wWin)))
                    //return +2;
                    return true;
            }
        }
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
