package com.soup.game.scene;

import com.soup.game.entities.Card;
import com.soup.game.meta.HandType;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private static final int MAX_SIZE = 8;
    private final List<Card> cards;
    private HandType hand;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void add(Card c) {
        this.cards.add(c);
    }

    public Card get(int index) {
        return this.cards.get(index);
    }

    public void remove(Card c) {
        this.cards.remove(c);
    }

    public int getMaxSize() {
        return MAX_SIZE;
    }

    public int size() {
        return cards.size();
    }

    public boolean isComplete() {
        return this.cards.size() >= MAX_SIZE;
    }

    public HandType getHand() {
        return hand;
    }

    public float getValue(HandType hand, List<Card> c) {
        return hand.calc(c);
    }

    public void clear() {
        cards.clear();
    }
}
