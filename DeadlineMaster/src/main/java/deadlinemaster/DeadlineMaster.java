package deadlinemaster;

import java.io.File;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * La classe {@code DeadlineMaster} rappresenta l'applicazione.
 */
public class DeadlineMaster extends Application {

    /**
     * Nome dell'applicazione.
     */
    public static final String APP_NAME = "Deadline Master";

    /**
     * Versione dell'applicazione.
     */
    public static final String APP_VERSION = "1.1.0.0";

    /**
     * Icona dell'applicazione.
     */
    public static final String APP_ICON = "images/icon.png";

    /**
     * File di dati di default dell'applicazione.
     */
    public static final String APP_DEFAULT_FILE = "deadlines.bin";

    /**
     * Larghezza dell'icona per le finestre di dialogo.
     */
    public static final int ALERT_GRAPHICS_WIDTH = 56;

    /**
     * Altezza dell'icona per le finestre di dialogo.
     */
    public static final int ALERT_GRAPHICS_HEIGHT = 56;

    /**
     * Periodo di aggiornamento della lista di scadenze (ms).
     */
    public static final int UPDATE_PERIOD = 100;

    private Preferences preferences;

    /**
     * Metodo {@code start} dell'applicazione JavaFX.
     *
     * @param stage Stage primario.
     * @throws IOException Sollevata in caso di errori durante l'accesso alle
     * risorse.
     * @throws BackingStoreException Sollevata in caso di errori durante
     * l'accesso alle preferenze.
     */
    @Override
    public void start(Stage stage) throws IOException, BackingStoreException {
        preferences = Preferences.userNodeForPackage(DeadlineMaster.class);
        if (preferences.get("saveFilePath", APP_DEFAULT_FILE).equals(APP_DEFAULT_FILE)) {
            preferences.put("saveFilePath", new File(APP_DEFAULT_FILE).getAbsolutePath());
            preferences.sync();
        }
        stage.setScene(new Scene(FXMLLoader.load(DeadlineMaster.class.getResource("views/DeadlineMaster.fxml"))));
        stage.getIcons().add(new Image(DeadlineMaster.class.getResourceAsStream(APP_ICON)));
        stage.setTitle(String.format("%s ver. %s [%s]", APP_NAME, APP_VERSION, preferences.get("saveFilePath", APP_DEFAULT_FILE)));
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Metodo {@code main} dell'applicazione Java.
     *
     * @param args Argomenti da riga di comando.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
