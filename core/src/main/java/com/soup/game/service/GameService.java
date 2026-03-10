package com.soup.game.service;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.soup.game.entities.Card;
import com.soup.game.intf.Service;
import com.soup.game.meta.GameState;
import com.soup.game.meta.HandType;
import com.soup.game.scene.Hand;
import com.soup.game.scene.Table;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class GameService implements Service {
    private final Table table;
    private final Stage stage;
    private final DeckService deckService;
    private final AudioService audioService;
    private List<Card> lastPlayedCards;

    public GameService(Table table, Stage stage, DeckService deckService,
                       AudioService audioService) {
        this.table = table;
        this.deckService = deckService;
        this.stage = stage;
        this.audioService = audioService;
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
        if(hand.isEmpty()) {
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
    }

    private void playHand() {
        Hand hand = table.getHand();
        if(hand.isReady()) {
            List<Card> selected = List.copyOf(hand.getSelectedCards());
            if(selected.isEmpty()) return;
            lastPlayedCards = new ArrayList<>(selected);
            for(int i = 0; i < selected.size(); i++) {
                Card c = selected.get(i);
                boolean last = (i == selected.size() - 1);
                c.addAction(Actions.sequence(
                    Actions.moveBy(0, 25f, 0.2f,
                        Interpolation.pow5Out),
                    Actions.run(() -> audioService.playFX(1)),
                    Actions.run(() -> {
                        table.add(c);
                        hand.remove(c);
                        if(last) {
                            hand.clearSelection();
                            hand.layout(stage);
                            table.setState(GameState.SCORING);
                        }

                    })
                ));
            }
        }
    }

    private void scoreHand() {
        Hand hand = table.getHand();
        if(lastPlayedCards == null || lastPlayedCards.isEmpty()) {
            table.setState(GameState.DISCARDING);
            return;
        }

        HandType type = hand.evaluate(lastPlayedCards);
        float points = hand.getValue(type, lastPlayedCards);
        table.addScore(points);

        lastPlayedCards.clear();
        table.getHand().setReady(false);
    }

    public void discardHand() {
        Hand hand = table.getHand();
        List<Card> toDiscard = new ArrayList<>(hand.getSelectedCards());

        if(toDiscard.isEmpty()) {
            table.setState(GameState.PLAYER_TURN);
            return;
        }

        for(Card c : toDiscard) {
            table.discard(c);
            hand.remove(c);

            Card newCard = deckService.draw();
            if(newCard != null) {
                hand.add(newCard);
            }
        }

        hand.clearSelection();
        hand.layout(stage);
        hand.setReady(false);

        table.setState(GameState.PLAYER_TURN);
    }
}
