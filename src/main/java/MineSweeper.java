import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MineSweeper extends Application {

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(generateMineSweeperFiled());
        primaryStage.setTitle("マインスイーパー");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent generateMineSweeperFiled() {
        GenerateDialog initialSettingDialog = new GenerateDialog();
        int vertical = initialSettingDialog.InputNumberDialog("縦の長さを入力してください。", "フィールドの設定", "入力");
        System.out.println(vertical); // debug
        int width = initialSettingDialog.InputNumberDialog("横の長さを入力してください。", "フィールドの設定", "入力");
        System.out.println(width);
        int numberOfBombs = initialSettingDialog.InputNumberDialog("爆弾の個数を入力してください", "爆弾の設定", "入力");
        System.out.println(numberOfBombs);

        Pane displayLayout = new Pane();
        displayLayout.setPrefSize(750, 750);
        FieldCreation fieldCreation = new FieldCreation();
        fieldCreation.filedInitialization(vertical, width, numberOfBombs);
        fieldCreation.AddTileToPane(vertical, width, displayLayout);
        return displayLayout;
    }
}
