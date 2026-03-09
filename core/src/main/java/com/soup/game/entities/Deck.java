package com.soup.game.entities;

import com.soup.game.intf.Entity;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;

import java.util.LinkedHashMap;
import java.util.Map;

public class Deck implements Entity {
    private final Map<Long, Card> cards;

    public Deck() {
        this.cards = new LinkedHashMap<>();
    }

    public Map<Long, Card> getCards() {
        return cards;
    }

    public void populate() {
        for(Suit suit : Suit.values()) {
            for(Rank rank : Rank.values()) {
                add(new Card(suit, rank, rank.getPoints(), false));
            }
            add(new Joker(suit, Rank.JOKER, Rank.JOKER.getPoints(), true));
        }
    }

    public void add(Card c) {
        this.cards.put(c.getId(), c);
    }
    public Card draw() {
        if(cards.isEmpty()) {
            return null;
        }

        Long key = cards.keySet().iterator().next();
        return cards.remove(key);
    }
    public void remove(Card c) { this.cards.remove(c.getId()); }
}
