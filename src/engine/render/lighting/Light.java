package engine.render.lighting;

import engine.math.Matrix4f;
import engine.math.Vector3f;

/**
 * Created by anarchist on 6/14/16.
 */
public class Light {
    private Vector3f position;
    private Vector3f color;
    private Vector3f attenuation = new Vector3f(1, 0, 0);
    private boolean castsShadows = true;
    private DynamicEnvironmentMap environmentMap;

    public Light(Vector3f position, Vector3f color) {
        this.position = position;
        this.color = color;
        this.environmentMap = new DynamicEnvironmentMap();
    }

    public Light(Vector3f position, Vector3f color, Vector3f att) {
        this(position, color);
        this.attenuation = att;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public boolean isCastsShadows() {
        return castsShadows;
    }

    public void setCastsShadows(boolean castsShadows) {
        this.castsShadows = castsShadows;
    }

    public DynamicEnvironmentMap getEnvironmentMap() {
        return environmentMap;
    }
}
