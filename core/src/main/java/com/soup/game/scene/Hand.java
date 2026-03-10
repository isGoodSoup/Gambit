package com.soup.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.soup.game.entities.Card;
import com.soup.game.meta.HandType;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private static final int MAX_SIZE = 8;
    private static final int MAX_SELECT = 5;
    private final List<Card> cards;
    private final List<Card> selected;
    private HandType hand;
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
        if(selected.contains(c)) {
            c.deselect();
            selected.remove(c);
            return;
        }

        if(selected.size() >= MAX_SELECT) {
            return;
        }

        selected.add(c);
        c.select();
    }

    public List<Card> getSelectedCards() {
        return selected;
    }

    public void clearSelection() {
        for(Card c : selected) { c.deselect(); }
        selected.clear();
    }

    public void remove(Card c) {
        this.cards.remove(c);
        c.remove();
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
        for(Card c : cards) {
            c.remove();
        }
        this.cards.clear();
    }

    public boolean isReady() {
        return isReady;
    }
    public void setReady(boolean ready) {
        this.isReady = ready;
    }

    public void layout(Stage stage) {
        for(Card c : cards) {
            if(c.getStage() != null) {
                c.remove();
            }
        }

        float handY = 150f;
        float overlapFactor = 0.6f;
        int numCards = size();
        if(numCards == 0) {
            return;
        }

        float cardWidth = cards.getFirst().getCardWidth();
        float spacing = cardWidth * overlapFactor;

        float totalWidth = spacing * (numCards - 1) + cardWidth;

        float maxHandWidth = Gdx.graphics.getWidth() * 0.9f;
        if(totalWidth > maxHandWidth) {
            spacing = (maxHandWidth - cardWidth) / (numCards - 1);
            totalWidth = maxHandWidth;
        }

        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2f;
        for(int i = 0; i < numCards; i++) {
            Card c = cards.get(i);
            c.setPosition(startX + i * spacing, handY);
            if(c.getStage() == null) {
                stage.addActor(c);
            }
        }
    }
}
