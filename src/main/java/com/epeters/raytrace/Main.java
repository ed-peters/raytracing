package com.epeters.raytrace;

import com.epeters.raytrace.hittables.BoundingVolume;
import com.epeters.raytrace.hittables.Hit;
import com.epeters.raytrace.materials.Material;
import com.epeters.raytrace.solids.Solid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String [] args) {

        Material blue = Material.lambertian(Utils.BLUE);
        List<Solid> solids = Arrays.asList(
                Solid.sphere(Vector.vec(1.0, 1.0, -1.0), 0.9, blue),
                Solid.sphere(Vector.vec(1.0, -1.0, -1.0), 1.0, blue),
                Solid.sphere(Vector.vec(-1.0, -1.0, -1.0), 1.1, blue),
                Solid.sphere(Vector.vec(-1.0, 1.0, -1.0), 1.2, blue));

        BoundingVolume volume = BoundingVolume.from(new ArrayList<>(solids));

        Ray ray = new Ray(1.0, 1.0, -1.0);
        Hit hit = volume.hit(ray, 1e-8, Double.MAX_VALUE);
        System.err.println(hit.info().get().getSolid());
    }

}
