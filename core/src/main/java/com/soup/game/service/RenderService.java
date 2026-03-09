package com.soup.game.service;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.soup.game.entities.Card;
import com.soup.game.entities.Joker;
import com.soup.game.intf.Service;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;

public class RenderService implements Service {
    private final Stage stage;
    private final Texture sheet;
    private final Texture jokers;
    private final TextureRegion[][] cardRegions;
    private final TextureRegion[][] jokerRegions;
    private final TextureRegion backCard;

    private final int cols = 13;
    private final int rows = 4;
    private final int jokerCols;
    private final int jokerRows;


    public RenderService(Stage stage, String spritesheetPath, String jokersPath,
                         String backPath, int jokerCols) {
        this.stage = stage;
        this.sheet = new Texture(spritesheetPath);
        this.jokers = new Texture(jokersPath);
        this.backCard = new TextureRegion(new Texture(backPath));

        int cardWidth = sheet.getWidth()/cols;
        int cardHeight = sheet.getHeight()/rows;
        this.cardRegions = TextureRegion.split(sheet, cardWidth, cardHeight);

        this.jokerCols = jokerCols;
        this.jokerRows = 4;

        int jokerWidth = jokers.getWidth()/jokerCols;
        int jokerHeight = jokers.getHeight()/jokerRows;
        this.jokerRegions = TextureRegion.split(jokers, jokerWidth, jokerHeight);
    }

    public TextureRegion getRegion(Card card) {
        if(card instanceof Joker) {
            int row = card.getSuit().ordinal();
            int col = 0;
            return jokerRegions[row][col];
        }

        int row = switch(card.getSuit()) {
            case DIAMONDS -> 0;
            case HEARTS -> 1;
            case SPADES -> 2;
            case CLUBS -> 3;
        };

        int col = card.getRank().ordinal();
        return cardRegions[row][col];
    }


    public void drawCard(Card card, float x, float y) {
        stage.getBatch().draw(getRegion(card), x, y);
    }

    public void dispose() {
        sheet.dispose();
        jokers.dispose();
        backCard.getTexture().dispose();
    }
}
