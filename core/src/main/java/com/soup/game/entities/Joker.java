package com.soup.game.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.soup.game.meta.Suit;

public class Joker extends Card {
    private static final int NEXT_ID = 1;
    private Suit virtualSuit;
    private int jokerIndex;

    public Joker(boolean isJoker, TextureRegion region) {
        super(isJoker, region);
        this.jokerIndex += NEXT_ID;
    }

    public void setVirtualSuit(Suit suit) {
        this.virtualSuit = suit;
    }

    @Override
    public Suit getSuit() {
        return (virtualSuit != null) ? virtualSuit : super.getSuit();
    }

    public int getJokerIndex() {
        return jokerIndex;
    }
}
