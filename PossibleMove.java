import java.awt.Color;
import java.util.regex.*;


/*
Holds moves.
*/

class PossibleMove{

    int x;
    int y;
    int score;
    Color[][] board;

    //private HashSet<String> runs = new HashSet<String>();

    Pattern[] wOpen3 = {Pattern.compile("-WWW-"),
                        Pattern.compile("-(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}-"),
                        Pattern.compile("-(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}-"),
                        Pattern.compile("-(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}-")
                       };

    Pattern[] bOpen3 = {Pattern.compile("-BBB-"),
                        Pattern.compile("-(W|B|-){7}B(W|B|-){7}B(W|B|-){7}B(W|B|-){7}-"),
                        Pattern.compile("-(W|B|-){8}B(W|B|-){8}B(W|B|-){8}B(W|B|-){8}-"),
                        Pattern.compile("-(W|B|-){6}B(W|B|-){6}B(W|B|-){6}B(W|B|-){6}-")
                       };

    Pattern[] wSideOpen4 = {Pattern.compile("-WWWW"),
                            Pattern.compile("WWWW-"),
                            Pattern.compile("-(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W"),
                            Pattern.compile("W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}-"),
                            Pattern.compile("-(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W"),
                            Pattern.compile("W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}-"),
                            Pattern.compile("-(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W"),
                            Pattern.compile("W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}-"),
                        };

    Pattern[] bSideOpen4 = {Pattern.compile("-BBBB"),
                            Pattern.compile("BBBB-"),
                            Pattern.compile("-(W|B|-){7}B(W|B|-){7}B(W|B|-){7}B(W|B|-){7}B"),
                            Pattern.compile("B(W|B|-){7}B(W|B|-){7}B(W|B|-){7}B(W|B|-){7}-"),
                            Pattern.compile("-(W|B|-){8}B(W|B|-){8}B(W|B|-){8}B(W|B|-){8}B"),
                            Pattern.compile("B(W|B|-){8}B(W|B|-){8}B(W|B|-){8}B(W|B|-){8}-"),
                            Pattern.compile("-(W|B|-){6}B(W|B|-){6}B(W|B|-){6}B(W|B|-){6}B"),
                            Pattern.compile("B(W|B|-){6}B(W|B|-){6}B(W|B|-){6}B(W|B|-){6}-"),
                        };

    Pattern[] wOpen4 = {Pattern.compile("-WWWW-"),
                        Pattern.compile("-(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}-"),
                        Pattern.compile("-(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}-"), 
                        Pattern.compile("-(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}-")
                    };
                    
    Pattern[] wwin = {Pattern.compile("WWWWW"),
                      Pattern.compile("W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W(W|B|-){7}W"),
                      Pattern.compile("W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W(W|B|-){8}W"),
                      Pattern.compile("W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W(W|B|-){6}W"),
                    };
                    
    final int open3 = 2;
    final int sideopen4 = 4;
    final int open4 = 50;
    final int win = 200;

    public PossibleMove(Color[][] board, int x, int y, Color me){
        this.x = x;
        this.y = y;
        board[x][y] = me;
        this.board = board;
    }

    
    public int score(){
        /*
            +1 for every white stone adjascent to another white stone
            -1 for every black stone adjascent ot another black stone

            Loops over board and looks at next stone below, diaganol below and right.

            NEED TO IMPROVE
                search for open 3's, 4's and one side open 3's, 4's and winning positions
                either use string and regex or use boolean array.
                    convert matrix to string --WW----BB and use regex
        */
        int score = 0;
        //score+=singleDigit();
        //score+=runs();
        score = regex();
        this.score = score;
        return score;
    }

    private int regex(){
        int score = 0;
        String state = boardToString();
        System.out.println(state);

        for(Pattern pattern : wOpen3){
            Matcher matcher = pattern.matcher(state);
            while(matcher.find()){
                System.out.println("here");
                score+=open3;
            } 
        }
        
        return score;

    }

    private String boardToString(){ 
        final Color black = new Color(0,0,0);
        final Color white = new Color(255,255,255);
        final int boardsize = board[0].length;
        String string = "";

        for(int i=0; i<boardsize; i++){
            for(int j=0; j<boardsize; j++){
                if(board[i][j]==null){
                    string = string + "-";
                }
                else if(board[i][j].equals(black)){
                    string = string + "B";
                }
                else{
                    string = string + "W";
                }
            }
        }
        return string;
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

    private int singleDigit(){
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