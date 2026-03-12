package com.soup.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

    public void update(float delta, Label currency, Label score) {
        switch(table.getState()) {
            case DEALING -> deal();
            case PLAYER_TURN -> playHand();
            case SCORING -> scoreHand(currency, score);
            case DISCARDING -> discardHand();
        }
    }

    public void update(Label currency, Label score) {
        currency.setText("$" + (int) table.getCurrency());
        currency.setColor(new Color(1f, 0.8f, 0.2f, 1f));
        score.setText(String.valueOf((int) table.getScore()));
    }

    private void deal() {
        Hand hand = table.getHand();
        if(hand.size() == 0) {
            deckService.shuffle();
        }

        while(hand.size() < hand.getMaxSize()) {
            Card c = deckService.draw();
            if(c == null) break;
            hand.add(c);
        }

        hand.layout(stage, true);
        table.setState(GameState.PLAYER_TURN);
    }

    private void playHand() {
        Hand hand = table.getHand();
        if(!hand.isReady()) return;

        List<Card> selected = List.copyOf(hand.getSelectedCards());
        if(selected.isEmpty()) return;

        lastPlayedCards = new ArrayList<>(selected);

        float tableX = Gdx.graphics.getWidth() / 2f;
        float tableY = Gdx.graphics.getHeight() / 2f;

        final int[] finishedCount = {0};
        final int total = selected.size();

        for (Card c : selected) {
            c.setAnimating(true);
            c.addAction(Actions.sequence(
                Actions.parallel(
                    Actions.moveTo(tableX, tableY, 0.5f, Interpolation.sineOut),
                    Actions.scaleBy(0.2f, 0.2f, 0.25f)
                ),
                Actions.run(() -> {
                    c.setAnimating(false);
                    table.add(c);
                    hand.remove(c);
                    audioService.playFX(1);
                    finishedCount[0]++;
                    if (finishedCount[0] == total) {
                        refill(hand);
                    }
                })
            ));
        }
    }

    private void refill(Hand hand) {
        int missing = hand.getMaxSize() - hand.size();
        if(missing <= 0) {
            hand.clearSelection();
            hand.setReady(false);
            table.setState(GameState.SCORING);
            return;
        }

        for(int i = 0; i < missing; i++) {
            Card newCard = deckService.draw();
            if(newCard == null) { break; }

            float deckX = table.getDeck().getX();
            float deckY = table.getDeck().getY();
            newCard.setPosition(deckX, deckY);
            newCard.getColor().a = 0f;

            hand.add(newCard);

            float targetX = newCard.getX();
            float targetY = newCard.getY();

            newCard.addAction(Actions.sequence(
                Actions.parallel(
                    Actions.moveTo(targetX, targetY, 0.4f, Interpolation.sineOut)
                )
            ));
        }

        stage.addAction(Actions.sequence(
            Actions.delay(0.35f),
            Actions.run(() -> {
                hand.layout(stage, true);
                hand.clearSelection();
                hand.setReady(false);
                table.setState(GameState.SCORING);
            })
        ));
    }

    private void scoreHand(Label currency, Label score) {
        Hand hand = table.getHand();
        if(lastPlayedCards == null || lastPlayedCards.isEmpty()) {
            table.setState(GameState.PLAYER_TURN);
            return;
        }

        HandType handValue = hand.evaluate(lastPlayedCards);
        float points = hand.getValue(handValue, lastPlayedCards);
        lastPlayedCards.clear();
        table.addScore(points);
        table.addCurrency(handValue.getValue());

        while(hand.size() < hand.getMaxSize()) {
            Card newCard = deckService.draw();
            if(newCard == null) {
                break;
            }
            float deckX = table.getDeck().getX();
            float deckY = table.getDeck().getY();
            newCard.setPosition(deckX, deckY);
            newCard.getColor().a = 0f;
            hand.add(newCard);
            float targetX = newCard.getX();
            float targetY = newCard.getY();

            newCard.addAction(Actions.sequence(
                Actions.parallel(
                    Actions.moveTo(targetX, targetY, 0.3f, Interpolation.sineOut)
                )
            ));
        }

        stage.addAction(Actions.sequence(
            Actions.delay(0.4f),
            Actions.run(() -> {
                hand.layout(stage, true);
                lastPlayedCards.clear();
                hand.setReady(false);
                table.setState(GameState.PLAYER_TURN);
            })
        ));
    }

    public void discardHand() {
        Hand hand = table.getHand();
        List<Card> toDiscard = new ArrayList<>(hand.getSelectedCards());

        if(toDiscard.isEmpty()) {
            table.setState(GameState.PLAYER_TURN);
            return;
        }

        for(Card c : toDiscard) {
            c.setAnimating(true);
            c.addAction(Actions.sequence(
                Actions.parallel(
                    Actions.moveTo(c.getX(), -100f, 0.25f, Interpolation.sine),
                    Actions.scaleBy(0.2f, 0.2f, 0.15f)
                ),
                Actions.run(() -> {
                    c.setAnimating(false);
                    table.discard(c);
                    hand.remove(c);
                })
            ));

            Card newCard = deckService.draw();
            if(newCard != null) {
                float deckX = table.getDeck().getX();
                float deckY = table.getDeck().getY();

                newCard.setPosition(deckX, deckY);
                newCard.getColor().a = 0f;

                hand.add(newCard);
                float targetX = newCard.getX();
                float targetY = newCard.getY();

                newCard.addAction(Actions.sequence(
                    Actions.parallel(
                        Actions.moveTo(targetX, targetY, 0.3f, Interpolation.sineOut)
                    )
                ));
            }
        }

        hand.clearSelection();
        stage.addAction(Actions.sequence(
            Actions.delay(0.4f),
            Actions.run(() -> {
                hand.layout(stage, true);
            })
        ));
        hand.setReady(false);

        table.setState(GameState.PLAYER_TURN);
    }
}
