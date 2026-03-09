package com.soup.game.entities;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.soup.game.intf.Entity;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;

public class Card extends Actor
    implements Entity {
    private static long NEXT_ID = 1;
    private final long id;
    private final Suit suit;
    private final Rank rank;
    private final float points;
    private final boolean isJoker;

    public Card(Suit suit, Rank rank, float points,
                boolean isJoker) {
        this.id = NEXT_ID++;
        this.suit = suit;
        this.rank = rank;
        this.points = points;
        this.isJoker = isJoker;
    }

    public Card(Suit suit, float points, boolean isJoker) {
        this.id = NEXT_ID++;
        this.suit = suit;
        this.rank = null;
        this.points = points;
        this.isJoker = isJoker;
    }

    public long getId() {
        return id;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public float getPoints() {
        return points;
    }

    public boolean isJoker() {
        return isJoker;
    }
}
