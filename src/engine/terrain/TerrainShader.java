package engine.terrain;

import engine.render.shader.BaseShader;

/**
 * Created by anarchist on 6/15/16.
 */
public class TerrainShader extends BaseShader {
    private static final String VERTEX_FILE = "terrain/terrainVertexShader.vert";
    private static final String FRAGMENT_FILE = "terrain/terrainFragmentShader.frag";


    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);

        super.bindAttribute(0, "position");
        super.bindAttribute(1, "texCoords");
        super.bindAttribute(2, "normal");
    }

    public void connectTextureUnits() {
        super.setTextureSlot("backgroundTexture", 0);
        super.setTextureSlot("rTexture", 1);
        super.setTextureSlot("gTexture", 2);
        super.setTextureSlot("bTexture", 3);
        super.setTextureSlot("blendMap", 4);
        super.setTextureSlot("shadowMap", 5);
    }
}
