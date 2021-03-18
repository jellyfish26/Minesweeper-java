import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;


class FieldCreation {

  final private int verticalSize, widthSize;
  final double rectangleLength;
  final Pane displayBase;
  private boolean isGameStarted;

  Tile[][] fieldTiles;
  Stage nowStage;
  int numberOfTileOpen;
  StopWatch stopWatch = new StopWatch();
  final int BOMB = 9;
  Text remainBombs;
  int numberOfBombs;
  int numberOfFlags = 0;

  // By using instances, we do not operate GC and operate applications with minimal memory.
  MineSweeper usedMineSweeper;

  FieldCreation(Stage nowStage,
              MineSweeper usedMineSweeper,
              int verticalSize,
              int widthSize,
              int numberOfBombs,
              int displayHeight,
              int displayWidth,
              double rectangleLength) {
    this.nowStage = nowStage;
    this.usedMineSweeper = usedMineSweeper;
    this.verticalSize = verticalSize;
    this.widthSize = widthSize;
    this.numberOfBombs = numberOfBombs;
    this.rectangleLength = rectangleLength;
    
    isGameStarted = false;

    this.displayBase = new Pane();
    displayBase.setPrefSize(displayHeight, displayWidth);

    displayBase.setOnMouseClicked(this::onMouseClick);

    numberOfTileOpen = verticalSize * widthSize - numberOfBombs;
    fieldTiles = new Tile[verticalSize][widthSize];
    stopWatch.start();
    remainBombs = new Text(Integer.toString(numberOfBombs));

    for (int verticalIdx = 0; verticalIdx < verticalSize; ++verticalIdx) {
      for (int widthIdx = 0; widthIdx < widthSize; ++widthIdx) {
        fieldTiles[verticalIdx][widthIdx] = new Tile(0, verticalIdx, widthIdx, this, rectangleLength);
      }
    }
  }

  // return true  : this index is verify
  // return false : this index is illegal
  private boolean checkIdx(int verticalIdx, int widthIdx) {
    if (verticalIdx < 0 || verticalIdx >= verticalSize) return false;
    if (widthIdx < 0 || widthIdx >= widthSize) return false;
    return true;
  }

  private void tileOpen(int verticalIdx, int widthIdx) {
    if (!checkIdx(verticalIdx, widthIdx)) return;
    Queue<Integer> exploreVertical = new ArrayDeque<>();
    Queue<Integer> exploreWidth = new ArrayDeque<>();
    exploreVertical.add(verticalIdx);
    exploreWidth.add(widthIdx);

    while (!exploreVertical.isEmpty()) {
      int nowVertexIdx = exploreVertical.poll();
      int nowWidthIdx = exploreWidth.poll();
      for (int moveVertex = -1; moveVertex <= 1; ++moveVertex) {
        for (int moveWidth = -1; moveWidth <= 1; ++moveWidth) {
          int nextVertexIdx = nowVertexIdx + moveVertex;
          int nextWidthIdx = nowWidthIdx + moveWidth;
          if (!checkIdx(nextVertexIdx, nextWidthIdx)) {
            continue;
          }
          
          boolean cannotOpen = false;
          cannotOpen |= fieldTiles[nextVertexIdx][nextWidthIdx].getTileState();
          cannotOpen |= (fieldTiles[nextVertexIdx][nextVertexIdx].getSurroundBombs() == 9);

          if (cannotOpen) {
            continue;
          }
          fieldTiles[nextVertexIdx][nextWidthIdx].open();
          exploreVertical.add(nextVertexIdx);
          exploreWidth.add(nextWidthIdx);
        }
      }
    }
  }

  void showAll(boolean clickBomb) {
    String elapsedTime = stopWatch.stop();
    for (int verticalCoordinate = 0; verticalCoordinate < fieldTiles.length; ++verticalCoordinate) {
      for (int widthCoordinate = 0; widthCoordinate < fieldTiles[verticalCoordinate].length; ++widthCoordinate) {
        tileOpen(verticalCoordinate, widthCoordinate);
      }
    }
    // System.out.println("finish " + clickBomb);
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

    // For once, in order to make garbage collection work.
    result = null;
    fieldTiles = null;

    usedMineSweeper.executionApplication(nowStage);
  }

  // flag install(establish)
  void rightClick(int vertical, int width) {
    if (fieldTiles[vertical][width].getTileState()) return;
    if (!fieldTiles[vertical][width].getFlagState()) {
      ++numberOfFlags;
      fieldTiles[vertical][width].setFlag();
    } else {
      --numberOfFlags;
      fieldTiles[vertical][width].removeFlag();
    }
    if (numberOfFlags < 0) numberOfFlags = 0;
    remainBombs.setText(Integer.toString(numberOfBombs - numberOfFlags));
  }

  private void onMouseClick(MouseEvent event) {
    Object obj = event.getTarget();
    if (!(obj instanceof Tile)) {
      return;
    }
    Tile clickedTile = (Tile)obj;
    int verticalIdx = clickedTile.getVerticalIdx(), widthIdx = clickedTile.getWidthIdx();
    if (event.getButton() == MouseButton.PRIMARY) {
      // At first id does not touch the bomb
      if (!isGameStarted) {
        tileOpen(verticalIdx, widthIdx);
        isGameStarted = true;
        return;
      }

      if (clickedTile.getSurroundBombs() == 9) {
        showAll(true);
      } else {
        tileOpen(verticalIdx, widthIdx);
      }
    } else {
      rightClick(verticalIdx, widthIdx);
    }
  }

  public void AddTileToPane() {
    for (int verticalCoordinate = 0; verticalCoordinate < verticalSize; ++verticalCoordinate) {
      for (int widthCoordinate = 0; widthCoordinate < widthSize; ++widthCoordinate) {
        Tile tile = fieldTiles[verticalCoordinate][widthCoordinate];
        tile.setTranslateX(rectangleLength * widthCoordinate);
        tile.setTranslateY(rectangleLength * verticalCoordinate);
        displayBase.getChildren().add(tile);
      }
    }
  }

  public void remainText() {
    Text DISPLAY_TEXT = new Text("残りの爆弾の個数");
    DISPLAY_TEXT.setLayoutX(1020);
    DISPLAY_TEXT.setLayoutY(100);
    DISPLAY_TEXT.setFont(Font.font(20));
    displayBase.getChildren().addAll(DISPLAY_TEXT);
    remainBombs.setLayoutX(1055);
    remainBombs.setLayoutY(150);
    remainBombs.setFont(Font.font(40));
    displayBase.getChildren().addAll(remainBombs);
  }

  public Parent getDisplayBase() {
    return displayBase;
  }
}
