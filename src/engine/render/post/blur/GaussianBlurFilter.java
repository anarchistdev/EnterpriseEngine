package engine.render.post.blur;

import engine.physics.AABB;
import engine.render.DisplayManager;
import engine.render.post.PostFilter;

/**
 * Created by anarchist on 7/14/16.
 */
public class GaussianBlurFilter extends PostFilter {
    private VerticalFilter verticalBlur1;
    private VerticalFilter verticalBlur2;

    private HorizontalFilter horizontalBlur1;
    private HorizontalFilter horizontalBlur2;

    public GaussianBlurFilter() {
        verticalBlur1 = new VerticalFilter(DisplayManager.getWindowWidth()/8, DisplayManager.getWindowHeight()/8);
        horizontalBlur1 = new HorizontalFilter(DisplayManager.getWindowWidth()/8, DisplayManager.getWindowHeight()/8);

        verticalBlur2 = new VerticalFilter(DisplayManager.getWindowWidth()/2, DisplayManager.getWindowHeight()/2);
        horizontalBlur2 = new HorizontalFilter(DisplayManager.getWindowWidth()/2, DisplayManager.getWindowHeight()/2);
    }

    public void render(int colorTexture) {
        horizontalBlur1.render(colorTexture);
        verticalBlur1.render(horizontalBlur1.getOutputTexture());

        horizontalBlur2.render(verticalBlur1.getOutputTexture());
        verticalBlur2.render(horizontalBlur2.getOutputTexture());
    }

    public AABB getBounds() {
        return null;
    }

    public int getOutputTexture() {
        return verticalBlur2.getOutputTexture();
    }

    public void cleanUp() {
        verticalBlur1.cleanUp();
        horizontalBlur1.cleanUp();

        verticalBlur2.cleanUp();
        horizontalBlur2.cleanUp();
    }
}
