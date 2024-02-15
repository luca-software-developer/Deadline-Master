package deadlinemaster;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * La classe {@code Deadline} rappresenta una scadenza.
 */
public class Deadline implements Comparable<Deadline>, Serializable {

    private String descrizione;
    private LocalDate scadenza;

    /**
     * Costruttore della classe {@code Deadline}.
     *
     * @param descrizione Descrizione della scadenza.
     * @param scadenza Data della scadenza.
     */
    public Deadline(String descrizione, LocalDate scadenza) {
        this.descrizione = descrizione;
        this.scadenza = scadenza;
    }

    /**
     * Restituisce la descrizione della scadenza.
     *
     * @return Restituisce la descrizione della scadenza.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione della scadenza.
     *
     * @param descrizione Descrizione della scadenza.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce la data della scadenza.
     *
     * @return Restituisce la data della scadenza.
     */
    public LocalDate getScadenza() {
        return scadenza;
    }

    /**
     * Imposta la data della scadenza.
     *
     * @param scadenza Data della scadenza.
     */
    public void setScadenza(LocalDate scadenza) {
        this.scadenza = scadenza;
    }

    /**
     * Confronta due scadenze.
     *
     * @param o Scadenza da confrontare.
     * @return Restituisce il risultato del confronto.
     */
    @Override
    public int compareTo(Deadline o) {
        if (getScadenza().equals(o.getScadenza())) {
            return getDescrizione().compareTo(o.getDescrizione());
        }
        return o.getScadenza().compareTo(getScadenza());
    }

}
