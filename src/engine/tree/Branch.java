package engine.tree;

import engine.math.Vector3f;

/**
 * Created by anarchist on 7/6/16.
 */
public class Branch {
    private float rootRing;
    private float ring0;
    private float ring1;
    private float ring2;
    private Branch parent;
    private float length;
    private float trunkType;
    private float radius;
    private Vector3f head;
    private Vector3f tangent;
    private float end;
    private Branch child0;
    private Branch child1;

    public Branch(Vector3f aHead, Branch parent) {
        rootRing = 0;
        ring0 = 0;
        ring1 = 0;
        ring2 = 0;
        length = 1;
        trunkType = 0;
        radius = 0;
        head = aHead;
        tangent = new Vector3f(0,0,0);
        this.parent = parent;
        end = 0;
    }

    public float getRootRing() {
        return rootRing;
    }

    public void setRootRing(float rootRing) {
        this.rootRing = rootRing;
    }

    public float getRing0() {
        return ring0;
    }

    public void setRing0(float ring0) {
        this.ring0 = ring0;
    }

    public float getRing1() {
        return ring1;
    }

    public void setRing1(float ring1) {
        this.ring1 = ring1;
    }

    public float getRing2() {
        return ring2;
    }

    public void setRing2(float ring2) {
        this.ring2 = ring2;
    }

    public Branch getParent() {
        return parent;
    }

    public void setParent(Branch parent) {
        this.parent = parent;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getTrunkType() {
        return trunkType;
    }

    public void setTrunkType(float trunkType) {
        this.trunkType = trunkType;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Vector3f getHead() {
        return head;
    }

    public void setHead(Vector3f head) {
        this.head = head;
    }

    public Vector3f getTangent() {
        return tangent;
    }

    public void setTangent(Vector3f tangent) {
        this.tangent = tangent;
    }

    public float getEnd() {
        return end;
    }

    public void setEnd(float end) {
        this.end = end;
    }

    public Branch getChild0() {
        return child0;
    }

    public void setChild0(Branch child0) {
        this.child0 = child0;
    }

    public Branch getChild1() {
        return child1;
    }

    public void setChild1(Branch child1) {
        this.child1 = child1;
    }

    private Vector3f mirrorBranch(Vector3f vec, Vector3f normal, TreeProperties properties) {
        Vector3f v = Vector3f.cross(normal, Vector3f.cross(vec, normal, null), null);
        float s = properties.getBranchFactor() * Vector3f.dot(vec, null);
        Vector3f res = new Vector3f(vec.x - v.x * s,
                vec.y - v.y * s,
                vec.z - v.z * s);
        return res;
    }

    protected void split(int aLevel, int aSteps, TreeProperties properties, int al1, int al2) {
        int rLevel = properties.getLevels() - aLevel;
        Vector3f po;
        if (parent != null) {
            po = parent.getHead();
        } else {
            po = new Vector3f(0,0,0);
            trunkType = 1;
        }

        Vector3f so = head;
        Vector3f dir;
        dir = so.normalise(Vector3f.sub(so, po, null));

        Vector3f a = new Vector3f(dir.z, dir.x, dir.y);
        Vector3f normal = Vector3f.cross(dir, a, null);
        Vector3f tangent = Vector3f.cross(dir, normal, null);
        float r = properties.random(rLevel * 10 + al1 * 5.0f + al2 + properties.getSeed());

        Vector3f adj = Vector3f.add(Vector3f.scale(normal, r), Vector3f.scale(tangent, 1-r), null);
        if (r > 0.5f) {
            adj = Vector3f.scale(adj, 1);
        }

        float clump = (properties.getClumpMax() - properties.getClumpMin()) * r + properties.getClumpMin();
        Vector3f newDir = Vector3f.normaliseStatic(Vector3f.add(Vector3f.scale(adj, 1-clump), Vector3f.scale(dir, clump), null));
        Vector3f newDir2 = mirrorBranch(newDir, dir, properties);
        if (r > 0.5) {
            Vector3f tmp = newDir;
            newDir = newDir2;
            newDir2 = tmp;
        }

        if (aSteps > 0) {
            float angle = aSteps / (float)properties.getTreeSteps() * 2 * (float)Math.PI * properties.getTwistRate();
            a.set((float)Math.sin(angle), r, (float)Math.cos(angle));
            newDir2 = a.normalise(null);
        }

        float growAmount = aLevel * aLevel / (float)(properties.getLevels() * properties.getLevels()) * properties.getGrowAmount();
        float dropAmount = rLevel * properties.getDropAmount();
        float sweepAmount = rLevel * properties.getSweepAmount();
        a.set(sweepAmount, dropAmount + growAmount, 0);
        newDir = Vector3f.add(newDir, a, null).normalise(null);
        newDir2 = Vector3f.add(newDir2, a, null).normalise(null);

        Vector3f head0 = Vector3f.add(so, Vector3f.scale(newDir, length), null);
        Vector3f head1 = Vector3f.add(so, Vector3f.scale(newDir2, length), null);
        child0 = new Branch(head0, this);
        child1 = new Branch(head1, this);

        child0.setLength((float)Math.pow(length, properties.getLengthFalloffPower()) * properties.getLengthFalloffFactor());
        child1.setLength((float)Math.pow(length, properties.getLengthFalloffPower()) * properties.getLengthFalloffFactor());

        if (aLevel > 0) {
            if (aSteps > 0) {
                a.set((r - 0.5f) * 2 * properties.getTrunkKink(),
                        properties.getClimbRate(),
                        (r - 0.5f) * 2 * properties.getTrunkKink());
                child0.setHead(Vector3f.add(head, a, null));
                child0.setTrunkType(1);
                child0.setLength(length * properties.getTaperRate());
                child0.split(aLevel, aSteps - 1, properties, al1 + 1, al2);
            } else {
                child0.split(aLevel - 1, 0, properties, al1 + 1 , al2);
            }

            child1.split(aLevel - 1, 0, properties, al1, al2 + 1);
        }

    }
}