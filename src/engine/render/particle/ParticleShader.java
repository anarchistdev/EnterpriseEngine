package engine.render.particle;

import engine.math.Matrix4f;
import engine.math.Vector2f;
import engine.math.Vector4f;
import engine.render.shader.RawShader;

/**
 * Created by anarchist on 6/19/16.
 */
public class ParticleShader extends RawShader {

    private static final String VERTEX_FILE = "particle/particleVertexShader.vert";
    private static final String FRAGMENT_FILE = "particle/particleFragmentShader.frag";

    public ParticleShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);

        super.bindAttribute(0, "position");
        super.bindAttribute(1, "modelViewMatrix");
        super.bindAttribute(5, "texOffsets");
        super.bindAttribute(6, "blend");
    }

    protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.setUniform("projectionMatrix", projectionMatrix);
    }

    protected void loadNumberOfRows(float numRows) {
        super.setUniform("projectionMatrix", numRows);
    }

}