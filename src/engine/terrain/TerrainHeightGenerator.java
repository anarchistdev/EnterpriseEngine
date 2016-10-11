package engine.terrain;

import engine.util.MathUtil;

import java.util.Random;

/**
 * Created by anarchist on 6/20/16.
 */
public class TerrainHeightGenerator {
    private static final float AMPLITUDE = 70f;
    private static final int OCTAVES = 4;
    private static final float ROUGHNESS = 0.3f;

    private Random random = new Random();
    private int seed;

    public TerrainHeightGenerator() {
        this.seed = random.nextInt(1000000000);
    }

    public float generateHeight(int x, int z) {
//        float total = getInterpolatedNoise(x/4f, z/4f) * AMPLITUDE;
//        total += getInterpolatedNoise(x/2f, z/2f) * AMPLITUDE/3f;
//        total += getInterpolatedNoise(x/2f, z/2f) * AMPLITUDE/3f;
//        total += getInterpolatedNoise(x, z) * AMPLITUDE/9f;
//        return total;

        float total = 0;
        float d = (float)Math.pow(2, OCTAVES-1);
        for (int i =0; i < OCTAVES; i++) {
            float freq = (float)Math.pow(2, i) / d;
            float exp = (float)Math.pow(ROUGHNESS, i) * AMPLITUDE;
            total += getInterpolatedNoise(x * freq, z * freq) * exp;
        }

        return total;
    }

    private float getInterpolatedNoise(float x, float z) {
        int intX = (int)x;
        int intZ = (int)z;
        float fraX = x - intX;
        float fracZ = z - intZ;

        float v1 = getSmoothNoise(intX, intZ);
        float v2 = getSmoothNoise(intX + 1, intZ);
        float v3 = getSmoothNoise(intX, intZ + 1);
        float v4 = getSmoothNoise(intX + 1, intZ + 1);
        float i1 = cosInterpolate(v1, v2, fraX);
        float i2 = cosInterpolate(v3, v4, fraX);
        return cosInterpolate(i1, i2, fracZ);
    }

    private float cosInterpolate(float a, float b, float blend) {
        double theta = blend * Math.PI;
        float f = (float)(1f - Math.cos(theta)) * 0.5f;
        return a * (1f - f) + b * f;
    }

    private float getSmoothNoise(int x, int z) {
        float corners = (getNoise(x-1, z-1) + getNoise(x+1, z-1) + getNoise(x+1, z-1) + getNoise(x-1, z+1)) / 16f;
        float sides = (getNoise(x-1, z) + getNoise(x+1, z) + getNoise(x, z-1) + getNoise(x, z+1)) / 8f;
        float center = getNoise(x, z) / 4f;
        return corners + sides + center;
    }

    private float getNoise(int x, int z) {
        random.setSeed(x * 49874 + z * 345684 + seed);
        return random.nextFloat() * 2f - 1f;
    }
}
