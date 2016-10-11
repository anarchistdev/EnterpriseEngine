package engine.render.gui.button;

import engine.input.CursorHandler;
import engine.input.MouseHandler;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.render.Loader;
import engine.render.gui.GUITexture;
import engine.render.texture.Texture;
import org.lwjgl.glfw.GLFW;

import java.util.List;

/**
 * Created by anarchist on 8/2/16.
 */
public class AbstractButton implements Button {
    private GUITexture texture;
    private Vector2f originalScale;

    private boolean isHidden = false;
    private boolean isHovering = false;

    public AbstractButton(String texture, Vector2f position, Vector2f scale) {
        this.texture = new GUITexture(Texture.loadTexture(texture).getTextureID(), position, scale);
        originalScale = scale;
    }

    public void update() {
        if (!isHidden) {
            Vector2f location = texture.getPosition();
            Vector2f scale = texture.getScale();
            Vector2f mouseCoords = CursorHandler.getNormalizedCoords();

            if      (location.y + scale.y > -mouseCoords.y &&
                    location.y - scale.y < -mouseCoords.y &&
                    location.x + scale.x > mouseCoords.x &&
                    location.x - scale.x < mouseCoords.x) {

                whileHovering(this);


                if (!isHovering) {
                    isHovering = true;
                    onStartHover(this);
                }

                if (MouseHandler.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && !isHidden()) {
                    onClick(this);
                }

            } else {
                if (isHovering) {
                    onStopHover(this);
                }

                isHovering = false;
            }
        }
    }

    public void show(List<GUITexture> guiTextures) {
        if (isHidden) {
            guiTextures.add(texture);
            isHidden = false;
        }
    }

    public void hide(List<GUITexture> guiTextures) {
        if (!isHidden) {
            guiTextures.remove(texture);
            isHidden = true;
        }
    }

    public void resetScale() {
        texture.setScale(originalScale);
    }

    public void scale(float scale) {
        texture.setScale(new Vector2f(originalScale.x + scale, originalScale.y + scale));
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void onClick(Button button) {}
    public void onStartHover(Button button) {}
    public void onStopHover(Button button) {}
    public void whileHovering(Button button) {}
}
