package engine.render.model.animation;

/**
 * Created by anarchist on 8/6/16.
 */
public class AnimationPlayer {
    public static final int ANIMATION_PLAY_ONCE = 0;
    public static final int ANIMATION_PLAY_LOOP = 1;
    public static final int ANIMATION_PLAY_INVERSE_ONCE = 2;
    public static final int ANIMATION_PLAY_INVERSE_LOOP = 3;

    private int animationPlay = ANIMATION_PLAY_ONCE;

    public AnimationPlayer() {

    }

    public int getAnimationPlay() {
        return animationPlay;
    }

    public void setAnimationPlay(int animationPlay) {
        this.animationPlay = animationPlay;
    }
}
