package deadlinemaster;

import static deadlinemaster.DeadlineMaster.APP_NAME;
import static deadlinemaster.DeadlineMaster.APP_VERSION;
import static deadlinemaster.DeadlineMaster.APP_ICON;
import static deadlinemaster.DeadlineMaster.APP_DEFAULT_FILE;
import static deadlinemaster.DeadlineMaster.ALERT_GRAPHICS_WIDTH;
import static deadlinemaster.DeadlineMaster.ALERT_GRAPHICS_HEIGHT;
import static deadlinemaster.DeadlineMaster.UPDATE_PERIOD;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller MVC dell'applicazione.
 */
public class DeadlineMasterController implements Initializable {

    /* MenuBar */
    @FXML
    private MenuItem miNuovo;

    @FXML
    private MenuItem miSalva;

    @FXML
    private MenuItem miEsci;

    @FXML
    private MenuItem miTaglia;

    @FXML
    private MenuItem miCopia;

    @FXML
    private MenuItem miIncolla;

    @FXML
    private MenuItem miDuplica;

    @FXML
    private MenuItem miElimina;

    @FXML
    private MenuItem miSelezionaTutto;

    @FXML
    private MenuItem miDeselezionaTutto;

    @FXML
    private MenuItem miPreferenze;

    @FXML
    private MenuItem miGuida;

    @FXML
    private MenuItem miInformazioni;

    /* ContextMenu */
    @FXML
    private MenuItem cmiTaglia;

    @FXML
    private MenuItem cmiCopia;

    @FXML
    private MenuItem cmiIncolla;

    @FXML
    private MenuItem cmiDuplica;

    @FXML
    private MenuItem cmiElimina;

    @FXML
    private MenuItem cmiSelezionaTutto;

    @FXML
    private MenuItem cmiDeselezionaTutto;

    /* ToolBar */
    @FXML
    private TextField txtDescrizione;

    @FXML
    private DatePicker dpScadenza;

    @FXML
    private Button btnSalva;

    @FXML
    private Button btnTaglia;

    @FXML
    private Button btnCopia;

    @FXML
    private Button btnIncolla;

    @FXML
    private Button btnElimina;

    @FXML
    private Button btnSelezionaTutto;

    /* TableView */
    @FXML
    private TableView<Deadline> tableView;

    @FXML
    private TableColumn<Deadline, String> tcDescrizione;

    @FXML
    private TableColumn<Deadline, String> tcScadenza;

    private ObservableList<Deadline> deadlines;

    private Clipboard clipboard;

    private Preferences preferences;

    private Alert errorAlert;

    private Alert guidaAlert;

    private Alert infoAlert;

    private Alert todayDeadlineAlert;

    private Alert tomorrowDeadlineAlert;

    private Image icon;

    private ImageView imageView;

    private long lastUpdate;

    /**
     * Metodo {@code initialize} dell'applicazione JavaFX.
     *
     * @param url URL utilizzata per risolvere i path relativi.
     * @param rb Risorse utilizzate per localizzare l'oggetto radice.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        deadlines = FXCollections.observableArrayList();
        clipboard = Clipboard.getSystemClipboard();
        preferences = Preferences.userNodeForPackage(DeadlineMaster.class);

        icon = new Image(DeadlineMaster.class.getResourceAsStream(APP_ICON));

        errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(APP_NAME);
        errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        ((Stage) errorAlert.getDialogPane().getScene().getWindow()).getIcons().add(icon);

        guidaAlert = new Alert(Alert.AlertType.INFORMATION);
        guidaAlert.setTitle("Guida di " + APP_NAME);
        guidaAlert.setHeaderText("Guida di " + APP_NAME);
        StringBuffer guidaContentText = new StringBuffer();
        guidaContentText.append("[ Creare una nuova scadenza ]\n");
        guidaContentText.append("Per creare una nuova scadenza digitare la descrizione nell'apposito campo di testo, selezionare la data e cliccare su [Salva] o dare [Invio]. ");
        guidaContentText.append("Il giorno prima della scadenza e il giorno stesso verrà visualizzato un messaggio di avviso all'avvio dell'applicazione. Le scadenze passate vengono eliminate automaticamente. \n\n");
        guidaContentText.append("[ Copiare una scadenza ]\n");
        guidaContentText.append("Gli strumenti [Taglia], [Copia] e [Incolla] consentono di tagliare, copiare e incollare scadenze, aggiungendole alla lista in automatico. ");
        guidaContentText.append("Incollando una scadenza in un altro programma, verrà incollata una stringa nel formato <descrizione>|<data>. \n\n");
        guidaContentText.append("[ Eliminare una scadenza ]\n");
        guidaContentText.append("Per eliminare una o più scadenze, selezionarle tenendo premuto [Ctrl] e scegliere l'opzione [Elimina] dal menu [Modifica]. \n\n");
        guidaAlert.setContentText(guidaContentText.toString());
        guidaAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        ((Stage) guidaAlert.getDialogPane().getScene().getWindow()).getIcons().add(icon);

        infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Informazioni su " + APP_NAME);
        infoAlert.setHeaderText(APP_NAME);
        infoAlert.setContentText(APP_NAME + " è un'applicazione per la gestione delle scadenze.\n\nAutore: Luca Dello Russo\nVersione: " + APP_VERSION);
        infoAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        ((Stage) infoAlert.getDialogPane().getScene().getWindow()).getIcons().add(icon);

        todayDeadlineAlert = new Alert(Alert.AlertType.INFORMATION);
        todayDeadlineAlert.setTitle(APP_NAME);
        todayDeadlineAlert.setHeaderText("La scadenza è oggi!");
        todayDeadlineAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        ((Stage) todayDeadlineAlert.getDialogPane().getScene().getWindow()).getIcons().add(icon);

        tomorrowDeadlineAlert = new Alert(Alert.AlertType.INFORMATION);
        tomorrowDeadlineAlert.setTitle(APP_NAME);
        tomorrowDeadlineAlert.setHeaderText("La scadenza è domani!");
        tomorrowDeadlineAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        ((Stage) tomorrowDeadlineAlert.getDialogPane().getScene().getWindow()).getIcons().add(icon);

        load();

        tcDescrizione.setCellValueFactory(new PropertyValueFactory<>("descrizione"));
        tcScadenza.setCellValueFactory(new PropertyValueFactory<>("scadenza"));
        tableView.setItems(deadlines);
        tableView.getSelectionModel()
                .setSelectionMode(SelectionMode.MULTIPLE);

        btnSalva.disableProperty()
                .bind(Bindings.createBooleanBinding(() -> txtDescrizione.getText().trim().isEmpty(),
                        txtDescrizione.textProperty()).or(dpScadenza.valueProperty().isNull()));
        miSalva.disableProperty()
                .bind(btnSalva.disableProperty());

        btnTaglia.disableProperty()
                .bind(tableView.getSelectionModel().selectedItemProperty().isNull().or(new SimpleListProperty<>(tableView.getSelectionModel().getSelectedItems()).sizeProperty().isNotEqualTo(1)));
        miTaglia.disableProperty()
                .bind(btnTaglia.disableProperty());
        cmiTaglia.disableProperty()
                .bind(miTaglia.disableProperty());

        btnCopia.disableProperty()
                .bind(btnTaglia.disableProperty());
        miCopia.disableProperty()
                .bind(btnCopia.disableProperty());
        cmiCopia.disableProperty()
                .bind(miCopia.disableProperty());

        miDuplica.disableProperty()
                .bind(btnTaglia.disableProperty());
        cmiDuplica.disableProperty()
                .bind(miDuplica.disableProperty());

        btnElimina.disableProperty()
                .bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        miElimina.disableProperty()
                .bind(btnElimina.disableProperty());
        cmiElimina.disableProperty()
                .bind(miElimina.disableProperty());

        btnSelezionaTutto.disableProperty()
                .bind(new SimpleListProperty<>(deadlines).emptyProperty());
        miSelezionaTutto.disableProperty()
                .bind(btnSelezionaTutto.disableProperty());
        cmiSelezionaTutto.disableProperty()
                .bind(miSelezionaTutto.disableProperty());

        miDeselezionaTutto.disableProperty()
                .bind(tableView.getSelectionModel().selectedItemProperty().isNull());
        cmiDeselezionaTutto.disableProperty()
                .bind(miDeselezionaTutto.disableProperty());

        dpScadenza.setValue(LocalDate.now());
        txtDescrizione.requestFocus();

        Platform.runLater(() -> {
            btnTaglia.setPrefWidth(Math.max(btnTaglia.getWidth(), btnCopia.getWidth()));
            btnCopia.setPrefWidth(Math.max(btnTaglia.getWidth(), btnCopia.getWidth()));
            btnElimina.setPrefWidth(Math.max(btnElimina.getWidth(), btnSelezionaTutto.getWidth()));
            btnSelezionaTutto.setPrefWidth(Math.max(btnElimina.getWidth(), btnSelezionaTutto.getWidth()));
            btnSalva.setPrefWidth(Math.max(btnSalva.getWidth(), btnIncolla.getWidth()));
            btnIncolla.setPrefWidth(Math.max(btnSalva.getWidth(), btnIncolla.getWidth()));
            tableView.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F5), () -> load());
            imageView = new ImageView(icon);
            imageView.setFitWidth(ALERT_GRAPHICS_WIDTH);
            imageView.setFitHeight(ALERT_GRAPHICS_HEIGHT);
            for (Deadline deadline : deadlines) {
                if (deadline.getScadenza().equals(LocalDate.now())) {
                    todayDeadlineAlert.setContentText(deadline.getDescrizione());
                    todayDeadlineAlert.setGraphic(imageView);
                    todayDeadlineAlert.showAndWait();
                }
            }

            for (Deadline deadline : deadlines) {
                if (deadline.getScadenza().equals(LocalDate.now().plusDays(1))) {
                    tomorrowDeadlineAlert.setContentText(deadline.getDescrizione());
                    tomorrowDeadlineAlert.setGraphic(imageView);
                    tomorrowDeadlineAlert.showAndWait();
                }
            }
        });

        lastUpdate = System.currentTimeMillis();
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                if (new File(preferences.get("saveFilePath", APP_DEFAULT_FILE)).lastModified() > lastUpdate) {
                    Platform.runLater(() -> {
                        load(false);
                    });
                }
                lastUpdate = System.currentTimeMillis();
            }
        }, 0, UPDATE_PERIOD);
    }

    private void load() {
        load(true);
    }

    private void load(boolean writeBack) {
        deadlines.clear();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(preferences.get("saveFilePath", APP_DEFAULT_FILE))))) {
            for (Deadline deadline : (ArrayList<Deadline>) objectInputStream.readObject()) {
                if (!deadline.getScadenza().isBefore(LocalDate.now())) {
                    deadlines.add(deadline);
                }
            }
            Collections.sort(deadlines);
            if (writeBack) {
                save();
            }
        } catch (FileNotFoundException ex) {
            //  Lista vuota.
        } catch (ClassNotFoundException | IOException ex) {
            errorAlert.setContentText("Errore durante la lettura delle deadlines!");
            errorAlert.showAndWait();
        }
    }

    private void save() {
        try (ObjectOutputStream objectInputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(preferences.get("saveFilePath", APP_DEFAULT_FILE))))) {
            objectInputStream.writeObject(new ArrayList(deadlines));
        } catch (IOException ex) {
            errorAlert.setContentText("Errore durante il salvataggio delle deadlines!");
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void nuovo(ActionEvent actionEvent) {
        txtDescrizione.requestFocus();
    }

    @FXML
    private void salva(ActionEvent actionEvent) {
        final String descrizione = txtDescrizione.getText().trim();
        final LocalDate scadenza = dpScadenza.getValue();
        deadlines.add(new Deadline(descrizione, scadenza));
        Collections.sort(deadlines);
        save();
        txtDescrizione.clear();
        dpScadenza.setValue(LocalDate.now());
        txtDescrizione.requestFocus();
    }

    @FXML
    private void esci(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    private void taglia(ActionEvent actionEvent) {
        copia(actionEvent);
        deadlines.remove(tableView.getSelectionModel().getSelectedItem());
        save();
    }

    @FXML
    private void copia(ActionEvent actionEvent) {
        final ClipboardContent clipboardContent = new ClipboardContent();
        final Deadline selectedItem = tableView.getSelectionModel().getSelectedItem();
        clipboardContent.putString(selectedItem.getDescrizione() + "|" + selectedItem.getScadenza());
        clipboard.setContent(clipboardContent);
    }

    @FXML
    private void incolla(ActionEvent actionEvent) {
        if (clipboard.hasString()) {
            try {
                final String descrizione = clipboard.getString().substring(0, clipboard.getString().lastIndexOf("|"));
                final LocalDate scadenza = LocalDate.parse(clipboard.getString().substring(clipboard.getString().lastIndexOf("|") + 1));
                deadlines.add(new Deadline(descrizione, scadenza));
                Collections.sort(deadlines);
                save();
            } catch (StringIndexOutOfBoundsException ex) {
                errorAlert.setContentText("L'elemento copiato non è una deadline!");
                errorAlert.showAndWait();
            }
        } else {
            errorAlert.setContentText("Nessun elemento da incollare!");
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void duplica(ActionEvent actionEvent) {
        final Deadline selectedItem = tableView.getSelectionModel().getSelectedItem();
        deadlines.add(new Deadline(selectedItem.getDescrizione(), selectedItem.getScadenza()));
        Collections.sort(deadlines);
        save();
    }

    @FXML
    private void elimina(ActionEvent actionEvent) {
        deadlines.removeAll(tableView.getSelectionModel().getSelectedItems());
        save();
    }

    @FXML
    private void selezionaTutto(ActionEvent actionEvent) {
        tableView.getSelectionModel().selectAll();
    }

    @FXML
    private void deselezionaTutto(ActionEvent actionEvent) {
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void preferenze(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(DeadlineMaster.class.getResource("views/Preferences.fxml"))));
        stage.getIcons().add(new Image(DeadlineMaster.class.getResourceAsStream(APP_ICON)));
        stage.setTitle("Preferenze di " + APP_NAME);
        stage.initOwner(tableView.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setMinHeight(200);
        stage.setHeight(stage.getMinHeight());
        stage.centerOnScreen();
        stage.showAndWait();
        load();
    }

    @FXML
    private void guida(ActionEvent actionEvent) {
        imageView = new ImageView(icon);
        imageView.setFitWidth(ALERT_GRAPHICS_WIDTH);
        imageView.setFitHeight(ALERT_GRAPHICS_HEIGHT);
        guidaAlert.setGraphic(imageView);
        guidaAlert.showAndWait();
    }

    @FXML
    private void informazioni(ActionEvent actionEvent) {
        imageView = new ImageView(icon);
        imageView.setFitWidth(ALERT_GRAPHICS_WIDTH);
        imageView.setFitHeight(ALERT_GRAPHICS_HEIGHT);
        infoAlert.setGraphic(imageView);
        infoAlert.showAndWait();
    }

}
