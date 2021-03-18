import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MineSweeper extends Application {
  private Stage nowStage;
  private final int displayHeight= 1000;
  private final int displayWidth = displayHeight + 200;

  @Override
  public void start(Stage primaryStage) {
    executionApplication(primaryStage);
  }

  void executionApplication(Stage continuationStage) {
    nowStage = continuationStage;
    Scene scene = new Scene(generateMineSweeperField());
    nowStage.setTitle("マインスイーパー");
    nowStage.setScene(scene);
    nowStage.show();
  }

  private Parent generateMineSweeperField() {
    GenerateDialog initialSettingDialog = new GenerateDialog();
    int vertical = initialSettingDialog.InputNumberDialog("縦の長さを入力してください。(4 ~ 400)", "フィールドの設定", 4, 400);
    // System.out.println(vertical);
    int width = initialSettingDialog.InputNumberDialog("横の長さを入力してください。(4 ~ 400)", "フィールドの設定", 4, 400);
    // System.out.println(width);
    int numberOfBombs = initialSettingDialog.InputNumberDialog("爆弾の個数を入力してください。(1 ~ " + (vertical * width - 10) + ")", "爆弾の設定", 1, vertical * width - 10);
    // System.out.println(numberOfBombs);
    double rectangleLength = (double) Math.min(displayHeight, displayWidth) / (double)Math.max(vertical, width);
    FieldCreation fieldCreation = new FieldCreation(nowStage, this, vertical, width, numberOfBombs, displayHeight, displayWidth, rectangleLength);

    fieldCreation.AddTileToPane();
    fieldCreation.remainText();
    return fieldCreation.getDisplayBase();
  }
}
