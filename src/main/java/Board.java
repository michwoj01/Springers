import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Board {
    protected Map<Vector, Springer> springers = new HashMap<>();
    private final ArrayList<Springer> team1 = new ArrayList<>();
    private final ArrayList<Springer> team2 = new ArrayList<>();
    private final ArrayList<Vector> normalmoves = new ArrayList<>();
    private final ArrayList<Vector> shortmove = new ArrayList<>();
    private final ArrayList<Vector> longmove = new ArrayList<>();

    public Board (){
        normalmoves.add(new Vector(0,1));
        normalmoves.add(new Vector(1,0));
        for (int i = 1; i<=8; i++){
            for (int j = 1; j <=2; j++){
                Vector temp1 = new Vector(i,8-j);
                Vector temp2 = new Vector(i,j+1);
                Springer s1 = new Springer(temp1,0,this);
                Springer s2 = new Springer(temp2,1,this);
                team1.add(s1);
                team2.add(s2);
                springers.put(temp1,s1);
                springers.put(temp2,s2);
            }
        }
    }

    public boolean isOccupied(Vector pos){
        return springers.containsKey(pos);
    }

    public Object objectAt(Vector pos) {
        if (isOccupied(pos)) return springers.get(pos);
        else return null;
    }

    public boolean validPos(Vector pos){
        return pos.x >= 1 && pos.x <= 8 && pos.y >= 1 && pos.y <= 8;
    }

    public int canMoveHere(Vector beg, Vector pos){
        shortmove.clear();
        longmove.clear();
        for (Vector vect : normalmoves){
            Vector temp1 = beg.add(vect);
            if((validPos(temp1) && !isOccupied(temp1)))  shortmove.add(temp1);
            else if(validPos(temp1.add(vect)) && isOccupied(temp1)) longmove.add(temp1.add(vect));
            Vector temp2 = beg.substract(vect);
            if(validPos(temp2) && !isOccupied(temp2)) shortmove.add(temp2);
            else if(validPos(temp2.substract(vect)) && isOccupied(temp2)) longmove.add(temp2.substract(vect));
        }
        if(shortmove.contains(pos)) return 0;
        else if(longmove.contains(pos)) return 1;
        else return 2;
    }

    public boolean rightPos(Vector pos, int x){
        if (x==0){
            return pos.y > 6;
        }
        else {
            return pos.y < 3;
        }
    }

    public int getTeamScore(int x){
        int score = 0;
        if (x==0){
            for (Springer s : team1){
                score += (rightPos(s.getPos(),x) ? 1 : 0);
            }
        }
        else {
            for (Springer s : team2){
                score += (rightPos(s.getPos(),x) ? 1 : 0);
            }
        }
        return score;
    }

    public void positionChanged(Springer s, Vector next){
        springers.remove(s.getPos());
        springers.put(next,s);
    }

}
