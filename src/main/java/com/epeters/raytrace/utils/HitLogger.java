package com.epeters.raytrace.utils;

import java.util.ArrayList;
import java.util.List;

public class HitLogger {

    private static final ThreadLocal<List<Vector>> points = new ThreadLocal<>(){
        @Override
        protected List<Vector> initialValue() {
            return new ArrayList<>();
        }
    };

    private static final ThreadLocal<List<Vector>> directions = new ThreadLocal<>(){
        @Override
        protected List<Vector> initialValue() {
            return new ArrayList<>();
        }
    };

    public static void init() {
        points.get().clear();
        directions.get().clear();
    }

    public static Vector last(List<Vector> list) {
        return list.get(list.size()-1);
    }

    public static void finish(boolean log) {
        if (!log || points.get().size() < 2) {
            return;
        }
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        List<Double> z = new ArrayList<>();
        x.add(278.0);
        y.add(278.0);
        z.add(-10.0);
        for (Vector v : points.get()) {
            x.add(v.x());
            y.add(v.y());
            z.add(v.z());
        }
        x.add(last(directions.get()).x());
        y.add(last(directions.get()).y());
        z.add(last(directions.get()).z());
        String s = String.format("r = %s\ng = %s\nb = %s\n", x, y, z);
        System.err.println(s);
    }

    public static void log(Vector point, Vector direction) {
        points.get().add(point);
        directions.get().add(direction);
    }
}
