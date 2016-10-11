package game;

import engine.core.Transform;
import engine.render.env.EnvironmentMapManager;
import engine.render.env.Fbo;
import engine.render.gui.button.AbstractButton;
import engine.render.gui.button.Button;
import engine.render.gui.font.FontType;
import engine.render.gui.font.GUIText;
import engine.render.gui.font.TextManager;
import engine.render.model.Mesh;
import engine.render.model.MeshLoader;
import engine.components.RenderComponent;
import engine.render.particle.ParticleManager;
import engine.render.post.PostProcessor;
import engine.render.post.underwater.UnderwaterFilter;
import engine.render.water.WaterShader;
import engine.render.water.WaterFrameBuffers;
import engine.render.water.WaterRenderer;
import engine.render.water.WaterTile;
import engine.math.Vector2f;
import engine.core.Node;
import engine.math.Vector3f;
import engine.components.PlayerComponent;
import engine.render.*;
import engine.render.gui.GUIRenderer;
import engine.render.lighting.Light;
import engine.render.texture.TerrainTexture;
import engine.render.texture.TerrainTexturePack;
import engine.render.texture.Texture;
import engine.terrain.ProceduralTerrain;
import engine.terrain.TerrainRenderer;
import engine.util.MathUtil;
import org.lwjgl.opengl.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anarchist on 6/13/16.
 */
public class GameLoop {

    public static void main(String [] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();
        MasterRenderer.createProjectionMatrix();

        MeshLoader.init();

        //Texture bunnyTex = Texture.loadTexture("guard1_body.png");
        //RenderComponent bunnyModel = new RenderComponent(MeshLoader.loadMesh("boblampclean.md5mesh", loader).get(0), bunnyTex);
        //RenderComponent headComp = new RenderComponent(MeshLoader.loadMesh("boblampclean.md5mesh", loader).get(1), Texture.loadTexture("guard1_face.png"));
        Texture dragonBugTex = Texture.loadTexture("creatures/DragonBug/DragonBug_Gold.tga");
        dragonBugTex.setNormalMap(Texture.loadTexture("creatures/DragonBug/DragonBug_N.tga").getTextureID());

        Mesh animMesh = MeshLoader.loadMesh("creatures/DragonBug/Enterprise/DragonBug_idle2.fbx", loader);
        animMesh.getAnimationController().setCurrentAnimation(("Armature|Idle"));

        RenderComponent dragonComp = new RenderComponent(animMesh, dragonBugTex);

        dragonComp.getPbrMaterial().setSpecularPower(0.5f);
        dragonComp.getPbrMaterial().setRoughness(0.1f);

        Node player = new Node("Player", new Transform(new Vector3f(0,0,0), new Vector3f(-90,0,-90),0.5f));
        player.setRenderer(new NormalMapRenderer());
        player.addRenderComponent(dragonComp);
        //player.addRenderComponent(headComp);

        PlayerComponent playerComponent = new PlayerComponent();
        player.addComponent(playerComponent);
        Camera camera = new Camera();

        player.addComponent(camera);

        MasterRenderer renderer = new MasterRenderer(loader, camera);

        TextManager.init(loader);

        List<Node> nodes = new ArrayList<>();

        FontType font = new FontType(Texture.loadTexture("font/roboto-thin.png").getTextureID(), new File("res/textures/font/roboto-thin.fnt"));

        GUIText text = new GUIText("Enterprise Engine, Pre-alpha v.001", 1.5f, font, new Vector2f(0,0), 1f, true);
//        text.setBorderWidth(0.4f);
//        text.setOutlineColor(new Vector3f(0,0.3f,0.3f));
        GUIText performanceText = new GUIText("frametime", 1f, font, new Vector2f(0.03f, 0.8f), 1f, false);
        performanceText.setColor(new Vector3f(10,10,10));
        GUIText particleText = new GUIText("particle", 1f, font, new Vector2f(0.03f, 0.828f), 1f, false);
        particleText.setColor(new Vector3f(1,1,1));

        TextManager.loadText(text);

        Light light = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(0.8f,0.8f,0.8f));

        TerrainTexture backgroundTex = new TerrainTexture(Texture.loadTexture("grass.png").getTextureID());
        TerrainTexture rTex = new TerrainTexture(Texture.loadTexture("StoneWall.png").getTextureID());
        TerrainTexture gTex = new TerrainTexture(Texture.loadTexture("grass.png").getTextureID());
        TerrainTexture bTex = new TerrainTexture(Texture.loadTexture("path.png").getTextureID());

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTex, rTex, gTex, bTex);

        TerrainTexture blendMapTex = new TerrainTexture(Texture.loadTexture("alphamap.png").getTextureID());

        Texture treeTex = Texture.loadTexture("foliage/tree/tree4.png");
        treeTex.setUseFakeLighting(true);
        treeTex.setHasTransparency(true);
        RenderComponent treeModel = new RenderComponent(MeshLoader.loadMesh("foliage/tree/obj__tree4.obj", loader), treeTex);

        Texture fernTex = Texture.loadTexture("fernAtlas.png");
        fernTex.setHasTransparency(true);
        fernTex.setUseFakeLighting(true);
        fernTex.setNumberOfRows(2);
        RenderComponent fernModel = new RenderComponent(MeshLoader.loadMesh("fern.obj", loader), fernTex);

        List<Node> trees = new ArrayList<>();
        List<Node> ferns = new ArrayList<>();

        GUIRenderer guiRenderer = new GUIRenderer(loader);

        Node terrainNode = new Node("TerrainNode");
        terrainNode.setRenderer(new TerrainRenderer());

        ProceduralTerrain terrain = new ProceduralTerrain(0,0, loader, texturePack, blendMapTex);
        terrainNode.addComponent(terrain);

        List<Light> lights = new ArrayList<>();
        //Light light2 = new Light(new Vector3f(309, 18, 730), new Vector3f(5,5,5), new Vector3f(0.005f, 0.005f, 0.005f));
        lights.add(light);
        //lights.add(light2);

        Texture barrelTex = Texture.loadTexture("normal/barrel.png");

        barrelTex.setNormalMap(Texture.loadTexture("normal/barrelNormal.png").getTextureID());
        //RenderComponent barrelModel = new RenderComponent(NormalMappedObjLoader.loadOBJ("barrel.obj", loader), barrelTex);
        RenderComponent barrelModel = new RenderComponent(MeshLoader.loadMesh("barrel.obj", loader), barrelTex);
        barrelModel.getPbrMaterial().setSpecularPower(0.5f);
        Node barrelNode  = new Node("barrelNode", new Transform(new Vector3f(50,15,50), new Vector3f(0,0,0), 1));
        barrelNode.addRenderComponent(barrelModel);
        barrelNode.setRenderer(new NormalMapRenderer());
        nodes.add(barrelNode);


        for (int i = 0; i < 75; i++) {
            float x = MathUtil.randomNextFloat(10, 600);
            float z = MathUtil.randomNextFloat(10, 700);
            float y = terrain.getHeightOfTerrain(x, z);
            Node tree = new Node("tree" + i, new Transform(new Vector3f(x, y, z), new Vector3f(0,0,0), MathUtil.randomNextFloat(0.8f, 1.4f)));
            tree.addRenderComponent(treeModel);
            trees.add(tree);
        }

        for (int i = 0; i < 300; i++) {
            float x = MathUtil.randomNextFloat(10, 600);
            float z = MathUtil.randomNextFloat(10, 700);
            float y = terrain.getHeightOfTerrain(x, z);
            Node fern = new Node("fern" + i, new Transform(new Vector3f(x, y, z), new Vector3f(0,0,0),MathUtil.randomNextFloat(0.4f, 2.5f)), MathUtil.randomNextInt(4));
            fern.addRenderComponent(fernModel);
            ferns.add(fern);
        }

        nodes.addAll(trees);
        nodes.addAll(ferns);
        nodes.add(player);
        nodes.add(terrainNode);

//        List<Terrain> terrains = new ArrayList<>();
//        terrains.add(terrain);

        WaterFrameBuffers fbos = new WaterFrameBuffers();

        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, MasterRenderer.getProjectionMatrix(), fbos);

        List<WaterTile> tiles = new ArrayList<>();
        WaterTile waterTile = new WaterTile(50, 50, -5);
        tiles.add(waterTile);

        ParticleManager.init(loader, MasterRenderer.getProjectionMatrix());

        Fbo outputFbo = new Fbo(DisplayManager.getWindowWidth(), DisplayManager.getWindowHeight(), Fbo.DEPTH_TEXTURE);


        Fbo multiSampleFbo = new Fbo(DisplayManager.getWindowWidth(), DisplayManager.getWindowHeight(), true, true) {
            @Override
            public void prepare(Camera camera1) {
                this.bindFrameBuffer();
            }

            @Override
            public void finish(Camera camera1) {
                this.unbindFrameBuffer();
                this.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, outputFbo);
            }
        };

        EnvironmentMapManager.addFbo(multiSampleFbo);

        PostProcessor.init(loader);

        PostProcessor.addEffect(new UnderwaterFilter(waterTile.generateUnderwaterBounds()));

        Node rootNode = new Node("Root");
        rootNode.getChildren().addAll(nodes);

        AbstractButton button = new AbstractButton("StoneWall.png", new Vector2f(0.0f, 0.0f), new Vector2f(0.2f, 0.2f)) {
            public void whileHovering(Button button1) {

            }

            public void onStartHover(Button button1) {
                button1.scale(0.09f);
            }

            public void onStopHover(Button button1) {
                button1.resetScale();
            }

            public void onClick(Button button1) {
                System.out.println("button clicked");
            }
        };

        //guiRenderer.addButton(button);


        while (!DisplayManager.getShouldWindowClose()) {
            playerComponent.move(terrain);
            camera.move();


            renderer.renderShadowMap(rootNode, light);

            //WeatherManager.updateText();
            ParticleManager.update(camera);

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - waterTile.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(rootNode, lights, camera, waterTile.getReflectClipPlane());

            camera.getPosition().y += distance;
            camera.invertPitch();

            fbos.unbindReflectionBuffer();

            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(rootNode, lights, camera, waterTile.getRefractClipPlane());

            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            fbos.unbindRefractionBuffer();


            //multiSampleFbo.bindFrameBuffer();

            EnvironmentMapManager.prepare(camera);
            //////////////// Render Scene ///////////////////////

            renderer.renderScene(rootNode, lights, camera, MasterRenderer.DEFAULT_CLIP_PLANE);
            waterRenderer.render(tiles, camera, light);
            ParticleManager.renderParticles(camera);

            /////////////// End Render /////////////////////////

            EnvironmentMapManager.finish(camera);

            //multiSampleFbo.unbindFrameBuffer();
            //multiSampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, outputFbo);

            PostProcessor.doPostProcessing(outputFbo.getColourTexture(), multiSampleFbo.getDepthTexture(), camera.getPosition());

            guiRenderer.render();

            performanceText.updateText("Frame Time: " + (float)DisplayManager.getFrameTimeSeconds() * 1000);
            particleText.updateText("Particles: " + ParticleManager.getNumParticles());

            TextManager.render();

            DisplayManager.loop();

        }

        multiSampleFbo.cleanUp();
        renderer.cleanUp();
        waterShader.cleanUp();
        DisplayManager.closeDisplay();
        loader.cleanUp();
        guiRenderer.cleanUp();
        TextManager.cleanUp();
        PostProcessor.cleanUp();

    }

}
