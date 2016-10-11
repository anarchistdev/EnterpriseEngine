package engine.render.texture;

import engine.resource.ResourceManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengles.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;

/**
 * Created by anarchist on 6/14/16.
 */
public class Texture {
    private int width;
    private int height;
    private int id;

    private int normalMap;

    private boolean hasTransparency = false;
    private boolean useFakeLighting = false;

    private int numberOfRows = 1;
    private float textureXOffset;
    private float textureYOffset;

    private boolean hasNormalMap = false;

    private int specularMap;
    private boolean hasSpecularMap = false;

    private ByteBuffer imageBuffer;

    public Texture(int width, int height, ByteBuffer data) {
        this(width, height, data, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, true, GL11.GL_RGBA8, GL11.GL_RGBA, true);
        this.imageBuffer = data;
    }

    public Texture(int width, int height, ByteBuffer data, int target, int filter, boolean mipMap, int internalFormat, int format, boolean clamp) {
        id = GL11.glGenTextures();
        this.width = width;
        this.height = height;

        GL11.glBindTexture(target, id);

//        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER);
//        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER);
//        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
//        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, filter);
        GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, filter);

        if (clamp) {
            GL11.glTexParameterf(target, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameterf(target, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        }
//        GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
//        GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        //GL11.glTexImage2D(target, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
        GL11.glTexImage2D(target, 0, internalFormat, width, height, 0, format, GL11.GL_UNSIGNED_BYTE, data);

        if (mipMap) {
            GL30.glGenerateMipmap(target);
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
            GL11.glTexParameterf(target, GL14.GL_TEXTURE_LOD_BIAS, 0);
            if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
                float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
                GL11.glTexParameterf(target,  EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
            }
        }
    }


    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    public void delete() {
        GL11.glDeleteTextures(id);
    }

    public void setSpecularMap(int map) {
        this.specularMap = map;
        this.hasSpecularMap = true;
    }

    public boolean isHasSpecularMap() {
        return hasSpecularMap;
    }

    public int getSpecularMap() {
        return specularMap;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTextureID() {
        return id;
    }

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public int getNormalMap() {
        return normalMap;
    }

    public void setNormalMap(int normalMap) {
        this.normalMap = normalMap;
        hasNormalMap = true;
    }

    public boolean hasNormalMap() {
        return hasNormalMap;
    }

    public float getTextureXOffset(int textureIndex) {
        int column = textureIndex % numberOfRows;
        return (float)column / (float)numberOfRows;
    }

    public void setTextureXOffset(float textureXOffset) {
        this.textureXOffset = textureXOffset;
    }

    public float getTextureYOffset(int textureIndex) {
        int row = textureIndex  / numberOfRows;
        return (float)row / (float)numberOfRows;
    }

    public void setTextureYOffset(float textureYOffset) {
        this.textureYOffset = textureYOffset;
    }

    public ByteBuffer getImageBuffer() {
        return imageBuffer;
    }

    public static Texture loadTexture(String path) {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(0);
        ByteBuffer image = stbi_load(ResourceManager.LoadTexturePath(path), w, h, comp, 4);
        if (image == null) {
            throw new RuntimeException("Failed to load a texture file!"
                    + System.lineSeparator() + stbi_failure_reason());
        }

        int width = w.get();
        int height = h.get();

        return new Texture(width, height, image);
    }

}
