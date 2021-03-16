import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


class FieldCreation extends ClickAction {

  FieldCreation(Stage stage,
                MineSweeper mineSweeper,
                int vertical,
                int width,
                int Bombs,
                double rectangleLength) {
    super(stage, mineSweeper, vertical, width, Bombs, rectangleLength);
  }

  void AddTileToPane(int vertical, int width, Pane pane) {
    for (int verticalCoordinate = 0; verticalCoordinate < vertical; ++verticalCoordinate) {
      for (int widthCoordinate = 0; widthCoordinate < width; ++widthCoordinate) {
        Tile tile = fieldTiles[verticalCoordinate][widthCoordinate];
        tile.setTranslateX(rectangleLength * widthCoordinate);
        tile.setTranslateY(rectangleLength * verticalCoordinate);
        pane.getChildren().add(tile);
      }
    }
  }

  void remainText(Pane pane) {
    Text DISPLAY_TEXT = new Text("残りの爆弾の個数");
    DISPLAY_TEXT.setLayoutX(1020);
    DISPLAY_TEXT.setLayoutY(100);
    DISPLAY_TEXT.setFont(Font.font(20));
    pane.getChildren().addAll(DISPLAY_TEXT);
    remainBombs.setLayoutX(1055);
    remainBombs.setLayoutY(150);
    remainBombs.setFont(Font.font(40));
    pane.getChildren().addAll(remainBombs);
  }
}
