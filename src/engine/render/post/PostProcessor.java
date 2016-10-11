package engine.render.post;

import engine.math.Vector3f;
import engine.render.DisplayManager;
import engine.render.Loader;
import engine.render.model.RawModel;
import engine.render.post.bloom.BrightFilter;
import engine.render.post.blur.GaussianBlurFilter;
import engine.render.post.dof.DepthOfFieldFilter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anarchist on 7/5/16.
 */
public class PostProcessor {

    private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
    private static RawModel quad;
    private static List<PostFilter> effects;

    private static GaussianBlurFilter gaussianBlurFilter;
    private static BrightFilter brightFilter;
    private static CombineFilter combineFilter;

    public static void init(Loader loader){
        quad = loader.loadToVAO(POSITIONS, 2);
        effects = new ArrayList<>();
//        hBlur = new HorizontalFilter(DisplayManager.getWindowWidth()/2, DisplayManager.getWindowHeight()/2);
//        vBlur = new VerticalFilter(DisplayManager.getWindowWidth()/5, DisplayManager.getWindowHeight()/5);
        gaussianBlurFilter = new GaussianBlurFilter();
        brightFilter = new BrightFilter(DisplayManager.getWindowWidth()/5, DisplayManager.getWindowHeight()/5);

        combineFilter = new CombineFilter();
    }

//    public static boolean shouldDoPost(Vector3f camPos) {
//        if (!effects.isEmpty()) {
//            for (PostFilter effect : effects) {
//                if (effect.getBounds() != null && effect.getBounds().intersectPoint(camPos)) {
//                    return true;
//                } else if (effect.getBounds() == null) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }

//    public static void doPostProcessing(int colorTexture, Vector3f camPos){
//        start();
//        for (PostFilter effect : effects) {
//            PostFilter previousEffect;
//            try {
//                previousEffect = effects.get(effects.indexOf(effect)-1);
//            } catch (ArrayIndexOutOfBoundsException e) {
//                previousEffect = null;
//            }
//
//            if (effect.getBounds() != null && effect.getBounds().intersectPoint(camPos)) {
//                effect.render(colorTexture);
//            } else if (effect.getBounds() == null) {
//                if (previousEffect == null) {
//                    effect.render(colorTexture);
//                } else {
//                    effect.render(previousEffect.getOutputTexture());
//                }
//            }
//
//        }
//        end();
//    }
//
//    private static void renderTo(int colorTexture, PostFilter effect, PostFilter previousEffect, Vector3f camPos) {
//        if (previousEffect == null) {
//            System.out.println(effect.getClass() + " 1");
//            effect.render(colorTexture);
//        } else if (effect.getBounds() != null && effect.getBounds().intersectPoint(camPos)) {
//            System.out.println(effect.getClass() + " 2");
//            effect.render(colorTexture);
//        } else {
//            effect.render(previousEffect.getOutputTexture());
//        }
//    }



    public static void doPostProcessing(int colorTexture, int depthTexture, Vector3f camPos){
        start();
        //ListIterator<PostFilter> iter = effects.listIterator();

        brightFilter.render(colorTexture);
//        hBlur.render(brightFilter.getOutputTexture());
//        vBlur.render(hBlur.getOutputTexture());

        gaussianBlurFilter.render(brightFilter.getOutputTexture());

        combineFilter.setHighlightTexture(gaussianBlurFilter.getOutputTexture());
        combineFilter.render(colorTexture);

        for (PostFilter effect : effects) {
            if (effect.getBounds() != null && effect.getBounds().intersectPoint(camPos)) {
                effect.render(colorTexture);
            } else if (effect.getBounds() == null) {
                effect.render(colorTexture);
            }
        }

//        gaussianBlurEffect.render(colorTexture);
//        contrastEffect.render(gaussianBlurEffect.getOutputTexture());

//        while (iter.hasNext()) {
//            PostFilter effect = iter.next();
//
//            if (!iter.hasPrevious()) {
//                if (effect.getBounds() != null && effect.getBounds().intersectPoint(camPos)) {
//                    effect.render(colorTexture);
//                } else if (effect.getBounds() == null) {
//                    effect.render(colorTexture);
//                }
//            } else {
//                PostFilter previousEffect = iter.previous();
//
//                if (effect.getBounds() != null && effect.getBounds().intersectPoint(camPos)) {
//                    effect.render(previousEffect.getOutputTexture());
//                } else if (effect.getBounds() == null) {
//                    effect.render(previousEffect.getOutputTexture());
//                }
//            }
//
//        }


        end();
    }

//    public static void doPostProcessing(int colorTexture, Vector3f camPos){
//        start();
//
//        ListIterator<PostFilter> iter = effects.listIterator();
//
//        while (iter.hasNext()) {
//            PostFilter effect = iter.next();
//            PostFilter previousEffect = iter.previous();
//
//            if (previousEffect == null) {
//                if (effect.getBounds() != null && effect.getBounds().intersectPoint(camPos)) {
//                    effect.render(colorTexture);
//                    //contrastEffect.render(effect.getOutputTexture());
//                } else if (effect.getBounds() == null) {
//                    effect.render(colorTexture);
//                    //contrastEffect.render(effect.getOutputTexture());
//                }
//            } else {
//                if (effect.getBounds() != null && effect.getBounds().intersectPoint(camPos)) {
//                    effect.render(colorTexture);
//                    //contrastEffect.render(effect.getOutputTexture());
//                } else if (effect.getBounds() == null) {
//                    effect.render(colorTexture);
//                    //contrastEffect.render(effect.getOutputTexture());
//                }
//            }
//        }
//
//
//        end();
//    }

    private static void start(){
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    private static void end(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public static void cleanUp() {
        for (PostFilter effect : effects) {
            effect.cleanUp();
        }
    }

    public static void addEffect(PostFilter effect) {
        effects.add(effect);
    }

}