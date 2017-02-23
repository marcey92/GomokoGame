import java.awt.Color;
import java.util.regex.*;


/*
Holds moves.

TODO HORIZONTAL NEEDS TO KNOW NEXT LINE. MAYBE JUST READ FROM ARRAY.
*/

class PossibleMove implements Comparable<PossibleMove>{

    int x;
    int y;
    boolean scoreCalculated = false;
    int score;
    Color[][] board;

    final static int open3 = 2;
    final static int sideopen4 = 4;
    final static int open4 = 50;
    final static int win = 200;

    static Pattern[] wOpen3 = {Pattern.compile("-WWW-"),
                        Pattern.compile("-(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}-"),
                        Pattern.compile("-(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}-"),
                        Pattern.compile("-(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}-")
                       };

    static Pattern[] bOpen3 = {Pattern.compile("-BBB-"),
                        Pattern.compile("-(W|B|-){7}B(W|B|-){7}B(W|B|-){7}B(W|B|-){7}-"),
                        Pattern.compile("-(W|B|-){8}B(W|B|-){8}B(W|B|-){8}B(W|B|-){8}-"),
                        Pattern.compile("-(W|B|-){6}B(W|B|-){6}B(W|B|-){6}B(W|B|-){6}-")
                       };

    static Pattern[] wSideOpen4 = {Pattern.compile("-WWWW"),
                            Pattern.compile("WWWW-"),
                            Pattern.compile("-(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W"),
                            Pattern.compile("W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}-"),
                            Pattern.compile("-(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W"),
                            Pattern.compile("W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}-"),
                            Pattern.compile("-(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W"),
                            Pattern.compile("W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}-"),
                        };

    static Pattern[] bSideOpen4 = {Pattern.compile("-BBBB"),
                            Pattern.compile("BBBB-"),
                            Pattern.compile("-(W|B|-){7}B(W|B|-){7}B(W|B|-){7}B(W|B|-){7}B"),
                            Pattern.compile("B(W|B|-){7}B(W|B|-){7}B(W|B|-){7}B(W|B|-){7}-"),
                            Pattern.compile("-(W|B|-){8}B(W|B|-){8}B(W|B|-){8}B(W|B|-){8}B"),
                            Pattern.compile("B(W|B|-){8}B(W|B|-){8}B(W|B|-){8}B(W|B|-){8}-"),
                            Pattern.compile("-(W|B|-){6}B(W|B|-){6}B(W|B|-){6}B(W|B|-){6}B"),
                            Pattern.compile("B(W|B|-){6}B(W|B|-){6}B(W|B|-){6}B(W|B|-){6}-"),
                        };

    static Pattern[] wOpen4 = {Pattern.compile("-WWWW-"),
                        Pattern.compile("-(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}-"),
                        Pattern.compile("-(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}-"), 
                        Pattern.compile("-(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}-")
                    };

    static Pattern[] bOpen4 = {Pattern.compile("-BBBB-"),
                        Pattern.compile("-(W|B|-){7}B(W|B|-){7}B(W|B|-){7}B(W|B|-){7}B(W|B|-){7}-"),
                        Pattern.compile("-(W|B|-){8}B(W|B|-){8}B(W|B|-){8}B(W|B|-){8}B(W|B|-){8}-"), 
                        Pattern.compile("-(W|B|-){6}B(W|B|-){6}B(W|B|-){6}B(W|B|-){6}B(W|B|-){6}-")
                    };
                    
    static Pattern[] wwin = {Pattern.compile("WWWWW"),
                      Pattern.compile("W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W"),
                      Pattern.compile("W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W"),
                      Pattern.compile("W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W"),
                    };
    static Pattern[] bwin = {Pattern.compile("BBBBB"),
                      Pattern.compile("B(W|B|-){7}B(W|B|-){7}B(W|B|-){7}B(W|B|-){7}B"),
                      Pattern.compile("B(W|B|-){8}B(W|B|-){8}B(W|B|-){8}B(W|B|-){8}B"),
                      Pattern.compile("B(W|B|-){6}B(W|B|-){6}B(W|B|-){6}B(W|B|-){6}B"),
                    };

    public PossibleMove(Color[][] board, int x, int y, Color me){
        this.x = x;
        this.y = y;
        board[x][y] = me;
        this.board = board;
    }

    @Override
    public int compareTo(PossibleMove otherMove){
        if(this.score()<otherMove.score())
            return -1;
        if(this.score()==otherMove.score())
            return 0;
        else
            return 1;
    }

    
    public int score(){
        /*
            NEED TO IMPROVE
                search for open 3's, 4's and one side open 3's, 4's and winning positions
                either use string and regex or use boolean array.
                    convert matrix to string --WW----BB and use regex
        */
        if(scoreCalculated){
            return this.score;
        }
        scoreCalculated=true;
        int score = 0;
        score = regex();
        this.score = score;
        return score;
    }

    private int regex(){
        int score = 0;
        String state = boardToString();

        for(Pattern pattern : wOpen3){
            Matcher matcher = pattern.matcher(state);
            while(matcher.find()){
                score+=open3;
            } 
        }
        for(Pattern pattern : bOpen3){
            Matcher matcher = pattern.matcher(state);
            while(matcher.find()){
                score-=open3;
            } 
        }

        for(Pattern pattern : wSideOpen4){
            Matcher matcher = pattern.matcher(state);
            while(matcher.find()){
                score+=sideopen4;
            } 
        }
        for(Pattern pattern : bSideOpen4){
            Matcher matcher = pattern.matcher(state);
            while(matcher.find()){
                score-=sideopen4;
            } 
        }


        for(Pattern pattern : wOpen4){
            Matcher matcher = pattern.matcher(state);
            while(matcher.find()){
                score+=open4;
            } 
        }
        for(Pattern pattern : bOpen4){
            Matcher matcher = pattern.matcher(state);
            while(matcher.find()){
                score-=open4;
            } 
        }

        for(Pattern pattern : wwin){
            Matcher matcher = pattern.matcher(state);
            while(matcher.find()){
                score+=win;
            } 
        }
        for(Pattern pattern : bwin){
            Matcher matcher = pattern.matcher(state);
            while(matcher.find()){
                score-=win;
            } 
        }


        return score;

    }

    private String boardToString(){ 
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

    private int runs(){
        final int D = 0;
        final int DL = 1;
        final int DR = 2;
        final int R = 3;
        final int U = 4;
        final int UL = 5;
        final int UR = 6;
        final int L = 7;

        final Color black = new Color(0,0,0);
        final Color white = new Color(255,255,255);
        
        final int boardsize = board[0].length;

        int score = 0;
        for(int i=0; i<boardsize; i++){
            for(int j=0; j<boardsize; j++){
        
                Color lookfor = board[i][j];

                //LinkedList<Integer> upOpen = new LinkedList<Integer>();

                if(lookfor!=null){
                    //check for space or colour before
                    if(i-1>-1){



                    }

                    if(j-1>-1){



                    }



                }

                //check for space above or oponent colour
                //if match roll through D, DL, DR, R until space
                //if rolled then check for space before


            }
        }

        return score;
    }

    public int singleDigit(){
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