package com.kangwang.word;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kangwang.pinghengche.PhcView;

public class GameScreen extends ScreenAdapter {
    private Stage stage;
    private InputMultiplexer inputMultiplexer;
    public GameScreen(MainGame mainGame) {
        inputMultiplexer = new InputMultiplexer();
        stage =  new Stage(mainGame.getViewport(),mainGame.getBatch());
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void show() {
        PhcView view = new PhcView();
        stage.addActor(view);
        stage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                view.userTouch(x,y);
            }
        });
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }
}
