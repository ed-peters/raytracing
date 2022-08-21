package com.epeters.raytrace;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static com.epeters.raytrace.Vector.ORIGIN;
import static com.epeters.raytrace.Vector.vec;

public class TestRay {

    @Test
    public void testAtAlongAxis() {
        Ray ray = new Ray(ORIGIN, vec(1.0, 0.0, 0.0));
        assertEquals(vec(-2.0, 0.0, 0.0), ray.at(-2.0));
        assertEquals(vec(0.5, 0.0, 0.0), ray.at(0.5));
        assertEquals(ORIGIN, ray.at(0.0));
        assertEquals(vec(2.0, 0.0, 0.0), ray.at(2.0));
    }

    @Test
    public void testAtWithAngle() {
        Ray ray = new Ray(ORIGIN, vec(1.0, 2.0, 3.0));
        assertEquals(vec(-2.0, -4.0, -6.0), ray.at(-7.483314773547883));
        assertEquals(vec(0.5, 1.0, 1.5), ray.at(1.8708286933869707));
        assertEquals(ORIGIN, ray.at(0.0));
        assertEquals(vec(2.0, 4.0, 6.0), ray.at(7.483314773547883));
    }
}
