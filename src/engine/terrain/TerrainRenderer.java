package engine.terrain;

import engine.components.RenderComponent;
import engine.math.Matrix4f;
import engine.math.Vector4f;
import engine.render.BaseRenderer;
import engine.render.Camera;
import engine.render.MasterRenderer;
import engine.render.lighting.Light;
import engine.render.lighting.PBRMaterial;
import engine.render.lighting.shadow.ShadowMasterRenderer;
import engine.render.lighting.shadow.ShadowOptions;
import engine.render.model.RawModel;
import engine.render.texture.TerrainTexturePack;
import engine.util.MathUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

/**
 * Created by anarchist on 6/15/16.
 */
public class TerrainRenderer extends BaseRenderer {
    private TerrainShader shader = new TerrainShader();

    public TerrainRenderer() {
        shader.start();
        shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
        shader.connectTextureUnits();

        shader.stop();

    }

    public void render(List<Light> lights, Camera camera, Vector4f clipPlane, ShadowOptions shadowOptions) {
        shader.start();
        shader.loadClipPlane(clipPlane);
        shader.loadSkycolorVariable(MasterRenderer.getSkyColor());
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        shader.loadShadowOptions(shadowOptions);

        for (RenderComponent renderComponent : getParent().getRenderComponents()) {
            TerrainRenderComponent terrainRenderComponent = (TerrainRenderComponent) renderComponent;

            shader.loadPBRMaterial(terrainRenderComponent.getPbrMaterial());
            prepareTerrain(terrainRenderComponent);
            loadModelMatrix();

            GL11.glDrawElements(GL11.GL_TRIANGLES, terrainRenderComponent.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

            unBindTexturedModel();
        }

        shader.stop();
    }

    private void prepareTerrain(TerrainRenderComponent terrain) {
        RawModel model = terrain.getRawModel();

        GL30.glBindVertexArray(model.getVaoID());

        GL20.glEnableVertexAttribArray(0); // This enables the pos
        GL20.glEnableVertexAttribArray(1); // This enables the texCoords
        GL20.glEnableVertexAttribArray(2); // This enables the normals


        bindTextures(terrain);
        shader.loadShineVariables(1, 0); // Stock options - make sure to allow these vars to exist w/ terrain
    }

    private void bindTextures(TerrainRenderComponent data) {
        TerrainTexturePack texturePack = data.getTerrainTexturePack();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, data.getBlendMap().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ShadowMasterRenderer.getShadowMap());
    }

    private void unBindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    private void loadModelMatrix() {
        Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(
                getParent().getTransform().getPosition(), getParent().getTransform().getRotation().x, getParent().getTransform().getRotation().y, getParent().getTransform().getRotation().z, getParent().getTransform().getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }

    public void cleanUp() {
        shader.cleanUp();
    }
}
