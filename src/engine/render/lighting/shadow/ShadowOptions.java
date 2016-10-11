package engine.render.lighting.shadow;

import engine.math.Matrix4f;

/**
 * Created by anarchist on 7/18/16.
 */
public class ShadowOptions {
    private int shadowMapSize = 8192;
    private int pcfCount = 8;
    private float shadowDistance = 200;
    private float shadowTransitionDistance = 15;
    private Matrix4f shadowMatrix;

    public ShadowOptions() {

    }

    public int getShadowMapSize() {
        return shadowMapSize;
    }

    public void setShadowMapSize(int shadowMapSize) {
        this.shadowMapSize = shadowMapSize;
    }

    public int getPcfCount() {
        return pcfCount;
    }

    public void setPcfCount(int pcfCount) {
        this.pcfCount = pcfCount;
    }

    public float getShadowDistance() {
        return shadowDistance;
    }

    public void setShadowDistance(float shadowDistance) {
        this.shadowDistance = shadowDistance;
    }

    public float getShadowTransitionDistance() {
        return shadowTransitionDistance;
    }

    public void setShadowTransitionDistance(float shadowTransitionDistance) {
        this.shadowTransitionDistance = shadowTransitionDistance;
    }

    public Matrix4f getShadowMatrix() {
        return shadowMatrix;
    }

    public void setShadowMatrix(Matrix4f shadowMatrix) {
        this.shadowMatrix = shadowMatrix;
    }
}
