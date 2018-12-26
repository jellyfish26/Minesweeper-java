import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Random;

class FieldCreation extends ClickAction {
    private final int rectangleVertical = 50;
    private final int rectangleWidth = 50;
    private final int BOMB = 9;

    void fieldInitialization(Stage stage, int vertical, int width, int numberOfBombs) {
        nowStage = stage;
        numberOfTileOpen = vertical * width - numberOfBombs;
        System.out.println(numberOfTileOpen);
        fieldTiles = new Tile[vertical][width];
        numberOfSurroundingBombs = new int[vertical][width];
        SettingBombs(vertical, width, numberOfBombs);

        for (int verticalCoordinate = 0; verticalCoordinate < vertical; ++verticalCoordinate) {
            for (int widthCoordinate = 0; widthCoordinate < width; ++widthCoordinate) {
                int surroundBomb = numberOfSurroundingBombs[verticalCoordinate][widthCoordinate];
                System.out.print(surroundBomb);
                if (surroundBomb == BOMB) {
                    fieldTiles[verticalCoordinate][widthCoordinate] = new Tile("B", verticalCoordinate, widthCoordinate, this);
                } else {
                    fieldTiles[verticalCoordinate][widthCoordinate] = new Tile(String.valueOf(surroundBomb), verticalCoordinate, widthCoordinate, this);
                }
            }
            System.out.println();
        }
    }

    private void SettingBombs(int vertical, int width, int numberOfBombs) {
        Random randomCoordinate = new Random();
        for (int setting = 0; setting < numberOfBombs; ++setting) {
            int bombVerticalCoordinate = randomCoordinate.nextInt(vertical);
            int bombWidthCoordinate = randomCoordinate.nextInt(width);
            if (numberOfSurroundingBombs[bombVerticalCoordinate][bombWidthCoordinate] == BOMB) {
                --setting; // A bomb has already been installed.
            } else {
                numberOfSurroundingBombs[bombVerticalCoordinate][bombWidthCoordinate] = BOMB;
                CountUpAroundBomb(bombVerticalCoordinate, bombWidthCoordinate);
            }
        }
    }

    private void CountUpAroundBomb(int setVerticalCoordinate, int setWidthCoordinate) {
        /*  1 1 1
         *  1 * 1
         *  1 1 1 */
        for (int vertical = -1; vertical <= 1; vertical++) {
            for (int width = -1; width <= 1; width++) {
                try {
                    if (numberOfSurroundingBombs[setVerticalCoordinate + vertical][setWidthCoordinate + width] != BOMB) {
                        ++numberOfSurroundingBombs[setVerticalCoordinate + vertical][setWidthCoordinate + width];
                    }
                } catch (IndexOutOfBoundsException e) {
                    // nothing to do
                }
            }
        }
    }

    void AddTileToPane(int vertical, int width, Pane pane) {
        for (int verticalCoordinate = 0; verticalCoordinate < vertical; ++verticalCoordinate) {
            for (int widthCoordinate = 0; widthCoordinate < width; ++widthCoordinate) {
                Tile tile = fieldTiles[verticalCoordinate][widthCoordinate];
                tile.setTranslateX(rectangleWidth * widthCoordinate);
                tile.setTranslateY(rectangleVertical * verticalCoordinate);
                pane.getChildren().add(tile);
            }
        }
    }

}
