package com.epeters.raytrace.material;

import com.epeters.raytrace.Vector;

public record Bounce(Vector albedo, Vector direction) {
}
