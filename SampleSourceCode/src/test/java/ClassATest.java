import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClassATest {

    @org.junit.Test
    public void testMethodA() throws Exception {
        ClassA a = new ClassA();
        assertEquals(a.methodA(), "Beurk goyave");
    }

    @Test
    public void testMethodB() throws Exception {
        ClassA a = new ClassA();
        assertTrue(a.methodB());
    }
}