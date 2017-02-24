
class ScoreMove{

    int score;
    PossibleMove move;

    public ScoreMove(PossibleMove move, int score){
        this.score = score;
        this.move = move;
    }
    public void Set(PossibleMove move, int score){
        this.score = score;
        this.move = move;        
    }
}