package com.epeters.raytrace.utils;

import com.epeters.raytrace.Vector;
import org.junit.Test;

import static com.epeters.raytrace.Utils.BLUE;
import static com.epeters.raytrace.Utils.GREEN;
import static com.epeters.raytrace.Utils.SKY_BLUE;
import static com.epeters.raytrace.Utils.WHITE;
import static com.epeters.raytrace.Utils.RED;
import static com.epeters.raytrace.Vector.vec;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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
        Vector v = vec(2.0f, 3.0f, 2.0f);
        assertEquals(17.0f, v.square(), DELTA);
    }

    @Test
    public void testMag() {
        Vector v = vec(3.0f, 4.0f, 5.0f);
        assertEquals(7.071067f, v.mag(), DELTA);
    }

    @Test
    public void testPlus() {
        Vector a = vec(1.0f, 2.0f, 3.0f);
        Vector b = vec(2.0f, 3.0f, 4.0f);
        Vector exp = vec(3.0f, 5.0f, 7.0f);
        assertEquals(exp, a.plus(b));
        assertEquals(exp, b.plus(a));
    }

    @Test
    public void testMinus() {
        Vector a = vec(1.0f, 2.0f, 3.0f);
        Vector b = vec(2.0f, 3.0f, 4.0f);
        Vector exp1 = vec(-1.0f, -1.0f, -1.0f);
        Vector exp2 = vec(1.0f, 1.0f, 1.0f);
        assertEquals(exp1, a.minus(b));
        assertEquals(exp2, b.minus(a));
    }

    @Test
    public void testMul() {
        Vector a = vec(1.0f, 2.0f, 3.0f);
        Vector exp = vec(0.5f, 1.0f, 1.5f);
        assertEquals(exp, a.mul(0.5f));
    }

    @Test
    public void testDiv() {
        Vector a = vec(1.0f, 2.0f, 3.0f);
        Vector exp = vec(0.5f, 1.0f, 1.5f);
        assertEquals(exp, a.div(2.0f));
    }

    @Test
    public void testDot() {
        Vector a = vec(1.0f, 2.0f, 3.0f);
        Vector b = vec(0.5f, 1.0f, 1.5f);
        assertEquals(7.0f, a.dot(b), DELTA);
    }

    @Test
    public void testNormalize() {
        Vector a = vec(1.0f, 2.0f, 3.0f);
        Vector exp = vec(0.26726124f, 0.5345225f, 0.8017837f);
        assetApproximatelyEqual(exp, a.normalize());
    }

    @Test
    public void testCross() {
        Vector l = vec(3.0, -3.0, 1.0);
        Vector r = vec(4.0, 9.0, 2.0);
        Vector expected = vec(-15.0, -2.0, 39.0);
        assertEquals(expected, l.cross(r));
    }

    private void assetApproximatelyEqual(Vector expected, Vector actual) {
        assertEquals(expected.x(), actual.x(), 1e-6);
        assertEquals(expected.y(), actual.y(), 1e-6);
        assertEquals(expected.z(), actual.z(), 1e-6);
    }
}
