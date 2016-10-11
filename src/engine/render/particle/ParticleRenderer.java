package engine.render.particle;

import engine.math.Matrix4f;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.render.Camera;
import engine.render.Loader;
import engine.render.model.RawModel;
import engine.render.texture.Texture;
import engine.util.MathUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.system.CallbackI;

import java.nio.FloatBuffer;
import java.util.*;

/**
 * Created by anarchist on 6/19/16.
 */

public class ParticleRenderer {

    private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
    private static final int MAX_INSTANCES = 100000;
    private static final int INSTANCE_DATA_LENGTH = 21;


    private RawModel quad;
    private ParticleShader shader;

    private Loader loader;
    private int vbo;

    private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
    private int pointer;

    private List<Particle> particleList = new ArrayList<>();

    protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix) {
        this.loader = loader;
        this.vbo = loader.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
        quad = loader.loadToVAO(VERTICES, 2);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 4, 4, INSTANCE_DATA_LENGTH, 12);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 5, 4, INSTANCE_DATA_LENGTH, 16);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 6, 1, INSTANCE_DATA_LENGTH, 20);
        shader = new ParticleShader();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    protected void render(Map<ParticleTexture, List<Particle>> sortedParticles, Camera camera) {
        Matrix4f viewMat = MathUtil.createViewMatrix(camera);
        prepare();
//        Map<ParticleTexture, List<Particle>> sortedParticles = new HashMap<>();
//        for (ParticleTexture texture : particles.keySet()) {
//            List<Particle> sortList = particles.get(texture);
//            Collections.sort(sortList, new Comparator<Particle>() {
//                @Override
//                public int compare(Particle p1, Particle p2) {
//                    return (int)(p1.getDistance() - p2.getDistance());
//                }
//            });
//
//            ParticleTexture sortedTexture = MathUtil.getKeyByValue(particles, sortList);
//
//            sortedParticles.put(sortedTexture, sortList);
//        }
        for (ParticleTexture texture : sortedParticles.keySet()) {
            bindTexture(texture);
            particleList = sortedParticles.get(texture);
            ParticleManager.setNumParticles(particleList.size());
            pointer = 0;
            float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];
            for (Particle particle : particleList) {
                vboData = updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMat, vboData);
                updateTexCoordInfo(particle, vboData);
            }
            loader.updateVbo(vbo, vboData, buffer);
            GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particleList.size());
        }

        finishRendering();
    }

    //The code below is for the updateModelViewMatrix() method
    //modelMatrix.m00 = viewMatrix.m00;
    //modelMatrix.m01 = viewMatrix.m10;
    //modelMatrix.m02 = viewMatrix.m20;
    //modelMatrix.m10 = viewMatrix.m01;
    //modelMatrix.m11 = viewMatrix.m11;
    //modelMatrix.m12 = viewMatrix.m21;
    //modelMatrix.m20 = viewMatrix.m02;
    //modelMatrix.m21 = viewMatrix.m12;
    //modelMatrix.m22 = viewMatrix.m22;

    protected void cleanUp() {
        shader.cleanUp();
    }

    private void updateTexCoordInfo(Particle particle, float[] data) {
        data[pointer++] = particle.getTexOfffset1().x;
        data[pointer++] = particle.getTexOfffset1().y;
        data[pointer++] = particle.getTexOfffset2().x;
        data[pointer++] = particle.getTexOfffset2().y;
        data[pointer++] = particle.getBlendFactor();
    }

    private void bindTexture(ParticleTexture texture) {
        if (texture.isUseAdditiveBlending()) {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        shader.loadNumberOfRows(texture.getNumberOfRows());
    }

    private float[] updateModelViewMatrix(Vector3f positon, float rotation, float scale, Matrix4f viewMatrix, float[] vboData) {
        Matrix4f modelMatrix = new Matrix4f();
        Matrix4f.translate(positon, modelMatrix, modelMatrix);


        modelMatrix.m00 = viewMatrix.m00;
        modelMatrix.m01 = viewMatrix.m10;
        modelMatrix.m02 = viewMatrix.m20;
        modelMatrix.m10 = viewMatrix.m01;
        modelMatrix.m11 = viewMatrix.m11;
        modelMatrix.m12 = viewMatrix.m21;
        modelMatrix.m20 = viewMatrix.m02;
        modelMatrix.m21 = viewMatrix.m12;
        modelMatrix.m22 = viewMatrix.m22;

//        Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
//        Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);
//        Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
//        storeMatrixData(modelViewMatrix, vboData);

        Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
        Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0,0,1), modelViewMatrix, modelViewMatrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), modelViewMatrix, modelViewMatrix);

        return storeMatrixData(modelViewMatrix, vboData);
    }

    private float[] storeMatrixData(Matrix4f matrix, float[] vbo) {
        float[] vboData = vbo;
        vboData[pointer++] = matrix.m00;
        vboData[pointer++] = matrix.m01;
        vboData[pointer++] = matrix.m02;
        vboData[pointer++] = matrix.m03;
        vboData[pointer++] = matrix.m10;
        vboData[pointer++] = matrix.m11;
        vboData[pointer++] = matrix.m12;
        vboData[pointer++] = matrix.m13;
        vboData[pointer++] = matrix.m20;
        vboData[pointer++] = matrix.m21;
        vboData[pointer++] = matrix.m22;
        vboData[pointer++] = matrix.m23;
        vboData[pointer++] = matrix.m30;
        vboData[pointer++] = matrix.m31;
        vboData[pointer++] = matrix.m32;
        vboData[pointer++] = matrix.m33;

        return vboData;
    }

    private void prepare() {
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        GL20.glEnableVertexAttribArray(4);
        GL20.glEnableVertexAttribArray(5);
        GL20.glEnableVertexAttribArray(6);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);
    }

    private void finishRendering() {
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL20.glDisableVertexAttribArray(4);
        GL20.glDisableVertexAttribArray(5);
        GL20.glDisableVertexAttribArray(6);
        GL11.glEnable(GL11.GL_BLEND);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

}