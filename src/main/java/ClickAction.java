class ClickAction extends FieldCreation {
    public void tileOpen(Tile[][] fieldTiles, int vertical, int width) {
        try {
            fieldTiles[vertical][width].tileOpenCHeck = true;
            fieldTiles[vertical][width].tileContentText.setText(fieldTiles[vertical][width].titleInText);
        } catch (IndexOutOfBoundsException e) {
            // nothing to do
        }
    }

    void openTilesOfZero(Tile[][] fieldTiles, int[][] numberOfSurroundingBombs, int vertical, int width) {
        try {
            if (fieldTiles[vertical][width].tileOpenCHeck) return;
            tileOpen(fieldTiles, vertical, width);
            if (numberOfSurroundingBombs[vertical][width] != 0) { return; }
        } catch (IndexOutOfBoundsException e) {
            return; // nothing to do
        }
        for (int verticalCoordinate = -1; verticalCoordinate <= 1; ++verticalCoordinate) {
            for (int widthCoordinate = -1; widthCoordinate <= 1; ++widthCoordinate) {
                openTilesOfZero(fieldTiles, numberOfSurroundingBombs, vertical + verticalCoordinate, width + widthCoordinate);
            }
        }
    }

    void clickBomb(Tile[][] fieldTiles) {
        for (int verticalCoordinate = 0; verticalCoordinate < fieldTiles.length; ++verticalCoordinate) {
            for (int widthCoordinate = 0; widthCoordinate < fieldTiles[verticalCoordinate].length; ++widthCoordinate) {
                tileOpen(fieldTiles, verticalCoordinate, widthCoordinate);
            }
        }
    }


}
