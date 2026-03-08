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
        populate();
    }

    public Map<Long, Card> getCards() {
        return cards;
    }

    public Card draw(Card c) {
        return cards.get(c.getId());
    }

    public void add(Card c) {
        cards.put(c.getId(), c);
    }

    public void populate() {
        for(Rank rank : Rank.values()) {
            for(Suit suit : Suit.values()) {
                add(new Card(suit, rank, rank.getPoints(), false));
                add(new Joker(suit, Rank.JOKER, Rank.JOKER.getPoints(), true));
            }
        }
    }

    public void remove(Card c) {
        cards.remove(c.getId());
    }
}
