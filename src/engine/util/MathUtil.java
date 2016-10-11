package engine.util;

import engine.math.*;
import engine.render.Camera;
import engine.render.DisplayManager;

import java.util.*;

/**
 * Created by anarchist on 6/14/16.
 */
public class MathUtil {

    private static Random random = new Random();

    // Compute barycentric coordinates (u, v, w) for
    // point p with respect to triangle (a, b, c)
    // Adapted from book Christer Ericson's Real-Time Collision Detection
    public static Vector3f barryCentric2(Vector3f p, Vector3f a, Vector3f b, Vector3f c, float u, float v, float w)  {
        Vector3f v0 = new Vector3f();
        v0 = Vector3f.sub(b, a, v0);
        Vector3f v1 = new Vector3f();
        v1 = Vector3f.sub(c, a, v1);
        Vector3f v2 = new Vector3f();
        v2 = Vector3f.sub(p,a,v2);

        float d00 = Vector3f.dot(v0, v0);
        float d01 = Vector3f.dot(v0, v1);
        float d11 = Vector3f.dot(v1, v1);
        float d20 = Vector3f.dot(v2, v0);
        float d21 = Vector3f.dot(v2, v1);
        float denom = d00 * d11 - d01 * d01;
        v = (d11 * d20 - d01 * d21) / denom;
        w = (d00 * d21 - d01 * d20) / denom;
        u = 1.0f - v - w;

        return new Vector3f(v,w,u);
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);

        Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);

        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, Quaternion rotation, Vector3f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);

        matrix = matrix.rotate(rotation);

        Matrix4f.scale(scale, matrix, matrix);

        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();

        Matrix4f.rotate((float)Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float)Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix, viewMatrix);
        // TODO - Add in roll

        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);

        return viewMatrix;
    }

    public static float randomNextFloat() {
        return random.nextFloat();
    }

    public static float randomNextFloat(float a, float b) {
        float random = randomNextFloat();
        float diff = b - a;
        float r = random * diff;
        return a + r;
    }

    public static int randomNextInt(int a) {
        return random.nextInt(a);
    }

    public static Matrix4f createTransformationMatrix(Vector2f translation, float ry, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y  * DisplayManager.getAspectRatio(), 1f), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,0,1), matrix, matrix);
        Matrix4f.translate(new Vector2f(), matrix, matrix);
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
        return matrix;
    }

    public static Vector3f convertToScreenSpace(Vector3f position, Matrix4f viewMatrix, Matrix4f projMatrix) {
        Vector4f coords = new Vector4f(position.x, position.y, position.z, 1f);
        Matrix4f.transform(viewMatrix, coords, coords);
        Matrix4f.transform(projMatrix, coords, coords);
        Vector3f screenCoords = clipSpaceToScreenSpace(coords);
        return screenCoords;
    }

    private static Vector3f clipSpaceToScreenSpace(Vector4f coords) {
        if (coords.w < 0) {
            return null;
        }
        Vector3f screenCoords = new Vector3f(((coords.x / coords.w) + 1) / 2f,
                1 - (((coords.y / coords.w) + 1) / 2f), coords.z);
        return screenCoords;
    }


    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

}
