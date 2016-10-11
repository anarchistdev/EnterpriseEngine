package engine.terrain;

import engine.core.Component;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.render.Loader;
import engine.render.model.RawModel;
import engine.render.texture.TerrainTexture;
import engine.render.texture.TerrainTexturePack;
import engine.resource.ResourceManager;
import engine.util.MathUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by anarchist on 6/15/16.
 */
public class HeightMapTerrain extends Component implements Terrain {
    private static final int SIZE = 800;
    private static final float MAX_HEIGHT = 60;
    private static final float MIN_HEIGHT = 40;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;

    private float x;
    private float z;

    private TerrainRenderComponent terrainRenderComponent;

    private float[][] heights;

    public HeightMapTerrain(int gridX, int gridZ, TerrainTexturePack texturePack, TerrainTexture blendMap, Loader loader, String heightMap) {

        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;

        terrainRenderComponent = new TerrainRenderComponent();
        terrainRenderComponent.setTerrainTexturePack(texturePack);
        terrainRenderComponent.setBlendMap(blendMap);

        terrainRenderComponent.setModel(generateTerrain(loader, heightMap));

    }

    @Override
    protected void onAdd() {
        getParent().getTransform().setPosition(new Vector3f(this.x, 0, this.z));
        getParent().addRenderComponent(terrainRenderComponent);
    }

    private RawModel generateTerrain(Loader loader, String heightMap){

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(ResourceManager.LoadTexturePath(heightMap)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int VERTEX_COUNT = image.getHeight();
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                float height = getHeight(j, i, image);
                heights[j][i] = height;
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private Vector3f calculateNormal(int x, int z, BufferedImage image) {
        float heightL = getHeight(x-1, z, image);
        float heightR = getHeight(x+1, z, image);
        float heightD = getHeight(x, z-1, image);
        float heightU = getHeight(x, z+1, image);

        Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }

    private float getHeight(int x, int z, BufferedImage image) {
        if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) {
            return 0;
        }

        float height = image.getRGB(x, z);
        height += MAX_PIXEL_COLOR  / 2;
        height /= MAX_PIXEL_COLOR / 2;
        height *= MAX_HEIGHT;
        return height;
    }

    public float getHeightOfTerrain(float worldX, float worldZ) {
        float terrainX = worldX - getParent().getTransform().getPosition().x;
        float terrainZ = worldZ - getParent().getTransform().getPosition().z;
        float gridSquare = SIZE / (float)(heights.length - 1);

        int gridX = (int)Math.floor(terrainX / gridSquare);
        int gridZ = (int)Math.floor(terrainZ / gridSquare);

        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
            return 0;
        }

        float xCoord = (terrainX % gridSquare) / gridSquare;
        float zCoord = (terrainZ % gridSquare) / gridSquare;
        float answer;

        if (xCoord <= (1-zCoord)) {
            answer = MathUtil
                    .barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ], 0), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            answer = MathUtil
                    .barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }

        return answer;

    }
}
