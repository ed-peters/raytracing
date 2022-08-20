package com.epeters.raytrace;

import org.junit.Test;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static com.epeters.raytrace.Vector.ORIGIN;

public class TestImage {

    @Test
    public void testUpdate() {

        Image image = new Image(20, 20);
        assertEquals(ORIGIN, image.get(0, 0));
        assertEquals(ORIGIN, image.get(1, 1));

        Vector oneX = new Vector(1.0f, 0.0f, 0.0f);
        image.update(0, 0, val -> val.plus(oneX));
        image.update(0, 0, val -> val.plus(oneX));
        image.update(0, 0, val -> val.plus(oneX));
        image.update(0, 0, val -> val.plus(oneX));
        assertEquals(new Vector(4.0f, 0.0f, 0.0f), image.get(0, 0));
        assertEquals(ORIGIN, image.get(1, 1));
    }

    @Test
    public void testWritePpm() throws IOException {
        String actual = computeActual();
        String expected = loadExpected();
        assertEquals(expected, actual);
    }

    private String computeActual() {

        Image image = new Image(256, 256);
        image.forEach((x, y) -> {
            float r = x / 255.0f;
            float g = y / 255.0f;
            float b = 0.25f;
            image.set(x, y, new Vector(r, g, b));
        });

        StringWriter writer = new StringWriter();
        image.writePpm(writer);
        return writer.toString();
    }

    private String loadExpected() throws IOException {

        InputStream stream = getClass().getResourceAsStream("/test.ppm");
        InputStreamReader reader = new InputStreamReader(stream);
        CharArrayWriter writer = new CharArrayWriter();
        char [] buff = new char[1024];

        while (true) {
            int cread = reader.read(buff);
            if (cread < 0) {
                return writer.toString();
            }
            if (cread > 0) {
                writer.write(buff, 0, cread);
            }
        }
    }
}
