package engine.components;

import engine.core.Component;
import engine.input.KeyboardHandler;
import engine.render.DisplayManager;
import engine.terrain.Terrain;
import org.lwjgl.glfw.GLFW;

/**
 * Created by anarchist on 6/15/16.
 */
public class PlayerComponent extends Component {

    private static final float RUN_SPEED = 40;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;


    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private boolean isJumping = false;

    public PlayerComponent() {
    }

    public void move(Terrain terrain) {
        checkInputs();
        increaseRotation(0, currentTurnSpeed * (float)DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * (float)DisplayManager.getFrameTimeSeconds();

        float dx = (float)(distance * Math.sin(Math.toRadians(getParent().getTransform().getRotation().y)));
        float dz = (float)(distance * Math.cos(Math.toRadians(getParent().getTransform().getRotation().y)));

        increasePosition(dx, 0, dz);
        float terrainHeight = terrain.getHeightOfTerrain(getParent().getTransform().getPosition().x, getParent().getTransform().getPosition().z);

        upwardsSpeed += GRAVITY * (float)DisplayManager.getFrameTimeSeconds();
        increasePosition(0, upwardsSpeed * (float)DisplayManager.getFrameTimeSeconds(), 0);
        if (getParent().getTransform().getPosition().y < terrainHeight) {
            upwardsSpeed = 0;
            getParent().getTransform().getPosition().setY(terrainHeight);
            isJumping = false;
        }
    }

    private void jump() {
        //if (!isJumping) {
        this.upwardsSpeed = JUMP_POWER;
        isJumping = true;
        //}
    }

    private void checkInputs() {
        if (KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_W)) {
            this.currentSpeed = RUN_SPEED;
        } else if (KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_S)) {
            this.currentSpeed = -RUN_SPEED;
        } else {
            this.currentSpeed = 0;
        }

        if (KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_A)) {
            this.currentTurnSpeed = TURN_SPEED;
        } else if (KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_D)) {
            this.currentTurnSpeed = -TURN_SPEED;
        } else {
            this.currentTurnSpeed = 0;
        }

        if (KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            jump();
        }
    }

    private void increasePosition(float dx, float dy, float dz) {
        getParent().getTransform().getPosition().x += dx;
        getParent().getTransform().getPosition().y += dy;
        getParent().getTransform().getPosition().z += dz;
    }

    private void increaseRotation(float dx, float dy, float dz) {
        getParent().getTransform().getRotation().x += dx;
        getParent().getTransform().getRotation().y += dy;
        getParent().getTransform().getRotation().z += dz;
    }
}
