package com.soup.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.soup.game.intf.Entity;

@SuppressWarnings("all")
public class Window extends Actor
    implements Entity {
    private static final Texture window = new Texture("ui/window.png");
    private final NinePatch patch;

    public Window(float x, float y, float width, float height) {
        this.patch = new NinePatch(window, 16, 16, 16, 16);
        setPosition(x, y);
        setSize(width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        patch.draw(batch, getX(), getY(), getWidth(), getHeight());
    }
}
