package com.soup.game.meta;

import com.soup.game.entities.Card;

import java.util.List;

public enum HandType {
    HIGH_CARD(5f),
    PAIR(10f),
    TWO_PAIR(20f),
    TRIPS(25f),
    STRAIGHT(30f),
    FLUSH(35f),
    FULL_HOUSE(40f),
    QUADS(45f),
    STRAIGHT_FLUSH(50f),
    ROYAL_FLUSH(100f);

    private final float value;

    HandType(float value) {
        this.value = value;
    }

    public float calc(List<Card> selected) {
        float kicker = 0f;
        for (Card c : selected) {
            kicker += c.getPoints();
        }
        return this.value + kicker;
    }

    public float getValue() {
        return value;
    }
}
