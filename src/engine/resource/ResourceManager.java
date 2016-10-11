package engine.resource;

/**
 * Created by anarchist on 6/17/16.
 */
public class ResourceManager {
    public static final String SHADER_INCLUDE_DIRECTIVE = "#include";

    private static final String SHADER_INCLUDE_PATH = "res/shaders/";
    private static final String MODEL_INCLUDE_PATH = "res/models/";
    private static final String TEXTURE_INCLUDE_PATH = "res/textures/";
    private static final String FONT_RES_PATH = "res/font";

    public static String LoadPath(String string) {
        return string;
    }

    public static String LoadShaderPath(String path) {
        return LoadPath(SHADER_INCLUDE_PATH + path);
    }

    public static String LoadModelPath(String path) {
        return LoadPath(MODEL_INCLUDE_PATH + path);
    }

    public static String LoadTexturePath(String path) {
        return LoadPath(TEXTURE_INCLUDE_PATH + path);
    }

    public static String LoadFontPath(String path) {
        return LoadPath(FONT_RES_PATH + path);
    }

}
