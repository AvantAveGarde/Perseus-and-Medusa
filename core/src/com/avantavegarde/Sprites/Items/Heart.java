package com.avantavegarde.Sprites.Items;

import com.avantavegarde.PerseusandMedusa;
import com.avantavegarde.Scenes.Hud;
import com.avantavegarde.Screens.PlayScreen;
import com.avantavegarde.Sprites.Perseus;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by Colton Lee (Laptop) on 12/3/2016.
 */
public class Heart extends Item {
    public Heart(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("Heart"), 0, 0, 16, 16);
        velocity = new Vector2(.7f, 0);

    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5/ PerseusandMedusa.PPM);
        fdef.filter.categoryBits = PerseusandMedusa.ITEM_BIT;
        fdef.filter.maskBits = PerseusandMedusa.PERSEUS_BIT |
                PerseusandMedusa.COIN_BIT |
                PerseusandMedusa.BRICK_BIT |
                PerseusandMedusa.ENEMY_BIT |
                PerseusandMedusa.OBJECT_BIT |
                PerseusandMedusa.GROUND_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Perseus perseus) {
        destroy();
        Hud.addScore(100);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
