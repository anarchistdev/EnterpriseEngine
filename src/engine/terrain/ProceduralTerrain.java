package engine.terrain;

import engine.core.Component;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.render.Loader;
import engine.render.model.RawModel;
import engine.render.texture.TerrainTexture;
import engine.render.texture.TerrainTexturePack;
import engine.util.MathUtil;

/**
 * Created by anarchist on 6/20/16.
 */
public class ProceduralTerrain extends Component implements Terrain {
    private static final int SIZE = 800;

    private int VERTEX_COUNT = 128;

    private float x;
    private float z;


    private float[][] heights;
    TerrainRenderComponent terrainRenderComponent;

    private TerrainHeightGenerator generator = new TerrainHeightGenerator();

    public ProceduralTerrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap) {
        terrainRenderComponent = new TerrainRenderComponent();
        terrainRenderComponent.setTerrainTexturePack(texturePack);
        terrainRenderComponent.setBlendMap(blendMap);

        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;

        terrainRenderComponent.setModel(generateTerrain(loader));

    }

    @Override
    protected void onAdd() {
        getParent().addRenderComponent(terrainRenderComponent);
        getParent().getTransform().setPosition(new Vector3f(x, 0, z));
    }

    private RawModel generateTerrain(Loader loader){
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
                float height = getHeight(j, i, generator);
                heights[j][i] = height;
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calculateNormal(j, i, generator);
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

    private Vector3f calculateNormal(int x, int z, TerrainHeightGenerator terrainHeightGenerator) {
        float heightL = getHeight(x-1, z, terrainHeightGenerator);
        float heightR = getHeight(x+1, z, terrainHeightGenerator);
        float heightD = getHeight(x, z-1, terrainHeightGenerator);
        float heightU = getHeight(x, z+1, terrainHeightGenerator);

        Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }

    private float getHeight(int x, int z, TerrainHeightGenerator terrainHeightGenerator) {
        return terrainHeightGenerator.generateHeight(x, z);
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
