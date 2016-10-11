package engine.weather;

import engine.math.Vector3f;
import engine.render.particle.ParticleSystem;
import engine.render.particle.ParticleTexture;
import engine.render.texture.Texture;
import engine.util.MathUtil;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anarchist on 6/21/16.
 */
public class WeatherManager {
    private static final int NUM_SYSTEMS = 2;

    private static List<ParticleSystem> darkCloudSystems = new ArrayList<>();
    private static List<ParticleSystem> rainSystems = new ArrayList<>();
    private static List<ParticleSystem> whiteCloudSystems = new ArrayList<>();

    private static List<Vector3f> rainPositions = new ArrayList<>();
    private static List<Vector3f> whiteCloudPositions = new ArrayList<>();

    private static ParticleTexture rainTex = new ParticleTexture(Texture.loadTexture("particle/weather/transparent_raindrop2.png").getTextureID(), 1);
    private static ParticleTexture darkCloudTex = new ParticleTexture(Texture.loadTexture("particle/weather/smokesprite-dark.png").getTextureID(), 8);
    private static ParticleTexture whiteCloudTex = new ParticleTexture(Texture.loadTexture("particle/weather/smokesprite.png").getTextureID(), 8);

    public static void init() {
        for (int i = 0; i < NUM_SYSTEMS; i++) {
            darkCloudSystems.add(new ParticleSystem(10, 2f, 0f, 10f, 15, darkCloudTex));
            ParticleSystem rainSystem  = new ParticleSystem(750, 10f, 0.4f, 4f, 1, rainTex);
            rainSystem.setLifeError(0.1f);
            rainSystems.add(rainSystem);
            whiteCloudSystems.add(new ParticleSystem(10, 2f, 0f, 10f, 15, whiteCloudTex));
        }
        generateClouds();
    }

    private static void generateClouds() {
        generateRainPositions();
        generateWhiteCloudPositions();
    }

    private static void generateWhiteCloudPositions() {
        for (int i = 0; i < NUM_SYSTEMS; i++) {
            float x = MathUtil.randomNextFloat(10, 600);
            float z = MathUtil.randomNextFloat(10, 700);
            float y = MathUtil.randomNextFloat(50, 150);

            whiteCloudPositions.add(new Vector3f(x,y,z));
        }
    }

    private static void generateRainPositions() {
        for (int i = 0; i < NUM_SYSTEMS; i++) {
            float x = MathUtil.randomNextFloat(10, 600);
            float z = MathUtil.randomNextFloat(10, 700);
            float y = MathUtil.randomNextFloat(50, 150);

            rainPositions.add(new Vector3f(x,y,z));
        }
    }

    public static void update() {
        for (int i = 0; i < NUM_SYSTEMS; i++) {
            darkCloudSystems.get(i).generateParticles(rainPositions.get(i));
            rainSystems.get(i).generateParticles(rainPositions.get(i));
            whiteCloudSystems.get(i).generateParticles(whiteCloudPositions.get(i));
        }
    }
}
