package engine.render.post.contrast;

import engine.physics.AABB;
import engine.render.post.ImageRenderer;
import engine.render.post.PostFilter;
import engine.render.shader.RawShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * Created by anarchist on 7/5/16.
 */
public class ContrastEffect extends PostFilter {
    private RawShader contrastShader;
    private ImageRenderer imageRenderer;
    private float contrast = 0.2f;
    private AABB bounds;

    public ContrastEffect() {
        contrastShader = new RawShader("post/contrast/contrastVertexShader.vert", "post/contrast/contrastFragmentShader.frag");

        imageRenderer = new ImageRenderer();
    }

    public void render(int texture) {
        contrastShader.start();
        contrastShader.setUniform("contrast", contrast);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        imageRenderer.renderQuad();
        contrastShader.stop();
    }

    public void cleanUp() {
        contrastShader.cleanUp();
        imageRenderer.cleanUp();
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
    }

    public void setBounds(AABB bounds) {
        this.bounds = bounds;
    }

    public AABB getBounds() {
        return bounds;
    }

    public int getOutputTexture() {
        return imageRenderer.getOutputTexture();
    }
}
