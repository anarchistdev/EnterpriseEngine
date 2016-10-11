package engine.terrain;

import engine.components.RenderComponent;
import engine.render.lighting.PBRMaterial;
import engine.render.model.RawModel;
import engine.render.texture.TerrainTexture;
import engine.render.texture.TerrainTexturePack;

/**
 * Created by anarchist on 7/28/16.
 */
public class TerrainRenderComponent extends RenderComponent {
    private RawModel model;
    private TerrainTexturePack terrainTexturePack;
    private TerrainTexture blendMap;
    private PBRMaterial pbrMaterial;

    public TerrainRenderComponent() {
        this.pbrMaterial = new PBRMaterial();
    }

    public RawModel getRawModel() {
        return model;
    }

    public void setModel(RawModel model) {
        this.model = model;
    }

    public TerrainTexturePack getTerrainTexturePack() {
        return terrainTexturePack;
    }

    public void setTerrainTexturePack(TerrainTexturePack terrainTexturePack) {
        this.terrainTexturePack = terrainTexturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public void setBlendMap(TerrainTexture blendMap) {
        this.blendMap = blendMap;
    }

    @Override
    public PBRMaterial getPbrMaterial() {
        return pbrMaterial;
    }

    @Override
    public boolean shouldCastShadow() {
        return false;
    }

}
