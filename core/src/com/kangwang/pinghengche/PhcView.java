package com.kangwang.pinghengche;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.kangwang.WorldConstant;
import com.kangwang.word.Constant;

public class PhcView extends Group {
    private Pendulum pendulum;
    private float targetX;
    private Pid pid;
    private Filter filter;
    private float time = 0;
    private Matrix4 combined;

    public PhcView(){
        //地板
        this.combined = new Matrix4();
        new Boundary(Constant.width, 2,Constant.width,10);
        pendulum = new Pendulum(Constant.width/2,Constant.hight/2);
        targetX = WorldConstant.convert(Constant.width/2.0f);
        pid = new Pid(30, 20, 20);
        filter = new Filter();
    }

    public float rectify(float x,float minValue,float maxValue) {
        if (x > maxValue) return maxValue;
        if (x < minValue) return minValue;
        return x;
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        Constant.world.step(1/60f, 3, 8);
        combined.set(Constant.combined);
        combined.scale(WorldConstant.PPM,
                WorldConstant.PPM,
                WorldConstant.PPM);

        Constant.renderer.render(Constant.world,combined);
        time += delta;
        if (time > 0.6) {
            Vector2 pos = pendulum.getPosition();
            Vector2 velocity = pendulum.getVelocity();
            filter.push(velocity.x);
            float smoothVelocity = filter.getValue();

            float predictPos = pos.x + 25 * smoothVelocity;
            if((predictPos - targetX) * (pos.x-targetX) < 0) {
                predictPos = pos.x + 30 * smoothVelocity;
            }

            float targetAngle = rectify(-(predictPos-targetX)/20,
                    -0.25f, 0.25f);

            if (-(predictPos-targetX) < 10) {
                targetAngle = rectify(-(predictPos-targetX)/20,
                        -0.15f, 0.15f);
            }

            float nowAngle = -pendulum.getAngleRadians();
            if(
                    Math.abs(10 * smoothVelocity) > 60 &&
                            (predictPos - targetX) * (pos.x-targetX) >= 0 &&
                            (predictPos - targetX) * smoothVelocity < 0
            ) {
                targetAngle = 0;
            }
            pid.setcError(targetAngle-nowAngle);
            pid.step(delta);
            float speed = -pid.getOutput();
            pendulum.setMotorSpeed(speed*330);
        }
    }

    public void userTouch(float x, float y) {
        targetX = x;
        pendulum.getPosition();
    }
}
