import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

class FieldCreation {

  final private int verticalSize, widthSize;
  final double rectangleLength;
  final Pane displayBase;
  private boolean isGameStarted;
  private final int bombNum;

  Tile[][] fieldTiles;
  int numberOfTileOpen;
  StopWatch stopWatch = new StopWatch();
  Text remainBombs;
  int numberOfFlags = 0;

  FieldCreation(int verticalSize, int widthSize, int bombNum, int displayHeight, int displayWidth,
      double rectangleLength) {
    this.verticalSize = verticalSize;
    this.widthSize = widthSize;
    this.bombNum = bombNum;
    this.rectangleLength = rectangleLength;

    isGameStarted = false;

    this.displayBase = new Pane();
    displayBase.setPrefSize(displayHeight, displayWidth);

    displayBase.setOnMouseClicked(this::onMouseClick);

    numberOfTileOpen = verticalSize * widthSize - bombNum;
    fieldTiles = new Tile[verticalSize][widthSize];
    stopWatch.start();
    remainBombs = new Text(Integer.toString(bombNum));

    for (int verticalIdx = 0; verticalIdx < verticalSize; ++verticalIdx) {
      for (int widthIdx = 0; widthIdx < widthSize; ++widthIdx) {
        fieldTiles[verticalIdx][widthIdx] = new Tile(0, verticalIdx, widthIdx, rectangleLength);
      }
    }
    initBomb();
  }

  // return true : this index is verify
  // return false : this index is illegal
  private boolean checkIdx(int verticalIdx, int widthIdx) {
    if (verticalIdx < 0 || verticalIdx >= verticalSize)
      return false;
    if (widthIdx < 0 || widthIdx >= widthSize)
      return false;
    return true;
  }

  private void initBomb() {
    List<Tile> tiles = new ArrayList<>(verticalSize * widthSize);
    for (int verticalIdx = 0; verticalIdx < verticalSize; ++verticalIdx) {
      for (int widthIdx = 0; widthIdx < widthSize; ++widthIdx) {
        tiles.add(fieldTiles[verticalIdx][widthIdx]);
      }
    }
    Collections.shuffle(tiles);
    for (int cnt = 0; cnt < bombNum; ++cnt) {
      Tile now = tiles.get(cnt);
      now.setBomb();
      // count up surrond
      for (int moveVertical = -1; moveVertical <= 1; ++moveVertical) {
        for (int moveWidth = -1; moveWidth <= 1; ++moveWidth) {
          int targetVerticalIdx = moveVertical + now.getVerticalIdx();
          int targetWidthIdx = moveWidth + now.getWidthIdx();
          if (!checkIdx(targetVerticalIdx, targetWidthIdx)) {
            continue;
          }
          fieldTiles[targetVerticalIdx][targetWidthIdx].incrementSurroundBombs();
        }
      }
    }
  }

  private void tileOpen(int verticalIdx, int widthIdx) {
    if (!checkIdx(verticalIdx, widthIdx)) {
      return;
    }
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
          Tile targetTile = fieldTiles[nextVertexIdx][nextWidthIdx];

          boolean cannotOpen = false;
          cannotOpen |= targetTile.getTileState();
          cannotOpen |= targetTile.getFlagState();
          cannotOpen |= targetTile.getIsBomb();
          if (cannotOpen) {
            continue;
          }

          targetTile.open();

          if (targetTile.getSurroundBombs() == 0) {
            exploreVertical.add(nextVertexIdx);
            exploreWidth.add(nextWidthIdx);
          }
        }
      }
    }
  }

  void gameEnd(boolean clickBomb) {
    String elapsedTime = stopWatch.stop();
    for (int verticalIdx = 0; verticalIdx < verticalSize; ++verticalIdx) {
      for (int widthIdx = 0; widthIdx < widthSize; ++widthIdx) {
        fieldTiles[verticalIdx][widthIdx].open();
      }
    }
    GenerateDialog result = new GenerateDialog();
    if (clickBomb) {
      if (result.resultDialog("You lose...", "Result") != ButtonType.YES) {
        System.exit(0);
      }
    } else {
      if (result.resultDialog("You win!! (Elapsed time:" + elapsedTime + ")", "Result") != ButtonType.YES) {
        System.exit(0);
      }
    }
    MineSweeper.newGame();
  }

  // flag install(establish)
  void rightClick(int vertical, int width) {
    if (fieldTiles[vertical][width].getTileState())
      return;
    if (!fieldTiles[vertical][width].getFlagState()) {
      ++numberOfFlags;
      fieldTiles[vertical][width].setFlag();
    } else {
      --numberOfFlags;
      fieldTiles[vertical][width].removeFlag();
    }
    if (numberOfFlags < 0)
      numberOfFlags = 0;
    remainBombs.setText(Integer.toString(bombNum - numberOfFlags));
  }

  private void onMouseClick(MouseEvent event) {
    Object obj = event.getTarget();
    if (!(obj instanceof Tile)) {
      return;
    }
    Tile clickedTile = (Tile) obj;
    int verticalIdx = clickedTile.getVerticalIdx(), widthIdx = clickedTile.getWidthIdx();
    if (event.getButton() == MouseButton.PRIMARY) {
      // At first id does not touch the bomb
      if (!isGameStarted) {
        tileOpen(verticalIdx, widthIdx);
        isGameStarted = true;
        return;
      }

      if (clickedTile.getIsBomb()) {
        gameEnd(true);
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
    Text display_text = new Text("Remain bombs");
    display_text.setLayoutX(1020);
    display_text.setLayoutY(100);
    display_text.setFont(Font.font(20));
    displayBase.getChildren().addAll(display_text);
    remainBombs.setLayoutX(1055);
    remainBombs.setLayoutY(150);
    remainBombs.setFont(Font.font(40));
    displayBase.getChildren().addAll(remainBombs);
  }

  public Parent getDisplayBase() {
    return displayBase;
  }
}
