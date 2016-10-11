package engine.render;

import engine.core.Component;
import engine.core.Node;
import engine.input.CursorHandler;
import engine.input.MouseHandler;
import engine.input.ScrollHandler;
import engine.math.Vector3f;

/**
 /**
 * Created by anarchist on 6/14/16.
 */
public class Camera extends Component {
    private float distanceFromPlayer = 30;
    private float angleAroundPlayer = 0;

    private float maxPitch = 70;
    private float minPitch = 1;
    private float sensitivity = 0.1f;

    private float maxZoom = 70f;
    private float minZoom = 5f;

    //private Vector3f position = new Vector3f(309,10,742);
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch = 0;
    private float yaw = 0;
    private float roll;

    public Camera() {
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();

        calculateCameraPos(horizontalDistance, verticalDistance);

        this.yaw = 180 - (getParent().getTransform().getRotation().y + angleAroundPlayer);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void invertPitch() {
        this.pitch = -pitch;
    }

    private void calculateZoom() {
        float zoomLevel = ScrollHandler.getY() * sensitivity;
        //distanceFromPlayer -= zoomLevel;
        if (distanceFromPlayer - zoomLevel > minZoom && distanceFromPlayer - zoomLevel < maxZoom)
            distanceFromPlayer -= zoomLevel;
    }

    private void calculatePitch() {
        if (MouseHandler.isButtonDown(1)) {
            float pitchChange = CursorHandler.getMouseDY() * sensitivity;
            pitch -= pitchChange;
            if(pitch > maxPitch) pitch = maxPitch;
            if(pitch < minPitch) pitch = minPitch;
        }
    }

    private void calculateAngleAroundPlayer() {
        if (MouseHandler.isButtonDown(0)) {
            float angleChange = CursorHandler.getMouseDX() * sensitivity;
            angleAroundPlayer -= angleChange;
        }
    }

    private float calculateHorizontalDistance() {
        return (float)(distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float)(distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPos(float horizontal, float vertical) {
        float theta = getParent().getTransform().getRotation().y + angleAroundPlayer;
        float offsetX = (float)(horizontal * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float)(horizontal * Math.cos(Math.toRadians(theta)));

        position.x = getParent().getTransform().getPosition().x - offsetX;
        position.z = getParent().getTransform().getPosition().z - offsetZ;
        position.y = getParent().getTransform().getPosition().y + vertical + 8;
    }

}
