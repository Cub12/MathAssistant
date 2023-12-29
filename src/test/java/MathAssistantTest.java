import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

public class MathAssistantTest {
    @Test
    public void testIsValidEquation1() {
        assertFalse(MathAssistant.isValidEquation("3+*4"));
    }

    @Test
    public void testIsValidEquation2() {
        assertFalse(MathAssistant.isValidEquation("4*-7"));
    }

    @Test
    public void testIsValidEquation3() {
        assertTrue(MathAssistant.isValidEquation("2*x+5=17"));
    }

    @Test
    public void testIsValidEquation4() {
        assertTrue(MathAssistant.isValidEquation("-1.3*5/x=1.2"));
    }

    @Test
    public void testIsValidEquation5() {
        assertTrue(MathAssistant.isValidEquation("2*x*x=10"));
    }

    @Test
    public void testIsValidEquation6() {
        assertTrue(MathAssistant.isValidEquation("2*(x+5+х)+5=10"));
    }

    @Test
    public void testIsValidEquation7() {
        assertTrue(MathAssistant.isValidEquation("17=2*x+5"));
    }

    @Test
    public void testIsValidEquation8() {
        assertFalse(MathAssistant.isValidEquation(""));
    }

    @Test
    public void testIsValidEquation9() {
        assertFalse(MathAssistant.isValidEquation("lalalala"));
    }

    @Test
    public void testIsValidEquation10() {
        assertFalse(MathAssistant.isValidEquation("1"));
    }

    @Test
    public void testIsValidEquation11() {
        assertFalse(MathAssistant.isValidEquation("1.1"));
    }

    @Test
    public void testIsValidEquation12() {
        assertFalse(MathAssistant.isValidEquation("1,1"));
    }

    @Test
    public void testIsValidEquation13() {
        assertFalse(MathAssistant.isValidEquation("1x"));
    }

    @Test
    public void testIsValidEquation14() {
        assertFalse(MathAssistant.isValidEquation("1*x"));
    }

    @Test
    public void testIsValidEquation15() {
        assertFalse(MathAssistant.isValidEquation("1/0"));
    }

    @Test
    public void testIsValidEquation16() {
        assertFalse(MathAssistant.isValidEquation("1"));
    }

    @Test
    public void testIsValidEquation17() {
        assertFalse(MathAssistant.isValidEquation("="));
    }

    @Test
    public void testIsValidEquation18() {
        assertFalse(MathAssistant.isValidEquation("1=1*/x"));
    }

    @Test
    public void testIsValidEquation19() {
        assertFalse(MathAssistant.isValidEquation("=x"));
    }

    @Test
    public void testIsValidEquation20() {
        assertTrue(MathAssistant.isValidEquation("x=x"));
    }

    @Test
    public void testIsValidEquation21() {
        assertTrue(MathAssistant.isValidEquation("x*x=x"));
    }

    @Test
    public void testIsValidEquation22() {
        assertTrue(MathAssistant.isValidEquation("x*x=5"));
    }

    @Test
    public void testIsValidEquation23() {
        assertTrue(MathAssistant.isValidEquation("1.2*x=5+(-3)"));
    }

    @Test
    public void testIsValidEquation24() {
        assertTrue(MathAssistant.isValidEquation("(((x*x)))=5,2"));
    }

    @Test
    public void testIsValidEquation25() {
        assertTrue(MathAssistant.isValidEquation("x*x=5.2"));
    }

    @Test
    public void testIsValidEquation26() {
        assertTrue(MathAssistant.isValidEquation("x*(-x)=5"));
    }

    @Test
    public void testIsValidEquation27() {
        assertTrue(MathAssistant.isValidEquation("(3)(x)=5"));
    }

    @Test
    public void testIsValidEquation28() {
        assertTrue(MathAssistant.isValidEquation("x * x - x= x + x * (2.33333 - 1)"));
    }

    @Test
    public void testIsValidEquation29() {
        assertFalse(MathAssistant.isValidEquation("x / (-0=5"));
    }

    @Test
    public void testIsValidEquation30() {
        assertFalse(MathAssistant.isValidEquation("x==5"));
    }


    @Test
    public void testIsRootOfEquation1() {
        assertTrue(MathAssistant.isRootOfEquation("2*x+5=17", 6));
    }

    @Test
    public void testIsRootOfEquation2() {
        assertTrue(MathAssistant.isRootOfEquation("-1.3*5/x=1.2", -5.4166666666));
    }

    @Test
    public void testIsRootOfEquation3() {
        assertTrue(MathAssistant.isRootOfEquation("2*x*x=10", 2.2360679774));
    }

    @Test
    public void testIsRootOfEquation4() {
        assertTrue(MathAssistant.isRootOfEquation("2*x*x=10", -2.2360679774));
    }

    @Test
    public void testIsRootOfEquation5() {
        assertTrue(MathAssistant.isRootOfEquation("2*(x+5+x)+5=10", -1.25));
    }

    @Test
    public void testIsRootOfEquation6() {
        assertTrue(MathAssistant.isRootOfEquation("17=2*x+5", 6));
    }

    @Test
    public void testIsRootOfEquation7() {
        assertTrue(MathAssistant.isRootOfEquation("x*x=x", 0));
    }

    @Test
    public void testIsRootOfEquation8() {
        assertTrue(MathAssistant.isRootOfEquation("x*x=x", 1));
    }

    @Test
    public void testIsRootOfEquation9() {
        assertTrue(MathAssistant.isRootOfEquation("(x+(x*x))*(x-x*x)=-12", 2));
    }

    @Test
    public void testIsRootOfEquation10() {
        assertTrue(MathAssistant.isRootOfEquation("x / -x*x=-x", 2));
    }
}