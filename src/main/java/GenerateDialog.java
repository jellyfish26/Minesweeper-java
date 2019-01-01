import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

class GenerateDialog {
    private String DO_NOT_QUIT_STRING = "7AE562B7CE81EA1218A088F6C4104F76D9F16027D2022DE2CE536C5727BF7C74850D6E3C6260A257A205ABC05C3126372DF807F9E73654AED7A70847E5DCA2B5";
    int  InputNumberDialog(String header, String title, int minValue, int maxValue) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText(header);
        inputDialog.setTitle(title);
        inputDialog.setContentText("入力");
        String result;
        while (true) {
            result = inputDialog.showAndWait().orElseGet(() -> {
                if (AnythingAlertDialog("終了しますか？", "終了動作") == ButtonType.OK) {
                    System.exit(0);
                }
                return DO_NOT_QUIT_STRING;
            });
            if (result.equals("")) {
                AnythingAlertDialog("入力してください。", "入力空です");
            } else if (!result.equals(DO_NOT_QUIT_STRING)) {
                try {
                    if (Integer.parseInt(result) < minValue) {
                        AnythingAlertDialog(minValue + "以上の整数を入力してください", "入力が不正です。");
                    } else if (Integer.parseInt(result) > maxValue) {
                        AnythingAlertDialog(maxValue + "以下の数字を入力してください。", "入力が不正です。");
                    } else {
                        return Integer.parseInt(result);
                    }
                } catch (NumberFormatException e) {
                    AnythingAlertDialog("整数を入力してください。", "入力が不正です。");
                }
            }
        }
    }

    private ButtonType AnythingAlertDialog(String header, String title) {
        Alert alertDialog = new Alert(Alert.AlertType.WARNING);
        alertDialog.setHeaderText(header);
        alertDialog.setTitle(title);
        alertDialog.setContentText("");
        return alertDialog.showAndWait().orElse(ButtonType.CANCEL);
    }

    ButtonType resultDialog(String header, String title) {
        Alert alertDialog = new Alert(Alert.AlertType.NONE, "", ButtonType.NO, ButtonType.YES);
        alertDialog.setHeaderText(header);
        alertDialog.setTitle(title);
        alertDialog.setContentText("続けますか？");
        return alertDialog.showAndWait().orElse(ButtonType.CANCEL);
    }
}
