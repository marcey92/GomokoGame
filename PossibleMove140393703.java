import java.awt.Color;
import java.util.Collections;
import java.util.regex.*;
import java.util.List;

import java.util.Arrays;

class PossibleMove140393703{

    int x;
    int y;
    boolean scoreCalculated = false;
    int score;
    Color[][] board;

    int whiteFactor;
    int blackFactor;

    final static Color black = Color.black;
    final static Color white = Color.WHITE;

    final static int minVal = Integer.MIN_VALUE;
    final static int maxVal = Integer.MIN_VALUE;

    final static int open2 = 1;
    final static int sideopen3 = 5;
    final static int open3 = 25;
    final static int inOpen4 = 18;
    final static int sideopen4 = 125;
    final static int open4 = 600;
    final static int win = 1200;

    static Color[] wOpen2[] = {{white,white,null,null,null},{null,white,white,null,null},{null,null,white,white,null},{null,null,null,white,white}};
    static Color[] bOpen2[] = {{black,black,null,null,null},{null,black,black,null,null},{null,null,black,black,null},{null,null,null,black,black}};
    static Color[] wOpen3 = {null,white,white,white,null};
    static Color[] bOpen3 = {null,black,black,black,null};
    static Color[] wSideOpen3[] = {{white,white,white,null,null},{null,null,white,white,white}};
    static Color[] bSideOpen3[] = {{black,black,black,null,null},{null,null,black,black,black}};
    static Color[] wInOpen4[] = {{white,null,white,white,white},{white,white,white,null,white},{white,white,null,white,white}};
    static Color[] bInOpen4[] = {{black,null,black,black,black},{black,black,black,null,black},{black,black,null,black,black}};
    static Color[] wSideOpen4[] = {{null,white,white,white,white},{white,white,white,white,null}};
    static Color[] bSideOpen4[] = {{null,black,black,black,black},{black,black,black,black,null}};
    static Color[] wOpen4 = {null,white,white,white,white,null};
    static List<Color> wOpen4List = Arrays.asList(wOpen4);
    static Color[] bOpen4 = {null,black,black,black,black,null};
    static List<Color> bOpen4List = Arrays.asList(bOpen4);
    static Color[] wWin = {white,white,white,white,white};
    static List<Color> wWinList = Arrays.asList(wWin);
    static Color[] bWin = {black,black,black,black,black};
    static List<Color> bWinList = Arrays.asList(bWin);


    public PossibleMove140393703(Color[][] board, int x, int y, Color peble){
        this.x = x;
        this.y = y;
        board[x][y] = peble;
        this.board = board;
    }
    public PossibleMove140393703(Color[][] board){
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
        score = calculateScore(whiteFactor, blackFactor);
        this.score = score;
        return score;
    }


    private int calculateScore(int w, int b){
        int score = 0;
        Color[][] rows = boardToRows();

        for(Color[] row: rows){
            List<Color> rowList = Arrays.asList(row);
            if(-1!=Collections.indexOfSubList(rowList, wWinList))
                score+=(win*w);
            if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(bWin)))
                 score-=(win*b);
            for(Color[] target: wOpen2)
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(target)))
                    score+=(open2*w);
            for(Color[] target: bOpen2)
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(target)))
                    score-=(open2*b);
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
            for(Color[] target: wInOpen4)
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(target)))
                    score+=(inOpen4*w);
            for(Color[] target: bInOpen4)
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(target)))
                    score-=(inOpen4*b);
            for(Color[] target: wSideOpen4)
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(target)))
                    score+=(sideopen4*w);
            for(Color[] target: bSideOpen4)
                if(-1!=Collections.indexOfSubList(rowList, Arrays.asList(target)))
                    score-=(sideopen4*b);
            if(-1!=Collections.indexOfSubList(rowList, wOpen4List))
                 score+=(open4*w);
            if(-1!=Collections.indexOfSubList(rowList, bOpen4List))
                 score-=(open4*b);
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

}