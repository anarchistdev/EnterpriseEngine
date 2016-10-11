package engine.render.gui;

import engine.math.Matrix4f;
import engine.render.Loader;
import engine.render.gui.button.Button;
import engine.render.model.RawModel;
import engine.render.shader.RawShader;
import engine.util.MathUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anarchist on 6/16/16.
 */
public class GUIRenderer {

    private final RawModel quad;
    private RawShader shader;

    private List<GUITexture> textureList;
    private List<Button> buttonList;

    public GUIRenderer(Loader loader) {
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positions, 2);
        shader = new RawShader("gui/guiVertexShader.vert", "gui/guiFragmentShader.frag");
        textureList = new ArrayList<>();
        buttonList = new ArrayList<>();
    }

    public void render() {
        update();


        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        for (GUITexture texture : textureList) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
            Matrix4f matrix = MathUtil.createTransformationMatrix(texture.getPosition(), texture.getScale());
            shader.setUniform("transformationMatrix", matrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    private void update() {
        for (Button button : buttonList) {
            button.update();
        }
    }

    public void cleanUp() {
        shader.cleanUp();
    }

    public void addTexture(GUITexture t) {
        textureList.add(t);
    }

    public void addButton(Button button) {
        buttonList.add(button);
        button.hide(textureList);
        button.show(textureList);
    }

}
