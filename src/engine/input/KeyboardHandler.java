package engine.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Created by anarchist on 6/15/16.
 */
public class KeyboardHandler extends GLFWKeyCallback {

    public static int[] keys = new int[65536];

    public void invoke(long window, int key, int scancode, int action, int mods) {
        if(key >= 0) {
            keys[key] = action;
        }
    }

    public static boolean isKeyDown(int keycode) {
        return keys[keycode] != GLFW.GLFW_RELEASE;
    }

    public static boolean isKeyPressed(int keycode) {
        return keys[keycode] == GLFW.GLFW_PRESS;
    }
}