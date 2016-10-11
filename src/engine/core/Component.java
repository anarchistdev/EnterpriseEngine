package engine.core;

/**
 * Created by anarchist on 7/10/16.
 */
public class Component {
    private Node parent;

    public static final int COMPONENT_TYPE_BASE = 0;
    public static final int COMPONENT_TYPE_RENDER = 1;

    private int componentType;

    public Component() {
        componentType = COMPONENT_TYPE_BASE;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getComponentType() {
        return componentType;
    }

    public void setComponentType(int componentType) {
        this.componentType = componentType;
    }

    /**
    This method will fire whenever the component is added to the node, so all the init
    stuff that needs getParent() should go here.
     */
    protected void onAdd() {}
}
