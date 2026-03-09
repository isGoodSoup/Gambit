package com.soup.game.service;

import com.soup.game.entities.Card;
import com.soup.game.intf.Service;
import com.soup.game.meta.GameState;
import com.soup.game.scene.Hand;
import com.soup.game.scene.Table;

import java.util.List;

public class GameService implements Service {
    private final Table table;

    public GameService(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    public void update(float delta) {
        switch(table.getState()) {
            case DEALING -> deal();
            case PLAYER_TURN -> playHand();
            case SCORING -> scoreHand();
            case DISCARDING -> discardHand();
        }
    }

    private void deal() {
        Hand hand = table.getHand();
        if(hand.isComplete()) {
            return;
        }

        while(!hand.isComplete()) {
            Card c = table.getDeck().draw();
            hand.add(c);
        }
        table.setState(GameState.PLAYER_TURN);
    }

    private void playHand() {
        Hand hand = table.getHand();
        List<Card> selected = hand.getSelectedCards();

        if(hand.isReady()) {
            for(Card c : selected) {
                table.add(c);
                hand.remove(c);
            }

            hand.clearSelection();
            table.setState(GameState.SCORING);
        }
    }

    private void scoreHand() {
        Hand hand = table.getHand();
        float points = hand.getValue(hand.getHand(), hand.getCards());
        table.addScore(points);
        table.setState(GameState.DISCARDING);
    }

    private void discardHand() {
        Hand hand = table.getHand();
        for(Card c : hand.getCards()) {
            table.discard(c);
        }

        hand.clear();
        table.setState(GameState.DEALING);
    }
}
