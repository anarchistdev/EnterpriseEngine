package engine.render.post.dof;

import engine.physics.AABB;
import engine.render.DisplayManager;
import engine.render.MasterRenderer;
import engine.render.post.ImageRenderer;
import engine.render.post.PostFilter;
import engine.render.shader.RawShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * Created by anarchist on 7/22/16.
 */
public class DepthOfFieldFilter extends PostFilter {
    private float focusDistance = 50f;
    private float focusRange = 10f;
    private float blurScale = 1f;

    private float xScale = 1.0f / DisplayManager.getWindowWidth();
    private float yScale = 1.0f / DisplayManager.getWindowHeight();

    private ImageRenderer imageRenderer;
    private RawShader shader;

    private AABB bounds;

    private int depthTexture;

    public DepthOfFieldFilter() {
        shader = new RawShader("post/dof/dofFragmentShader.frag", "post/postVertexShader.vert");
        imageRenderer = new ImageRenderer(DisplayManager.getWindowWidth()/5, DisplayManager.getWindowHeight()/5);

        shader.start();

        shader.setTextureSlot("colorTexture", 0);
        shader.setTextureSlot("depthTexture", 1);

        shader.setUniform("focusDistance", focusDistance);
        shader.setUniform("focusRange", focusRange);
        shader.setUniform("blurScale", blurScale);

        shader.setUniform("xScale", blurScale * xScale);
        shader.setUniform("yScale", blurScale * yScale);

        shader.setUniform("near", MasterRenderer.NEAR_PLANE);
        shader.setUniform("far", MasterRenderer.FAR_PLANE);

        shader.stop();
    }

    public void setDepthTexture(int depth) {
        this.depthTexture = depth;
    }

    public void render(int colorTexture) {
        shader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);

        imageRenderer.renderQuad();
        shader.stop();
    }

    public int getOutputTexture() {
        return imageRenderer.getOutputTexture();
    }

    public void cleanUp() {
        shader.cleanUp();
        imageRenderer.cleanUp();
    }

    public void setBounds(AABB bounds) {
        this.bounds = bounds;
    }

    public AABB getBounds() {
        return bounds;
    }

}
