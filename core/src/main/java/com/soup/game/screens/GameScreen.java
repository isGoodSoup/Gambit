package com.soup.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.soup.game.entities.*;
import com.soup.game.scene.Hand;
import com.soup.game.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class GameScreen implements Screen {
    private static final Logger log = LoggerFactory.getLogger(GameScreen.class);
    private final ServiceFactory service;
    private final Stage stage;
    private final Group group;
    private final float buttonWidth = 183f;
    private final float buttonHeight = 80f;
    private final float spacing = buttonWidth/8f;

    private Table table;
    private Label currency;
    private Label score;
    private GameService gameService;

    public GameScreen(ServiceFactory service, Stage stage) {
        this.service = service;
        this.stage = stage;
        this.group = new Group();

        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Actor target = event.getTarget();
                if(target instanceof Card card) {
                    service.get(AudioService.class).playFX(0);
                    gameService.getTable().getHand().select(card);
                }
            }
        });
    }

    @Override
    public void show() {
        log.info("Loading new assets and actors");
        stage.addAction(Actions.fadeIn(1f, Interpolation.fade));
        gameService = service.get(GameService.class);

        gameService.update(0f, currency, score);
        Hand hand = gameService.getTable().getHand();
        Deck deck = gameService.getTable().getDeck();
        Gdx.input.setInputProcessor(stage);

        float mainWidth = 800f;
        float mainHeight = 200f;
        Window mainWindow = new Window(mainWidth, mainHeight);
        mainWindow.setPosition(
            Gdx.graphics.getWidth()/2f - mainWidth/2f,
            hand.getCardsY() - 50f
        );
        group.addActor(mainWindow);

        hand.layout(stage, false);
        for(Card c : hand.getCards()) {
            group.addActor(c);
        }

        log.debug("Creating table");
        table = new Table();
        table.setFillParent(true);
        table.bottom().padBottom(25f);

        Label.LabelStyle labelStyle = new Label.LabelStyle
            (service.get(UIAssets.class).getFont(), Color.WHITE);

        currency = new Label("", labelStyle);
        score = new Label("", labelStyle);

        Window currencyWindow = new Window(buttonWidth, buttonHeight,
            currency, service, new Color(1f, 0.8f, 0.3f, 1f));

        Window scoreWindow = new Window(buttonWidth, buttonHeight,
            score, service, Color.WHITE);

        String play = Localization.lang.t("button.play");
        String discard = Localization.lang.t("button.discard");

        Button readyButton = new Button(false, 0f, 0f, buttonWidth, buttonHeight,
            () -> hand.setReady(true), play, service);

        Button discardButton = new Button(true, 0f, 0f, buttonWidth, buttonHeight,
            () -> gameService.discardHand(), discard, service);

        table.add(currencyWindow).width(buttonWidth).height(buttonHeight).padRight(spacing);
        table.add(readyButton).width(buttonWidth).height(buttonHeight).padRight(spacing);
        table.add(discardButton).width(buttonWidth).height(buttonHeight).padRight(spacing);
        table.add(scoreWindow).width(buttonWidth).height(buttonHeight);
        stage.addActor(table);

        Jokers jokers = new Jokers(service.get(RenderService.class));
        jokers.setPosition(Gdx.graphics.getWidth()/2f - jokers.getWidth()/2f,
            mainHeight + 75f);

        stage.addActor(jokers);
        stage.addActor(group);

        jokers.setPosition(0, -Gdx.graphics.getHeight());
        jokers.addAction(Actions.sequence(
            Actions.delay(0.4f),
            Actions.moveTo(0, 0, 0.8f, Interpolation.sine)
        ));

        table.setPosition(0, -Gdx.graphics.getHeight());
        table.addAction(Actions.sequence(
            Actions.delay(0.4f),
            Actions.moveTo(0, 0, 0.8f, Interpolation.sine)
        ));

        group.setPosition(0, -Gdx.graphics.getHeight());
        group.addAction(Actions.sequence(
            Actions.delay(0.4f),
            Actions.moveTo(0, 0, 0.8f, Interpolation.sine)
        ));
        log.info("Ready");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.5f, 0.3f, 1f);
        gameService.update(delta, currency, score);
        gameService.update(currency, score);
        Card.updateGlobalTime(Gdx.graphics.getDeltaTime());
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
