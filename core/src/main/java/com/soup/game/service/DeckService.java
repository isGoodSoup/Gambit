package com.soup.game.service;

import com.soup.game.entities.Card;
import com.soup.game.entities.Deck;
import com.soup.game.intf.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckService implements Service {
    private final List<Card> cards;

    public DeckService(Deck deck) {
        this.cards = deck.getCards();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        if(cards.isEmpty()) { return null; }
        Card copy = cards.removeFirst();
        return new Card(copy);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }
}
