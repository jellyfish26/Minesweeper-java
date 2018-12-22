import javafx.scene.control.TextInputDialog;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class GenerateDialog {
    public int InputNumberDialog(String header, String title, String content) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText(header);
        inputDialog.setTitle(title);
        inputDialog.setContentText(content);
        while (true) {
            int[] returnValue = {-1};
            Optional<String> outputResult = inputDialog.showAndWait();
            outputResult.ifPresent(input -> returnValue[0] = Integer.valueOf(input));
            if (returnValue[0] != -1) {
                return  returnValue[0];
            }
        }
    }
}
