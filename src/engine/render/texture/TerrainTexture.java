package engine.render.texture;

/**
 * Created by anarchist on 6/15/16.
 */
public class TerrainTexture {
    private int textureID;

    public TerrainTexture(int textureID) {
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }
}
