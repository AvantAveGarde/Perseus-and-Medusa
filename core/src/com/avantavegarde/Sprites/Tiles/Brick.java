package com.avantavegarde.Sprites.Tiles;

import com.avantavegarde.PerseusandMedusa;
import com.avantavegarde.Scenes.Hud;
import com.avantavegarde.Screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Colton Lee (Laptop) on 11/29/2016.
 */
public class Brick extends InterractiveTileObject {
    public Brick(PlayScreen screen, MapObject object){

        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(PerseusandMedusa.BRICK_BIT);

    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(PerseusandMedusa.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        PerseusandMedusa.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
