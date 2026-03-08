package com.soup.game.entities;

import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;

public class Joker extends Card {

    public Joker(Suit suit, Rank rank, float points, boolean isJoker) {
        super(suit, rank, points, isJoker);
    }
}
