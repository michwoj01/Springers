public class Springer {
    private Vector pos;
    private final int team;
    private final Board board;
    public Springer (Vector pos, int team, Board board) {
        this.pos = pos;
        this.team = team;
        this.board = board;
    }
    public void move(Vector newPos){
        board.positionChanged(this,newPos);
        this.pos = newPos;
    }

    public Vector getPos() {
        return pos;
    }

    public int getTeam() {
        return team;
    }
}
