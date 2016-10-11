package engine.render.shader;

import engine.math.Matrix4f;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.math.Vector4f;
import engine.resource.ResourceManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anarchist on 7/19/16.
 *
 */
public class RawShader {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    private Map<String, Integer> uniformMap = new HashMap<>();

    public RawShader(String vertFile, String fragFile) {
        vertexShaderID = loadShader(vertFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragFile, GL20.GL_FRAGMENT_SHADER);

        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);

        //bindAttributes();

        // Default attributes - if you need custom ones, extend RawShader
        // and define them yourselves, or use the public method bindAttribute(int, String)
        bindAttribute(0, "position");
        bindAttribute(1, "texCoords");
        bindAttribute(2, "normal");
        bindAttribute(3, "tangent");

        verify();

        //getAllUniformLocations();
    }

    //protected abstract void bindAttributes();

    private void verify() {
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
    }

    protected int getUniformLocation(String uniformName) {
        return  GL20.glGetUniformLocation(programID, uniformName);
    }

    //protected abstract void getAllUniformLocations();

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);

        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(vertexShaderID);

        GL20.glDeleteProgram(programID);
    }

    //protected abstract void bindAttributes();

    public void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    private void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    private void loadVector3(int location, Vector3f vector) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    private void loadVector2(int location, Vector2f vector) {
        GL20.glUniform2f(location, vector.x, vector.y);
    }

    private void loadInt(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    private void loadBoolean(int location, boolean b) {
        if (b) {
            GL20.glUniform1f(location, 1);
        } else {
            GL20.glUniform1f(location, 0);
        }
    }

    private void loadVector4(int location, Vector4f vector4f) {
        GL20.glUniform4f(location, vector4f.x, vector4f.y, vector4f.z, vector4f.w);
    }

    private void loadMatrix(int location, Matrix4f mat4) {
        mat4.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    public void setUniform(String name, float val) {
        int loc;
        if (inMap(name)) {
            loc = uniformMap.get(name);
        } else {
            loc = getUniformLocation(name);
            uniformMap.put(name, loc);
        }

        loadFloat(loc, val);
    }

    public void setUniform(String name, Vector3f val) {
        int loc;
        if (inMap(name)) {
            loc = uniformMap.get(name);
        } else {
            loc = getUniformLocation(name);
            uniformMap.put(name, loc);
        }
        loadVector3(loc, val);
    }

    public void setUniform(String name, Vector4f val) {
        int loc;
        if (inMap(name)) {
            loc = uniformMap.get(name);
        } else {
            loc = getUniformLocation(name);
            uniformMap.put(name, loc);
        }
        loadVector4(loc, val);
    }

    public void setUniform(String name, boolean val) {
        int loc;
        if (inMap(name)) {
            loc = uniformMap.get(name);
        } else {
            loc = getUniformLocation(name);
            uniformMap.put(name, loc);
        }
        loadBoolean(loc, val);
    }

    public void setUniform(String name, Matrix4f val) {
        int loc;
        if (inMap(name)) {
            loc = uniformMap.get(name);
        } else {
            loc = getUniformLocation(name);
            uniformMap.put(name, loc);
        }
        loadMatrix(loc, val);
    }

    public void setUniform(String name, Vector2f val) {
        int loc;
        if (inMap(name)) {
            loc = uniformMap.get(name);
        } else {
            loc = getUniformLocation(name);
            uniformMap.put(name, loc);
        }
        loadVector2(loc, val);
    }

    public void setTextureSlot(String name, int val) {
        int loc;
        if (inMap(name)) {
            loc = uniformMap.get(name);
        } else {
            loc = getUniformLocation(name);
            uniformMap.put(name, loc);
        }
        loadInt(loc, val);
    }

    private boolean inMap(String str) {
        return uniformMap.containsKey(str);
    }

    private static int loadShader(String file, int type) {
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, processShader(file));
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader");
            System.exit(-1);
        }

        return shaderID;

    }

    private static String processShader(String fileName) {
        StringBuilder shaderSource = new StringBuilder();
        BufferedReader bf = null;

        try {
            bf = new BufferedReader(new FileReader(ResourceManager.LoadShaderPath(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                if (line.startsWith(ResourceManager.SHADER_INCLUDE_DIRECTIVE)) {
                    shaderSource.append(processShader(line.substring(ResourceManager.SHADER_INCLUDE_DIRECTIVE.length() + 2, line.length() - 1)));
                } else {
                    shaderSource.append(line).append("\n");
                }
            }

            bf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shaderSource.toString();
    }


}
