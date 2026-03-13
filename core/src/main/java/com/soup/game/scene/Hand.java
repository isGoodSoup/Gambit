package com.soup.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.soup.game.entities.Card;
import com.soup.game.entities.Joker;
import com.soup.game.meta.HandType;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Hand {
    private static final int MAX_SIZE = 8;
    private static final int MAX_SELECT = 5;
    private static final Logger log = LoggerFactory.getLogger(Hand.class);
    private final List<Card> cards;
    private final List<Card> selected;
    private int jokerCount = 0;
    private int jokerMultiplier = 0;
    private boolean isReady;

    public Hand() {
        this.cards = new ArrayList<>();
        this.selected = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getJokerCount() {
        return jokerCount;
    }
    public int getJokerMultiplier() {
        return jokerMultiplier;
    }
    public void setJokerMultiplier(int jokerCount) {
        this.jokerMultiplier += jokerCount;
    }

    public void add(Card c) {
        this.cards.add(c);
    }

    public void select(Card c) {
        selected.removeIf(card -> !cards.contains(card));
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

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public float getCardsY() {
        return cards.getFirst().getBaseY();
    }

    public void layout(Stage stage, boolean canAnimate) {
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
            float targetX = startX + i * spacing;
            float targetY = handY;

            if(c.getStage() == null) {
                stage.addActor(c);
                if(canAnimate) {
                    c.setPosition(targetX, targetY + 200); // start above hand
                    c.addAction(Actions.moveTo(targetX, targetY, 0.5f, Interpolation.sineOut));
                } else {
                    c.setPosition(targetX, targetY);
                }
            } else if(canAnimate && !c.isAnimating()) {
                c.addAction(Actions.moveTo(targetX, targetY, 0.3f, Interpolation.sineOut));
            } else {
                c.setPosition(targetX, targetY);
            }
        }
    }

    @SuppressWarnings("all")
    public HandType evaluate(List<Card> activeHand) {
        if(activeHand.isEmpty()) { return HandType.HIGH_CARD; }

        jokerCount = 0;
        jokerMultiplier = 1;
        List<Card> nonJokers = new ArrayList<>();
        for(Card c : activeHand) {
            if(c instanceof Joker) {
                jokerCount++;
                jokerMultiplier += 1;
            } else {
                nonJokers.add(c);
            }
        }

        int totalCards = nonJokers.size() + jokerCount;

        Map<Rank, Integer> rankCounts = new HashMap<>();
        for(Card c : nonJokers) {
            rankCounts.put(c.getRank(), rankCounts.getOrDefault(c.getRank(), 0) + 1);
        }

        Map<Suit, Integer> suitCounts = new HashMap<>();
        for(Card c : nonJokers) {
            suitCounts.put(c.getSuit(), suitCounts.getOrDefault(c.getSuit(), 0) + 1);
        }

        Suit bestSuit = suitCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(Suit.HEARTS);

        for(Card c : activeHand) {
            if(c instanceof Joker joker) {
                joker.setVirtualSuit(bestSuit);
            }
        }

        Map<Suit, Integer> totalSuitCounts = new HashMap<>();
        for(Card c : activeHand) {
            Suit s = c.getSuit();
            if(s != null) {
                totalSuitCounts.put(s, totalSuitCounts.getOrDefault(s, 0) + 1);
            }
        }

        if(totalCards < 5) {
            int pairs = 0, trips = 0, quads = 0;
            for(int count : rankCounts.values()) {
                pairs += (count == 2 ? 1 : 0);
                trips += (count == 3 ? 1 : 0);
                quads += (count == 4 ? 1 : 0);
            }

            if(quads > 0) { return HandType.QUADS; }
            if(trips > 0 && pairs > 0) { return HandType.FULL_HOUSE; }
            if(trips > 0) { return HandType.TRIPS; }
            if(pairs > 1) { return HandType.TWO_PAIR; }
            if(pairs == 1) { return HandType.PAIR; }
            return HandType.HIGH_CARD;
        }

        Set<Integer> rankSet = new HashSet<>();
        for(Card c : activeHand) {
            Rank r = c.getRank();
            if(r != null) {
                rankSet.add(r.ordinal());
            }
        }

        boolean isStraight = false;
        for(int start = 0; start <= 14 - 5; start++) {
            int missing = 0;
            for(int i = 0; i < 5; i++) {
                if(!rankSet.contains(start + i)) {
                    missing++;
                }
            }
            if(missing <= jokerCount) {
                isStraight = true;
                break;
            }
        }
        int missingAceLow = 0;
        for(int r : List.of(0, 1, 2, 3, 12)) {
            if(!rankSet.contains(r)) {
                missingAceLow++;
            }
        }
        if(missingAceLow <= jokerCount) {
            isStraight = true;
        }

        boolean isFlush = totalSuitCounts.values().stream().anyMatch(count -> count >= 5);

        int neededForRoyal = 0;
        for(Rank r : List.of(Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING, Rank.ACE)) {
            if(!rankSet.contains(r.ordinal())) {
                neededForRoyal++;
            }
        }

        boolean isRoyal = isFlush && neededForRoyal <= jokerCount;

        int pairs = 0, trips = 0, quads = 0;
        for(int count : rankCounts.values()) {
            pairs += (count == 2 ? 1 : 0);
            trips += (count == 3 ? 1 : 0);
            quads += (count == 4 ? 1 : 0);
        }

        return (isStraight && isFlush && isRoyal) ? HandType.ROYAL_FLUSH :
            (isStraight && isFlush) ? HandType.STRAIGHT_FLUSH :
                (quads > 0) ? HandType.QUADS :
                    (trips > 0 && pairs > 0) ? HandType.FULL_HOUSE :
                        (isFlush) ? HandType.FLUSH :
                            (isStraight) ? HandType.STRAIGHT :
                                (trips > 0) ? HandType.TRIPS :
                                    (pairs > 1) ? HandType.TWO_PAIR :
                                        (pairs == 1) ? HandType.PAIR :
                                            HandType.HIGH_CARD;
    }
}
