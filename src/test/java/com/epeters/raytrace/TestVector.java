package com.epeters.raytrace;

import org.junit.Test;

import static com.epeters.raytrace.Utils.BLUE;
import static com.epeters.raytrace.Utils.GREEN;
import static com.epeters.raytrace.Utils.SKY_BLUE;
import static com.epeters.raytrace.Utils.WHITE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static com.epeters.raytrace.Utils.RED;

public class TestVector {

    public static final double DELTA = 0.0001;

    @Test
    public void testToRgb() {
        assertArrayEquals(new int[]{ 255, 0, 0 }, RED.toRgb());
        assertArrayEquals(new int[]{ 0, 255, 0 }, GREEN.toRgb());
        assertArrayEquals(new int[]{ 0, 0, 255 }, BLUE.toRgb());
        assertArrayEquals(new int[]{ 255, 255, 255 }, WHITE.toRgb());
        assertArrayEquals(new int[]{ 127, 178, 255 }, SKY_BLUE.toRgb());
    }

    @Test
    public void testSquare() {
        Vector v = new Vector(2.0f, 3.0f, 2.0f);
        assertEquals(17.0f, v.square(), DELTA);
    }

    @Test
    public void testMag() {
        Vector v = new Vector(3.0f, 4.0f, 5.0f);
        assertEquals(7.071067f, v.mag(), DELTA);
    }

    @Test
    public void testPlus() {
        Vector a = new Vector(1.0f, 2.0f, 3.0f);
        Vector b = new Vector(2.0f, 3.0f, 4.0f);
        Vector exp = new Vector(3.0f, 5.0f, 7.0f);
        assertEquals(exp, a.plus(b));
        assertEquals(exp, b.plus(a));
    }

    @Test
    public void testMinus() {
        Vector a = new Vector(1.0f, 2.0f, 3.0f);
        Vector b = new Vector(2.0f, 3.0f, 4.0f);
        Vector exp1 = new Vector(-1.0f, -1.0f, -1.0f);
        Vector exp2 = new Vector(1.0f, 1.0f, 1.0f);
        assertEquals(exp1, a.minus(b));
        assertEquals(exp2, b.minus(a));
    }

    @Test
    public void testMul() {
        Vector a = new Vector(1.0f, 2.0f, 3.0f);
        Vector exp = new Vector(0.5f, 1.0f, 1.5f);
        assertEquals(exp, a.mul(0.5f));
    }

    @Test
    public void testDiv() {
        Vector a = new Vector(1.0f, 2.0f, 3.0f);
        Vector exp = new Vector(0.5f, 1.0f, 1.5f);
        assertEquals(exp, a.div(2.0f));
    }

    @Test
    public void testDot() {
        Vector a = new Vector(1.0f, 2.0f, 3.0f);
        Vector b = new Vector(0.5f, 1.0f, 1.5f);
        assertEquals(7.0f, a.dot(b), DELTA);
    }

    @Test
    public void testNormalize() {
        Vector a = new Vector(1.0f, 2.0f, 3.0f);
        Vector exp = new Vector(0.26726124f, 0.5345225f, 0.8017837f);
        assertEquals(exp, a.normalize());
    }
}
