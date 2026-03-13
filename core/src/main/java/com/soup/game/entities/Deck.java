package com.soup.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.soup.game.intf.Entity;
import com.soup.game.meta.Jokers;
import com.soup.game.meta.Rank;
import com.soup.game.meta.Suit;
import com.soup.game.service.RenderService;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class Deck extends Actor
    implements Entity {
    private final List<Card> cards;
    private static final Texture back = new Texture("sprites/cards_back.png");

    public Deck() {
        this.cards = new ArrayList<>();
        setPosition(getX(), getY());
        setSize(back.getWidth(), back.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(back, getX(), getY(), getWidth(), getHeight());
    }

    public List<Card> getCards() {
        return cards;
    }

    public void populate(RenderService renderService) {
        for(Suit suit : Suit.values()) {
            for(Rank rank : Rank.values()) {
                add(new Card(suit, rank, false, renderService.getRegion(suit, rank)));
            }
        }

        add(new Joker(true, renderService.getRegion(Jokers.BLACK)));
        add(new Joker(true, renderService.getRegion(Jokers.RED)));
    }

    public void add(Card c) {
        this.cards.add(c);
    }
    public void remove(Card c) { this.cards.remove(c); }
}
