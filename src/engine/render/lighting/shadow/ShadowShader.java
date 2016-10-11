package engine.render.lighting.shadow;

import engine.math.Matrix4f;
import engine.render.shader.RawShader;


/**
 * Created by anarchist on 6/23/16.
 */
public class ShadowShader extends RawShader {

    private static final String VERTEX_FILE = "shadows/shadowMapVertexShader.vert";
    private static final String FRAGMENT_FILE = "shadows/shadowMapFragmentShader.frag";


    protected ShadowShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);

        super.bindAttribute(0, "in_position");
        super.bindAttribute(1, "in_texCoords");
    }

    protected void loadMvpMatrix(Matrix4f mvpMatrix){
        super.setUniform("mvpMatrix", mvpMatrix);
    }

}
