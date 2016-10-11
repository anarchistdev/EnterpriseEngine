package engine.render.skybox;

import engine.math.Matrix;
import engine.math.Matrix4f;
import engine.math.Vector3f;
import engine.render.Camera;
import engine.render.DisplayManager;
import engine.render.shader.BaseShader;
import engine.util.MathUtil;

/**
 * Created by anarchist on 6/17/16.
 */
public class SkyboxShader extends BaseShader {
    private static String VERTEX_FILE = "skybox/skyboxVertexShader.vert";
    private static String FRAG_FILE = "skybox/skyboxFragmentShader.frag";

    private static final float ROTATE_SPEED = 0.3f;
    private float rotation = 0;

    public SkyboxShader() {
        super(VERTEX_FILE, FRAG_FILE);

        super.bindAttribute(0, "position");
    }

    public void loadFogColor(Vector3f color) {
        super.setUniform("fogColor", color);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.setUniform("projectionMatrix", matrix);
    }

    public void loadViewMatrix(Camera cam) {
        Matrix4f viewMatrix = MathUtil.createViewMatrix(cam);
        viewMatrix.m30 = 0;
        viewMatrix.m31 = 0;
        rotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
        Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0,1,0), viewMatrix, viewMatrix);
        viewMatrix.m32 = 0;

        super.setUniform("viewMatrix", viewMatrix);
    }

    public void loadBlendFactor(float factor) {
        super.setUniform("blendFactor", factor);
    }

    public void connectTextureUnits() {
        super.setTextureSlot("cubeMap", 0);
        super.setTextureSlot("cubeMap2", 1);
    }


}
