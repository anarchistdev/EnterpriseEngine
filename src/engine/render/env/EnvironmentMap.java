package engine.render.env;

import engine.render.Camera;

/**
 * Created by anarchist on 8/3/16.
 */
public interface EnvironmentMap {
    /**
     * This method allows the fbo to put the camera into an ideal positions before rendering
     * @param camera The main camera
     */
    void prepare(Camera camera);

    /**
     * This method should unbind the framebuffer, and reset the camera to it's
     * default position.
     * @param camera The main camera
     */
    void finish(Camera camera);
}
