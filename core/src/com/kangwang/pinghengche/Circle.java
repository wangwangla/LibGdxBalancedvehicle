package com.kangwang.pinghengche;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.kangwang.WorldConstant;
import com.kangwang.word.Constant;

public class Circle {
    public Body body;
    public Circle(float x,float y,float r){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
                WorldConstant.convert(x),
                WorldConstant.convert(y));
        CircleShape polygonShape = new CircleShape();
        polygonShape.setRadius(
                WorldConstant.convert(r));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1.0F;
        fixtureDef.friction = 0.5F;
        fixtureDef.restitution = 0.2f;
        this.body = Constant.world.createBody(bodyDef);
        body.createFixture(fixtureDef);
    }
}
