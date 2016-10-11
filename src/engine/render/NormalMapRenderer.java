package engine.render;

import engine.core.Node;
import engine.math.Matrix4f;
import engine.math.Vector2f;
import engine.math.Vector4f;
import engine.render.lighting.Light;
import engine.components.RenderComponent;
import engine.render.lighting.PBRMaterial;
import engine.render.lighting.shadow.ShadowMasterRenderer;
import engine.render.lighting.shadow.ShadowOptions;
import engine.render.model.Mesh;
import engine.render.model.RawModel;
import engine.render.shader.NormalMapShader;
import engine.render.texture.Texture;
import engine.util.MathUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

/**
 * Created by anarchist on 6/19/16.
 */
public class NormalMapRenderer extends BaseRenderer {

    private NormalMapShader normalShader = new NormalMapShader();

    public NormalMapRenderer() {
        normalShader.start();
        normalShader.connectTextureUnits();
        normalShader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
        normalShader.stop();
    }

    public void render(List<Light> lights, Camera camera, Vector4f clipPlane, ShadowOptions shadowOptions) {
        normalShader.start();
        normalShader.loadClipPlane(clipPlane);
        normalShader.loadSkycolorVariable(MasterRenderer.getSkyColor());
        normalShader.loadLights(lights);
        normalShader.loadViewMatrix(camera);
        normalShader.loadShadowOptions(shadowOptions);

        for (RenderComponent renderComponent : getParent().getRenderComponents()) {
            for (RawModel model : renderComponent.getMesh().getRawModels()) {
                normalShader.loadPBRMaterial(renderComponent.getPbrMaterial());

                normalShader.loadUsesSpecularMap(renderComponent.getTexture().isHasSpecularMap());

                prepareTexturedModel(renderComponent, model);
                prepareInstance(getParent(), renderComponent);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
                unBindTexturedModel();

            }

        }

        normalShader.stop();

    }

    private void prepareTexturedModel(RenderComponent model, RawModel mod) {
        GL30.glBindVertexArray(mod.getVaoID());

        GL20.glEnableVertexAttribArray(0); // This enables the pos
        GL20.glEnableVertexAttribArray(1); // This enables the texCoords
        GL20.glEnableVertexAttribArray(2); // This enables the normals
        GL20.glEnableVertexAttribArray(3); // enables the tangents

        Texture texture = model.getTexture();

        normalShader.loadNumberOfRows(texture.getNumberOfRows());

        if (texture.isHasTransparency()) {
            MasterRenderer.disableCulling();
        }

        normalShader.loadUseFakeLightingVariable(texture.isUseFakeLighting());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        model.getTexture().bind();
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getNormalMap());

        if (model.getTexture().isHasSpecularMap()) {
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getSpecularMap());
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ShadowMasterRenderer.getShadowMap());

    }

    private void unBindTexturedModel() {
        MasterRenderer.enableCulling();

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);

        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Node node, RenderComponent renderComponent) {
        Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(node.getTransform().getPosition(), node.getTransform().getRotation().x, node.getTransform().getRotation().y, node.getTransform().getRotation().z, node.getTransform().getScale());

        Mesh mesh = renderComponent.getMesh();

        normalShader.loadTransformationMatrix(transformationMatrix);
        normalShader.loadOffset(new Vector2f(renderComponent.getTexture().getTextureXOffset(node.getTextureIndex()), renderComponent.getTexture().getTextureYOffset(node.getTextureIndex())));
    }

    public void cleanUp() {
        normalShader.cleanUp();
    }

}
