import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

class Tile extends StackPane {

  final private int verticalIdx, widthIdx;
  private int surroundBombs;
  private boolean isFlagEnabled, isOpen, isBomb;
  private Rectangle tileBorder;
  private Text tileContentText = new Text();


  Tile(Integer tile, int verticalIdx, int widthIdx, double rectangleLength) {
    this.verticalIdx = verticalIdx;
    this.widthIdx = widthIdx;
    isFlagEnabled = false;
    isOpen = false;
    isBomb = false;
    surroundBombs = 0;

    tileBorder = new Rectangle(rectangleLength, rectangleLength);
    tileBorder.setFill(null);
    tileBorder.setStroke(Color.BLACK);
    tileContentText.setText("");
    tileContentText.setFont(Font.font(rectangleLength * 0.9));

    setAlignment(Pos.CENTER);
    getChildren().addAll(tileBorder, tileContentText);
  }

  public void incrementSurroundBombs() {
    surroundBombs++;
  }

  public void decrementSurroundBombs() {
    surroundBombs--;
  }

  public int getSurroundBombs() {
    return surroundBombs;
  }

  public void setFlag() {
    tileContentText.setFill(Color.BLACK);
    tileContentText.setText("F");
    tileBorder.setFill(Color.GREENYELLOW);
    isFlagEnabled = true;
  }

  public void removeFlag() {
    tileBorder.setFill(null);
    tileContentText.setFill(Color.WHITE);
    tileContentText.setText("");
    isFlagEnabled = false;
  }

  public boolean getFlagState() {
    return isFlagEnabled;
  }

  public int getVerticalIdx() {
    return verticalIdx;
  }

  public int getWidthIdx() {
    return widthIdx;
  }

  private Color numberOfBombsInColor(int bombNumber) {
    switch (bombNumber) {
    case 0:
      return Color.DEEPSKYBLUE;
    case 1:
      return Color.BLUE;
    case 2:
      return Color.GREEN;
    case 3:
      return Color.ORANGE;
    case 4:
      return Color.ORANGERED;
    case 5:
      return Color.RED;
    case 6:
      return Color.MEDIUMVIOLETRED;
    case 7:
      return Color.PURPLE;
    case 8:
      return Color.INDIGO;
    }
    return Color.BLACK;
  }

  public void open() {
    isOpen = true;
    if (isBomb) {
      if (isFlagEnabled) {
        tileBorder.setFill(Color.GREENYELLOW);
      } else {
        tileBorder.setFill(Color.DARKORCHID);
      }
      tileContentText.setFill(Color.ORANGE);
      tileContentText.setText("B");
      return;
    }
    tileContentText.setFill(numberOfBombsInColor(surroundBombs));
    tileContentText.setText(String.valueOf(surroundBombs));
  }

  // open state
  public boolean getTileState() {
    return isOpen;
  }

  public void setBomb() {
    isBomb = true;
  }

  public void removeBomb() {
    isBomb = false;
  }

  public boolean getIsBomb() {
    return isBomb;
  }
}
