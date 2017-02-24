import java.awt.Color;
import java.util.Collections;
import java.util.regex.*;
import java.util.List;

import java.util.Arrays;

class PossibleMove{

    int x;
    int y;
    boolean scoreCalculated = false;
    int score;
    Color[][] board;

    int whiteFactor;
    int blackFactor;

    final static Color black = new Color(0,0,0);
    final static Color white = new Color(255,255,255);

    final static int minVal = Integer.MIN_VALUE;
    final static int maxVal = Integer.MIN_VALUE;

    final static int open2 = 1;
    final static int sideopen3 = 10;
    final static int open3 = 100;
    final static int sideopen4 = 1000;
    // final static int open4 = 1000;
    // final static int win = 400;

    static Color[] wOpen2[] = {{white,white,null,null,null},{null,white,white,null,null},{null,null,white,white,null},{null,null,null,white,white}};
    static Color[] bOpen2[] = {{black,black,null,null,null},{null,black,black,null,null},{null,null,black,black,null},{null,null,null,black,black}};
    static Color[] wOpen3 = {null,white,white,white,null};
    static Color[] bOpen3 = {null,black,black,black,null};
    static Color[] wSideOpen3[] = {{white,white,white,null,null},{null,null,white,white,white}};
    static Color[] bSideOpen3[] = {{black,black,black,null,null},{null,null,black,black,black}};
    static Color[] wSideOpen4[] = {{null,white,white,white,white},{white,white,white,white,null},{white,null,white,white,white},{white,white,white,null,white},{white,white,null,white,white}};
    static Color[] bSideOpen4[] = {{null,black,black,black,black},{black,black,black,black,null},{black,null,black,black,black},{black,black,black,null,black},{black,black,null,black,black}};
    static Color[] wOpen4 = {null,white,white,white,white,null};
    static Color[] bOpen4 = {null,black,black,black,black,null};
    static Color[] wWin = {white,white,white,white,white,};
    static Color[] bWin = {black,black,black,black,black,};


    public PossibleMove(Color[][] board, int x, int y, Color peble){
        this.x = x;
        this.y = y;
        board[x][y] = peble;
        this.board = board;
    }
    public PossibleMove(Color[][] board){
        this.board = board;
    }

    public void makeMove(int x, int y, Color peble){
        board[x][y] = peble;
    }
    
    public int score(int whiteFactor, int blackFactor){
        /*  White sores positive
            Black scores negative  */
        if(scoreCalculated){
            return this.score;
        }
        scoreCalculated=true;
        int score = 0;
        score = regex(whiteFactor, blackFactor);
        this.score = score;
        return score;
    }

    private int regex(int w, int b){
        /*  Uses a regex system to score board
            by converting the board to a string and looking for patterns
            search for open 3's, 4's and one side open 3's, 4's and winning positions  */
        int score = 0;
        Color[][] rows = boardToRows();

        //add bolean for twoOfSideOpen they are good

        for(Color[] row: rows){
            List<Color> rowList = Arrays.asList(row);
            // if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(wWin)))
            //     return Integer.MAX_VALUE;
            // if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(bWin)))
            //     return Integer.MIN_VALUE;
            for(Color[] target: wOpen2)
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(target)))
                    score+=(open2*w);
            for(Color[] target: bOpen2)
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(target)))
                    score-=(open2*b);

            // if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(wOpen2)))
            //     score+=(open2*w);
            // if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(bOpen2)))
            //     score-=(open2*b);
            for(Color[] target: wSideOpen3)
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(target)))
                    score+=(sideopen3*w);
            for(Color[] target: bSideOpen3)
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(target)))
                    score-=(sideopen3*b);
            if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(wOpen3)))
                score+=(open3*w);
            if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(bOpen3)))
                score-=(open3*b);
            for(Color[] target: wSideOpen4)
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(target)))
                    score+=(sideopen4*w);
            for(Color[] target: bSideOpen4)
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(target)))
                    score-=(sideopen4*b);
            // if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(wOpen4)))
            //     score+=(open4);
            // if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(bOpen4)))
            //     score-=(open4);
        }
        return score;
    }

    private Color[][] boardToRows(){

        Color[][] rows = new Color[30][];
        int pos = 0;
        
        Color[][] vertical = new Color[8][8];
        //walk horizontal + vertical
        //can imporove horizontal
        for(int i=0;i<8;i++){
            Color[] horizontal = new Color[8];
            for(int j=0;j<8;j++){
                horizontal[j] = board[i][j];
                vertical[i][j] = board[j][i];
            }
            rows[pos] = horizontal;
            pos++;
        }
        for(Color[] arr: vertical){
            rows[pos] = arr;
            pos++;
        }

        //walk diaganol Top Left to Bottom Right
        int limit = 8;
        int charSize = 5;
        for(int i=3; i>-1; i--){
            Color[] diaganol = new Color[charSize];
            int x=i;
            int y=0;
            while(x<limit){
                diaganol[y] = board[x][y];
                x++;
                y++;
            }
            rows[pos] = diaganol;
            pos++;
            charSize++;
        }
        charSize = 7;
        for(int i=1; i<4; i++){
            Color[] diaganol = new Color[charSize];
            int x=0;
            int y=i;
            while(y<limit){
                diaganol[x] = board[x][y];
                x++;
                y++;
            }
            rows[pos] = diaganol;
            pos++;
            charSize--;
        }
        
        //walk diaganol Bottom Left to Top Right
        charSize=5;
        for(int i=3; i>-1; i--){
            Color[] diaganol = new Color[charSize];
            int x=i;
            int y=7;
            int diagpos = 0;
            while(x<limit){
                diaganol[diagpos] = board[x][y];
                x++;
                y--;
                diagpos++;
            }
            rows[pos] = diaganol;
            pos++;
            charSize++;
        }
        charSize = 7;
        for(int i=6; i>3; i--){
            Color[] diaganol = new Color[charSize];
            int x=0;
            int y=i;
            int diagpos = 0;
            while(y>-1){
                diaganol[x] = board[x][y];
                x++;
                y--;
                diagpos++;
            }
            rows[pos] = diaganol;
            pos++;
            charSize--;
        }
        return rows;
    }

    private char ColorToChar(Color in){
        if(in==null)
            return '-';
        else if(in.equals(black))    
            return 'B';
        else
            return 'W';
    }

    private String boardToString(){ 
        /*  Creates a string representation of board */
        final Color black = new Color(0,0,0);
        final Color white = new Color(255,255,255);
        final int boardsize = board[0].length;
        String[] chars = new String[91];
        String string = "";

        int pos = 0;
        for(int i=0; i<boardsize; i++){
            for(int j=0; j<boardsize; j++){
                if(board[i][j]==null){
                    chars[pos] = "-";
                }
                else if(board[i][j].equals(black)){
                    chars[pos] = "B";
                }
                else{
                    chars[pos] = "W";
                }
                pos++;
            }
        }
        return String.join("",chars);
    }

    private String rowToString(){
        return "foo";
    }

    public int singleDigit(){
        /*Scores 1 for each peble that is adgescent to another
          May be usefull for start game*/
        int boardsize = board[0].length;
        int score = 0;

        for(int i=0; i<boardsize; i++){
            for(int j=0; j<boardsize; j++){
                Color lookfor = board[i][j];
                //System.out.println("lookfor:"+lookfor);
                //System.out.println("boardsize:"+boardsize);
                                    
                if(lookfor!=null){
                    //System.out.println("("+i+","+j+")");
                    int point = 0;
                    if (lookfor.equals(new Color(0,0,0))){
                        point = -1;
                    }
                    else{
                        point = 1;
                    }
                    if(i!=boardsize-1){
                        if(j-1 > -1){
                            //check diaganol left
                            if((board[i+1][j-1]!=null)&&(board[i+1][j-1].equals(lookfor))){
                                //System.out.println("DL");
                                score = score + point;
                            }
                        }
                        if(j+1 < boardsize){
                            //check diaganol right
                            if((board[i+1][j+1]!=null)&&(board[i+1][j+1].equals(lookfor))){
                                score = score + point;
                            }
                        }
                        //check below
                        if((board[i+1][j]!=null)&&(board[i+1][j].equals(lookfor))){
                            score = score + point;
                        }
                    }
                    if(j+1 < boardsize){
                        //check right
                        if((board[i][j+1]!=null)&&(board[i][j+1].equals(lookfor))){
                            score = score + point;
                        }
                    }
                        
                }
            }
        }
        return score;
    }

    
}