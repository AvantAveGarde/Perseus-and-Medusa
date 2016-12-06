package com.avantavegarde.Sprites.Enemies;

import com.avantavegarde.PerseusandMedusa;
import com.avantavegarde.Scenes.Hud;
import com.avantavegarde.Screens.PlayScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Colton Lee (Laptop) on 12/4/2016.
 */
public class Bull extends Enemy {
    public enum State {WALKING}
    public State currentState;
    public State previousState;
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

    public Bull(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for(int i = 3; i < 7; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Bull"), i* 49 + 1, 11, 49, 48));
        walkAnimation = new Animation(0.2f, frames);
        currentState = previousState = State.WALKING;
        setBounds(getX(), getY(), 38 / PerseusandMedusa.PPM, 38 / PerseusandMedusa.PPM);

    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = walkAnimation.getKeyFrame(stateTime, true);
        if (velocity.x > 0 && region.isFlipX() == true) {
            region.flip(true, false);
        }
        if (velocity.x < 0 && region.isFlipX() == false) {
            region.flip(true, false);
        }
        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }
    public void update(float dt){
        setRegion(getFrame(dt));

        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("Bull"), 52, 0, 52, 48));
            stateTime = 0;
        }
        else if(!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y + 0/PerseusandMedusa.PPM - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }


    }
    public void draw(Batch batch){
        if(!destroyed || stateTime < .5)
            super.draw(batch);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10/PerseusandMedusa.PPM);
        fdef.filter.categoryBits = PerseusandMedusa.ENEMY_BIT;
        fdef.filter.maskBits = PerseusandMedusa.GROUND_BIT |
                PerseusandMedusa.COIN_BIT |
                PerseusandMedusa.BRICK_BIT |
                PerseusandMedusa.ENEMY_BIT |
                PerseusandMedusa.OBJECT_BIT |
                PerseusandMedusa.INVISIBLE_BIT |
                PerseusandMedusa.PERSEUS_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //creates head for collision
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-9, 8).scl(1 / PerseusandMedusa.PPM);
        vertice[1] = new Vector2(9, 8).scl(1 / PerseusandMedusa.PPM);
        vertice[2] = new Vector2(-3, 5).scl(1 / PerseusandMedusa.PPM);
        vertice[3] = new Vector2(3, 5).scl(1 / PerseusandMedusa.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = PerseusandMedusa.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);


    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;
        Hud.addScore(300);
        PerseusandMedusa.manager.get("audio/sounds/moo.wav", Sound.class).play();
    }

}
