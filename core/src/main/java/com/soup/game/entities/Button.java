package com.soup.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.soup.game.intf.Entity;

@SuppressWarnings("ALL")
public class Button extends Actor
    implements Entity {
    private static final Texture button = new Texture("ui/button.png");
    private static final Texture buttonAlt = new Texture("ui/button_alt.png");
    private static final Texture frame = new Texture("ui/button_on.png");
    private final NinePatch patch;
    private final NinePatch patchFrame;
    private final Runnable action;
    private boolean isHovered;

    public Button(boolean isAlt, float x, float y, float width, float height,
                  Runnable action) {
        this.patch = new NinePatch(isAlt ? buttonAlt : button, 16, 16, 16, 16);
        this.patchFrame = new NinePatch(frame, 16, 16, 16, 16);
        this.action = action;
        setSize(width, height);
        setPosition(x, y);
        setTouchable(Touchable.enabled);
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
