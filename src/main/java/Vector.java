import java.util.Objects;

public class Vector {
    public int x;
    public int y;
    public Vector(int x, int y){
        this.x = x;
        this.y =y;
    }
    public String toString(){
        return "(" + this.x + ',' + this.y + ")";
    }
    public boolean equals(Object other) {
        if (other instanceof Vector){
            return ((Vector) other).x == this.x && ((Vector) other).y == this.y;
        }
        return false;
    }
    public Vector add(Vector other){
        return new Vector(this.x + other.x, this.y + other.y);
    }
    public Vector substract(Vector other) {
        return new Vector(this.x - other.x, this.y - other.y);
    }
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
