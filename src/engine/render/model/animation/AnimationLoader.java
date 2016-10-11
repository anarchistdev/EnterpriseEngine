package engine.render.model.animation;

import engine.math.Quaternion;
import engine.math.Vector3f;
import engine.render.model.assimp.GameWrapperProvider;
import jassimp.AiAnimation;
import jassimp.AiNodeAnim;
import jassimp.AiQuaternion;
import jassimp.Jassimp;

/**
 * Created by anarchist on 8/6/16.
 */
public class AnimationLoader {

    public static void playAnimation(AiAnimation animation) {
        int frames = (int)animation.getDuration();
        System.out.println(frames);
        System.out.println(animation.getNumChannels());

        for (int i = 0; i < frames; i++) {

        }

        for (AiNodeAnim node : animation.getChannels()) {
            processAnimNode(node);
        }

    }

    private static void processAnimNode(AiNodeAnim anim) {
        System.out.println(anim.getNodeName());
    }

    private static int findPositon(double time, AiNodeAnim nodeAnim) {
        for (int i = 0; i < nodeAnim.getNumPosKeys() - 1; i++) {
            if (time >= nodeAnim.getPosKeyTime(i) && time < nodeAnim.getPosKeyTime(i+1)) {
                return i;
            }
        }

        System.err.println("Couldn't find position");
        return -1;
    }

    public static Vector3f lerpPosition(double aTime, AiNodeAnim nodeAnim) {
        if (nodeAnim.getNumPosKeys() == 0) {
            return new Vector3f(0,0,0);
        }

        if (nodeAnim.getNumPosKeys() == 1) {
            return getPositionKeys(nodeAnim, 0);
        }

        int index = findPositon(aTime, nodeAnim);
        int nextIndex = index + 1;

        if (nextIndex >= nodeAnim.getNumPosKeys()) {
            return getPositionKeys(nodeAnim, 0);
        }

        double deltaT = nodeAnim.getPosKeyTime(nextIndex) - nodeAnim.getPosKeyTime(index);
        double factor = (aTime - nodeAnim.getPosKeyTime(index)) / deltaT;

        if (factor < 0 || factor > 1) {
            return getPositionKeys(nodeAnim, 0);
        }

        Vector3f start = getPositionKeys(nodeAnim, index);
        Vector3f end = getPositionKeys(nodeAnim, nextIndex);

        Vector3f deltaV = Vector3f.sub(end, start, null);

        return Vector3f.add(start, Vector3f.scale(deltaV, (float)factor), null);

    }

    public static Quaternion lerpRotate(double aTime, AiNodeAnim nodeAnim) {
        if (nodeAnim.getNumRotKeys() == 0) {
            return new Quaternion();
        }

        if (nodeAnim.getNumRotKeys() == 1) {
            return getQuaternion(nodeAnim, 0);
        }

        int index = findRotate(aTime, nodeAnim);
        int nextIndex = index + 1;

        if (nextIndex >= nodeAnim.getNumScaleKeys()) {
            return getQuaternion(nodeAnim, 0);
        }

        double deltaT = nodeAnim.getRotKeyTime(nextIndex) - nodeAnim.getRotKeyTime(index);
        double factor = (aTime - nodeAnim.getRotKeyTime(index)) / deltaT;

        if (factor < 0 || factor > 1) {
            return getQuaternion(nodeAnim, 0);
        }

        Quaternion start = getQuaternion(nodeAnim, index);
        Quaternion end = getQuaternion(nodeAnim, nextIndex);
        Quaternion res = Quaternion.slerp(start, end, (float)factor);
        res.normalise();
        return res;
    }

    private static int findRotate(double time, AiNodeAnim nodeAnim) {
        for (int i = 0; i < nodeAnim.getNumRotKeys() - 1; i++) {
            if (time >= nodeAnim.getRotKeyTime(i) && time < nodeAnim.getRotKeyTime(i+1)) {
                return i;
            }
        }

        System.err.println("Couldn't find rotation");
        return 0;
    }

    private static int findScale(double time, AiNodeAnim nodeAnim) {
        for (int i = 0; i < nodeAnim.getNumScaleKeys(); i++) {
            if (time >= nodeAnim.getScaleKeyTime(i) && time < nodeAnim.getScaleKeyTime(i + 1)) {
                return i;
            }
        }

        System.err.println("Couldn't find scale");
        return 0;
    }

    public static Vector3f lerpScale(double aTime, AiNodeAnim nodeAnim) {
        if (nodeAnim.getNumScaleKeys() == 0) {
            return new Vector3f(0,0,0);
        }

        if (nodeAnim.getNumScaleKeys() == 1) {
            return getScaleKeys(nodeAnim, 0);
        }

        int index = findScale(aTime, nodeAnim);
        int nextIndex = index + 1;
        if (nextIndex >= nodeAnim.getNumScaleKeys()) {
            return getScaleKeys(nodeAnim, 0);
        }

        double deltaT = nodeAnim.getScaleKeyTime(nextIndex);
        double factor = (aTime - nodeAnim.getScaleKeyTime(index)) / deltaT;

        if (factor < 0 || factor > 1) {
            return getScaleKeys(nodeAnim, 0);
        }

        Vector3f start = getScaleKeys(nodeAnim, index);
        Vector3f end = getScaleKeys(nodeAnim, nextIndex);
        Vector3f deltaV = Vector3f.sub(end, start, null);
        Vector3f scale = Vector3f.add(start, Vector3f.scale(deltaV, (float)factor), null);

        return scale;
    }

    public static AiNodeAnim findNodeAnim(AiAnimation pAnim, String name) {
        for (AiNodeAnim nodeAnim : pAnim.getChannels()) {
            if (nodeAnim.getNodeName().equals(name)) {
                return nodeAnim;
            }
        }

        return null;
    }


    private static Vector3f getPositionKeys(AiNodeAnim nodeAnim, int i) {
        return new Vector3f(nodeAnim.getPosKeyX(i), nodeAnim.getPosKeyY(i), nodeAnim.getPosKeyZ(i));
    }

    private static Vector3f getScaleKeys(AiNodeAnim nodeAnim, int i) {
        return new Vector3f(nodeAnim.getScaleKeyX(i), nodeAnim.getScaleKeyY(i), nodeAnim.getScaleKeyZ(i));
    }

    private static Quaternion getQuaternion(AiNodeAnim nodeAnim, int i) {
        return new Quaternion(nodeAnim.getRotKeyX(i), nodeAnim.getRotKeyY(i), nodeAnim.getRotKeyZ(i), nodeAnim.getRotKeyW(i));
    }

}
