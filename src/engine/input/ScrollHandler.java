package engine.input;

/**
 * Created by anarchist on 6/15/16.
 */
import org.lwjgl.glfw.GLFWScrollCallback;

public class ScrollHandler extends GLFWScrollCallback {

    private static float Y = 0;
    private static float deltaY;
    private static float fov = 0;

    @Override
    public void invoke(long window, double xoffset, double yoffset) {
        deltaY = Y - (float)(yoffset / 2f);
        Y += yoffset/2f;

        fov -= yoffset;
    }

    public static float getFov() {
        return fov;
    }

    public static float getY() {
        return Y;
    }

    public static float getDeltaY() {
        return deltaY;
    }
}
