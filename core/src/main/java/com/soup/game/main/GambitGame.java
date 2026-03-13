package com.soup.game.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.soup.game.scene.Table;
import com.soup.game.screens.OpenScreen;
import com.soup.game.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GambitGame extends Game {
    private static final Logger log = LoggerFactory.getLogger(GambitGame.class);

    @Override
    public void create() {
        log.info("Creating new session");
        ServiceFactory service = new ServiceFactory();
        Stage stage = new Stage(new ScreenViewport());
        stage.setDebugAll(false);
        stage.getRoot().setTransform(false);

        String sheet = "sprites/cards_4x.png";
        String jokers = "sprites/jokers_4x.png";

        log.debug("Loading services");
        service.register(RenderService.class, new RenderService(sheet, jokers, 1));
        Table table = new Table(service.get(RenderService.class));

        service.register(AudioService.class, new AudioService());
        service.register(DeckService.class, new DeckService(table.getDeck()));
        service.register(GameService.class, new GameService(table, stage,
            service.get(DeckService.class), service.get(AudioService.class)));
        service.register(UIAssets.class, new UIAssets());

        log.info("Booting up game");
        service.get(AudioService.class).playMusic();
        setScreen(new OpenScreen(this, stage, service));
    }
}
