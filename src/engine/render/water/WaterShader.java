package engine.render.water;

import engine.math.Matrix4f;
import engine.math.Vector3f;
import engine.render.Camera;
import engine.render.lighting.Light;
import engine.render.shader.BaseShader;
import engine.util.MathUtil;

/**
 * Created by anarchist on 6/18/16.
 */
public class WaterShader extends BaseShader {
    private static String VERTEX_FILE = "water/waterVertexShader.vert";
    private static String FRAG_FILE = "water/waterFragmentShader.frag";

//    private int location_modelMatrix;
//    private int location_reflectionTex;
//    private int location_refractionTex;
//    private int location_dudv;
//    private int location_depthMap;
//
//    private int location_moveFactor;
//    private int location_color;
//
//    private int location_cameraPos;
//
//    private int location_viewMatrix;
//
//    private int location_normalMap;
//    private int location_lightColor;
//    private int location_lightPosition;
//
//    private int location_far;
//    private int location_near;
//
//    private int location_tiling;

    public WaterShader() {
        super(VERTEX_FILE, FRAG_FILE);

        bindAttribute(0, "position");
    }

    public void loadModelMatrix(Matrix4f matrix4f) {
        super.setUniform("modelMatrix", matrix4f);
    }

    @Override
    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = MathUtil.createViewMatrix(camera);
        setUniform("viewMatrix", viewMatrix);
        super.setUniform("cameraPos", camera.getPosition());
    }

    public void loadLight(Light light) {
        super.setUniform("lightColor", light.getColor());
        super.setUniform("lightPosition", light.getPosition());
    }

    public void connectTextureUnits() {
        super.setTextureSlot("reflectionTexture", 0);
        super.setTextureSlot("refractionTexture", 1);
        super.setTextureSlot("dudvMap", 2);
        super.setTextureSlot("normalMap", 3);
        super.setTextureSlot("depthMap", 4);
    }

    public void loadMoveFactor(float factor) {
        super.setUniform("moveFactor", factor);
    }

    public void loadWaterColor(Vector3f color) {
        super.setUniform("color", color);
    }

    public void loadFar(float far) {
        super.setUniform("far", far);
    }

    public void loadNear(float near) {
        super.setUniform("near", near);
    }

    public void loadTiling(float tiling) {
        super.setUniform("tiling", tiling);
    }

}
