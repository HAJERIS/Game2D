package com.example.game2d;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Hero {
    private Bitmap bitmap;
    private float x, y;

    public Hero(Bitmap bitmap, float x, float y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void move(float x, float y, int screenWidth, int screenHeight) {
        // Ensure the new position is within the screen boundaries
        this.x = Math.max(0, Math.min(x, screenWidth - bitmap.getWidth()));
        this.y = Math.max(0, Math.min(y, screenHeight - bitmap.getHeight()));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    // Inside the Hero class

    public boolean intersects(float otherX, float otherY, int otherWidth, int otherHeight) {
        float heroRight = x + bitmap.getWidth();
        float heroBottom = y + bitmap.getHeight();

        float otherRight = otherX + otherWidth;
        float otherBottom = otherY + otherHeight;

        // Check for intersection
        return x < otherRight && heroRight > otherX && y < otherBottom && heroBottom > otherY;
    }

}
