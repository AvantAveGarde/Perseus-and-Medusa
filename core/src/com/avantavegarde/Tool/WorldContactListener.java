package com.avantavegarde.Tool;

import com.avantavegarde.PerseusandMedusa;
import com.avantavegarde.Sprites.Enemies.Enemy;
import com.avantavegarde.Sprites.Items.Item;
import com.avantavegarde.Sprites.Perseus;
import com.avantavegarde.Sprites.Tiles.InterractiveTileObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Colton Lee (Laptop) on 12/3/2016.
 */
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (fixA.getUserData() == "head" || fixB.getUserData() == "head") {
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if (object.getUserData() != null && InterractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InterractiveTileObject) object.getUserData()).onHeadHit();
            }
        }

        switch (cDef) {
            case PerseusandMedusa.PERSEUS_HEAD_BIT | PerseusandMedusa.BRICK_BIT:
            case PerseusandMedusa.PERSEUS_HEAD_BIT | PerseusandMedusa.COIN_BIT:
                if(fixA.getFilterData().categoryBits == PerseusandMedusa.PERSEUS_HEAD_BIT)
                    ((InterractiveTileObject) fixB.getUserData()).onHeadHit();
                else
                    ((InterractiveTileObject) fixA.getUserData()).onHeadHit();
                break;
            case PerseusandMedusa.ENEMY_HEAD_BIT | PerseusandMedusa.PERSEUS_BIT:
                if (fixA.getFilterData().categoryBits == PerseusandMedusa.ENEMY_HEAD_BIT)
                    ((Enemy) fixA.getUserData()).hitOnHead();
                else
                    ((Enemy) fixB.getUserData()).hitOnHead();
                break;
            case PerseusandMedusa.ENEMY_BIT | PerseusandMedusa.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == PerseusandMedusa.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case PerseusandMedusa.ENEMY_BIT | PerseusandMedusa.INVISIBLE_BIT:
                if (fixA.getFilterData().categoryBits == PerseusandMedusa.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case PerseusandMedusa.ENEMY_BIT | PerseusandMedusa.COIN_BIT:
                if (fixA.getFilterData().categoryBits == PerseusandMedusa.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case PerseusandMedusa.PERSEUS_BIT | PerseusandMedusa.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == PerseusandMedusa.PERSEUS_BIT)
                    Gdx.app.log("Perseus", "Died");
                break;
            case PerseusandMedusa.ENEMY_BIT | PerseusandMedusa.ENEMY_BIT:
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case PerseusandMedusa.ITEM_BIT | PerseusandMedusa.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == PerseusandMedusa.ITEM_BIT)
                    ((Item) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case PerseusandMedusa.ITEM_BIT | PerseusandMedusa.PERSEUS_BIT:
                if (fixA.getFilterData().categoryBits == PerseusandMedusa.ITEM_BIT)
                    ((Item) fixA.getUserData()).use((Perseus) fixB.getUserData());
                else
                    ((Item) fixB.getUserData()).use((Perseus) fixA.getUserData());
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
