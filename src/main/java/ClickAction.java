import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

class ClickAction {

    Tile[][] fieldTiles;
    int[][] numberOfSurroundingBombs;
    Stage nowStage;
    int numberOfTileOpen;

    private void tileOpen(int vertical, int width) {
        try {
            --numberOfTileOpen;
            System.out.println(numberOfTileOpen);
            fieldTiles[vertical][width].tileOpenCHeck = true;
            fieldTiles[vertical][width].tileContentText.setText(fieldTiles[vertical][width].titleInText);
        } catch (IndexOutOfBoundsException e) {
            // nothing to do
        }
    }

    void openTilesOfZero(int vertical, int width) {
        try {
            if (fieldTiles[vertical][width].tileOpenCHeck) return;
            tileOpen(vertical, width);
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
                tileOpen(verticalCoordinate, widthCoordinate);
            }
        }
        System.out.println("finish " + clickBomb);
        MineSweeper newGame = new MineSweeper();
        GenerateDialog result = new GenerateDialog();
        if (clickBomb) {
            if (result.resultDialog("あなたの負けです。", "リザルト") == ButtonType.YES) {
                newGame.executionApplication(nowStage);
            } else{
                System.exit(0);
            }
        } else {
            if (result.resultDialog("あなたの勝ちです。", "リザルト") == ButtonType.YES) {
                newGame.executionApplication(nowStage);
            } else{
                System.exit(0);
            }
        }
    }
}
