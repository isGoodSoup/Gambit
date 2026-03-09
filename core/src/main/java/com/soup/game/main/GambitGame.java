package com.soup.game.main;

import com.badlogic.gdx.Game;
import com.soup.game.scene.Table;
import com.soup.game.screens.GameScreen;
import com.soup.game.service.DeckService;
import com.soup.game.service.GameService;
import com.soup.game.service.ServiceFactory;

public class GambitGame extends Game {
    private ServiceFactory service;

    @Override
    public void create() {
        service = new ServiceFactory();
        Table table = new Table();
        service.register(GameService.class, new GameService(table));
        service.register(DeckService.class, new DeckService());
        setScreen(new GameScreen(service));
    }
}
