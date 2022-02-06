
public class Engine implements Runnable{
    protected final SimulationApp observer;
    protected boolean go = false;
    protected final Board board;

    public Engine(Board board, SimulationApp observer){
        this.board = board;
        this.observer = observer;
    }

    public void setGo (boolean val) {this.go = val;}

    @Override
    public void run() {
        String winner;
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.print("Operation interrupted :(");
            }
            if (go) {
                go = false;
                if (board.getTeamScore(0) == 16) {
                    winner = "Niebiescy";
                    break;
                } else if (board.getTeamScore(1) == 16) {
                    winner = "Czerwoni";
                    break;
                }
                else {
                    observer.positionChange();
                }
            }
        }
        observer.endGame(winner);
    }
}
