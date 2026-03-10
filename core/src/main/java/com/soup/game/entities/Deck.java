package com.soup.game.entities;

import com.soup.game.intf.Entity;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;
import com.soup.game.service.RenderService;

import java.util.ArrayList;
import java.util.List;

public class Deck implements Entity {
    private final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void populate(RenderService renderService) {
        for(Suit suit : Suit.values()) {
            for(Rank rank : Rank.values()) {
                add(new Card(suit, rank, rank.getPoints(), false,
                    renderService.getRegion(suit, rank)));
            }
        }
    }

    public void add(Card c) {
        this.cards.add(c);
    }
    public void remove(Card c) { this.cards.remove(c); }
}
