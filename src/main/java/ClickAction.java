import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

class ClickAction extends NumberColor{

    Tile[][] fieldTiles;
    int[][] numberOfSurroundingBombs;
    boolean[][] flagInstall;
    Stage nowStage;
    int numberOfTileOpen;

    /* By using instances, we do not operate GC and operate applications with minimal memory. */
    MineSweeper usedMineSweeper;

    private void tileOpen(int vertical, int width, boolean lose) {
        try {
            --numberOfTileOpen;
            System.out.println(numberOfTileOpen);
            fieldTiles[vertical][width].tileOpenCheck = true;
            int tileNumber = numberOfSurroundingBombs[vertical][width];
            if (tileNumber != 9) {
                fieldTiles[vertical][width].tileContentText.setFill(numberOfBombsInColor(numberOfSurroundingBombs[vertical][width]));
            } else {
                if (!flagInstall[vertical][width]) fieldTiles[vertical][width].tileBorder.setFill(Color.DARKORCHID);
                if (!lose) fieldTiles[vertical][width].tileBorder.setFill(Color.GREENYELLOW);
                fieldTiles[vertical][width].tileContentText.setFill(Color.ORANGE);
            }
            fieldTiles[vertical][width].tileContentText.setText(fieldTiles[vertical][width].titleInText);
        } catch (IndexOutOfBoundsException e) {
            // nothing to do
        }
    }

    void openTilesOfZero(int vertical, int width) {
        try {
            if (fieldTiles[vertical][width].tileOpenCheck) return;
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
            if (result.resultDialog("あなたの勝ちです。", "リザルト") != ButtonType.YES) {
                System.exit(0);
            }
        }

        /* For once, in order to make garbage collection work. */
        result = null;
        fieldTiles = null;
        numberOfSurroundingBombs = null;

        usedMineSweeper.executionApplication(nowStage);
    }

    void rightClick(int vertical, int width) {
        if (fieldTiles[vertical][width].tileOpenCheck) return;
        if (!flagInstall[vertical][width]) {
            fieldTiles[vertical][width].tileBorder.setFill(Color.GREENYELLOW);
            fieldTiles[vertical][width].tileContentText.setFill(Color.BLACK);
            fieldTiles[vertical][width].tileContentText.setText("F");
            flagInstall[vertical][width] = true;
        } else {
            fieldTiles[vertical][width].tileBorder.setFill(null);
            fieldTiles[vertical][width].tileContentText.setFill(null);
            fieldTiles[vertical][width].tileContentText.setText("");
            flagInstall[vertical][width] = false;
        }
    }
}
