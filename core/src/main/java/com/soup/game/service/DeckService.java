package com.soup.game.service;

import com.soup.game.entities.Card;
import com.soup.game.entities.Deck;
import com.soup.game.intf.Service;

import java.util.Arrays;
import java.util.Collections;

public class DeckService implements Service {
    private final Deck deck;

    public DeckService(Deck deck) {
        this.deck = deck;
    }

    public void shuffle() {
        if(!this.deck.getCards().isEmpty()) {
            Collections.shuffle(Arrays.asList(
                deck.getCards().values().toArray()));
        }
    }

    public Card draw() {
        return deck.draw();
    }
}
