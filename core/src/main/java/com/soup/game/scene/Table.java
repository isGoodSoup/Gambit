package com.soup.game.scene;

import com.soup.game.entities.Card;
import com.soup.game.entities.Deck;
import com.soup.game.meta.GameState;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final Deck deck;
    private final Hand hand;
    private final Deck discarded;
    private final List<Card> inPlay;
    private float score;
    private GameState state;

    private int round;
    private int chips;
    private float multiplier;

    public Table() {
        this.deck = new Deck();
        deck.populate();

        this.hand = new Hand();
        this.discarded = new Deck();
        this.inPlay = new ArrayList<>();

        this.score = 0;
        this.state = GameState.DEALING;
    }

    public Deck getDeck() { return deck; }
    public Hand getHand() { return hand; }

    public void add(Card c) {
        this.inPlay.add(c);
    }

    public Deck getDiscarded() { return discarded; }
    public List<Card> getInPlay() { return inPlay; }

    public float getScore() { return score; }
    public void addScore(float s) { score += s; }

    public GameState getState() { return state; }
    public void setState(GameState state) { this.state = state; }

    public void discard(Card c) {
        discarded.add(c);
    }

    public void roundUp(int i) {
        this.round += i;
    }

    public void addChip(int i) {
        this.chips += i;
    }

    public void lose() {
        this.chips = 0;
    }

    public void multiply(float i) {
        this.multiplier += i;
    }
}
