package engine.render;

import engine.input.CursorHandler;
import engine.input.KeyboardHandler;
import engine.input.MouseHandler;
import engine.input.ScrollHandler;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;


/**
 * Created by anarchist on 6/13/16.
 *
 */
public class DisplayManager {

    private static String title = "Enterprise Engine";

    private static long window;

    private static double lastFrameTime;
    private static double delta;

    private static int WINDOW_WIDTH = 1920;
    private static int WINDOW_HEIGHT = 1080;
    private static float ASPECT_RATIO = WINDOW_WIDTH / WINDOW_HEIGHT;

    private static GLFWKeyCallback keyCallback;
    private static GLFWMouseButtonCallback mouseCallback;
    private static GLFWScrollCallback scrollCallback;
    private static GLFWCursorPosCallback cursorCallback;

    public static void createDisplay() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !GLFW.glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");


        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

        window = GLFW.glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, title, MemoryUtil.NULL, MemoryUtil.NULL);

        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create window");
        }

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        // Center our window
       GLFW.glfwSetWindowPos(
                window,
                (vidmode.width() - WINDOW_WIDTH) / 2,
                (vidmode.height() - WINDOW_HEIGHT) / 2
        );

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window);

        // Enable v-sync
        GLFW.glfwSwapInterval(1);

        // Make the window visible
        GLFW.glfwShowWindow(window);

        GLFW.glfwSetMouseButtonCallback(window, mouseCallback = new MouseHandler());
        GLFW.glfwSetScrollCallback(window, scrollCallback = new ScrollHandler());
        GLFW.glfwSetKeyCallback(window, keyCallback = new KeyboardHandler());
        GLFW.glfwSetCursorPosCallback(window, cursorCallback = new CursorHandler());

        lastFrameTime = currentTimeMillis();

        GL.createCapabilities();
    }

    public static boolean getShouldWindowClose() {
        return (GLFW.glfwWindowShouldClose(window));
    }

    public static void loop() {
        inputGraphicsUpdate();

        double currentTimeFrame = currentTimeMillis();
        delta = (currentTimeFrame - lastFrameTime) / 1000f;
        lastFrameTime = currentTimeFrame;
    }

    private static void inputGraphicsUpdate() {
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public static void closeDisplay() {
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    public static double getFrameTimeSeconds() {
        return delta;
    }

    public static int getWindowWidth() {
        return WINDOW_WIDTH;
    }

    public static int getWindowHeight() {
        return WINDOW_HEIGHT;
    }

    public static float getAspectRatio() {
        return ASPECT_RATIO;
    }

    public static double currentTimeMillis() {
        return GLFW.glfwGetTime() * 1000;
    }

}
