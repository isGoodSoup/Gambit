package com.soup.game.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.soup.game.scene.Table;
import com.soup.game.screens.GameScreen;
import com.soup.game.service.DeckService;
import com.soup.game.service.GameService;
import com.soup.game.service.RenderService;
import com.soup.game.service.ServiceFactory;

public class GambitGame extends Game {
    private ServiceFactory service;

    @Override
    public void create() {
        service = new ServiceFactory();
        Table table = new Table();
        Stage stage = new Stage();

        String sheet = "cards_4x.png";
        String jokers = "jokers_4x.png";
        String back = "cards_back.png";

        service.register(DeckService.class, new DeckService(table.getDeck()));
        service.register(GameService.class, new GameService(table, service.get(DeckService.class)));
        service.register(RenderService.class, new RenderService(stage, sheet, jokers, back, 1));
        setScreen(new GameScreen(service, stage));
    }
}
