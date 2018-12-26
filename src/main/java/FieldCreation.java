import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Random;

public class FieldCreation {

    public Tile[][] filedTiles;
    public int[][] numberOfSurroundingBombs;
    public boolean[][] tileOpenCheck;
    private final int rectangleVertical = 50;
    private final int rectangleWidth = 50;
    private final int BOMB = 9;


    void filedInitialization(int vertical, int width, int numberOfBombs) {
        filedTiles = new Tile[vertical][width];
        numberOfSurroundingBombs = new int[vertical][width];
        SettingBombs(vertical, width, numberOfBombs);

        for (int verticalCoordinate = 0; verticalCoordinate < vertical; ++verticalCoordinate) {
            for (int widthCoordinate = 0; widthCoordinate < width; ++widthCoordinate) {
                int surroundBomb = numberOfSurroundingBombs[verticalCoordinate][widthCoordinate];
                System.out.print(surroundBomb);
                if (surroundBomb == BOMB) {
                    filedTiles[verticalCoordinate][widthCoordinate] = new Tile("B", verticalCoordinate, widthCoordinate);
                } else {
                    filedTiles[verticalCoordinate][widthCoordinate] = new Tile(String.valueOf(surroundBomb), verticalCoordinate, widthCoordinate);
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

    public class Tile extends StackPane {
        Text tileContentText = new Text();
        private ClickAction clickAction = new ClickAction();
        String titleInText;
        Boolean tileOpenCHeck;
        private int verticalCoordinate;
        private int widthCoordinate;

        Tile (String tile, int vertical, int width) {
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
                clickAction.clickBomb(filedTiles);
            } else {
                clickAction.openTilesOfZero(filedTiles, numberOfSurroundingBombs, verticalCoordinate, widthCoordinate);
            }
        }
    }

    void AddTileToPane(int vertical, int width, Pane pane) {
        for (int verticalCoordinate = 0; verticalCoordinate < vertical; ++verticalCoordinate) {
            for (int widthCoordinate = 0; widthCoordinate < width; ++widthCoordinate) {
                Tile tile = filedTiles[verticalCoordinate][widthCoordinate];
                tile.setTranslateX(rectangleWidth * widthCoordinate);
                tile.setTranslateY(rectangleVertical * verticalCoordinate);
                pane.getChildren().add(tile);
            }
        }
    }

}
