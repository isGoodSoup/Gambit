package com.soup.game.meta;

import com.soup.game.entities.Card;

import java.util.List;

public enum HandType {
    HIGH_CARD(5f),
    PAIR(10f),
    TWO_PAIR(20f),
    TRIPS(25f),
    STRAIGHT(35f),
    FLUSH(40f),
    FULL_HOUSE(80f),
    QUADS(90f),
    STRAIGHT_FLUSH(100f),
    ROYAL_FLUSH(200f);

    private final float value;

    HandType(float value) {
        this.value = value;
    }

    public float calc(List<Card> cards) {
        float total = 0f;
        for(Card c : cards) {
            total += c.getPoints();
        }
        return total + this.value;
    }
}
