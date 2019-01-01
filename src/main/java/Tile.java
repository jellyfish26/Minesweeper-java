import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


class Tile extends StackPane {

    Text tileContentText = new Text();
    Integer titleInText;
    Boolean tileOpenCheck;
    Rectangle tileBorder;
    private ClickAction clickAction;
    private int verticalCoordinate;
    private int widthCoordinate;

    Tile (Integer tile, int vertical, int width, ClickAction click, double rectangleLength) {
        clickAction = click;
        tileBorder = new Rectangle(rectangleLength, rectangleLength);
        titleInText = tile;
        tileOpenCheck = false;
        verticalCoordinate = vertical;
        widthCoordinate = width;
        tileBorder.setFill(null);
        tileBorder.setStroke(Color.BLACK);
        tileContentText.setText("");
        tileContentText.setFont(Font.font(rectangleLength * 0.9));

        setAlignment(Pos.CENTER);
        getChildren().addAll(tileBorder, tileContentText);

        setOnMouseClicked(this::onMouseClick);
    }

    private void onMouseClick(MouseEvent event) {
        System.out.printf("%s, %s, %s\n", event, verticalCoordinate, widthCoordinate);
        if (event.getButton() == MouseButton.PRIMARY) {
            /* At first it does not touch the bomb */
            if (clickAction.manipulateBombs.firstClick) {
                clickAction.openTilesOfZero(verticalCoordinate, widthCoordinate);
                return;
            }
            if (titleInText >= 9) {
                clickAction.showAll(true); // true is click bomb
            } else {
                clickAction.openTilesOfZero(verticalCoordinate, widthCoordinate);
            }
        } else {
            clickAction.rightClick(verticalCoordinate, widthCoordinate);
        }
    }
}