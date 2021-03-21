import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

class GenerateDialog {
  int inputNumberDialog(String header, String title, int minValue, int maxValue) {
    TextInputDialog inputDialog = new TextInputDialog();
    inputDialog.setHeaderText(header);
    inputDialog.setTitle(title);
    inputDialog.setContentText("Input");
    String result;
    while (true) {
      result = inputDialog.showAndWait().orElse(null);
      if (result == null) {
        System.exit(0);
      }
      if (result.equals("")) {
        anythingAlertDialog("Please enter.", "Input is empty.");
        result = null;
      } else {
        try {
          if (Integer.parseInt(result) < minValue) {
            anythingAlertDialog("Enter the integer greater than or equal to " + minValue, "Illegal Input");
          } else if (Integer.parseInt(result) > maxValue) {
            anythingAlertDialog("Enter the integer less than or equal to " + maxValue, "Illegal Input");
          } else {
            return Integer.parseInt(result);
          }
        } catch (NumberFormatException e) {
          anythingAlertDialog("Please input integer.", "Illegal Input");
        }
      }
    }
  }

  private ButtonType anythingAlertDialog(String header, String title) {
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
    alertDialog.setContentText("Do you want you continue?");
    return alertDialog.showAndWait().orElse(ButtonType.CANCEL);
  }
}
