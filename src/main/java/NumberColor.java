import javafx.scene.paint.Color;

class NumberColor {
  Color numberOfBombsInColor(int bombNumber) {
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
}
