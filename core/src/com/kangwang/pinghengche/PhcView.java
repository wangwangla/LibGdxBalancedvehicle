package com.kangwang.pinghengche;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.kangwang.WorldConstant;
import com.kangwang.word.Constant;

public class PhcView extends Group {
    private static final Color TARGET_LINE_COLOR = new Color(1f, 127f / 255f, 127f / 255f, 1f);
    private static final Color PREDICT_LINE_COLOR = new Color(127f / 255f, 1f, 127f / 255f, 1f);
    private static final Color TOP_LINE_COLOR = new Color(200f / 255f, 200f / 255f, 200f / 255f, 1f);
    private static final Color VELOCITY_POINT_COLOR = TARGET_LINE_COLOR;
    private static final float TOP_LINE_Y = 150f;
    private static final float VELOCITY_POINT_SCALE = 8f;

    private Pendulum pendulum;
    private float targetX;
    private Pid pid;
    private Filter filter;
    private float time = 0;
    private Matrix4 combined;
    private ShapeRenderer guideRenderer;
    private float predictX;
    private FloatArray velocityDebugValues;

    public PhcView(){
        //地板
        this.combined = new Matrix4();
        new Boundary(Constant.width, 2,Constant.width,10);
        pendulum = new Pendulum(Constant.width/2,Constant.hight/2);
        targetX = WorldConstant.convert(Constant.width/2.0f);
        pid = new Pid(30, 20, 20);
        filter = new Filter();
        guideRenderer = new ShapeRenderer();
        predictX = targetX;
        velocityDebugValues = new FloatArray();
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
            velocityDebugValues.add(VELOCITY_POINT_SCALE * smoothVelocity);
            int maxPoints = Math.max(1, (int) Constant.width);
            if (velocityDebugValues.size > maxPoints) {
                velocityDebugValues.removeRange(0, velocityDebugValues.size - maxPoints - 1);
            }
            float predictPos = pos.x + 25 * smoothVelocity;
//            if((predictPos - targetX) * (pos.x-targetX) < 0) {
//
//                System.out.println("predict pos"+(predictPos - targetX) * (pos.x-targetX));
//                predictPos = pos.x + 100 * smoothVelocity;
//            }
            predictX = predictPos;

            float targetAngle = rectify(-(predictPos-targetX)/20,
                    -0.25f, 0.25f);

            if (-Math.abs(predictPos-targetX) < 20) {
                targetAngle = rectify(-(predictPos-targetX)/670,
                        -0.1f, 0.1f);
            }

            float nowAngle = -pendulum.getAngleRadians();
            if(
                    Math.abs(smoothVelocity) > 60 &&
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (guideRenderer == null || getStage() == null) {
            return;
        }
        batch.end();
        guideRenderer.setProjectionMatrix(getStage().getCamera().combined);
        guideRenderer.begin(ShapeRenderer.ShapeType.Line);
        guideRenderer.setColor(TOP_LINE_COLOR);
        guideRenderer.line(0, TOP_LINE_Y, Constant.width, TOP_LINE_Y);
        guideRenderer.setColor(TARGET_LINE_COLOR);
        guideRenderer.line(targetX, Constant.hight / 2.0f, targetX, Constant.hight);
        guideRenderer.setColor(PREDICT_LINE_COLOR);
        guideRenderer.line(predictX, Constant.hight / 2.0f, predictX, Constant.hight);
        guideRenderer.setColor(VELOCITY_POINT_COLOR);
        for (int i = 0; i < velocityDebugValues.size; i++) {
            guideRenderer.point(i, TOP_LINE_Y + velocityDebugValues.get(i), 0);
        }
        guideRenderer.end();
        batch.begin();
    }

    public void dispose() {
        if (guideRenderer != null) {
            guideRenderer.dispose();
            guideRenderer = null;
        }
    }

    public void userTouch(float x, float y) {
        targetX = x;
        pendulum.getPosition();
    }
}
