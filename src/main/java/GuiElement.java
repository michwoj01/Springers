import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
public class GuiElement {
    VBox verticalBox;
    public GuiElement(Image image){
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        verticalBox = new VBox(imageView);
        verticalBox.setAlignment(Pos.CENTER);
    }
}
