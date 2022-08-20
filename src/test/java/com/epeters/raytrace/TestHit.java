package com.epeters.raytrace;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestHit {

    public static final Vector K = new Vector(0.0f, 0.0f, 1.0f);

    @Test
    public void testFrontHit() {
        Ray ray = new Ray(1.0f, 2.0f, -3.0f);
        Hit hit = Hit.from(ray, 1.0f, (p) -> K);
        assertNotNull(hit);
        assertEquals(1.0f, hit.t(), 0.01f);
        assertEquals(K, hit.normal());
        assertTrue(hit.front());
    }

    @Test
    public void testBackHit() {
        Ray ray = new Ray(1.0f, 2.0f, -3.0f);
        Hit hit = Hit.from(ray, 1.0f, (p) -> K.negate());
        assertNotNull(hit);
        assertEquals(1.0f, hit.t(), 0.01f);
        assertEquals(K, hit.normal());
        assertFalse(hit.front());
    }
}
