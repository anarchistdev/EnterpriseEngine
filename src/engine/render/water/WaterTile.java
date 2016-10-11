package engine.render.water;

import engine.math.Vector3f;
import engine.math.Vector4f;
import engine.physics.AABB;
import engine.render.Loader;
import engine.render.model.RawModel;

/**
 * Created by anarchist on 6/18/16.
 */

public class WaterTile {
    private int tileSize = 700;

    private float height;
    private float tiling = tileSize / 18;
    private float x, z;

    private Vector3f color = new Vector3f(0, 0.3f, 0.3f);

    private float depth = 15;

    public WaterTile(float x, float z, float height) {
        this.x = x;
        this.z = z;
        this.height = height;

    }

    public AABB generateUnderwaterBounds() {
        AABB waterBox = new AABB(new Vector3f(-tileSize * x, getHeight() * depth, -tileSize * z), new Vector3f(tileSize * x, getHeight(), tileSize * z));
        return waterBox;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public float getTiling() {
        return tiling;
    }

    public float getTileSize() {
        return tileSize;
    }

    public Vector4f getReflectClipPlane() {
        return new Vector4f(0, 1, 0, -getHeight()+1f);
    }

    public Vector4f getRefractClipPlane() {
        return new Vector4f(0, -1, 0, getHeight()+1f);
    }
}
