package engine.render.shader;

import engine.math.Matrix4f;
import engine.math.Vector3f;
import engine.math.Vector4f;
import engine.render.Camera;
import engine.render.lighting.Light;
import engine.render.lighting.PBRMaterial;
import engine.render.lighting.shadow.ShadowOptions;
import engine.util.MathUtil;

import java.util.List;

/**
 * Created by anarchist on 6/17/16.
 */
public class BaseShader extends RawShader {

    protected static final int MAX_LIGHTS = 4;

    public BaseShader(String vertFile, String fragFile) {
        super(vertFile, fragFile);

    }

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                super.setUniform("lightPosition[" + i + "]", lights.get(i).getPosition());
                super.setUniform("lightColor[" + i + "]", lights.get(i).getColor());
                super.setUniform("attenuation[" + i + "]", lights.get(i).getAttenuation());
            } else {
                super.setUniform("lightPosition[" + i + "]", new Vector3f(0,0,0));
                super.setUniform("lightColor[" + i + "]", new Vector3f(0,0,0));
                super.setUniform("attenuation[" + i + "]", new Vector3f(1,0,0));
            }
        }
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.setUniform("transformationMatrix", matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.setUniform("projectionMatrix", matrix);
    }

    public void loadViewMatrix(Camera cam) {
        Matrix4f viewMatrix = MathUtil.createViewMatrix(cam);
        super.setUniform("viewMatrix", viewMatrix);
    }

    public static int getMaxLights() {
        return MAX_LIGHTS;
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.setUniform("reflectivity", reflectivity);
        super.setUniform("shineDamper", damper);
    }

    public void loadClipPlane(Vector4f plane) {
        super.setUniform("clipPlane", plane);
    }

    public void loadUseFakeLightingVariable(boolean b) {
        super.setUniform("useFakeLighting", b);
    }

    public void loadSkycolorVariable(Vector3f color) {
        super.setUniform("skyColor", color);
    }

    private void loadToShadowMapSpace(Matrix4f matrix4f) {
        super.setUniform("toShadowMapSpace", matrix4f);
    }

    private void loadShadowDistance(float distance) {
        super.setUniform("shadowDistance", distance);
    }

    private void loadPCFCount(int count) {
        super.setUniform("pcfCount", count);
    }

    private void loadShadowMapSize(float size){
        super.setUniform("shadowMapSize", size);
    }

    private void loadShadowTransitionDistance(float distance) {
        super.setUniform("shadowTransitionDistance", distance);
    }

    public void loadShadowOptions(ShadowOptions shadowOptions) {
        this.loadShadowMapSize(shadowOptions.getShadowMapSize());
        this.loadShadowDistance(shadowOptions.getShadowDistance());
        this.loadPCFCount(shadowOptions.getPcfCount());
        this.loadShadowTransitionDistance(shadowOptions.getShadowTransitionDistance());
        this.loadToShadowMapSpace(shadowOptions.getShadowMatrix());
    }

    public void loadPBRMaterial(PBRMaterial material) {
        this.setUniform("roughnessValue", material.getRoughness());
        this.setUniform("F0", material.getFresnel());
        this.setUniform("specularPower", material.getSpecularPower());
        this.setUniform("reflectivity", material.getReflectivity());
    }

    public void loadUsesSpecularMap(boolean tf) {
        this.setUniform("hasSpecularMap", tf);
    }
}
