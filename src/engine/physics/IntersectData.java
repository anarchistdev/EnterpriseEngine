package engine.physics;

/**
 * Created by anarchist on 7/5/16.
 */
public class IntersectData {
    private boolean doesIntersect;
    private float distance;

    public IntersectData(boolean doesIntersect, float distance) {
        this.distance = distance;
        this.doesIntersect = doesIntersect;
    }

    public boolean isDoesIntersect() {
        return doesIntersect;
    }

    public void setDoesIntersect(boolean doesIntersect) {
        this.doesIntersect = doesIntersect;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
