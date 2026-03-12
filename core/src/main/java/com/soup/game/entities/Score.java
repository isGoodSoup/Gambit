package com.soup.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.soup.game.intf.Entity;
import com.soup.game.service.GameService;
import com.soup.game.service.ServiceFactory;
import com.soup.game.service.UIAssets;

public class Score extends Actor
    implements Entity {
    private final ServiceFactory service;
    private final GlyphLayout layout = new GlyphLayout();
    private int lastScore = -1;
    private String score;

    public Score(ServiceFactory service) {
        this.service = service;
        setScale(1f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        int score = (int) service.get(GameService.class).getTable().getScore();
        if(score != lastScore) {
            lastScore = score;
            clearActions();
            setScale(1f);
            addAction(Actions.sequence(
                Actions.scaleTo(1.6f, 1.6f, 0.06f, Interpolation.pow3Out),
                Actions.scaleTo(1f, 1f, 0.10f, Interpolation.swingOut)
            ));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        BitmapFont font = service.get(UIAssets.class).getFont();
        font.setColor(Color.WHITE);
        score = String.valueOf(lastScore);

        float scale = getScaleX();
        font.getData().setScale(scale);

        layout.setText(font, score);
        float textX = getX() + getWidth()/2f - layout.width/2f;
        float textY = getY() + getHeight()/2f + layout.height/2f;

        font.draw(batch, layout, textX, textY);
        font.getData().setScale(1f);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        setOrigin(getWidth()/2f, getHeight()/2f);
    }

    public String getValue() {
        return score;
    }
}
