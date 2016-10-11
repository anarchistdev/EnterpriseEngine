package engine.render.shader;

import engine.math.Vector2f;

/**
 * Created by anarchist on 6/19/16.
 */
public class NormalMapShader extends BaseShader {
    private static String VERT_FILE = "normal/normalMapVertexShader.vert";
    private static String FRAG_FILE = "normal/normalMapFragmentShader.frag";

//    private int location_numberOfRows;
//    private int location_offset;
//
//    private int location_normalMap;
//    private int location_modelTexture;

    public NormalMapShader() {
        super(VERT_FILE, FRAG_FILE);

        super.bindAttribute(0, "position");
        super.bindAttribute(1, "texCoords");
        super.bindAttribute(2, "normal");
        super.bindAttribute(3, "tangent");
    }

//    @Override
//    protected void bindAttributes() {
//        super.bindAttribute(0, "position");
//        super.bindAttribute(1, "textureCoordinates");
//        super.bindAttribute(2, "normal");
//        super.bindAttribute(3, "tangent");
//    }

//    @Override
//    protected void getAllUniformLocations() {
//        super.getAllUniformLocations();
//
//        location_numberOfRows = super.getUniformLocation("numberOfRows");
//        location_offset = super.getUniformLocation("offset");
//        location_normalMap = super.getUniformLocation("normalMap");
//        location_modelTexture = super.getUniformLocation("modelTexture");
//    }

    public void loadNumberOfRows(float numberOfRows) {
        super.setUniform("numberOfRows", numberOfRows);
    }
    public void loadOffset(Vector2f offset) {
        super.setUniform("offset", offset);
    }

    public void connectTextureUnits() {
        super.setTextureSlot("modelTexture", 0);
        super.setTextureSlot("normalMap", 1);
        super.setTextureSlot("specularMap", 2);

        super.setTextureSlot("shadowMap", 5);
    }
}
