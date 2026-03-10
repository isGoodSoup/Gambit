package com.soup.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.soup.game.entities.Card;
import com.soup.game.meta.HandType;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;

import java.util.*;

public class Hand {
    private static final int MAX_SIZE = 8;
    private static final int MAX_SELECT = 5;
    private final List<Card> cards;
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

    @SuppressWarnings("all")
    public HandType evaluate(List<Card> activeHand) {
        activeHand = selected;
        int size = activeHand.size();
        if(size == 0 || size == 1) {
            return HandType.HIGH_CARD;
        }

        if(size < 5) {
            Map<Rank, Integer> rankCounts = new HashMap<>();
            for(Card c : activeHand) rankCounts.put(c.getRank(), rankCounts.getOrDefault(c.getRank(), 0) + 1);

            int pairs = 0, trips = 0, quads = 0;
            for(int count : rankCounts.values()) {
                pairs += (count == 2 ? 1 : 0);
                trips += (count == 3 ? 1 : 0);
                quads += (count == 4 ? 1 : 0);
            }

            return quads > 0 ? HandType.QUADS
                : (trips > 0 && pairs > 0) ? HandType.FULL_HOUSE
                : (trips > 0) ? HandType.TRIPS
                : (pairs > 1) ? HandType.TWO_PAIR
                : (pairs == 1) ? HandType.PAIR
                : HandType.HIGH_CARD;
        }

        List<Card> sorted = new ArrayList<>(activeHand);
        sorted.sort(Comparator.comparing(Card::getRank));

        Map<Rank, Integer> rankCounts = new HashMap<>();
        for(Card c : sorted) rankCounts.put(c.getRank(), rankCounts.getOrDefault(c.getRank(), 0) + 1);

        Collection<Integer> counts = rankCounts.values();
        boolean hasPair = counts.contains(2);
        boolean hasTrips = counts.contains(3);
        boolean hasQuads = counts.contains(4);

        Suit firstSuit = sorted.getFirst().getSuit();
        boolean isFlush = sorted.stream().allMatch(c -> c.getSuit() == firstSuit);

        boolean isStraight = true;
        for(int i = 0; i < sorted.size() - 1; i++)
            if(sorted.get(i + 1).getRank().ordinal() != sorted.get(i).getRank().ordinal() + 1) { isStraight = false; break; }

        boolean isRoyal = sorted.get(0).getRank() == Rank.TEN
            && sorted.get(1).getRank() == Rank.JACK
            && sorted.get(2).getRank() == Rank.QUEEN
            && sorted.get(3).getRank() == Rank.KING
            && sorted.get(4).getRank() == Rank.ACE
            && isFlush;

        return (isStraight && isRoyal) ? HandType.ROYAL_FLUSH
            : (isStraight && isFlush) ? HandType.STRAIGHT_FLUSH
            : hasQuads ? HandType.QUADS
            : (hasTrips && hasPair) ? HandType.FULL_HOUSE
            : isFlush ? HandType.FLUSH
            : isStraight ? HandType.STRAIGHT
            : hasTrips ? HandType.TRIPS
            : (counts.stream().filter(c -> c == 2).count() == 2) ? HandType.TWO_PAIR
            : hasPair ? HandType.PAIR
            : HandType.HIGH_CARD;
    }

    public void discard(Stage stage) {
        List<Card> toDiscard = new ArrayList<>(selected);
        for(Card c : toDiscard) {
            c.clearActions();
            c.addAction(Actions.moveBy(0f, -100f, 0.2f));
            remove(c);
        }
        layout(stage);
    }
}
