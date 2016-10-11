package engine.input;

/**
 * Created by anarchist on 6/15/16.
 */
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseHandler extends GLFWMouseButtonCallback {

    private static boolean[] buttons = new boolean[5];
    @Override
    public void invoke(long window, int button, int action, int mods) {
        buttons[button] = (action == GLFW.GLFW_PRESS);
    }

    public static boolean isButtonDown(int button) {
        return buttons[button];
    }

}
