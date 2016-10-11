package engine.render.model.assimp;

import engine.math.*;
import jassimp.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by anarchist on 7/24/16.
 */
public class GameWrapperProvider implements AiWrapperProvider<ReadableVector, Matrix4f, AiColor, AiNode, Quaternion> {

    @Override
    public ReadableVector wrapVector3f(ByteBuffer buffer, int offset, int numComponents) {
        if (numComponents == 3) {
            return new Vector3f(
                    buffer.getFloat(offset),
                    buffer.getFloat(offset + 4),
                    buffer.getFloat(offset + 8));
        } else {
            return new Vector2f(
                    buffer.getFloat(offset),
                    buffer.getFloat(offset + 4));
        }
    }

    /**
     * Wraps a 4x4 matrix of floats.<p>
     *
     * The calling code will allocate a new array for each invocation of this
     * method. It is safe to store a reference to the  passed in array and
     * use the array to store the matrix data.
     *
     * @param data the matrix data in row-major order
     * @return the wrapped matrix
     */
    @Override
//    public float[] wrapMatrix4f(float[] data) {
//        return transpose(data);
//    }
    public Matrix4f wrapMatrix4f(float[] data) {
        Matrix4f mat4 = new Matrix4f();
        mat4.load(FloatBuffer.wrap(data));
        return mat4;
    }


    /**
     * Wraps a RGBA color.<p>
     *
     * A color consists of 4 float values (r,g,b,a) starting from offset
     *
     * @param buffer the buffer to wrap
     * @param offset the offset into buffer
     * @return the wrapped color
     */
    @Override
    public AiColor wrapColor(ByteBuffer buffer, int offset) {
        return new AiColor(buffer, offset);
    }


    /**
     * Wraps a scene graph node.<p>
     *
     * See {@link AiNode} for a description of the scene graph structure used
     * by assimp.<p>
     *
     * The parent node is either null or an instance returned by this method.
     * It is therefore safe to cast the passed in parent object to the
     * implementation specific type
     *
     * @param parent the parent node
     * @param matrix the transformation matrix
     * @param meshReferences array of mesh references (indexes)
     * @param name the name of the node
     * @return the wrapped scene graph node
     */
    @Override
    public AiNode wrapSceneNode(Object parent, Object matrix, int[] meshReferences,
                                String name) {
        return new AiNode((AiNode) parent, matrix, meshReferences, name);
    }


    /**
     * Wraps a quaternion.<p>
     *
     * A quaternion consists of 4 float values (w,x,y,z) starting from offset
     *
     * @param buffer the buffer to wrap
     * @param offset the offset into buffer
     * @return the wrapped quaternion
     */
    @Override
    public Quaternion wrapQuaternion(ByteBuffer buffer, int offset) {
        float w = buffer.getFloat(offset);
        float x = buffer.getFloat(offset + 4);
        float y = buffer.getFloat(offset + 8);
        float z = buffer.getFloat(offset + 12);
        return new Quaternion(x, y, z, w);
    }

    public static float[] transpose(float[] matrix){
        float[] transposed = new float[16];
        transposed[0] = matrix[0];
        transposed[4] = matrix[1];
        transposed[8] = matrix[2];
        transposed[12] = matrix[3];
        transposed[1] = matrix[4];
        transposed[5] = matrix[5];
        transposed[9] = matrix[6];
        transposed[13] = matrix[7];
        transposed[2] = matrix[8];
        transposed[6] = matrix[9];
        transposed[10] = matrix[10];
        transposed[14] = matrix[11];
        transposed[3] = matrix[12];
        transposed[7] = matrix[13];
        transposed[11] = matrix[14];
        transposed[15] = matrix[15];
        return transposed;
    }
}
