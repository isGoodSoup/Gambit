package com.soup.game.entities;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.soup.game.intf.Entity;
import com.soup.game.service.ServiceFactory;
import com.soup.game.service.UIAssets;

public class FloatingText extends Label
    implements Entity {

    public FloatingText(ServiceFactory service, String text) {
        super(text, createStyle(service));

        setAlignment(com.badlogic.gdx.utils.Align.center);

        addAction(Actions.forever(
            Actions.sequence(
                Actions.fadeOut(1f, Interpolation.fade),
                Actions.delay(0.8f),
                Actions.fadeIn(1f, Interpolation.fade)
            )
        ));
    }

    private static LabelStyle createStyle(ServiceFactory service) {
        LabelStyle style = new LabelStyle();
        style.font = service.get(UIAssets.class).getFont();
        return style;
    }
}
