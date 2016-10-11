package engine.render.particle;

import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.render.Camera;
import engine.render.DisplayManager;
import engine.render.texture.Texture;

/**
 * Created by anarchist on 6/19/16.
 */
public class Particle {
    private Vector3f position;
    private Vector3f velocity;
    private float gravityEffect;
    private float lifeLength;
    private float rotation;
    private float scale;

    private float elapsedTime = 0;

    private static float GRAVITY = -50;

    private ParticleTexture texture;

    private Vector2f texOfffset1 = new Vector2f();
    private Vector2f texOfffset2 = new Vector2f();
    private float blendFactor;

    private Vector3f reusableChange = new Vector3f();

    private float distance;

    private boolean alive = false;

    public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale, ParticleTexture texture) {
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        this.texture = texture;
    }


    // TODO - implement
    public void setActive(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale, ParticleTexture texture, boolean alive) {
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    protected boolean update(Camera camera) {
        velocity.y += GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
        reusableChange.set(velocity);
        reusableChange.scale((float)DisplayManager.getFrameTimeSeconds());
        Vector3f.add(reusableChange , position, position);
        distance = Vector3f.sub(camera.getPosition(), getPosition(), null).lengthSquared();
        updateTexCoordInfo();
        elapsedTime += DisplayManager.getFrameTimeSeconds();
        return elapsedTime < lifeLength;
    }

    public ParticleTexture getTexture() {
        return texture;
    }

    public Vector2f getTexOfffset1() {
        return texOfffset1;
    }

    public float getBlendFactor() {
        return blendFactor;
    }

    public Vector2f getTexOfffset2() {
        return texOfffset2;
    }

    private void updateTexCoordInfo() {
        float lifeFactor = elapsedTime / lifeLength;
        int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
        float atlasProgression = lifeFactor * stageCount;
        int index1 = (int)Math.floor(atlasProgression);
        int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
        this.blendFactor = atlasProgression % 1;
        setTextureOffset(texOfffset1, index1);
        setTextureOffset(texOfffset2, index2);
    }

    private void setTextureOffset(Vector2f offset, int index) {
        int column = index % texture.getNumberOfRows();
        int row = index / texture.getNumberOfRows();
        offset.x = (float)column / texture.getNumberOfRows();
        offset.y = (float)row / texture.getNumberOfRows();
    }

    public float getDistance() {
        return distance;
    }

}
