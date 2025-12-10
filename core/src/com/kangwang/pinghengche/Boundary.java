package com.kangwang.pinghengche;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.kangwang.WorldConstant;
import com.kangwang.word.Constant;

public class Boundary {
    public Boundary(float x,float y,float w,float h){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(WorldConstant.convert(x),WorldConstant.convert(y));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0F;
        fixtureDef.friction = 0.5F;
        fixtureDef.restitution = 0.2F;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WorldConstant.convert(w),WorldConstant.convert(h/2.0f));
        fixtureDef.shape = shape;
        Body body = Constant.world.createBody(def);
        body.createFixture(fixtureDef);
    }
}
