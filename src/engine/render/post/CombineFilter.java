package engine.render.post;

import engine.physics.AABB;
import engine.render.post.ImageRenderer;
import engine.render.post.PostFilter;
import engine.render.shader.RawShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * Created by anarchist on 7/20/16.
 */
public class CombineFilter extends PostFilter {
    private ImageRenderer imageRenderer;
    private RawShader shader;
    private int highlightTexture;
    private float sceneDamper = 0.15f;

    public CombineFilter() {
        shader = new RawShader("post/postVertexShader.vert", "post/bloom/combineFragmentShader.frag");
        shader.start();
        shader.setTextureSlot("colorTexture", 0);
        shader.setTextureSlot("highlightTexture", 1);
        shader.stop();

        imageRenderer = new ImageRenderer();
    }

    public void setHighlightTexture(int tex) {
        this.highlightTexture = tex;
    }

    public void render(int colorTexture) {
        shader.start();
        shader.setUniform("sceneDamper", sceneDamper);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, highlightTexture);
        imageRenderer.renderQuad();
        shader.stop();
    }

    public int getOutputTexture() {
        return imageRenderer.getOutputTexture();
    }

    public AABB getBounds() {
        return null;
    }

    public void cleanUp() {
        imageRenderer.cleanUp();
        shader.cleanUp();
    }
}
