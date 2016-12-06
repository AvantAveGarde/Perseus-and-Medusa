package com.avantavegarde.Screens;

import com.avantavegarde.PerseusandMedusa;
import com.avantavegarde.Scenes.Hud;
import com.avantavegarde.Sprites.Enemies.Enemy;
import com.avantavegarde.Sprites.Items.Heart;
import com.avantavegarde.Sprites.Items.Item;
import com.avantavegarde.Sprites.Items.ItemDef;
import com.avantavegarde.Sprites.Perseus;
import com.avantavegarde.Tool.B2WorldCreator;
import com.avantavegarde.Tool.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Colton Lee (Laptop) on 11/16/2016.
 */
public class PlayScreen implements Screen {
    private PerseusandMedusa game;
    private TextureAtlas atlas;
    public static boolean alreadyDestroyed = false;

    //basic playscreen variables
    private Viewport gamePort;
    private Hud hud;
    private OrthographicCamera gamecam;

    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //Perseus Sprite
    private Perseus player;
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;


    //music
    private Music music;


    public PlayScreen(PerseusandMedusa game){
        atlas = new TextureAtlas("Perseus_and_Medusa.pack");
        this.game = game;
        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio
        gamePort = new FitViewport(PerseusandMedusa.V_Width / PerseusandMedusa.PPM, PerseusandMedusa.V_Height / PerseusandMedusa.PPM, gamecam);

        //create our game HUD for scores/timer
        hud = new Hud(game.batch);

        //Load our map and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("mapfinal.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PerseusandMedusa.PPM);

        //initially set our gamecam to be centered correctly at the start of the world
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        world = new World(new Vector2(0,-10), true);

        //allows for debug lines
        b2dr = new Box2DDebugRenderer();
        //creates world
        creator = new B2WorldCreator(this);
        //creates perseus
        player = new Perseus(this);

        world.setContactListener(new WorldContactListener());

        music = PerseusandMedusa.manager.get("audio/music/main_theme.ogg", Music.class);
        music.setLooping(true);
        music.play();
        music.setVolume(.3f);

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

    }
    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Heart.class){
                items.add(new Heart(this, idef.position.x, idef.position.y));
            }
        }
    }
    public TextureAtlas getAtlas(){
        return atlas;
    }
    @Override
    public void show() {

    }

    public void handleInput(float dt){

        //If our user is holding down buttons
        if(player.currentState != Perseus.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player.jump();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }
    public void update(float dt){

        //handle user input first
        handleInput(dt);
        handleSpawningItems();

        //takes 1 step in the physics simulation(60 times / second)
        world.step(1/60f, 6, 2);

        player.update(dt);
        for(Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / PerseusandMedusa.PPM)
                enemy.b2body.setActive(true);

        }

        for(Item item : items)
            item.update(dt);

        //attatch our gamecam to our players.x coordinate
        gamecam.position.x = player.b2body.getPosition().x;

        //updates our gamecam
        gamecam.update();

        renderer.setView(gamecam);

    }

    @Override
    public void render(float delta) {

        update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //render our game map
        renderer.render();

        //renderer our Box2DDebugLines
        //b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);
        for(Item item: items)
            item.draw(game.batch);
        game.batch.end();

        //Set our batch to now draw what the Hud Camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }
    
    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }
}
