package engine.render.env;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import engine.render.Camera;
import engine.render.DisplayManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

/**
 * Created by anarchist on 7/5/16.
 */
public class Fbo implements EnvironmentMap {

    public static final int NONE = 0;
    public static final int DEPTH_TEXTURE = 1;
    public static final int DEPTH_RENDER_BUFFER = 2;

    private final int width;
    private final int height;

    private int frameBuffer;

    private int colourTexture;
    private int depthTexture;

    private int depthBuffer;
    private int colourBuffer;

    private boolean multiSample;
    private boolean multipleTargets;
    private int numSamples = 4;

    private int colorBuffer2;

    /**
     * Creates an FBO of a specified width and height, with the desired type of
     * depth buffer attachment.-
     *
     * @param width
     *            - the width of the FBO.
     * @param height
     *            - the height of the FBO.
     * @param depthBufferType
     *            - an int indicating the type of depth buffer attachment that
     *            this FBO should use.
     */
    public Fbo(int width, int height, int depthBufferType) {
        this.width = width;
        this.height = height;
        initialiseFrameBuffer(depthBufferType);

        multipleTargets = false;
        multiSample = false;
    }

    public Fbo(int width, int height, boolean ... b) {
        this.width = width;
        this.height = height;

        this.multiSample = b[0];
        this.multipleTargets = b[1];

        initialiseFrameBuffer(DEPTH_RENDER_BUFFER);
    }

    /**
     * Deletes the frame buffer and its attachments when the game closes.
     */
    public void cleanUp() {
        GL30.glDeleteFramebuffers(frameBuffer);
        GL11.glDeleteTextures(colourTexture);
        GL11.glDeleteTextures(depthTexture);
        GL30.glDeleteRenderbuffers(depthBuffer);
        GL30.glDeleteRenderbuffers(colourBuffer);
        GL30.glDeleteRenderbuffers(colorBuffer2);
    }

    /**
     * Binds the frame buffer, setting it as the current render target. Anything
     * rendered after this will be rendered to this FBO, and not to the screen.
     */
    public void bindFrameBuffer() {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer);
        GL11.glViewport(0, 0, width, height);
    }

    /**
     * Unbinds the frame buffer, setting the default frame buffer as the current
     * render target. Anything rendered after this will be rendered to the
     * screen, and not this FBO.
     */
    public void unbindFrameBuffer() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, DisplayManager.getWindowWidth(), DisplayManager.getWindowHeight());
    }

    /**
     * Binds the current FBO to be read from (not used in tutorial 43).
     */
    public void bindToRead() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
        GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
    }

    /**
     * @return The ID of the texture containing the colour buffer of the FBO.
     */
    public int getColourTexture() {
        return colourTexture;
    }

    /**
     * @return The texture containing the FBOs depth buffer.
     */
    public int getDepthTexture() {
        return depthTexture;
    }

    public void resolveToFbo(int readBuffer, Fbo output) {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, output.frameBuffer);
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.frameBuffer);
        GL11.glReadBuffer(readBuffer);
        GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, output.width, output.height,
                GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
        this.unbindFrameBuffer();
    }

    public void resolveToScreen() {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.frameBuffer);
        GL11.glDrawBuffer(GL11.GL_BACK);
        GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, DisplayManager.getWindowWidth(), DisplayManager.getWindowHeight(),
                GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
        this.unbindFrameBuffer();
    }

    /**
     * Creates the FBO along with a colour buffer texture attachment, and
     * possibly a depth buffer.
     *
     * @param type
     *            - the type of depth buffer attachment to be attached to the
     *            FBO.
     */
    private void initialiseFrameBuffer(int type) {
        createFrameBuffer();
        if (multiSample && multipleTargets) {
            colourBuffer = createMultiSampleColorAttachment(GL30.GL_COLOR_ATTACHMENT0);
            colorBuffer2 = createMultiSampleColorAttachment(GL30.GL_COLOR_ATTACHMENT1);
        } else if (multipleTargets) {
            colourBuffer = createTextureAttachment(GL30.GL_COLOR_ATTACHMENT0);
            colorBuffer2 = createTextureAttachment(GL30.GL_COLOR_ATTACHMENT1);
        } else if (multiSample) {
            colourBuffer = createMultiSampleColorAttachment(GL30.GL_COLOR_ATTACHMENT0);
        } else {
            colourBuffer = createTextureAttachment(GL30.GL_COLOR_ATTACHMENT0);
        }

        if (type == DEPTH_RENDER_BUFFER) {
            createDepthBufferAttachment();
        } else if (type == DEPTH_TEXTURE) {
            createDepthTextureAttachment();
        }
        unbindFrameBuffer();
    }

    private void determineDrawBuffers() {
        IntBuffer drawBuffers = BufferUtils.createIntBuffer(2);
        drawBuffers.put(GL30.GL_COLOR_ATTACHMENT0);
        if (multipleTargets) {
            drawBuffers.put(GL30.GL_COLOR_ATTACHMENT1);
        }
        drawBuffers.flip();

        GL20.glDrawBuffers(drawBuffers);
    }

    /**
     * Creates a new frame buffer object and sets the buffer to which drawing
     * will occur - colour attachment 0. This is the attachment where the colour
     * buffer texture is.
     *
     */
    private void createFrameBuffer() {
        frameBuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        determineDrawBuffers();
    }

    /**
     * Creates a texture and sets it as the colour buffer attachment for this
     * FBO.
     */
    private int createTextureAttachment(int attachment) {
        colourTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colourTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, colourTexture,
                0);
        return colourTexture;
    }

    /**
     * Adds a depth buffer to the FBO in the form of a texture, which can later
     * be sampled.
     */
    private void createDepthTextureAttachment() {
        depthTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, GL11.GL_DEPTH_COMPONENT,
                GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);
    }

    private int createMultiSampleColorAttachment(int attachment) {
        colourBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, colourBuffer);
        GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, numSamples, GL11.GL_RGBA8, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment, GL30.GL_RENDERBUFFER,
                colourBuffer);
        return colourBuffer;
    }

    /**
     * Adds a depth buffer to the FBO in the form of a render buffer. This can't
     * be used for sampling in the shaders.
     */
    private void createDepthBufferAttachment() {
        depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        if (!multiSample) {
            GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
        } else {
            GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, numSamples, GL14.GL_DEPTH_COMPONENT24, width, height);
        }

        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
                depthBuffer);
    }


    public void prepare(Camera camera) {}
    public void finish(Camera camera) {}

}
