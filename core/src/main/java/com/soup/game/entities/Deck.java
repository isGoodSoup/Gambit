package com.soup.game.entities;

import com.soup.game.intf.Entity;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;
import com.soup.game.service.RenderService;

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

    public void populate(RenderService renderService) {
        for(Suit suit : Suit.values()) {
            for(Rank rank : Rank.values()) {
                add(new Card(suit, rank, rank.getPoints(), false,
                    renderService.getRegion(suit, rank)));
            }
            add(new Joker(suit, 50f, true, renderService.getRegion(suit)));
        }
    }

    public void add(Card c) {
        this.cards.put(c.getId(), c);
    }
    public void remove(Card c) { this.cards.remove(c.getId()); }

    public Card draw() {
        if(cards.isEmpty()) { return null; }
        Card first = cards.values().iterator().next();
        remove(first);
        return first;
    }
}
