import javafx.scene.layout.Pane;
import javafx.stage.Stage;


class FieldCreation extends ClickAction {
    double rectangleLength = 50;

    void fieldInitialization(Stage stage, MineSweeper mineSweeper, int vertical, int width, int numberOfBombs) {
        usedMineSweeper = mineSweeper;
        nowStage = stage;
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

}
