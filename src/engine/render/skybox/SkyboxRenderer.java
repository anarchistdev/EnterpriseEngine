package engine.render.skybox;

import engine.math.Matrix4f;
import engine.math.Vector3f;
import engine.render.Camera;
import engine.render.Loader;
import engine.render.model.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Created by anarchist on 6/17/16.
 */
public class SkyboxRenderer {

    private static final float SIZE = 500f;

    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };

    private static String[] TEXTURE_FILES = {"skybox/right.png", "skybox/left.png", "skybox/top.png", "skybox/bottom.png", "skybox/back.png", "skybox/front.png"};
    private static String[] TEXTURE_FILES2 = {"skybox/nightRight.png", "skybox/nightLeft.png", "skybox/nightTop.png", "skybox/nightBottom.png", "skybox/nightBack.png", "skybox/nightFront.png"};

    private int texture;
    private RawModel cube;
    private SkyboxShader shader;
    private int texture2;

    private float time = 0;

    public SkyboxRenderer(Matrix4f projectionMatrix) {
        shader = new SkyboxShader();
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void init(Loader loader) {
        cube = loader.loadToVAO(VERTICES, 3);
        texture = loader.loadCubeMap(TEXTURE_FILES);
        texture2 = loader.loadCubeMap(TEXTURE_FILES2);
    }

    public void render(Camera camera, Vector3f fogColor) {
        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadFogColor(fogColor);
        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        bindTextures();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    private void bindTextures() {
//        time += DisplayManager.getFrameTimeSeconds() * 1000;
//        time %= 24000;
//        int tex1;
//        int tex2;
//        float blendFactor;
//        if (time >= 0 && time < 5000) {
//            tex1 = texture2;
//            tex2 = texture2;
//            blendFactor = (time - 0) / (5000 - 0);
//        } else if (time >= 5000 && time < 8000) {
//            tex1 = texture2;
//            tex2 = texture;
//            blendFactor = (time - 5000) / (8000 - 5000);
//        } else if (time >= 8000 && time < 21000) {
//            tex1 = texture;
//            tex2 = texture;
//            blendFactor = (time - 8000) / (21000 - 8000);
//        } else {
//            tex1 = texture;
//            tex2 = texture2;
//            blendFactor = (time - 21000) / (24000 - 21000);
//        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        //GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, tex2);
        shader.loadBlendFactor(0);
    }

}
