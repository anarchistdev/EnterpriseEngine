package engine.render.post.blur;

import engine.physics.AABB;
import engine.render.post.ImageRenderer;
import engine.render.post.PostFilter;
import engine.render.shader.RawShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * Created by anarchist on 7/14/16.
 */
public class HorizontalFilter extends PostFilter {
    private ImageRenderer renderer;
    private RawShader shader;
    private AABB bounds;

    public HorizontalFilter(int width, int height) {
        shader = new RawShader("post/blur/horizontalVertexShader.vert", "post/blur/blurFragmentShader.frag");
        shader.start();
        shader.setUniform("targetWidth", width);
        shader.stop();
        renderer = new ImageRenderer(width, height);
    }

    public void render(int texture) {
        shader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        renderer.renderQuad();
        shader.stop();
    }

    public int getOutputTexture() {
        return renderer.getOutputTexture();
    }

    public AABB getBounds() {
        return bounds;
    }

    public void cleanUp() {
        renderer.cleanUp();
        shader.cleanUp();
    }
}
