package engine.render.post.bloom;

import engine.physics.AABB;
import engine.render.post.ImageRenderer;
import engine.render.post.PostFilter;
import engine.render.shader.RawShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * Created by anarchist on 7/20/16.
 */
public class BrightFilter extends PostFilter {
    private ImageRenderer imageRenderer;
    private RawShader shader;

    public BrightFilter(int width, int height) {
        shader = new RawShader("post/postVertexShader.vert", "post/bloom/brightFragmentShader.frag");
        imageRenderer = new ImageRenderer(width, height);
    }

    public void render(int colorTexture) {
        shader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
        imageRenderer.renderQuad();
        shader.stop();
    }

    public AABB getBounds() {
        return null;
    }

    public int getOutputTexture() {
        return imageRenderer.getOutputTexture();
    }

    public void cleanUp() {
        shader.cleanUp();
        imageRenderer.cleanUp();
    }
}
