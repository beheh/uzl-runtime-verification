package de.uni_luebeck.isp.quadratic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test cases for root computation of quadratic functions.
 *
 * @author Benedict Etzel <benedict.etzel@informatik.uni-luebeck.de>
 *
 */
public class QuadraticTest {

    @Test
    public void testInvalid() {
        assertEquals("Invalid number of arguments", "Invalid",
                Quadratic.roots("1 2 3 4 5"));
    }

    @Test
    public void testNullInput() {
        assertEquals("Null input must be invalid", "Invalid",
                Quadratic.roots(null));
    }

    @Test
    public void testEmptyString() {
        assertEquals("Empty string must be invalid", "Invalid",
                Quadratic.roots(""));
    }

    @Test
    public void testEmptyLines() {
        assertEquals("Empty line", "Invalid",
                Quadratic.roots("\n"));
        assertEquals("Multiple empty lines", "Invalid\nInvalid",
                Quadratic.roots("\n\n"));
    }

    @Test
    public void testNonNumeric() {
        assertEquals("Invalid string as parameter", "Invalid",
                Quadratic.roots("1 2 Luebeck"));
        assertEquals("Invalid dangling string", "Invalid",
                Quadratic.roots("1 2 Luebeck 3"));
    }

    /* I'll admit that this and the following test are *slightly* unfair,
     since parseDouble will happily parse NaN and Infinity. I guess an empty
     output would be okay here too, but NaN and Infinity are not part of ℝ
     as per input specification so they're really just invalid strings.
     (see http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/sun/misc/FloatingDecimal.java#l1854 )
     */
    @Test
    public void testNaN() {
        assertEquals("Invalid parameter \"NaN\"", "Invalid",
                Quadratic.roots("NaN 1 1"));
    }

    @Test
    public void testInfinity() {
        assertEquals("Invalid parameter \"Infinity\"", "Invalid",
                Quadratic.roots("Infinity 1 1"));
    }
    /* End of unfair tests :-) */

    @Test
    public void testZeroDenominator() {
        assertEquals("Solution for 0 2 3 is -1.5", "-1.5",
                Quadratic.roots("0 2 3"));
    }

    @Test
    public void testZeroDiscriminant() {
        assertEquals("Solution for 1 2 1 is -1", "-1",
                Quadratic.roots("1 2 1"));
    }

    @Test
    public void testNegativeDiscriminant() {
        assertEquals("No solution for 2 1 2", "",
                Quadratic.roots("2 1 2"));
    }

    @Test
    public void testResultRounding() {
        // more precise solutions are -2.61803 and -0.38193
        assertEquals("Solutions for 1 3 1 are -2.618 and -0.382", "-2.618 -0.382",
                Quadratic.roots("1 3 1"));
    }

    @Test
    public void testSeparator() {
        assertEquals("Separating tabs should be invalid", "Invalid",
                Quadratic.roots("2\t-4\t-16"));

        /* The following assertion is also slightly unfair: parseDouble will
         trim, so if we split by whitespaces only, some special characters
         with an ascii code <= \u0020 won't throw a NumberFormatException -
         even though we're only allowed to separate by spaces.
        
         Also, in reality we should be following the robustness principle:
         "Be conservative in what you send, be liberal in what you accept"
        
         But since this is purely an academic exercise I'll just have some fun :-)
         */
        assertEquals("Invalid separating characters", "Invalid",
                Quadratic.roots("22\t -4 " + ((char) 7) + "-16"));
    }

    @Test
    public void testLeadingSpaces() {
        assertEquals("Leading spaces should be invalid", "Invalid",
                Quadratic.roots("   2 -4 -16"));
    }

    @Test
    public void testMultipleSpaces() {
        assertEquals("Multiple spaces should be allowed", "-2 4",
                Quadratic.roots("2  -4  -16"));
    }

    @Test
    public void testTrailingSpaces() {
        assertEquals("Roots of 2 -4 -16 with trailing spaces must be -2 and 4 (in order)", "-2 4",
                Quadratic.roots("2 -4 -16   "));
    }

    @Test
    public void testTrailingSpecialCharacters() {
        assertEquals("Trailing tab should make result invalid", "Invalid",
                Quadratic.roots("2 -4 -16   \t"));
    }

    @Test
    public void testSorting() {
        assertEquals("Solutions for 2 -4 -16 are -2 and 4 (in order)", "-2 4",
                Quadratic.roots("2 -4 -16"));
        assertEquals("Solutions for 2 4 -16 are -4 and 2 (in order)", "-4 2",
                Quadratic.roots("2 4 -16"));
    }

    @Test
    public void testMultiline() {
        assertEquals("Multiline solutions", "-2 4\n-4 2",
                Quadratic.roots("2 -4 -16\n2 4 -16"));
    }
}
