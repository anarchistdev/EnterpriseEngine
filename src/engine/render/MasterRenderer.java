package engine.render;

import engine.math.Vector4f;
import engine.core.Node;
import engine.math.Matrix4f;
import engine.math.Vector3f;
import engine.render.lighting.Light;
import engine.render.lighting.shadow.ShadowMasterRenderer;
import engine.render.skybox.SkyboxRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.*;

/**
 * Created by anarchist on 6/15/16.
 */
public class MasterRenderer {
    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000f;
    public static final Vector4f DEFAULT_CLIP_PLANE = new Vector4f(0, -1, 0, 100000000);

    private SkyboxRenderer skyboxRenderer;

    private List<Node> nodeList = new ArrayList<>();

    private static Matrix4f projectionMatrix;

    private ShadowMasterRenderer shadowMasterRenderer;

    private static final float RED = 0.5444f;
    private static final float GREEN = 0.62f;
    private static final float BLUE = 0.69f;
    private static final float ALPHA = 1.0f;

    private static Vector3f skyColorVar = new Vector3f(RED, GREEN, BLUE);

    public MasterRenderer(Loader loader, Camera camera) {
        enableCulling();

        shadowMasterRenderer = new ShadowMasterRenderer(camera);

        skyboxRenderer = new SkyboxRenderer(projectionMatrix);
        skyboxRenderer.init(loader);
    }

    public static Vector3f getSkyColor() {
        return skyColorVar;
    }

    public static void setSkyColor(Vector3f skyColorVar) {
        MasterRenderer.skyColorVar = skyColorVar;
    }

    public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
        prepare();

        for (Node node : nodeList) {
            if (!node.getRenderComponents().isEmpty()) {
                shadowMasterRenderer.calcToShadowMapSpaceMatrix();
                node.render(lights, camera, clipPlane, shadowMasterRenderer.getShadowOptions());
            }
        }

        skyboxRenderer.render(camera, skyColorVar);

        nodeList.clear();
    }

    /// Temporary method
    public void renderScene(Node rootNode, List<Light> lights, Camera camera, Vector4f action) {
        for (Node node : rootNode.getChildren()) {
            processNode(node);
        }

        render(lights, camera, action);
    }

    public void renderShadowMap(Node rootNode, Light sun) {
        for (Node node : rootNode.getChildren()) {
            processNode(node);
        }
        shadowMasterRenderer.render(nodeList, sun);
        nodeList.clear();
    }


    private void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(RED, GREEN, BLUE, ALPHA);

        GL11.glEnable(GL13.GL_MULTISAMPLE);
    }

    public static Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    private void processNode(Node node) {
        nodeList.add(node);
    }


    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
     }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

//    private void createProjectionMatrix() {
////        IntBuffer w = BufferUtils.createIntBuffer(1);
////        IntBuffer h = BufferUtils.createIntBuffer(1);
////        GLFW.glfwGetWindowSize(GLFW.glfwGetCurrentContext(), w, h);
////        int width = w.get(0);
////        int height = h.get(0);
//
//        int width = DisplayManager.getWindowWidth();
//        int height = DisplayManager.getWindowHeight();
//
//        float aspectRatio = (float)width / (float)height;
//        //float aspectRatio = DisplayManager.getAspectRatio();
//        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
//        float x_scale = y_scale / aspectRatio;
//        float frustum_length = FAR_PLANE - NEAR_PLANE;
//        projectionMatrix = new Matrix4f();
//        projectionMatrix.m00 = x_scale;
//        projectionMatrix.m11 = y_scale;
//        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
//        projectionMatrix.m23 = -1;
//        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
//        projectionMatrix.m33 = 0;
//    }

    public static void createProjectionMatrix() {
            projectionMatrix = new Matrix4f();
            float aspectRatio = (float) DisplayManager.getWindowWidth() / (float)DisplayManager.getWindowHeight();
            float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
            float x_scale = y_scale / aspectRatio;
            float frustum_length = FAR_PLANE - NEAR_PLANE;

            projectionMatrix.m00 = x_scale;
            projectionMatrix.m11 = y_scale;
            projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
            projectionMatrix.m23 = -1;
            projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
            projectionMatrix.m33 = 0;
    }

    public void cleanUp() {
        for (Node node : nodeList) {
            node.cleanUp();
        }
        this.shadowMasterRenderer.cleanUp();
    }

}
