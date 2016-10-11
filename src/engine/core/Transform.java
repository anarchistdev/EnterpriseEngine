package engine.core;

import engine.math.Vector3f;

/**
 * Created by anarchist on 7/10/16.
 */
public class Transform {
    private Vector3f rotation = new Vector3f();
    private float scale;
    private Vector3f position = new Vector3f();

    public Transform(Vector3f position, Vector3f rotation, float scale) {
        this.rotation = rotation;
        this.scale = scale;
        this.position = position;
    }

    public Transform() {
        this.rotation = new Vector3f(0,0,0);
        this.scale = 1;
        this.position = new Vector3f(1,1,1);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
