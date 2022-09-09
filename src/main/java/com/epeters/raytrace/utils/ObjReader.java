package com.epeters.raytrace.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import static com.epeters.raytrace.utils.Vector.vec;

public class ObjReader {

    public static final Pattern V = Pattern.compile("v ");

    public static Mesh readFile(String path) throws IOException {

        Mesh mesh = new Mesh();
        BufferedReader reader = new BufferedReader(new InputStreamReader(ObjReader.class.getResourceAsStream(path)));

        while (true) {

            String line = reader.readLine();
            if (line == null) {
                return mesh;
            }

            if (line.startsWith("v")) {
                String k = Integer.toString(mesh.size() + 1);
                Vector v = parseVertex(line);
                mesh.put(k, v);
            }
            else if (line.startsWith("f")) {
                String [] v = parseTriangle(line);
                mesh.addTriangle(v[0], v[1], v[2]);
            }
        }
    }

    public static Vector parseVertex(String line) {
        String [] parts = line.split("\\s+");
        double x = Double.parseDouble(parts[3]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[1]);
        return vec(x, y, z);
    }

    public static String [] parseTriangle(String line) {
        String [] parts = line.split("\\s+");
        if (parts.length != 4) {
            throw new IllegalArgumentException("unparseable face '"+line+"'");
        }
        return new String[]{ parts[1], parts[2], parts[3] };
    }

    public static void main(String [] args) throws Exception {
        Mesh m = readFile("/gourd.obj");
        System.err.println(m);
    }
}
