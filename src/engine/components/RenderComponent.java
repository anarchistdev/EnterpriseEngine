package engine.components;

import engine.core.Component;
import engine.render.lighting.PBRMaterial;
import engine.render.model.Mesh;
import engine.render.model.RawModel;
import engine.render.texture.Texture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anarchist on 6/14/16.
 */
public class RenderComponent extends Component {
    private Mesh mesh;
    private Texture texture;
    private PBRMaterial pbrMaterial;
    private boolean shouldShadow = true;

    public RenderComponent(Mesh model, Texture tex) {
        this.mesh = model;
        this.texture = tex;
        this.pbrMaterial = new PBRMaterial();
    }

    public RenderComponent() {
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public PBRMaterial getPbrMaterial() {
        return pbrMaterial;
    }

    public void setPbrMaterial(PBRMaterial pbrMaterial) {
        this.pbrMaterial = pbrMaterial;
    }

    public boolean shouldCastShadow() {
        return shouldShadow;
    }

    public void setShouldCastShadow(boolean shouldShadow) {
        this.shouldShadow = shouldShadow;
    }
}
