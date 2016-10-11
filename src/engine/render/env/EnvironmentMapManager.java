package engine.render.env;

import engine.render.Camera;
import engine.render.RenderPass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anarchist on 8/3/16.
 */
public class EnvironmentMapManager {
    private static List<EnvironmentMap> fbos = new ArrayList<>();
    private static Map<RenderPass, EnvironmentMap> masterRenderMap = new HashMap<>();

    public static void prepare(Camera camera) {
        for (EnvironmentMap map : fbos) {
            map.prepare(camera);
        }
    }

    public static void finish(Camera camera) {
        for (EnvironmentMap map : fbos) {
            map.finish(camera);
        }
    }

    public static void addFbo(EnvironmentMap fbo) {
        fbos.add(fbo);
    }

    public static void removeFbo(EnvironmentMap fbo) {
        fbos.remove(fbo);
    }
}
