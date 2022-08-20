package com.epeters.raytrace.material;

import com.epeters.raytrace.Hit;

public interface Material {

    Bounce computeBounce(Hit hit);
}
