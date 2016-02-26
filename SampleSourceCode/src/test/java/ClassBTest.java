import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClassBTest {

    @org.junit.Test
        public void testMethodA() throws Exception {
        ClassB a = new ClassB();
        assertEquals(a.methodA(), "Beurk poire");
    }

    @org.junit.Test
    public void testMethodB() throws Exception {
        ClassB a = new ClassB();
        assertEquals(a.methodB(), "Beurk pomme");
    }

    @Test
    public void testMethodC() throws Exception {
        ClassB a = new ClassB();
        assertEquals(a.methodC(), 0);
    }

    @Test
    public void testMethodD() throws Exception {
        ClassB a = new ClassB();
        assertEquals(a.methodD(), 2);
    }

    @Test
    public void testMethodE() throws Exception {
        ClassB a = new ClassB();
        assertEquals(a.methodE(), 6);
    }

    @Test
    public void testMethodF() throws Exception {
        ClassB a = new ClassB();
        assertEquals(a.methodF(), 1);
    }

    @Test
    public void testMethodG() throws Exception {
        ClassB a = new ClassB();
        assertEquals(a.methodG(), 0);
    }
}