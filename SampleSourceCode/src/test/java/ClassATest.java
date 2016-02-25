import static org.junit.Assert.*;

public class ClassATest {

    @org.junit.Test
    public void testMethodA() throws Exception {
        ClassA a = new ClassA();
        assertEquals(a.methodA(), "Beurk goyave");
    }
}