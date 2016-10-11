package engine.core;

import engine.math.Matrix4f;
import engine.math.Vector3f;
import engine.math.Vector4f;
import engine.render.BaseRenderer;
import engine.render.Camera;
import engine.render.NodeRenderer;
import engine.render.lighting.Light;
import engine.components.RenderComponent;
import engine.render.lighting.shadow.ShadowMasterRenderer;
import engine.render.lighting.shadow.ShadowOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anarchist on 6/14/16.
 */
public class Node {
    private Node parent;
    private Transform transform;
    private List<Node> children;
    private List<Component> components;

    private String name;

    private List<RenderComponent> renderComponents;
    private BaseRenderer renderer;

    private int textureIndex = 0;

    // IDEA: some sort of attribute system, so each Node has a HashMap of attributes. This is intended to
    // be able to add node-specific features.

    public Node(String name, Transform transform, int index) {
        this(name, transform);
        textureIndex = index;
    }

    public Node(String name, Vector3f position) {
        this(name, new Transform(position, new Vector3f(0,0,0), 1));
    }

    public Node(String name) {
        this(name, new Transform());
    }

    public Node(String name, Transform transform) {
        this.name = name;
        this.transform = transform;
        this.children = new ArrayList<>();
        this.components = new ArrayList<>();
        this.renderComponents = new ArrayList<>();

        setRenderer(new NodeRenderer());
    }

    public int getTextureIndex() {
        return textureIndex;
    }

    public String getName() {
        return name;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node node) {
        this.parent = node;
    }

    public void addRenderComponent(RenderComponent component) {
         renderComponents.add(component);
    }

    public void addComponent(Component component) {
        this.components.add(component);
        component.setParent(this);
        component.onAdd();
    }

    /**
     *
     * @param type Type of component
     * @return A List<Component> of components that match the component type.
     */
    public List<Component> getComponentByType(int type) {
        List<Component> compTypeList = new ArrayList<>();
        for (Component component : components) {
            if (component.getComponentType() == type) {
                compTypeList.add(component);
            }
        }

        return compTypeList;
    }

    public void addChild(Node other) {
        children.add(other);
        other.setParent(this);
    }

    public void removeFromParent() {
        parent.getChildren().remove(this);
    }

    public List<Node> getChildren() {
        return children;
    }

    public float getTextureXOffset(int rendId) {
        int column = textureIndex % renderComponents.get(rendId).getTexture().getNumberOfRows();
        return (float)column / (float)renderComponents.get(rendId).getTexture().getNumberOfRows();
    }

    public float getTextureYOffset(int rendId) {
        int row = textureIndex  / renderComponents.get(rendId).getTexture().getNumberOfRows();
        return (float)row / (float)renderComponents.get(rendId).getTexture().getNumberOfRows();
    }

    public List<RenderComponent> getRenderComponents() {
        return renderComponents;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setRenderer(BaseRenderer renderer) {
        this.renderer = renderer;
        addComponent(this.renderer);
    }

    public void render(List<Light> lights, Camera camera, Vector4f clipPlane, ShadowOptions shadowOptions) {
        renderer.render(lights, camera, clipPlane, shadowOptions);
    }

    public void cleanUp() {
        renderer.cleanUp();
    }

    public BaseRenderer getRenderer() {
        return renderer;
    }
}
