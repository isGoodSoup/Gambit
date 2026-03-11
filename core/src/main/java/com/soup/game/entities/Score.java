package com.soup.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.soup.game.intf.Entity;
import com.soup.game.service.GameService;
import com.soup.game.service.ServiceFactory;
import com.soup.game.service.UIAssets;

public class Score extends Actor
    implements Entity {
    private final ServiceFactory service;
    private final GlyphLayout layout = new GlyphLayout();

    public Score(ServiceFactory service) {
        this.service = service;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        BitmapFont font = service.get(UIAssets.class).getFont();
        String score = String.valueOf((int)
            service.get(GameService.class).getTable().getScore());
        layout.setText(font, score);
        float textX = getX() - layout.width / 2f;
        float textY = getY();
        font.draw(batch, layout, textX, textY);
    }
}
