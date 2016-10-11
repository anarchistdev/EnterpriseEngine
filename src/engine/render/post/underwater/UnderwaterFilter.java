package engine.render.post.underwater;

import engine.physics.AABB;
import engine.render.post.ImageRenderer;
import engine.render.post.PostFilter;
import engine.render.shader.RawShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * Created by anarchist on 7/5/16.
 */
public class UnderwaterFilter extends PostFilter {
    private AABB bounds = null;
    private RawShader shader;
    private ImageRenderer imageRenderer;

    public UnderwaterFilter() {
        imageRenderer = new ImageRenderer();
        shader = new RawShader("post/underwater/underwaterVertexShader.vert",  "post/underwater/underwaterFragmentShader.frag");
    }

    public UnderwaterFilter(AABB bounds) {
        this();
        this.bounds = bounds;
    }

    public void render(int colorTexture) {
        shader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
        imageRenderer.renderQuad();
        shader.stop();
    }

    public void cleanUp() {
        shader.cleanUp();
        imageRenderer.cleanUp();
    }

    @Override
    public AABB getBounds() {
        return bounds;
    }

    public void setBounds(AABB bounds) {
        this.bounds = bounds;
    }

    public int getOutputTexture() {
        return imageRenderer.getOutputTexture();
    }

}
