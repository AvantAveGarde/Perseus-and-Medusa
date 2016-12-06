package com.avantavegarde.Sprites;

import com.avantavegarde.PerseusandMedusa;
import com.avantavegarde.Scenes.Hud;
import com.avantavegarde.Screens.PlayScreen;
import com.avantavegarde.Sprites.Enemies.Enemy;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import static com.avantavegarde.PerseusandMedusa.GROUND_BIT;
import static com.avantavegarde.PerseusandMedusa.PERSEUS_HEAD_BIT;

/**
 * Created by Colton Lee (Laptop) on 11/28/2016.
 */
public class Perseus extends Sprite {
    public enum State {FALLING, JUMPING, STANDING, RUNNING, DYING, DEAD}
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion perseusStand;
    private Animation perseusJump;
    private Animation perseusRun;

    private float stateTimer;
    private boolean runningRight;
    private PlayScreen screen;



    //private boolean perseusflashAnimation;


    public Perseus(PlayScreen screen){
        super(screen.getAtlas().findRegion("Perseus"));
        //initialize default values
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;


        Array<TextureRegion> frames = new Array<TextureRegion>();

        //run animation
        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Perseus"), i* 19 , 0, 14, 25));
        perseusRun = new Animation(0.1f, frames);

        frames.clear();
        //get jump animation
        for(int i = 4; i < 6; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Perseus"), i* 19 + 4, 0, 14, 25));
        perseusJump = new Animation(0.1f, frames);
        frames.clear();
        perseusStand = new TextureRegion(getTexture(),346, 33, 14, 25);
        setBounds(0, 0, 14/ PerseusandMedusa.PPM, 25/ PerseusandMedusa.PPM);
        setRegion(perseusStand);

        definePerseus();
    }
    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch(currentState){
            case JUMPING:
                region = perseusJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = perseusRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = perseusStand;
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt: 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y <0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void jump(){
        if ( currentState != State.JUMPING ) {
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void definePerseus(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(64/PerseusandMedusa.PPM, 32/PerseusandMedusa.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / PerseusandMedusa.PPM);
        vertice[1] = new Vector2(3, 8).scl(1 / PerseusandMedusa.PPM);
        vertice[2] = new Vector2(-5, -12).scl(1 / PerseusandMedusa.PPM);
        vertice[3] = new Vector2(3, -12).scl(1 / PerseusandMedusa.PPM);
        shape.set(vertice);
        fdef.filter.categoryBits = PerseusandMedusa.PERSEUS_BIT;
        fdef.filter.maskBits = PerseusandMedusa.GROUND_BIT |
                PerseusandMedusa.COIN_BIT |
                PerseusandMedusa.BRICK_BIT |
                PerseusandMedusa.ENEMY_BIT |
                PerseusandMedusa.ENEMY_HEAD_BIT |
                PerseusandMedusa.OBJECT_BIT|
                PerseusandMedusa.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/ PerseusandMedusa.PPM, 11 / PerseusandMedusa.PPM), new Vector2(2/ PerseusandMedusa.PPM, 11 /PerseusandMedusa.PPM));
        fdef.filter.categoryBits = PERSEUS_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

    }
}
