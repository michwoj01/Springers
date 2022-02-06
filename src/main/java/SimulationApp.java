import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SimulationApp extends Application {
    private final Alert alert = new Alert(Alert.AlertType.ERROR);
    private static Stage stage;
    private Engine engine;
    private final ArrayList<Image> images = new ArrayList<>();
    private final Board board = new Board();
    private final ArrayList<Vector> moves = new ArrayList<>();
    private final GridPane gridPane = new GridPane();
    private final Label red = new Label();
    private final Label orders = new Label();
    private final Label blue = new Label();
    private final Label turn = new Label("Kolej niebieskich");
    private int team = 0;
    private Springer actualSpringer;
    private boolean canMoveAgain = true;
    @Override
    public void init() throws FileNotFoundException{
        images.add(new Image(new FileInputStream("src/main/resources/blue.png")));
        images.add(new Image(new FileInputStream("src/main/resources/red.png")));
        gridPane.setAlignment(Pos.CENTER);
        orders.setStyle("-fx-font-size: 30px");
        orders.setAlignment(Pos.CENTER);
        gridPane.setGridLinesVisible(true);
        red.setText("0");
        blue.setText("0");
        turn.setStyle("-fx-font-size: 30px");
        turn.setAlignment(Pos.CENTER);
        for (int k = 0; k <= 8; k++) gridPane.getColumnConstraints().add(new ColumnConstraints(40));
        for (int j = 0; j <= 8; j++) gridPane.getRowConstraints().add(new RowConstraints(40));
        alert.setTitle("Very important error");
    }
    @Override
    public void start(Stage primaryStage){
        Label title = new Label("Zagraj w skoczki!");
        Button start = new Button("Start");
        VBox main = new VBox(title,start);
        main.setAlignment(Pos.CENTER);
        main.setSpacing(30);
        main.setStyle("-fx-font-size:20px");
        Scene scene = new Scene(main,300,200);
        stage = primaryStage;
        primaryStage.setScene(scene);
        primaryStage.show();
        start.setOnAction( e -> {
            drawPane();
            HBox scoreBoard = new HBox(new Label("Niebiescy: "), blue,new Label("Czerwoni: "), red);
            scoreBoard.setAlignment(Pos.CENTER);
            scoreBoard.setSpacing(10);
            scoreBoard.setStyle("-fx-font-size:20px");
            Button stop = new Button("Wykonaj ruch");
            stop.setStyle("-fx-font-size:20px");
            stop.setOnAction(a -> executeOrders());
            Button resume = new Button("Anuluj ruch");
            resume.setStyle("-fx-font-size:20px");
            resume.setOnAction(b -> clearOrders());
            HBox buttons = new HBox(stop,resume);
            buttons.setSpacing(20);
            buttons.setAlignment(Pos.CENTER);
            VBox content = new VBox(scoreBoard,turn,orders,buttons);
            content.setSpacing(10);
            content.setAlignment(Pos.CENTER);
            HBox hello = new HBox(gridPane,content);
            hello.setSpacing(20);
            hello.setAlignment(Pos.CENTER);
            Scene scene1 = new Scene(hello,800,500);
            engine = new Engine(board,this);
            Thread thread = new Thread(engine);
            thread.start();
            primaryStage.setScene(scene1);
            primaryStage.centerOnScreen();
        });
    }

    public void stop(){
        System.exit(0);
    }

    public void positionChange() {
        Platform.runLater(() -> {
            Node node = gridPane.getChildren().get(0);
            gridPane.getChildren().clear();
            gridPane.getChildren().add(0,node);
            updateScore();
            drawPane();
        });
    }

    public void updateCommands(){
        StringBuilder result = new StringBuilder();
        for (Vector vector : moves){
            result.append(vector.toString());
            result.append(" ");
        }
        orders.setText(result.toString());
    }

    public void drawPane() {
        for (int j = 1; j <= 8; j++) gridPane.add(new Label(String.valueOf(j)), 0, j);
        for (int i = 1; i <= 8; i++) {
            gridPane.add(new Label(String.valueOf(i)), i, 0);
            for (int j = 1; j <= 8; j++) {
                Pane pane = new Pane();
                Vector temp = new Vector(i,j);
                Button play = new Button();
                play.setStyle("-fx-background-radius:0; -fx-background-color:transparent;");
                play.setMaxSize(40,40);
                pane.setStyle("-fx-background-color:transparent");
                gridPane.add(pane, i, j);
                Object el = board.objectAt(temp);
                if (el != null) {
                    GuiElement box = new GuiElement(images.get(((Springer) el).getTeam()));
                    gridPane.add(box.verticalBox, i, j);
                    play.setOnAction(event -> {
                        if(((Springer) el).getTeam() != team) {setAlert("Nie twoja kolej!");}
                        else if(el != actualSpringer){
                            if(actualSpringer != null){
                                Pane last = getNodeByRowColumnIndex(actualSpringer.getPos().y, actualSpringer.getPos().x, gridPane);
                                last.setStyle("-fx-background-color:transparent");
                            }
                            moves.clear();
                            updateCommands();
                            canMoveAgain = true;
                            actualSpringer = (Springer) el;
                            pane.setStyle("-fx-background-color:fff700");
                        }
                    });
                }
                else{
                    play.setOnAction( f -> {
                        if(actualSpringer != null) {
                            Vector begin;
                            if (moves.isEmpty()) begin = actualSpringer.getPos();
                            else begin = moves.get(moves.size() - 1);
                            int result = board.canMoveHere(begin, temp);
                            if (canMoveAgain && (result == 1 || (result == 0 && moves.isEmpty()))) {
                                moves.add(temp);
                                updateCommands();
                                if(result == 0) canMoveAgain = false;
                            }
                            else {setAlert("Zabroniony ruch kolego!");}
                        }
                    });
                }
                gridPane.add(play,i,j);
            }
        }
        gridPane.getChildren().forEach(e -> GridPane.setHalignment(e, HPos.CENTER));
    }

    public void setAlert(String text){
        Platform.runLater(() -> {
            alert.setHeaderText(text);
            alert.show();
        });
    }

    public Pane getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Pane result = null;
        List<Node> childrens = gridPane.getChildren().subList(1,gridPane.getChildren().size());
        for (Node node : childrens) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column && node instanceof Pane) {
                result = (Pane) node;
                break;
            }
        }
        return result;
    }

    public void updateScore(){
        red.setText(String.valueOf(board.getTeamScore(1)));
        blue.setText(String.valueOf(board.getTeamScore(0)));
    }

    public void executeOrders(){
        if(!moves.isEmpty()) {
            actualSpringer.move(moves.get(moves.size() - 1));
            moves.clear();
            updateCommands();
            actualSpringer = null;
            if (team == 0) {
                turn.setText("Kolej czerwonych");
                team = 1;
            }
            else {
                turn.setText("Kolej niebieskich");
                team = 0;
            }
            engine.setGo(true);
            canMoveAgain = true;
        }
    }

    public void clearOrders(){
        if(!moves.isEmpty()) {
            moves.clear();
            canMoveAgain = true;
            updateCommands();
        }
    }

    public void endGame(String text){
        Platform.runLater(() -> {
            Label endingLabel = new Label(text + " wygrali mistrzostwa !!!");
            endingLabel.setStyle("-fx-font-size:20px");
            endingLabel.setAlignment(Pos.CENTER);
            Scene endingScene = new Scene(endingLabel,400,100);
            stage.setScene(endingScene);
            stage.centerOnScreen();
        });
    }
}
