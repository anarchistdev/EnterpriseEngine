package engine.render.post;

import engine.physics.AABB;

/**
 * Created by anarchist on 7/5/16.
 */
public abstract class PostFilter {

    public abstract void render(int texture);
    public abstract void cleanUp();
    public abstract AABB getBounds();
    public abstract int getOutputTexture();

}
