import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

class GenerateDialog {
    private String DO_NOT_QUIT_STRING = "7AE562B7CE81EA1218A088F6C4104F76D9F16027D2022DE2CE536C5727BF7C74850D6E3C6260A257A205ABC05C3126372DF807F9E73654AED7A70847E5DCA2B5";
    int  InputNumberDialog(String header, String title, String content) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText(header);
        inputDialog.setTitle(title);
        inputDialog.setContentText(content);
        String result;
        while (true) {
            result = inputDialog.showAndWait().orElseGet(() -> {
                if (AnythingAlertDialog(Alert.AlertType.WARNING, "終了しますか？", "終了動作", "") == ButtonType.OK) {
                    System.exit(0);
                }
                return DO_NOT_QUIT_STRING;
            });
            if (result.equals("")) {
                AnythingAlertDialog(Alert.AlertType.WARNING, "入力してください。", "入力空です", "");
            } else if (result.equals(DO_NOT_QUIT_STRING)) { // not and operation
            } else try {
                return Integer.parseInt(result);
            } catch (NumberFormatException e) {
                AnythingAlertDialog(Alert.AlertType.WARNING, "数字を入力してください。", "入力が不正です。", "");
            }
        }
    }

    public ButtonType AnythingAlertDialog(Alert.AlertType alertType, String header, String title, String content) {
        Alert alertDialog = new Alert(alertType);
        alertDialog.setHeaderText(header);
        alertDialog.setTitle(title);
        alertDialog.setContentText(content);
        return alertDialog.showAndWait().orElse(ButtonType.CANCEL);
    }
}
