package com.soup.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.soup.game.intf.Entity;

@SuppressWarnings("all")
public class Window extends Table
    implements Entity {
    private static final Texture window = new Texture("ui/window.png");
    private final NinePatch patch;

    public Window(float width, float height) {
        this.patch = new NinePatch(window, 16,16,16,16);
        setSize(width, height);
        setBackground(new NinePatchDrawable(patch));
    }

    public void setContent(Actor actor) {
        clear();
        Table container = new Table();
        container.setFillParent(true);
        container.add(actor).expand().center();
        add(container).expand().fill();
    }
}
