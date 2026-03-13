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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class GameService implements Service {
    public final float deckX = Gdx.graphics.getWidth() - 400f;
    public final float deckY = 50f;
    public static float gameSpeed = 1f;

    private static final Logger log = LoggerFactory.getLogger(GameService.class);
    private final Table table;
    private final Stage stage;
    private final DeckService deckService;
    private final AudioService audioService;

    private List<Card> lastPlayedCards;

    private boolean wasHandLogged;
    private boolean hasPlayed;

    public GameService(Table table, Stage stage, DeckService deckService,
                       AudioService audioService) {
        this.table = table;
        this.deckService = deckService;
        this.stage = stage;
        this.audioService = audioService;
        log.info("Dealing cards (for the first time)");
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
            if(c == null) { break;   }
            hand.add(c);
        }

        hand.layout(stage, true);
        log.info("Switching to player turn");
        table.setState(GameState.PLAYER_TURN);
    }

    private void playHand() {
        if(!wasHandLogged) {
            log.info("Playing selected hand");
            wasHandLogged = true;
        }
        Hand hand = table.getHand();
        if(!hand.isReady()) return;

        List<Card> selected = List.copyOf(hand.getSelectedCards());
        if(selected.isEmpty()) return;

        lastPlayedCards = new ArrayList<>(selected);

        float tableY = Gdx.graphics.getHeight()/2f;

        final int[] finishedCount = { 0 };
        final int total = selected.size();

        for(Card c : selected) {
            c.setAnimating(true);

            float tableX = c.getX();
            float offScreenY = -100f;

            c.addAction(Actions.sequence(
                Actions.moveTo(tableX, tableY, 0.2f * gameSpeed, Interpolation.sineOut),
                Actions.delay(1f),
                Actions.moveTo(tableX, offScreenY, 0.4f * gameSpeed, Interpolation.pow5In),
                Actions.run(() -> {
                    c.setAnimating(false);
                    hand.remove(c);
                    table.add(c);
                    finishedCount[0]++;
                    if(finishedCount[0] == total) {
                        hasPlayed = false;
                        refill(hand);
                    }
                })
            ));
        }
    }

    private void refill(Hand hand) {
        log.info("Attempting refill...");
        wasHandLogged = false;
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
                    Actions.moveTo(targetX, targetY, 0.4f * gameSpeed,
                        Interpolation.sineOut)
                )
            ));
        }

        stage.addAction(Actions.sequence(
            Actions.delay(0.4f * gameSpeed),
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
        if(hand.getJokerCount() > 0) {
            hand.setJokerMultiplier(hand.getJokerCount());
            points *= hand.getJokerMultiplier();
        }
        lastPlayedCards.clear();
        table.addScore(points);
        table.addCurrency(handValue.getValue());
        log.info("Score!");
        log.info("Scored hand: {}", handValue);
        log.info("Current score: {}", table.getScore());
        log.info("Current balance: {}", table.getCurrency());

        log.info("Drawing new card(s) (score)");
        while(hand.size() < hand.getMaxSize()) {
            Card newCard = deckService.draw();
            if(newCard == null) {
                break;
            }
            newCard.setPosition(deckX, deckY);
            newCard.flip();
            hand.add(newCard);
            float targetX = newCard.getX();
            float targetY = newCard.getY();

            newCard.addAction(Actions.sequence(
                Actions.parallel(
                    Actions.moveTo(targetX, targetY, 0.3f * gameSpeed,
                        Interpolation.sineOut),
                    Actions.run(newCard::flip)
                )
            ));
        }

        stage.addAction(Actions.sequence(
            Actions.delay(0.4f * gameSpeed),
            Actions.run(() -> {
                hand.layout(stage, true);
                lastPlayedCards.clear();
                hand.setReady(false);
                table.setState(GameState.PLAYER_TURN);
            })
        ));
    }

    public void discardHand() {
        log.info("Discarding card(s)");
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
                    Actions.moveTo(c.getX(), -100f, 0.25f * gameSpeed,
                        Interpolation.sine),
                    Actions.scaleBy(0.2f, 0.2f,
                        0.2f * gameSpeed)
                ),
                Actions.run(() -> {
                    c.setAnimating(false);
                    table.discard(c);
                    hand.remove(c);
                })
            ));

            log.info("Drawing new card(s) (discard)");
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
                        Actions.moveTo(targetX, targetY, 0.4f * gameSpeed,
                            Interpolation.sineOut)
                    )
                ));
            }
        }

        hand.clearSelection();
        stage.addAction(Actions.sequence(
            Actions.delay(0.4f  * gameSpeed),
            Actions.run(() -> {
                hand.layout(stage, true);
            })
        ));
        hand.setReady(false);

        table.setState(GameState.PLAYER_TURN);
    }

    public float getGameSpeed() {
        return gameSpeed;
    }

    public void setGameSpeed(float gameSpeed) {
        this.gameSpeed = gameSpeed;
    }
}
