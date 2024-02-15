package deadlinemaster;

import static deadlinemaster.DeadlineMaster.APP_NAME;
import static deadlinemaster.DeadlineMaster.APP_VERSION;
import static deadlinemaster.DeadlineMaster.APP_DEFAULT_FILE;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * Controller MVC della view {@code Preferences}.
 */
public class PreferencesController implements Initializable {

    /* Salvataggio */
    @FXML
    private TextField txtSaveFilePath;

    @FXML
    private Button btnSaveFilePath;

    @FXML
    private Button btnSaveFilePathReset;

    private Preferences preferences;

    /**
     * Metodo {@code initialize} della view {@code Preferences}.
     *
     * @param url URL utilizzata per risolvere i path relativi.
     * @param rb Risorse utilizzate per localizzare l'oggetto radice.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preferences = Preferences.userNodeForPackage(DeadlineMaster.class);
        txtSaveFilePath.setText(preferences.get("saveFilePath", APP_DEFAULT_FILE));
    }

    private void updateUI() {
        txtSaveFilePath.setText(preferences.get("saveFilePath", APP_DEFAULT_FILE));
        final Stage owner = (Stage) ((Stage) btnSaveFilePath.getScene().getWindow()).getOwner();
        owner.setTitle(String.format("%s ver. %s [%s]", APP_NAME, APP_VERSION, preferences.get("saveFilePath", APP_DEFAULT_FILE)));
    }

    @FXML
    private void openSaveFileChooser(ActionEvent actionEvent) throws BackingStoreException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sfoglia");
        fileChooser.setInitialFileName(preferences.get("saveFilePath", APP_DEFAULT_FILE));
        fileChooser.getExtensionFilters().add(new ExtensionFilter("File BIN (.bin)", "*.bin"));
        File file = fileChooser.showSaveDialog(btnSaveFilePath.getScene().getWindow());
        if (file != null) {
            preferences.put("saveFilePath", file.getAbsolutePath());
            preferences.sync();
            updateUI();
        }
    }

    @FXML
    private void resetSaveFile(ActionEvent actionEvent) throws BackingStoreException {
        preferences.put("saveFilePath", new File(APP_DEFAULT_FILE).getAbsolutePath());
        preferences.sync();
        updateUI();
    }

}
