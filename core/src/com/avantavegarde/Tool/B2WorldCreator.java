package com.avantavegarde.Tool;

import com.avantavegarde.PerseusandMedusa;
import com.avantavegarde.Screens.PlayScreen;
import com.avantavegarde.Sprites.Enemies.Bird;
import com.avantavegarde.Sprites.Enemies.Bull;
import com.avantavegarde.Sprites.Enemies.Enemy;
import com.avantavegarde.Sprites.Enemies.Medusa;
import com.avantavegarde.Sprites.Tiles.Brick;
import com.avantavegarde.Sprites.Tiles.Coin;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Colton Lee (Laptop) on 11/29/2016.
 */
public class B2WorldCreator {
    private Array<Bird> Birds;
    private Array<Bull> Bulls;
    private Array<Medusa> Medusa;

    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create powerup
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PerseusandMedusa.PPM, (rect.getY() + rect.getHeight() / 2) / PerseusandMedusa.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PerseusandMedusa.PPM, rect.getHeight() / 2 / PerseusandMedusa.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        //create coins
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {

            new Coin(screen, object);
        }
        //create bricks
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {

            new Brick(screen, object);
        }
        //create floor
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PerseusandMedusa.PPM, (rect.getY() + rect.getHeight() / 2) / PerseusandMedusa.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PerseusandMedusa.PPM, rect.getHeight() / 2 / PerseusandMedusa.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = PerseusandMedusa.GROUND_BIT;
            body.createFixture(fdef);
        }
        //create walls/fixtures
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PerseusandMedusa.PPM, (rect.getY() + rect.getHeight() / 2) / PerseusandMedusa.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PerseusandMedusa.PPM, rect.getHeight() / 2 / PerseusandMedusa.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = PerseusandMedusa.OBJECT_BIT;
            body.createFixture(fdef);
        }
        //creates invisible walls
        for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PerseusandMedusa.PPM, (rect.getY() + rect.getHeight() / 2) / PerseusandMedusa.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PerseusandMedusa.PPM, rect.getHeight() / 2 / PerseusandMedusa.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = PerseusandMedusa.INVISIBLE_BIT;
            body.createFixture(fdef);
        }
        //create all Birds
        Birds = new Array<Bird>();
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            Birds.add(new Bird(screen, rect.getX() / PerseusandMedusa.PPM, rect.getY() / PerseusandMedusa.PPM));
        }
        //create all Bulls
        Bulls = new Array<Bull>();
        for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            Bulls.add(new Bull(screen, rect.getX() / PerseusandMedusa.PPM, rect.getY() / PerseusandMedusa.PPM));
        }
        //create all Medusa
        Medusa = new Array<Medusa>();
        for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            Medusa.add(new Medusa(screen, rect.getX() / PerseusandMedusa.PPM, rect.getY() / PerseusandMedusa.PPM));
        }
    }

    public Array<Bird> getBirds() {
        return Birds;
    }
    public Array<Bull> getBulls() {
        return Bulls;
    }
    public Array<Enemy> getEnemies() {
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(Birds);
        enemies.addAll(Bulls);
        enemies.addAll(Medusa);
        return enemies;
    }
}
