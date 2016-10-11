package engine.render.water;

import engine.render.env.Fbo;

/**
 * Created by anarchist on 6/18/16.
 */
public class WaterFrameBuffers {

    protected static final int REFLECTION_WIDTH = 640;
    private static final int REFLECTION_HEIGHT = 360;

    protected static final int REFRACTION_WIDTH = 1280;
    private static final int REFRACTION_HEIGHT = 720;

//    private FrameBufferObject reflectionFrameBuffer;
//    private int reflectionDepthBuffer;
//
//    private FrameBufferObject refractionFrameBuffer;
//    private int refractionDepthTexture;

    private Fbo reflectionFrameBuffer;
    private Fbo refractionFrameBuffer;

    public WaterFrameBuffers() {
        reflectionFrameBuffer = new Fbo(REFLECTION_WIDTH, REFLECTION_HEIGHT, Fbo.DEPTH_RENDER_BUFFER);
        refractionFrameBuffer = new Fbo(REFRACTION_WIDTH, REFRACTION_HEIGHT, Fbo.DEPTH_TEXTURE);


    }

    public void cleanUp() {//call when closing the game
        reflectionFrameBuffer.cleanUp();
        refractionFrameBuffer.cleanUp();
    }

    public void bindReflectionFrameBuffer() {//call before rendering to this FBO
        reflectionFrameBuffer.bindFrameBuffer();
    }

    public void bindRefractionFrameBuffer() {//call before rendering to this FBO
        refractionFrameBuffer.bindFrameBuffer();
    }

    public void unbindReflectionBuffer() {
        reflectionFrameBuffer.unbindFrameBuffer();
    }

    public void unbindRefractionBuffer() {
        refractionFrameBuffer.unbindFrameBuffer();
    }

    public int getReflectionTexture() {//get the resulting texture
        return reflectionFrameBuffer.getColourTexture();
    }

    public int getRefractionTexture() {//get the resulting texture
        return refractionFrameBuffer.getColourTexture();
    }

    public int getRefractionDepthTexture(){//get the resulting depth texture
        return refractionFrameBuffer.getDepthTexture();
    }


}
