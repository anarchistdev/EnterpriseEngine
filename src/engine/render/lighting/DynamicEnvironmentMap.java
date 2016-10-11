package engine.render.lighting;

import engine.render.env.Fbo;

/**
 * Created by anarchist on 7/26/16.
 */
public class DynamicEnvironmentMap {
    private Fbo left;
    private Fbo right;
    private Fbo top;
    private Fbo bottom;
    private Fbo front;
    private Fbo back;

    private int texture;

    public DynamicEnvironmentMap() {

    }

}
