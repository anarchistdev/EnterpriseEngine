package engine.input;

/**
 * Created by anarchist on 6/15/16.
 */
import engine.math.Vector2f;
import engine.render.DisplayManager;
import org.lwjgl.glfw.GLFWCursorPosCallback;


public class CursorHandler extends GLFWCursorPosCallback {

    private static float mouseX;
    private static float mouseY;

    private static int mouseDX;
    private static int mouseDY;

    @Override
    public void invoke(long window, double xpos, double ypos) {
        // TODO Auto-generated method stub
        // this basically just prints out the X and Y coordinates
        // of our mouse whenever it is in our window
        ///System.out.println("X: " + xpos + " Y: " + ypos);

        mouseDX = (int)(xpos - mouseX);
        mouseDY = (int)(ypos - mouseY);

        mouseX = (float)xpos;
        mouseY = (float)ypos;

    }

    public static float getMouseX() {
        return mouseY;
    }

    public static float getMouseY() {
        return mouseY;
    }

    public static int getMouseDX() {
        return mouseDX;
    }

    public static int getMouseDY() {
        return mouseDY;
    }

    public static Vector2f getNormalizedCoords() {
        float normalizeX =  -1.0f + 2.0f * getMouseX() / DisplayManager.getWindowWidth();
        float normalizeY =  1.0f - 2.0f  * getMouseY() / DisplayManager.getWindowHeight();
        return new Vector2f(normalizeX, normalizeY);
    }
}