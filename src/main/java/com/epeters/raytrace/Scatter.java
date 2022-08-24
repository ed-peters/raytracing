package com.epeters.raytrace;

/**
 * Captures the two important facets of light scattering
 * @param attenuation the color attenuation (darker means light is absorbed)
 * @param direction the direction of the scatter
 */
public record Scatter(Vector attenuation, Vector direction) {
}