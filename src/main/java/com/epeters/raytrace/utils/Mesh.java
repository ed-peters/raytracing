package com.epeters.raytrace.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Mesh extends HashMap<String,Vector> {

    private final List<Triangle> triangles = new ArrayList<>();

    public void addTriangle(String a, String b, String c) {
        triangles.add(new Triangle(a, b, c));
    }

    public List<List<Vector>> toTriangles() {
        List<List<Vector>> result = new ArrayList<>();
        for (Triangle t : triangles) {
            result.add(Arrays.asList(get(t.a), get(t.b), get(t.c)));
        }
        return result;
    }

    record Triangle(String a, String b, String c) {
    }
}
