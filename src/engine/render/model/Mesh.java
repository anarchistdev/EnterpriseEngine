package engine.render.model;

import engine.math.Matrix4f;
import engine.math.Quaternion;
import engine.math.Vector3f;
import engine.render.model.animation.AnimationController;
import engine.render.model.animation.AnimationLoader;
import jassimp.AiAnimation;
import jassimp.AiBone;
import jassimp.AiNode;
import jassimp.AiNodeAnim;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anarchist on 6/13/16.
 */
public class Mesh {
    private List<RawModel> rawModels = new ArrayList<>();

    private AnimationController animationController;

    public Mesh(int vaoID, int vertexCount) {
        this();
        rawModels.add(new RawModel(vaoID, vertexCount));
    }

    public Mesh() {
        animationController = new AnimationController();
    }

    public void addRawModel(RawModel model) {
        rawModels.add(model);
    }

    public List<RawModel> getRawModels() {
        return rawModels;
    }

    public AnimationController getAnimationController() {
        return animationController;
    }
}
