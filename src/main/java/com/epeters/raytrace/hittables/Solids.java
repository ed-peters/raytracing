package com.epeters.raytrace.hittables;

import com.epeters.raytrace.surfaces.Material;
import com.epeters.raytrace.surfaces.Texture;
import com.epeters.raytrace.utils.Mesh;
import com.epeters.raytrace.utils.ObjReader;
import com.epeters.raytrace.utils.Vector;
import com.epeters.raytrace.utils.XYZPlane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.epeters.raytrace.utils.Utils.WHITE;
import static com.epeters.raytrace.utils.XYZPlane.XY;
import static com.epeters.raytrace.utils.XYZPlane.YZ;
import static com.epeters.raytrace.utils.XYZPlane.XZ;

public class Solids {

    public static Hittable sphere(Vector center, double radius, Material m) {
        return new Sphere(m, center, radius);
    }

    public static Hittable lsphere(Vector center, double radius, Vector color) {
        Material m = Material.lambertian(color);
        return sphere(center, radius, m);
    }

    public static Hittable msphere(Vector center, double radius, Vector color, double fuzz) {
        Material m = Material.metal(color, fuzz);
        return sphere(center, radius, m);
    }

    public static Hittable csphere(Vector center, double radius, Vector even, Vector odd) {
        Texture t = Texture.checker(even, odd);
        Material m = Material.lambertian(t);
        return sphere(center, radius, m);
    }

    public static Hittable gsphere(Vector center, double radius, double ratio) {
        Material m = Material.dialectric(ratio);
        return sphere(center, radius, m);
    }

    public static Hittable isphere(Vector center, double radius, String path) {
        Texture t = Texture.image(path);
        Material m = Material.lambertian(t);
        return sphere(center, radius, m);
    }

    public static Hittable tsphere(Vector center, double radius, double scale, int depth) {
        Texture t = Texture.turbulence(WHITE, scale, depth);
        Material m = Material.lambertian(t);
        return sphere(center, radius, m);
    }

    public static Hittable light(double x0, double y0, double x1, double y1, double z, Vector color) {
        Material m = Material.light(color);
        return rect(XYZPlane.XY, x0, y0, x1, y1, z, m);
    }

    public static Hittable rect(XYZPlane plane, double i0, double j0, double i1, double j1, double k, Material material) {
        return new Rectangle(material, plane, i0, j0, i1, j1, k);
    }

    public static Hittable tmesh(Material material, Mesh mesh) {
        List<Hittable> list = new ArrayList<>();
        for (List<Vector> tri : mesh.toTriangles()) {
            list.add(new Triangle(material, tri.get(0), tri.get(1), tri.get(2)));
        }
        return HittableVolume.from(list);
    }

    public static Hittable tmesh(Material material, String path) {
        try {
            return tmesh(material, ObjReader.readFile(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Hittable box(Vector min, Vector max, Material material) {
        List<Hittable> sides = new ArrayList<>();
        sides.add(rect(XY, min.x(), min.y(), max.x(), max.y(), min.z(), material));
        sides.add(rect(XY, min.x(), min.y(), max.x(), max.y(), max.z(), material));
        sides.add(rect(XZ, min.x(), min.z(), max.x(), max.z(), min.y(), material));
        sides.add(rect(XZ, min.x(), min.z(), max.x(), max.z(), max.y(), material));
        sides.add(rect(YZ, min.y(), min.z(), max.y(), max.z(), min.x(), material));
        sides.add(rect(YZ, min.y(), min.z(), max.y(), max.z(), max.x(), material));
        return HittableVolume.from(sides);
    }

    public static Hittable fog(Hittable boundary, double density, Vector color) {
        return new ConstantMedium(Material.isotropic(color), boundary, density);
    }
}
