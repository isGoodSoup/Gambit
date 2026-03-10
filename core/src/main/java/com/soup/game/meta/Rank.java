package com.soup.game.meta;

public enum Rank {
    ACE(15f),
    TWO(2f),
    THREE(3f),
    FOUR(4f),
    FIVE(5f),
    SIX(6f),
    SEVEN(7f),
    EIGHT(8f),
    NINE(9f),
    TEN(10f),
    JACK(12f),
    QUEEN(13f),
    KING(14f);

    private final float points;

    Rank(float points) {
        this.points = points;
    }

    public float getPoints() {
        return this.points;
    }
}
