package com.kangwang.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kangwang.WorldConstant;

public class BodyActor extends Group {
    private Body body;
    private Image image;

    public BodyActor(Texture texture,Body body){
        this.body = body;
        image = new Image(texture);
        addActor(image);
        setSize(image.getWidth(),image.getHeight());

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setRotation((float) Math.toDegrees(body.getAngle()));
        setPosition(WorldConstant.reconvert(body.getPosition().x), WorldConstant.reconvert(body.getPosition().y), Align.center);
    }
}
