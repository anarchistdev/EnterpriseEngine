package engine.render.particle;

/**
 * Created by anarchist on 6/19/16.
 */
public class ParticleTexture {
    private int textureID;
    private int numberOfRows;

    private boolean useAdditiveBlending = false;

    public ParticleTexture(int textureID, int numberOfRows) {
        this.textureID = textureID;
        this.numberOfRows = numberOfRows;
    }

    public int getTextureID() {
        return textureID;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public boolean isUseAdditiveBlending() {
        return useAdditiveBlending;
    }

    public void setUseAdditiveBlending(boolean useAdditiveBlending) {
        this.useAdditiveBlending = useAdditiveBlending;
    }
}
