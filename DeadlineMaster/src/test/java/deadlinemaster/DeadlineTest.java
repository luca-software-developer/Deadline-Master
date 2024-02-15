package deadlinemaster;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeadlineTest {

    private Deadline instance;

    public DeadlineTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        instance = new Deadline("Example description", LocalDate.now());
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getDescrizione method, of class Deadline.
     */
    @Test
    public void testGetDescrizione() {
        System.out.println("getDescrizione");
        String expResult = "Example description";
        String result = instance.getDescrizione();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDescrizione method, of class Deadline.
     */
    @Test
    public void testSetDescrizione() {
        System.out.println("setDescrizione");
        String descrizione = "Test description";
        instance.setDescrizione(descrizione);
        assertEquals(descrizione, instance.getDescrizione());
    }

    /**
     * Test of getScadenza method, of class Deadline.
     */
    @Test
    public void testGetScadenza() {
        System.out.println("getScadenza");
        LocalDate expResult = LocalDate.now();
        LocalDate result = instance.getScadenza();
        assertEquals(expResult, result);
    }

    /**
     * Test of setScadenza method, of class Deadline.
     */
    @Test
    public void testSetScadenza() {
        System.out.println("setScadenza");
        LocalDate scadenza = LocalDate.now().plusDays(1);
        instance.setScadenza(scadenza);
        assertEquals(scadenza, instance.getScadenza());
    }

    /**
     * Test of compareTo method #1, of class Deadline.
     */
    @Test
    public void testCompareTo1() {
        System.out.println("compareTo #1");
        Deadline o = new Deadline("Test description", LocalDate.now().minusDays(1));
        int result = instance.compareTo(o);
        assertTrue(result < 0);
    }

    /**
     * Test of compareTo method #2, of class Deadline.
     */
    @Test
    public void testCompareTo2() {
        System.out.println("compareTo #2");
        Deadline o = new Deadline("Test description", LocalDate.now().plusDays(1));
        int result = instance.compareTo(o);
        assertTrue(result > 0);
    }

    /**
     * Test of compareTo method #3, of class Deadline.
     */
    @Test
    public void testCompareTo3() {
        System.out.println("compareTo #3");
        Deadline o = new Deadline("Example description", LocalDate.now());
        int result = instance.compareTo(o);
        assertTrue(result == 0);
    }

    /**
     * Test of compareTo method #4, of class Deadline.
     */
    @Test
    public void testCompareTo4() {
        System.out.println("compareTo #4");
        Deadline o = new Deadline("Test description", LocalDate.now());
        int result = instance.compareTo(o);
        assertTrue(result < 0);
    }

    /**
     * Test of compareTo method #5, of class Deadline.
     */
    @Test
    public void testCompareTo5() {
        System.out.println("compareTo #5");
        Deadline o = new Deadline("Alternative description", LocalDate.now());
        int result = instance.compareTo(o);
        assertTrue(result > 0);
    }

    /**
     * Test of compareTo method #6, of class Deadline.
     */
    @Test
    public void testCompareTo6() {
        System.out.println("compareTo #6");
        Deadline o = new Deadline("Alternative description", LocalDate.now().minusDays(1));
        int result = instance.compareTo(o);
        assertTrue(result < 0);
    }

}
