package engine.render.particle;

import engine.math.Matrix4f;
import engine.render.Camera;
import engine.render.Loader;
import engine.algorithim.InsertionSort;

import java.util.*;

/**
 * Created by anarchist on 6/19/16.
 */
public class ParticleManager {
    private static Map<ParticleTexture, List<Particle>> particles = new HashMap<>();
    private static ParticleRenderer renderer;

    private static int numParticles;

    public static void init(Loader loader, Matrix4f projMat) {
        renderer = new ParticleRenderer(loader, projMat);
    }

    public static void update(Camera camera) {
        Iterator<Map.Entry<ParticleTexture, List<Particle>>> mapIter = particles.entrySet().iterator();

        while (mapIter.hasNext()) {
            List<Particle> list = mapIter.next().getValue();
            Iterator<Particle> iterator = list.iterator();
            while (iterator.hasNext()) {
                Particle p = iterator.next();
                boolean stillAlive = p.update(camera);
                if (!stillAlive) {
                    iterator.remove();
                    if (list.isEmpty()) {
                        mapIter.remove();
                    }
                }
            }

            InsertionSort.sortHighToLow(list);

        }

    }

    public static void renderParticles(Camera camera) {
        renderer.render(particles, camera);
    }

    public void cleanUp() {
        renderer.cleanUp();
    }

    public static void addParticle(Particle particle) {
        List<Particle> list = particles.get(particle.getTexture());
        if (list == null) {
            list = new ArrayList<>();
            particles.put(particle.getTexture(), list);
        }
        list.add(particle);
    }

    public static int getNumParticles() {
        return numParticles;
    }

    protected static void setNumParticles(int num) {
        numParticles = num;
    }
}
