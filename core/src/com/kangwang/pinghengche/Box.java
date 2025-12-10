package com.kangwang.pinghengche;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.kangwang.WorldConstant;
import com.kangwang.word.Constant;

public class Box {
    public Body body;

    public Box(float x,float y,float w, float h){
        this(x,y,w,h,false);
    }

    public Box(float x,float y,float w, float h,boolean falg){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(WorldConstant.convert(x),WorldConstant.convert(y));

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(WorldConstant.convert(w/2), WorldConstant.convert(h/2));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1.0F;
        fixtureDef.friction = 0.5F;
        fixtureDef.restitution = 0.2f;
        if (falg) {
            fixtureDef.filter.maskBits = 0;
            bodyDef.linearDamping = 0.1F;
        }
        this.body = Constant.world.createBody(bodyDef);
        body.createFixture(fixtureDef);
    }
}
