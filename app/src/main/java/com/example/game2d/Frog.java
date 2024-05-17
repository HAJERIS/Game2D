package com.example.game2d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Frog {
    private Bitmap bitmap;
    private Bitmap deadBitmap;
    private float x, y;
    private boolean alive;
    private boolean dying; // Flag to indicate if the frog is currently dying
    private int deadTime;
    private float velocityX, velocityY; // Velocity components

    public Frog(Bitmap bitmap, Bitmap deadBitmap, float x, float y) {

        // Initialize velocity components to random values
        this.velocityX = (float) (Math.random() * 5) - 2.5f; // Random velocity between -2.5 and 2.5
        this.velocityY = (float) (Math.random() * 5) - 2.5f; // Random velocity between -2.5 and 2.5

        // Scale the bitmaps to a smaller size
        int newWidth = bitmap.getWidth() / 2;
        int newHeight = bitmap.getHeight() / 2;
        this.bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        this.deadBitmap = Bitmap.createScaledBitmap(deadBitmap, newWidth, newHeight, false);
        this.x = x;
        this.y = y;
        this.alive = true;
        this.dying = false;
        this.deadTime = 60; // Initial dead time (frames before disappearing)
    }

    public void draw(Canvas canvas) {
        if (alive) {
            canvas.drawBitmap(bitmap, x, y, null);
        } else {
            canvas.drawBitmap(deadBitmap, x, y, null);
        }
    }

    public boolean intersects(float touchX, float touchY) {
        RectF rect = new RectF(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
        return rect.contains(touchX, touchY);
    }

    public void kill() {
        alive = false;
        deadTime = 60; // 60 frames before respawn
        // Change the bitmap to deadBitmap
    }

    public void decrementDeadTime() {
        if (deadTime > 0) {
            deadTime--;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public int getDeadTime() {
        return deadTime;
    }

    public void respawn(int screenWidth, int screenHeight, float heroX, float heroY, int heroWidth, int heroHeight) {
        alive = true;
        // Change the bitmap back to the alive bitmap
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap, this.bitmap.getWidth(), this.bitmap.getHeight(), false);

        // Define limitations for random coordinates
        int minX = 0;
        int maxX = screenWidth - bitmap.getWidth();
        int minY = 0;
        int maxY = screenHeight - bitmap.getHeight();

        // Set the initial position within the limited screen area, ensuring it's not too close to the hero
        do {
            x = (float) (Math.random() * (maxX - minX + 1) + minX); // Add 1 to include the max value
            y = (float) (Math.random() * (maxY - minY + 1) + minY); // Add 1 to include the max value
        } while (Math.abs(x - heroX) < heroWidth * 2 && Math.abs(y - heroY) < heroHeight * 2);
    }


    public void move(int screenWidth, int screenHeight) {
        // Update frog's position based on velocity
        x += velocityX;
        y += velocityY;

        // Check if frog hits screen boundaries and reverse velocity if needed
        if (x < 0 || x + bitmap.getWidth() > screenWidth) {
            velocityX *= -1; // Reverse horizontal velocity
        }
        if (y < 0 || y + bitmap.getHeight() > screenHeight) {
            velocityY *= -1; // Reverse vertical velocity
        }
    }

    public void onTouch() {
        if (alive && !dying) {
            // Start the dying animation
            dying = true;
            // Set a timer for 1 second before disappearing (adjust as needed)
            deadTime = 60; // Assuming 60 frames per second, this represents 1 second
        }
    }

    public void update() {
        if (dying) {
            // Decrement the timer
            deadTime--;
            // Check if the timer has expired
            if (deadTime <= 0) {
                // Remove the frog from the game
                alive = false;
                dying = false;
            }
        }
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
