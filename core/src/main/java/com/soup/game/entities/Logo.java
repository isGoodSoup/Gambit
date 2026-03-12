package com.soup.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.soup.game.intf.Entity;

@SuppressWarnings("all")
public class Logo extends Actor
    implements Entity {
    private static final Texture logo = new Texture("logo_v2.png");

    public Logo() {
        setSize(logo.getWidth(), logo.getHeight());
        setPosition(Gdx.graphics.getWidth()/2f - logo.getWidth()/2f, Gdx.graphics.getHeight() + 100f);

        float duration = 1f;
        addAction(Actions.delay(duration));
        addAction(Actions.sequence(
            Actions.moveTo(getX(), Gdx.graphics.getHeight()/2f - 150f,
                duration/2f, Interpolation.sine),
            Actions.forever(
                Actions.sequence(
                    Actions.moveBy(0f, 8f, duration, Interpolation.sine),
                    Actions.moveBy(0f , -8f, duration, Interpolation.sine)
                )
            )
        ));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(logo, getX(), getY(), getWidth(), getHeight());
    }
}
