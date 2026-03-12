package com.soup.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.soup.game.intf.Entity;
import com.soup.game.meta.Suit;
import com.soup.game.service.RenderService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("all")
public class Jokers extends Group
    implements Entity {
    private final RenderService renderService;
    private final List<Joker> deck;
    private final List<Joker> row;
    private final Random random = new Random();

    public Jokers(RenderService renderService) {
        this.deck = new ArrayList<>();
        this.row = new ArrayList<>();
        this.renderService = renderService;
        populate();
        pickRow();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float spacing = 12f;
        for(int i = 0; i < row.size(); i++) {
            Joker joker = row.get(i);
            float x = getX() + i * (renderService.getRegion(joker.getSuit()).getRegionWidth() + spacing);
            batch.draw(renderService.getRegion(joker.getSuit()), x, getY());
        }
    }

    public void populate() {
        if(deck.isEmpty()) {
            for(Suit suit : Suit.values()) {
                for(int i = 0; i <= 5; i++) {
                    deck.add(new Joker(suit, (float) Math.random() * 10,
                        true, renderService.getRegion(suit)));
                }
            }
        }
    }


    public void pickRow() {
        clearChildren();
        Collections.shuffle(deck);
        float spacing = 12f;
        for(int i = 0; i < 5; i++) {
            Joker joker = deck.get(i);
            float x = i * (joker.getWidth() + spacing);
            float y = 0;
            joker.setPosition(x, y);
            addActor(joker);
        }
    }

    public List<Joker> getDeck() {
        return deck;
    }

    public Joker get(int index) {
        return deck.get(index);
    }

    public void add(Joker j) {
        deck.add(j);
    }

    public Joker draw() {
        return deck.get(random.nextInt(0, deck.size() - 1));
    }

    public void remove(Joker j) {
        deck.remove(j);
        j.remove();
    }
}
