package com.soup.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.soup.game.intf.Entity;
import com.soup.game.service.ServiceFactory;

@SuppressWarnings("all")
public class Window extends Group
    implements Entity {
    private static final Texture window = new Texture("ui/window.png");
    private final NinePatch patch;
    private Table table;
    private Label label;

    public Window(float width, float height) {
        this.patch = new NinePatch(window, 16,16,16,16);
        setSize(width, height);
    }

    public Window(float width, float height, Label label,
                  ServiceFactory service, Color color) {
        this.patch = new NinePatch(window, 16, 16, 16, 16);
        this.label = label;
        setSize(width, height);

        table = new Table();
        table.setFillParent(true);
        table.center();
        addActor(table);
        table.add(this.label);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        patch.draw(batch, getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);
    }
}
