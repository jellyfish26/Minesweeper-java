import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Random;


class ClickAction extends NumberColor{

    Tile[][] fieldTiles;
    int[][] numberOfSurroundingBombs;
    boolean[][] flagInstall;
    Stage nowStage;
    int numberOfTileOpen;
    StopWatch stopWatch = new StopWatch();
    final int BOMB = 9;
    ManipulateBombs manipulateBombs;
    Text remainBombs;
    int numberOfBombs;
    int numberOfFlags = 0;

    /* By using instances, we do not operate GC and operate applications with minimal memory. */
    MineSweeper usedMineSweeper;

    private void tileOpen(int vertical, int width, boolean lose) {
        if (flagInstall[vertical][width]) return;
        try {
            --numberOfTileOpen;
            System.out.println(numberOfTileOpen);
            fieldTiles[vertical][width].tileOpenCheck = true;
            int tileNumber = numberOfSurroundingBombs[vertical][width];
            if (tileNumber < 9) {
                fieldTiles[vertical][width].tileContentText.setFill(numberOfBombsInColor(numberOfSurroundingBombs[vertical][width]));
                fieldTiles[vertical][width].tileContentText.setText(String.valueOf(numberOfSurroundingBombs[vertical][width]));
            } else {
                if (!flagInstall[vertical][width]) fieldTiles[vertical][width].tileBorder.setFill(Color.DARKORCHID);
                if (!lose) fieldTiles[vertical][width].tileBorder.setFill(Color.GREENYELLOW);
                fieldTiles[vertical][width].tileContentText.setFill(Color.ORANGE);
                fieldTiles[vertical][width].tileContentText.setText("B");
            }
        } catch (IndexOutOfBoundsException e) {
            // nothing to do
        }
    }

    /*  Zero tiles open around. and non-zero tiles open */
    void openTilesOfZero(int vertical, int width) {
        if (manipulateBombs.firstClick) setFirstClick(vertical, width);
        manipulateBombs.firstClick = false;
        try {
            if (fieldTiles[vertical][width].tileOpenCheck || flagInstall[vertical][width]) return;
            tileOpen(vertical, width, false);
            if (numberOfTileOpen == 0) {
                showAll(false); //  did not click on any bombs (false)
                return;
            }
            if (numberOfSurroundingBombs[vertical][width] != 0) { return; }
        } catch (IndexOutOfBoundsException e) {
            return; // nothing to do
        }
        for (int verticalCoordinate = -1; verticalCoordinate <= 1; ++verticalCoordinate) {
            for (int widthCoordinate = -1; widthCoordinate <= 1; ++widthCoordinate) {
                openTilesOfZero(vertical + verticalCoordinate, width + widthCoordinate);
            }
        }
    }

    void showAll(boolean clickBomb) {
        String elapsedTime = stopWatch.stop();
        for (int verticalCoordinate = 0; verticalCoordinate < fieldTiles.length; ++verticalCoordinate) {
            for (int widthCoordinate = 0; widthCoordinate < fieldTiles[verticalCoordinate].length; ++widthCoordinate) {
                tileOpen(verticalCoordinate, widthCoordinate, clickBomb);
            }
        }
        System.out.println("finish " + clickBomb);
        GenerateDialog result = new GenerateDialog();
        if (clickBomb) {
            if (result.resultDialog("あなたの負けです。", "リザルト") != ButtonType.YES) {
                System.exit(0);
            }
        } else {
            if (result.resultDialog("あなたの勝ちです。(経過時間:" + elapsedTime + ")", "リザルト") != ButtonType.YES) {
                System.exit(0);
            }
        }

        /* For once, in order to make garbage collection work. */
        result = null;
        fieldTiles = null;
        numberOfSurroundingBombs = null;

        usedMineSweeper.executionApplication(nowStage);
    }

    // flag install(establish)
    void rightClick(int vertical, int width) {
        if (fieldTiles[vertical][width].tileOpenCheck) return;
        if (!flagInstall[vertical][width]) {
            ++numberOfFlags;
            fieldTiles[vertical][width].tileBorder.setFill(Color.GREENYELLOW);
            fieldTiles[vertical][width].tileContentText.setFill(Color.BLACK);
            fieldTiles[vertical][width].tileContentText.setText("F");
            flagInstall[vertical][width] = true;
        } else {
            --numberOfFlags;
            fieldTiles[vertical][width].tileBorder.setFill(null);
            fieldTiles[vertical][width].tileContentText.setFill(null);
            fieldTiles[vertical][width].tileContentText.setText("");
            flagInstall[vertical][width] = false;
        }
        if (numberOfFlags < 0) numberOfFlags = 0;
        remainBombs.setText(Integer.toString(numberOfBombs - numberOfFlags));
    }

    private void setFirstClick(int vertical, int width) {
        int bombCount = 0;
        if (numberOfSurroundingBombs[vertical][width] == 0) return;
        fieldTiles[vertical][width].titleInText = 0;
        for (int verticalCoordinate = -1; verticalCoordinate <= 1; ++verticalCoordinate) {
            for (int widthCoordinate = -1; widthCoordinate <= 1; ++widthCoordinate) {
                try {
                    int aroundBomb = numberOfSurroundingBombs[vertical + verticalCoordinate][width + widthCoordinate];
                    if (aroundBomb >= 9) {
                        manipulateBombs.removeBomb(vertical + verticalCoordinate, width + widthCoordinate);
                        ++bombCount;
                    }
                } catch (IndexOutOfBoundsException e) {
                    // nothing to do
                }
            }
        }
        manipulateBombs.SettingBombs(bombCount, vertical, width);
    }

    class ManipulateBombs {
        int fieldVertical;
        int fieldWidth;
        boolean firstClick = false;

        private void removeBomb(int vertical, int width) {
            int bombCount = 0;
            for (int verticalCoordinate = -1; verticalCoordinate <= 1; ++verticalCoordinate) {
                for (int widthCoordinate = -1; widthCoordinate <= 1; ++widthCoordinate) {
                    try {
                        if (verticalCoordinate == 0 && widthCoordinate == 0) continue;
                        int aroundBomb = numberOfSurroundingBombs[vertical + verticalCoordinate][width + widthCoordinate];
                        if (aroundBomb >= 9) {
                            ++bombCount;
                        } else {
                            --numberOfSurroundingBombs[vertical + verticalCoordinate][width + widthCoordinate];
                            fieldTiles[vertical + verticalCoordinate][width + widthCoordinate].titleInText = aroundBomb - 1; // A tile instance has already been created.
                        }
                    } catch (IndexOutOfBoundsException e) {
                        // nothing to do
                    }
                }
            }
            numberOfSurroundingBombs[vertical][width] = bombCount;
            fieldTiles[vertical][width].titleInText = bombCount;
        }

        // not prohibited input is -2
        void SettingBombs(int numberOfBombs, int prohibitedVerticalCoordinate, int prohibitedWidthCoordinate) {
            Random randomCoordinate = new Random();
            for (int setting = 0; setting < numberOfBombs; ++setting) {
                System.out.println(fieldVertical);
                int bombVerticalCoordinate = randomCoordinate.nextInt(fieldVertical);
                int bombWidthCoordinate = randomCoordinate.nextInt(fieldWidth);
                boolean prohibitedVerticalCheck = prohibitedVerticalCoordinate -1 <= bombVerticalCoordinate && bombVerticalCoordinate <= prohibitedVerticalCoordinate + 1;
                boolean prohobitedWidthCheck = prohibitedWidthCoordinate -1 <= bombWidthCoordinate && bombWidthCoordinate <= prohibitedWidthCoordinate + 1;
                if (numberOfSurroundingBombs[bombVerticalCoordinate][bombWidthCoordinate] == BOMB || (prohibitedVerticalCheck & prohobitedWidthCheck)) {
                    --setting; // A bomb has already been installed.
                } else {
                    numberOfSurroundingBombs[bombVerticalCoordinate][bombWidthCoordinate] = BOMB;
                    if (firstClick) fieldTiles[bombVerticalCoordinate][bombWidthCoordinate].titleInText = 9; // It is checking whether a tile instance has been created.
                    CountUpAroundBomb(bombVerticalCoordinate, bombWidthCoordinate);
                }
            }
        }

        private void CountUpAroundBomb(int setVerticalCoordinate, int setWidthCoordinate) {
            /*  1 1 1
             *  1 B 1
             *  1 1 1 */
            for (int vertical = -1; vertical <= 1; vertical++) {
                for (int width = -1; width <= 1; width++) {
                    try {
                        int aroundBomb = numberOfSurroundingBombs[setVerticalCoordinate + vertical][setWidthCoordinate + width];
                        if (aroundBomb != BOMB) {
                            ++numberOfSurroundingBombs[setVerticalCoordinate + vertical][setWidthCoordinate + width];
                            if (firstClick) fieldTiles[setVerticalCoordinate + vertical][setWidthCoordinate + width].titleInText = aroundBomb;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        // nothing to do
                    }
                }
            }
        }
    }
}
