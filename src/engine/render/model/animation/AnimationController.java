package engine.render.model.animation;

import engine.math.Matrix4f;
import engine.math.Quaternion;
import engine.math.Vector3f;
import engine.render.model.MeshLoader;
import engine.util.MathUtil;
import jassimp.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anarchist on 8/8/16.
 */
public class AnimationController {

    private List<AiAnimation> animations = new ArrayList<>();
    private AiAnimation currentAnimation = null;
    private List<AiBone> bones = new ArrayList<>();
    private Map<String, Integer> boneMap = new HashMap<>();

    public AnimationController() {

    }

    public boolean hasAnimations() {
        return animations.size() > 0;
    }

    public boolean hasBones() {
        return bones.size() > 0;
    }

    public void addBones(List<AiBone> bones) {
        this.bones.addAll(bones);
    }

    public void setAnimations(List<AiAnimation> animations) {
        this.animations = animations;
    }

    public AiAnimation getCurrentAnimation() {
        return currentAnimation;
    }

    public boolean setCurrentAnimation(String name) {
        if (hasAnimations()) {
            for (AiAnimation animation : animations) {
                if (animation.getName().equals(name)) {
                    currentAnimation = animation;
                    return true;
                }
            }
        }

        return false;
    }

/*    public Matrix4f updateTransformationMatrix(double aTime, AiNode pNode, Matrix4f transformation) {
        String nodeName = pNode.getName();
        if (currentAnimation == null) {
            return null;
        }

        Matrix4f nodeTransform = pNode.getTransform(MeshLoader.getGameWrapperProvider());
        AiNodeAnim nodeAnim = AnimationLoader.findNodeAnim(currentAnimation, nodeName);

        if (nodeAnim != null) {
            Vector3f scaling = AnimationLoader.lerpScale(aTime, nodeAnim);
            Quaternion rotation = AnimationLoader.lerpRotate(aTime, nodeAnim);
            Vector3f translation = AnimationLoader.lerpPosition(aTime, nodeAnim);


            nodeTransform = MathUtil.createTransformationMatrix(translation, rotation, scaling);
        }

        Matrix4f globalTransform = Matrix4f.mul(nodeTransform, transformation, null);

        if (findBone(nodeName) != bones.)
    }

    public void loadBones(int meshIndex, AiMesh mesh) {
        for (int i = 0; i < mesh.getBones().size(); i++) {
            int boneIndex = 0;
            String boneName = mesh.getBones().get(i).getName();

            if (boneMap.containsKey(boneName)) {
                boneIndex =
            }
        }
    }*/


}
