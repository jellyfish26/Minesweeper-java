import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MineSweeper extends Application {
    private Stage nowStage;

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
        int vertical = initialSettingDialog.InputNumberDialog("縦の長さを入力してください。", "フィールドの設定");
        System.out.println(vertical); // debug
        int width = initialSettingDialog.InputNumberDialog("横の長さを入力してください。", "フィールドの設定");
        System.out.println(width);
        int numberOfBombs = initialSettingDialog.InputNumberDialog("爆弾の個数を入力してください", "爆弾の設定");
        System.out.println(numberOfBombs);
        Pane displayLayout = new Pane();
        displayLayout.setPrefSize(1000, 1000);
        FieldCreation fieldCreation = new FieldCreation();

        // 1000 x 1000 fit the fields
        fieldCreation.rectangleLength = 1000 / (double)Math.max(vertical, width);
        System.out.println(fieldCreation.rectangleLength);

        fieldCreation.fieldInitialization(nowStage, vertical, width, numberOfBombs);
        fieldCreation.AddTileToPane(vertical, width, displayLayout);
        return displayLayout;
    }
}
