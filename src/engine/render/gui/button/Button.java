package engine.render.gui.button;

import engine.render.gui.GUITexture;

import java.util.List;

/**
 * Created by anarchist on 8/2/16.
 */
public interface Button {
    void onClick(Button button);
    void onStartHover(Button button);
    void onStopHover(Button button);
    void whileHovering(Button button);
    void show(List<GUITexture> guiTextures);
    void hide(List<GUITexture> guiTextures);
    void scale(float scaleFactor);
    void resetScale();
    void update();
}
