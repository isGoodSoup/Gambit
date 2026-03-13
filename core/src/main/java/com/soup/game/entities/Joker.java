package com.soup.game.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Joker extends Card {
    private static final int NEXT_ID = 1;
    private int jokerIndex;

    public Joker(float points, boolean isJoker, TextureRegion region) {
        super(null, points, isJoker, region);
        this.jokerIndex += NEXT_ID;
    }

    public int getJokerIndex() {
        return jokerIndex;
    }
}
