package engine.terrain;

import engine.render.model.RawModel;
import engine.render.texture.TerrainTexture;
import engine.render.texture.TerrainTexturePack;

/**
 * Created by anarchist on 6/20/16.
 */
public interface Terrain {
    float getHeightOfTerrain(float worldX, float worldZ);
}
