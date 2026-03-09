package com.soup.game.service;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.soup.game.entities.Card;
import com.soup.game.intf.Service;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;

public class RenderService implements Service {
    private final Stage stage;
    private final Texture sheet;
    private final TextureRegion[][] cardRegions;
    private final TextureRegion backCard;

    private final int cols = 14;
    private final int rows = 4;

    public RenderService(Stage stage, String spritesheetPath,
                         String backPath) {
        this.stage = stage;
        this.sheet = new Texture(spritesheetPath);
        this.backCard = new TextureRegion(new Texture(backPath));

        int cardWidth = sheet.getWidth()/cols;
        int cardHeight = sheet.getHeight()/rows;

        this.cardRegions = TextureRegion.split(sheet, cardWidth, cardHeight);
    }

    public TextureRegion getRegion(Card card) {
        int row = switch (card.getSuit()) {
            case DIAMONDS -> 0;
            case HEARTS -> 1;
            case SPADES -> 2;
            case CLUBS -> 3;
        };

        int col = 0;
        if (card.getRank() == Rank.JOKER) {
            col = 13;
        } else {
            col = card.getRank().ordinal();
        }
        return cardRegions[row][col];
    }

    public void drawCard(Card card, float x, float y) {
        Batch batch = stage.getBatch();
        batch.begin();
        batch.draw(getRegion(card), x, y);
        batch.end();
    }

    public void dispose() {
        sheet.dispose();
        backCard.getTexture().dispose();
    }
}
