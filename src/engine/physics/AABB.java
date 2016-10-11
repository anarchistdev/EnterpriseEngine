package engine.physics;

import engine.math.Vector3f;

/**
 * Created by anarchist on 7/5/16.
 */
public class AABB {
    private Vector3f minExtents;
    private Vector3f maxExtents;

    public AABB(Vector3f minExtents, Vector3f maxExtents) {
        this.minExtents = minExtents;
        this.maxExtents = maxExtents;
    }

    public AABB() {
        this.minExtents = new Vector3f();
        this.maxExtents = new Vector3f();
    }

    public IntersectData intersectAABB(AABB box) {
        Vector3f distances1 = new Vector3f();
        Vector3f distances2 = new Vector3f();

        distances1 = Vector3f.sub(box.getMinExtents(), maxExtents, distances1);
        distances2 = Vector3f.sub(minExtents, box.getMinExtents(), distances2);
        Vector3f distance = Vector3f.max(distances1, distances2);

        float maxDistance = distance.max();

        return new IntersectData(maxDistance < 0, maxDistance);
    }

    public boolean intersectPoint(Vector3f loc) {
        if(loc.x > minExtents.x && loc.x < maxExtents.x &&
                loc.y > minExtents.y && loc.y < maxExtents.y &&
                loc.z > minExtents.z && loc.z < maxExtents.z) {
            return true;
        }

        return false;

    }

    public Vector3f getMinExtents() {
        return minExtents;
    }

    public void setMinExtents(Vector3f minExtents) {
        this.minExtents = minExtents;
    }

    public Vector3f getMaxExtents() {
        return maxExtents;
    }

    public void setMaxExtents(Vector3f maxExtents) {
        this.maxExtents = maxExtents;
    }
}
