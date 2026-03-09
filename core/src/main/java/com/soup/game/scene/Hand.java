package com.soup.game.scene;

import com.soup.game.entities.Card;
import com.soup.game.meta.HandType;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private static final int MAX_SIZE = 8;
    private final List<Card> cards;
    private HandType hand;
    private int selectionIndex = -1;
    private final List<Card> selected;
    private boolean isReady;

    public Hand() {
        this.cards = new ArrayList<>();
        this.selected = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void add(Card c) {
        this.cards.add(c);
    }

    public void select(Card c) {
        selectionIndex = cards.indexOf(c);
        if (selectionIndex >= 0 && !selected.contains(c)) {
            selected.add(c);
        }
    }

    public List<Card> getSelectedCards() {
        return selected;
    }

    public void clearSelection() {
        selectionIndex = -1;
        selected.clear();
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
        this.cards.clear();
    }

    public boolean isReady() {
        return isReady;
    }
    public void setReady(boolean ready) {
        this.isReady = ready;
    }
}
