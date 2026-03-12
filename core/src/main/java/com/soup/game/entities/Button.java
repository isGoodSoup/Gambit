package com.soup.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.soup.game.intf.Entity;
import com.soup.game.service.ServiceFactory;
import com.soup.game.service.UIAssets;

@SuppressWarnings("ALL")
public class Button extends Group
    implements Entity {
    private static final Texture button = new Texture("ui/button.png");
    private static final Texture buttonAlt = new Texture("ui/button_alt.png");
    private static final Texture frame = new Texture("ui/button_on.png");
    private final NinePatch patch;
    private final NinePatch patchFrame;
    private final Runnable action;
    private Table table;
    private Label label;
    private boolean isHovered;

    public Button(boolean isAlt, float x, float y, float width, float height,
                  Runnable action, String text, ServiceFactory service) {
        this.patch = new NinePatch(isAlt ? buttonAlt : button, 16, 16, 16, 16);
        this.patchFrame = new NinePatch(frame, 16, 16, 16, 16);
        this.action = action;
        setSize(width, height);
        setTouchable(Touchable.enabled);

        table = new Table();
        table.setFillParent(true);
        table.center();
        addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle
            (service.get(UIAssets.class).getSmallFont(), Color.WHITE);
        label = new Label(text, labelStyle);
        table.add(label);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isHovered = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                isHovered = false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        patch.draw(batch, getX(), getY(), getWidth(), getHeight());
        if (isHovered) {
            patchFrame.draw(batch, getX(), getY(), getWidth(), getHeight());
        }
    }
}
