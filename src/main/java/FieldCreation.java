import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


class FieldCreation extends ClickAction {
    double rectangleLength = 50;

    void fieldInitialization(Stage stage, MineSweeper mineSweeper, int vertical, int width, int Bombs) {
        usedMineSweeper = mineSweeper;
        nowStage = stage;
        numberOfBombs = Bombs;
        numberOfFlags = 0;
        numberOfTileOpen = vertical * width - numberOfBombs;
        System.out.println(numberOfTileOpen);
        fieldTiles = new Tile[vertical][width];
        numberOfSurroundingBombs = new int[vertical][width];
        flagInstall = new boolean[vertical][width];
        manipulateBombs = new ManipulateBombs();
        manipulateBombs.fieldVertical = vertical;
        manipulateBombs.fieldWidth = width;
        manipulateBombs.SettingBombs(numberOfBombs, -2, -2);
        stopWatch.start();
        remainBombs = new Text(Integer.toString(numberOfBombs));

        for (int verticalCoordinate = 0; verticalCoordinate < vertical; ++verticalCoordinate) {
            for (int widthCoordinate = 0; widthCoordinate < width; ++widthCoordinate) {
                int surroundBomb = numberOfSurroundingBombs[verticalCoordinate][widthCoordinate];
                System.out.print(surroundBomb);
                if (surroundBomb == BOMB) {
                    fieldTiles[verticalCoordinate][widthCoordinate] = new Tile(9, verticalCoordinate, widthCoordinate, this, rectangleLength);
                } else {
                    fieldTiles[verticalCoordinate][widthCoordinate] = new Tile( surroundBomb, verticalCoordinate, widthCoordinate, this, rectangleLength);
                }
            }
            System.out.println();
        }
        manipulateBombs.firstClick = true;
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
