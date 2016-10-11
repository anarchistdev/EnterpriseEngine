package engine.render;

import engine.core.Component;
import engine.math.Matrix4f;
import engine.math.Vector4f;
import engine.render.lighting.Light;
import engine.render.lighting.shadow.ShadowMasterRenderer;
import engine.render.lighting.shadow.ShadowOptions;

import java.util.List;

/**
 * Created by anarchist on 6/17/16.
 */
public abstract class BaseRenderer extends Component {
    public abstract void render(List<Light> lights, Camera camera, Vector4f clipPlane, ShadowOptions shadowOptions);
    public abstract void cleanUp();
}
