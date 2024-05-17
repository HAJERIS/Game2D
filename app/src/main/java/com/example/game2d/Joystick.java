package com.example.game2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Joystick {
    private float centerX, centerY, baseRadius, hatRadius, joystickX, joystickY;
    private boolean isPressed;
    private Paint basePaint, hatPaint;

    public Joystick(float centerX, float centerY, float baseRadius, float hatRadius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.baseRadius = baseRadius;
        this.hatRadius = hatRadius;
        this.joystickX = centerX;
        this.joystickY = centerY;
        this.isPressed = false;

        basePaint = new Paint();
        basePaint.setColor(Color.GRAY);
        basePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        hatPaint = new Paint();
        hatPaint.setColor(Color.DKGRAY);
        hatPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, baseRadius, basePaint);
        canvas.drawCircle(joystickX, joystickY, hatRadius, hatPaint);
    }

    public void update(MotionEvent event) {
        float distance = (float) Math.sqrt(Math.pow(event.getX() - centerX, 2) + Math.pow(event.getY() - centerY, 2));
        if (distance < baseRadius) {
            joystickX = event.getX();
            joystickY = event.getY();
            isPressed = true;
        } else {
            joystickX = (event.getX() - centerX) * (baseRadius / distance) + centerX;
            joystickY = (event.getY() - centerY) * (baseRadius / distance) + centerY;
            isPressed = true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            joystickX = centerX;
            joystickY = centerY;
            isPressed = false;
        }
    }

    public float getXPercent() {
        return (joystickX - centerX) / baseRadius;
    }

    public float getYPercent() {
        return (joystickY - centerY) / baseRadius;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public boolean isTouchWithinBounds(MotionEvent event) {
        float distance = (float) Math.sqrt(Math.pow(event.getX() - centerX, 2) + Math.pow(event.getY() - centerY, 2));
        return distance < baseRadius;
    }
}
