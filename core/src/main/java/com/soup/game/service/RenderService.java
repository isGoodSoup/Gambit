package com.soup.game.service;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.soup.game.intf.Service;
import com.soup.game.meta.Jokers;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;

@SuppressWarnings("all")
public class RenderService implements Service {
    private final Texture sheet;
    private final Texture jokers;
    private final TextureRegion[][] cardRegions;
    private final TextureRegion[][] jokerRegions;
    private final Skin skin;

    private final int cols = 13;
    private final int rows = 4;
    private final int jokerCols;
    private final int jokerRows;

    public RenderService(String spritesheetPath, String jokersPath, int jokerCols) {
        this.sheet = new Texture(spritesheetPath);
        this.jokers = new Texture(jokersPath);
        this.skin = new Skin();

        int cardWidth = sheet.getWidth()/cols;
        int cardHeight = sheet.getHeight()/rows;
        this.cardRegions = TextureRegion.split(sheet, cardWidth, cardHeight);

        this.jokerCols = jokerCols;
        this.jokerRows = 2;

        int jokerWidth = jokers.getWidth()/jokerCols;
        int jokerHeight = jokers.getHeight()/jokerRows;
        this.jokerRegions = TextureRegion.split(jokers, jokerWidth, jokerHeight);
    }

    public Skin getSkin() {
        return skin;
    }

    public TextureRegion getRegion(Suit suit, Rank rank) {
        int row = switch(suit) {
            case DIAMONDS-> 0;
            case HEARTS -> 1;
            case SPADES -> 2;
            case CLUBS -> 3;
        };

        int col = rank.ordinal();
        return cardRegions[row][col];
    }

    public TextureRegion getRegion(Jokers suit) {
        int row = suit.ordinal();
        int col = 0;
        return jokerRegions[row][col];
    }

    public void dispose() {
        sheet.dispose();
        jokers.dispose();
    }
}
