package engine.render.model;

import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.render.Loader;
import engine.render.model.assimp.GameWrapperProvider;
import engine.resource.ResourceManager;
import engine.util.JavaUtil;
import jassimp.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by anarchist on 7/22/16.
 */
public class MeshLoader {

    private static Set<AiPostProcessSteps> processSteps = new HashSet<>();
    private static GameWrapperProvider gameWrapperProvider = new GameWrapperProvider();

    public static void init() {
        processSteps.add(AiPostProcessSteps.CALC_TANGENT_SPACE);
        processSteps.add(AiPostProcessSteps.OPTIMIZE_GRAPH);
        processSteps.add(AiPostProcessSteps.OPTIMIZE_MESHES);
        processSteps.add(AiPostProcessSteps.GEN_SMOOTH_NORMALS);
        processSteps.add(AiPostProcessSteps.TRIANGULATE);

        Jassimp.setWrapperProvider(gameWrapperProvider);
    }

    public static GameWrapperProvider getGameWrapperProvider() {
        return gameWrapperProvider;
    }

    public static Mesh loadMesh(String file, Loader loader) {
        AiScene scene = null;

        Mesh mesh = new Mesh();

        try {
            scene = Jassimp.importFile(ResourceManager.LoadModelPath(file) , processSteps);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (scene.getNumMeshes() > 0) {
            for (AiMesh aiMesh : scene.getMeshes()) {
                mesh.addRawModel((processMesh(aiMesh, loader)));
                mesh.getAnimationController().addBones(aiMesh.getBones());
            }
        } else {
            System.err.println("No meshes found in file: " + file);
            return null;
        }

        if (scene.getNumAnimations() > 0) {
            mesh.getAnimationController().setAnimations(scene.getAnimations());
        }


        return mesh;

    }


    private static MeshData fillData(AiMesh mesh) {
        MeshData data = new MeshData();

        for (int i = 0; i < mesh.getNumVertices(); i++) {
            Vector3f position = new Vector3f(mesh.getPositionX(i), mesh.getPositionY(i), mesh.getPositionZ(i));
            Vertex vertex = new Vertex(data.getVertices().size(), position);

            Vector2f fullCoords = mesh.hasTexCoords(0) ? new Vector2f(mesh.getTexCoordU(i, 0), mesh.getTexCoordV(i, 0)) : new Vector2f(0,0);
            data.getTexCoords().add(fullCoords);

            data.getIndices().add(mesh.getIndexBuffer().get(i));

            data.getNormals().add(new Vector3f(mesh.getNormalX(i), mesh.getNormalY(i), mesh.getNormalZ(i)));
            vertex.setNormalIndex(data.getVertices().size());

            if (mesh.hasTangentsAndBitangents()) {

                vertex.setTangent(new Vector3f(mesh.getTangentX(i), mesh.getTangentY(i), mesh.getTangentZ(i)));
            }

            vertex.setTextureIndex(data.getVertices().size());

            data.getVertices().add(vertex);

        }

        return data;
    }

    private static RawModel processMesh(AiMesh mesh, Loader loader) {

        MeshData data = fillData(mesh);

        float[] verticesArray = new float[data.getVertices().size() * 3];
        float[] texturesArray = new float[data.getVertices().size() * 2];
        float[] normalsArray = new float[data.getVertices().size() * 3];
        float[] tangentsArray = new float[data.getVertices().size() * 3];
        int[] indexBuffer = JavaUtil.listToArray(data.getIndices());

        if (mesh.hasTangentsAndBitangents()) {
            float furthest = data.convertDataToArrays(verticesArray, texturesArray, normalsArray, tangentsArray);
            return loader.loadToVAO(verticesArray, tangentsArray, texturesArray, normalsArray, indexBuffer);
        } else {
            float furthest = data.convertDataToArrays(verticesArray, texturesArray, normalsArray);
            return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indexBuffer);
        }
    }


}
