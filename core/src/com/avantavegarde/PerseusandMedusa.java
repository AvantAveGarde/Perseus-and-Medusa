package com.avantavegarde;

import com.avantavegarde.Screens.PlayScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PerseusandMedusa extends Game {
	public static final int V_Width = 400;
	public static final int V_Height = 208;
	public static final float PPM = 100;
	public static final short GROUND_BIT = 1;
	public static final short PERSEUS_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short INVISIBLE_BIT = 512;
	public static final short PERSEUS_HEAD_BIT = 1024;

	public SpriteBatch batch;
// Warning using AssetManager in a static way can cause issues, especially on mobile devices, allocate it to the classes who need it generally.
	// We will use it in a static context in the interest of time

	public static AssetManager manager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/music/main_theme.ogg", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
		manager.load("audio/sounds/moo.wav", Sound.class);
		manager.load("audio/sounds/crow.wav", Sound.class);
		manager.load("audio/sounds/Medusadeath" +
				".wav", Sound.class);
		manager.finishLoading();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();

	}
}
