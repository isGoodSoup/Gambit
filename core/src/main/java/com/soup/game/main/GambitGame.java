package com.soup.game.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.soup.game.scene.Table;
import com.soup.game.screens.GameScreen;
import com.soup.game.service.*;

public class GambitGame extends Game {

    @Override
    public void create() {
        ServiceFactory service = new ServiceFactory();
        Stage stage = new Stage(new ScreenViewport());
        stage.setDebugAll(false);
        stage.getRoot().setTransform(false);

        String sheet = "cards_4x.png";
        String jokers = "jokers_4x.png";
        String back = "cards_back.png";

        service.register(RenderService.class, new RenderService(sheet, jokers, back, 1));
        Table table = new Table(service.get(RenderService.class));

        service.register(AudioService.class, new AudioService());
        service.register(DeckService.class, new DeckService(table.getDeck()));
        service.register(GameService.class, new GameService(table, stage,
            service.get(DeckService.class), service.get(AudioService.class)));
        setScreen(new GameScreen(service, stage));
    }
}
