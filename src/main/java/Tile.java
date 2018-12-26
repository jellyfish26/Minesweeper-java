import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

class Tile extends StackPane {

    private final int rectangleVertical = 50;
    private final int rectangleWidth = 50;
    Text tileContentText = new Text();
    String titleInText;
    Boolean tileOpenCHeck;
    private ClickAction clickAction;
    private int verticalCoordinate;
    private int widthCoordinate;

    Tile (String tile, int vertical, int width, ClickAction click) {
        clickAction = click;
        Rectangle tileBorder = new Rectangle(rectangleWidth, rectangleVertical);
        titleInText = tile;
        tileOpenCHeck = false;
        verticalCoordinate = vertical;
        widthCoordinate = width;
        tileBorder.setFill(null);
        tileBorder.setStroke(Color.BLACK);
        tileContentText.setText("");
        tileContentText.setFont(Font.font(30));

        setAlignment(Pos.CENTER);
        getChildren().addAll(tileBorder, tileContentText);

        setOnMouseClicked(this::onMouseClick);
    }

    private void onMouseClick(MouseEvent event) {
        System.out.printf("%s, %s, %s\n", event, verticalCoordinate, widthCoordinate);
        if (titleInText.equals("B")) {
            clickAction.showAll(true); // true is click bomb
        } else {
            clickAction.openTilesOfZero(verticalCoordinate, widthCoordinate);
        }
    }
}