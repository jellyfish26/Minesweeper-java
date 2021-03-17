import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


class Tile extends StackPane {

  private int surroundBombs;
  private boolean isFlagEnabled;

  Text tileContentText = new Text();
  Integer titleInText;
  Boolean tileOpenCheck;
  Rectangle tileBorder;
  private FieldCreation field;
  private int verticalCoordinate;
  private int widthCoordinate;

  Tile (Integer tile, int vertical, int width, FieldCreation field, double rectangleLength) {
    this.field = field;
    tileBorder = new Rectangle(rectangleLength, rectangleLength);
    titleInText = tile;
    tileOpenCheck = false;
    verticalCoordinate = vertical;
    widthCoordinate = width;
    tileBorder.setFill(null);
    tileBorder.setStroke(Color.BLACK);
    tileContentText.setText("");
    tileContentText.setFont(Font.font(rectangleLength * 0.9));

    setAlignment(Pos.CENTER);
    getChildren().addAll(tileBorder, tileContentText);

    setOnMouseClicked(this::onMouseClick);
  }

  public void setSurroundBombs(int surroundBombs) {
    this.surroundBombs = surroundBombs;
  }

  public int getSurroundBombs() {
    return surroundBombs;
  }

  public void setFlag() {
    tileBorder.setFill(Color.GREENYELLOW);
    tileContentText.setFill(Color.BLACK);
    tileContentText.setText("F");
    isFlagEnabled = true;
  }

  public void removeFlag() {
    tileBorder.setFill(Color.WHITE);
    tileContentText.setFill(Color.WHITE);
    tileContentText.setText("");
    isFlagEnabled = false;
  }

  public boolean getFlagState() {
    return isFlagEnabled;
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
    tileOpenCheck = true;
    if (surroundBombs == 9) {
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


  private void onMouseClick(MouseEvent event) {
    // System.out.printf("%s, %s, %s\n", event, verticalCoordinate, widthCoordinate);
    if (event.getButton() == MouseButton.PRIMARY) {
      // At first it does not touch the bomb
      if (field.manipulateBombs.firstClick) {
        field.openTilesOfZero(verticalCoordinate, widthCoordinate);
        return;
      }
      if (surroundBombs == 9) {
        field.showAll(true); // true is click bomb
      } else {
        field.openTilesOfZero(verticalCoordinate, widthCoordinate);
      }
    } else {
      field.rightClick(verticalCoordinate, widthCoordinate);
    }
  }
}
