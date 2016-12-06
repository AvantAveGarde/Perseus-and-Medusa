package com.avantavegarde.Sprites.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Colton Lee (Laptop) on 12/3/2016.
 */
public class ItemDef {
    public Vector2 position;
    public Class<?> type;
    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
