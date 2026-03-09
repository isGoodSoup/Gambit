package com.soup.game.entities;

import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;

public class Joker extends Card {
    private static final int NEXT_ID = 1;
    private int jokerIndex;

    public Joker(Suit suit, Rank rank, float points, boolean isJoker) {
        super(suit, rank, points, isJoker);
        this.jokerIndex += NEXT_ID;
    }

    public int getJokerIndex() {
        return jokerIndex;
    }
}
