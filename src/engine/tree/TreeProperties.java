package engine.tree;

/**
 * Created by anarchist on 7/5/16.
 */
public class TreeProperties {
    private float clumpMin;
    private float clumpMax;
    private float lengthFalloffFactor;
    private float lengthFalloffPower;
    private float branchFactor;
    private float radiusFalloffRate;
    private float climbRate;
    private float trunkKink;
    private float maxRadius;
    private int treeSteps;
    private float taperRate;
    private float twistRate;
    private int segments;
    private int levels;
    private float sweepAmount;
    private float initialBranchLength;
    private float dropAmount;
    private float growAmount;
    private float vMultiplier;
    private float twigScale;
    private int seed;
    private int mRSeed;
    private float trunkLength;

    public TreeProperties() {

    }

    public float getClumpMin() {
        return clumpMin;
    }

    public int getmRSeed() {
        return mRSeed;
    }

    public float getTrunkLength() {
        return trunkLength;
    }

    public void setTrunkLength(float trunkLength) {
        this.trunkLength = trunkLength;
    }

    public void setmRSeed(int mRSeed) {
        this.mRSeed = mRSeed;
    }

    public void setClumpMin(float clumpMin) {
        this.clumpMin = clumpMin;
    }

    public float getLengthFalloffPower() {
        return lengthFalloffPower;
    }

    public void setLengthFalloffPower(float lengthFalloffPower) {
        this.lengthFalloffPower = lengthFalloffPower;
    }

    public float getClumpMax() {
        return clumpMax;
    }

    public void setClumpMax(float clumpMax) {
        this.clumpMax = clumpMax;
    }

    public float getLengthFalloffFactor() {
        return lengthFalloffFactor;
    }

    public void setLengthFalloffFactor(float lengthFalloffFactor) {
        this.lengthFalloffFactor = lengthFalloffFactor;
    }

    public float getBranchFactor() {
        return branchFactor;
    }

    public void setBranchFactor(float branchFactor) {
        this.branchFactor = branchFactor;
    }

    public float getRadiusFalloffRate() {
        return radiusFalloffRate;
    }

    public void setRadiusFalloffRate(float radiusFalloffRate) {
        this.radiusFalloffRate = radiusFalloffRate;
    }

    public float getClimbRate() {
        return climbRate;
    }

    public void setClimbRate(float climbRate) {
        this.climbRate = climbRate;
    }

    public float getTrunkKink() {
        return trunkKink;
    }

    public void setTrunkKink(float trunkKink) {
        this.trunkKink = trunkKink;
    }

    public float getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(float maxRadius) {
        this.maxRadius = maxRadius;
    }

    public int getTreeSteps() {
        return treeSteps;
    }

    public void setTreeSteps(int treeSteps) {
        this.treeSteps = treeSteps;
    }

    public float getTaperRate() {
        return taperRate;
    }

    public void setTaperRate(float taperRate) {
        this.taperRate = taperRate;
    }

    public float getTwistRate() {
        return twistRate;
    }

    public void setTwistRate(float twistRate) {
        this.twistRate = twistRate;
    }

    public int getSegments() {
        return segments;
    }

    public void setSegments(int segments) {
        this.segments = segments;
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public float getSweepAmount() {
        return sweepAmount;
    }

    public void setSweepAmount(float sweepAmount) {
        this.sweepAmount = sweepAmount;
    }

    public float getInitialBranchLength() {
        return initialBranchLength;
    }

    public void setInitialBranchLength(float initialBranchLength) {
        this.initialBranchLength = initialBranchLength;
    }

    public float getDropAmount() {
        return dropAmount;
    }

    public void setDropAmount(float dropAmount) {
        this.dropAmount = dropAmount;
    }

    public float getGrowAmount() {
        return growAmount;
    }

    public void setGrowAmount(float growAmount) {
        this.growAmount = growAmount;
    }

    public float getvMultiplier() {
        return vMultiplier;
    }

    public void setvMultiplier(float vMultiplier) {
        this.vMultiplier = vMultiplier;
    }

    public float getTwigScale() {
        return twigScale;
    }

    public void setTwigScale(float twigScale) {
        this.twigScale = twigScale;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    protected float random(float aFixed) {
        if (aFixed != 0) {
            aFixed = (float)mRSeed;
        }

        return (float)(Math.abs(Math.cos(aFixed + aFixed * aFixed)));
    }
}
