package engine.render.water;

/**
 * Created by anarchist on 6/18/16.
 */
import engine.math.Matrix4f;
import engine.math.Vector3f;
import engine.render.Camera;
import engine.render.DisplayManager;
import engine.render.Loader;
import engine.render.MasterRenderer;
import engine.render.lighting.Light;
import engine.render.model.RawModel;
import engine.render.texture.Texture;
import engine.util.MathUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class WaterRenderer {

    private RawModel quad;
    private WaterShader shader;
    private WaterFrameBuffers fbos;
    private static final String DUDV_MAP = "water/waterDUDV.png";
    private static final float WAVE_SPEED = 0.03f;
    private int dudvTexture;
    private int normalMapTexture;

    private float moveFactor;

    public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
        dudvTexture = Texture.loadTexture(DUDV_MAP).getTextureID();
        normalMapTexture = Texture.loadTexture("water/matchingNormalMap.png").getTextureID();
        this.shader = shader;
        this.fbos = fbos;
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO(loader);
    }

    public void render(List<WaterTile> water, Camera camera, Light light) {
        prepareRender(camera, light);
        for (WaterTile tile : water) {
            Matrix4f modelMatrix = MathUtil.createTransformationMatrix(
                    new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
                    tile.getTileSize());
            shader.loadModelMatrix(modelMatrix);
            shader.loadWaterColor(tile.getColor());
            shader.loadTiling(tile.getTiling());
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
        }
        unbind();
    }

    private void prepareRender(Camera camera, Light light){
        shader.start();
        shader.loadViewMatrix(camera);
        moveFactor += WAVE_SPEED * DisplayManager.getFrameTimeSeconds();
        moveFactor %= 1;
        shader.loadMoveFactor(moveFactor);
        shader.loadLight(light);
        shader.loadNear(MasterRenderer.NEAR_PLANE);
        shader.loadFar(MasterRenderer.FAR_PLANE);
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMapTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void unbind(){
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    private void setUpVAO(Loader loader) {
        // Just x and z vectex positions here, y is set to 0 in v.shader
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadToVAO(vertices, 2);
        //plane = new Plane(128, 700);
        //plane.generateShape(loader);
    }

}