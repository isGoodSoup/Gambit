package com.soup.game.service;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.soup.game.entities.Card;
import com.soup.game.intf.Service;
import com.soup.game.meta.GameState;
import com.soup.game.scene.Hand;
import com.soup.game.scene.Table;

import java.util.List;

public class GameService implements Service {
    private final Table table;
    private final DeckService deckService;
    private final Stage stage;

    public GameService(Table table, DeckService deckService, Stage stage) {
        this.table = table;
        this.deckService = deckService;
        this.stage = stage;
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
        if(table.getState() == GameState.DEALING && hand.size() == 0) {
            deckService.shuffle();
        }

        if(hand.isComplete()) {
            return;
        }

        while(!hand.isComplete()) {
            Card c = deckService.draw();
            if(c == null) { break; }
            hand.add(c);
        }

        hand.layout(stage);
        table.setState(GameState.PLAYER_TURN);
    }

    private void playHand() {
        Hand hand = table.getHand();
        if(hand.isReady()) {
            for(Card c : List.copyOf(hand.getSelectedCards())) {
                table.add(c);
                hand.remove(c);
            }

            hand.clearSelection();
            hand.layout(stage);
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
        for(Card c : List.copyOf(hand.getCards())) {
            table.discard(c);
        }

        hand.clear();
        table.setState(GameState.DEALING);
    }
}
