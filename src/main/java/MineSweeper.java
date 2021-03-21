import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MineSweeper extends Application {
  private static Stage nowStage;
  private static final int DISPLAY_HEIGHT = 1000;
  private static final int DISPLAY_WIDTH = DISPLAY_HEIGHT + 200;

  @Override
  public void start(Stage primaryStage) {
    nowStage = primaryStage;
    newGame();
  }

  public static void newGame() {
    Scene scene = new Scene(generateMineSweeperField());
    nowStage.setTitle("Minesweeper");
    nowStage.setScene(scene);
    nowStage.show();
  }

  static private Parent generateMineSweeperField() {
    GenerateDialog initialSettingDialog = new GenerateDialog();
    int vertical = initialSettingDialog.InputNumberDialog("Input vertical length. (4 ~ 400)", "Field Settings", 4, 400);
    int width = initialSettingDialog.InputNumberDialog("Input width length. (4 ~ 400)", "Field Settings", 4, 400);
    int numberOfBombs = initialSettingDialog.InputNumberDialog(
        "Input number of bombs. (1 ~ " + (vertical * width - 10) + ")", "Bomb Settings", 1, vertical * width - 10);

    double rectangleLength = (double) Math.min(DISPLAY_HEIGHT, DISPLAY_WIDTH) / (double) Math.max(vertical, width);
    FieldCreation fieldCreation = new FieldCreation(vertical, width, numberOfBombs, DISPLAY_HEIGHT, DISPLAY_WIDTH,
        rectangleLength);

    fieldCreation.AddTileToPane();
    fieldCreation.remainText();
    return fieldCreation.getDisplayBase();
  }
}
