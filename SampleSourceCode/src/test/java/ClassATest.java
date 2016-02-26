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

    @Test
    public void testMethodC() throws Exception {
        ClassA a = new ClassA();
        assertTrue(a.methodC());
    }

    @Test
    public void testMethodD() throws Exception {
        ClassA a = new ClassA();
        assertTrue(a.methodD());
    }

    @Test
    public void testMethodE() throws Exception {
        ClassA a = new ClassA();
        assertTrue(a.methodE());
    }

    @Test
    public void testMethodF() throws Exception {
        ClassA a = new ClassA();
        assertTrue(a.methodF());
    }

    @Test
    public void testMethodG() throws Exception {
        ClassA a = new ClassA();
        assertTrue(a.methodG());
    }
}