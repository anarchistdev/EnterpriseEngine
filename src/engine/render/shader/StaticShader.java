package engine.render.shader;

import engine.math.Vector2f;
import engine.math.Vector4f;

/**
 * Created by anarchist on 6/14/16.
 */
public class StaticShader extends BaseShader {

    private static final String VERTEX_FILE = "vertexShader.vert";
    private static final String FRAGMENT_FILE = "fragmentShader.frag";

    private int location_numberOfRows;
    private int location_offset;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);

        super.bindAttribute(0, "position");
        super.bindAttribute(1, "texCoords");
        super.bindAttribute(2, "normal");
        super.bindAttribute(3, "tangent");
    }

//    @Override
//    protected void bindAttributes() {
//        super.bindAttribute(0, "position");
//        super.bindAttribute(1, "texCoords");
//        super.bindAttribute(2, "normal");
//    }
//
//    @Override
//    protected void getAllUniformLocations() {
//        super.getAllUniformLocations();
//
//        location_numberOfRows = super.getUniformLocation("numberOfRows");
//        location_offset = super.getUniformLocation("offset");
//    }

    public void loadNumberOfRows(float numberOfRows) {
        super.setUniform("numberOfRows", numberOfRows);
    }
    public void loadOffset(Vector2f offset) {
        super.setUniform("offset", offset);
    }

    public void connectTextureUnits() {
        super.setTextureSlot("modelTexture", 0);
        //super.setTextureSlot("normalMap", 1);
        super.setTextureSlot("specularMap", 1);

        super.setTextureSlot("shadowMap", 5);
    }

}
