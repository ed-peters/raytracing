package com.epeters.raytrace;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static com.epeters.raytrace.Vector.ORIGIN;

public class TestRay {

    @Test
    public void testAtAlongAxis() {
        Ray ray = new Ray(ORIGIN, new Vector(1.0f, 0.0f, 0.0f));
        assertEquals(new Vector(-2.0f, 0.0f, 0.0f), ray.at(-2.0f));
        assertEquals(new Vector(0.5f, 0.0f, 0.0f), ray.at(0.5f));
        assertEquals(ORIGIN, ray.at(0.0f));
        assertEquals(new Vector(2.0f, 0.0f, 0.0f), ray.at(2.0f));
    }

    @Test
    public void testAtWithAngle() {
        Ray ray = new Ray(ORIGIN, new Vector(1.0f, 2.0f, 3.0f));
        assertEquals(new Vector(-2.0f, -4.0f, -6.0f), ray.at(-2.0f));
        assertEquals(new Vector(0.5f, 1.0f, 1.5f), ray.at(0.5f));
        assertEquals(ORIGIN, ray.at(0.0f));
        assertEquals(new Vector(2.0f, 4.0f, 6.0f), ray.at(2.0f));
    }

    @Test
    public void testAtWithAngleAndOffset() {
        Ray ray = new Ray(new Vector(1.0f, 2.0f, 3.0f), new Vector(1.0f, 2.0f, 3.0f));
        assertEquals(new Vector(-1.0f, -2.0f, -3.0f), ray.at(-2.0f));
        assertEquals(new Vector(1.5f, 3.0f, 4.5f), ray.at(0.5f));
        assertEquals(new Vector(1.0f, 2.0f, 3.0f), ray.at(0.0f));
        assertEquals(new Vector(3.0f, 6.0f, 9.0f), ray.at(2.0f));
    }
}
