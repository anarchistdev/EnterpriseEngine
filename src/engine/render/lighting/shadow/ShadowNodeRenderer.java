package engine.render.lighting.shadow;

import engine.components.RenderComponent;
import engine.core.Node;
import engine.math.Matrix4f;
import engine.render.MasterRenderer;
import engine.render.model.RawModel;
import engine.util.MathUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

/**
 * Created by anarchist on 6/23/16.
 */
public class ShadowNodeRenderer {

    private Matrix4f projectionViewMatrix;
    private ShadowShader shader;

    /**
     * @param shader
     *            - the simple shader program being used for the shadow render
     *            pass.
     * @param projectionViewMatrix
     *            - the orthographic projection matrix multiplied by the light's
     *            "view" matrix.
     */
    protected ShadowNodeRenderer(ShadowShader shader, Matrix4f projectionViewMatrix) {
        this.shader = shader;
        this.projectionViewMatrix = projectionViewMatrix;
    }

    /**
     * Renders entieis to the shadow map. Each model is first bound and then all
     * of the entities using that model are rendered to the shadow map.
     *
     * @param entities
     *            - the entities to be rendered to the shadow map.
     */
    protected void render(List<Node> entities) {
        for (Node  data : entities) {
            for (RenderComponent renderComponent : data.getRenderComponents()) {
                if (renderComponent.shouldCastShadow()) {
                    for (RawModel rawModel : renderComponent.getMesh().getRawModels()) {
                        bindModel(rawModel);
                        GL13.glActiveTexture(GL13.GL_TEXTURE0);
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderComponent.getTexture().getTextureID());
                        if (renderComponent.getTexture().isHasTransparency()) {
                            MasterRenderer.disableCulling();
                        }
                        prepareInstance(data);
                        GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(),
                                GL11.GL_UNSIGNED_INT, 0);

                        if (renderComponent.getTexture().isHasTransparency()) {
                            MasterRenderer.enableCulling();
                        }
                    }
                }
            }
        }

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    private void bindModel(RawModel rawModel) {
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
    }

    private void prepareInstance(Node data) {
        Matrix4f modelMatrix = MathUtil.createTransformationMatrix(data.getTransform().getPosition(),
                data.getTransform().getRotation().x, data.getTransform().getRotation().y, data.getTransform().getRotation().z,data.getTransform().getScale());
        Matrix4f mvpMatrix = Matrix4f.mul(projectionViewMatrix, modelMatrix, null);
        shader.loadMvpMatrix(mvpMatrix);
    }
}
