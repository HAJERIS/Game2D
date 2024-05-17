package com.example.game2d;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Hearts {
    private Bitmap heartBitmap;
    private int heartSpacing;
    private float scaleFactor;

    public Hearts(Context context, float scaleFactor) {
        // Load heart bitmap
        heartBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
        // Calculate spacing between hearts
        heartSpacing = 10;
        // Set the scale factor
        this.scaleFactor = scaleFactor;
        // Scale the heart bitmap
        heartBitmap = Bitmap.createScaledBitmap(heartBitmap, (int) (heartBitmap.getWidth() * scaleFactor), (int) (heartBitmap.getHeight() * scaleFactor), false);
    }

    public void draw(Canvas canvas, int screenWidth, int screenHeight) {
        // Calculate total width of hearts
        int totalWidth = heartBitmap.getWidth() * 3 + heartSpacing * 2;

        // Calculate starting X coordinate to center hearts horizontally
        int startX = (screenWidth - totalWidth) / 2;

        // Draw hearts
        for (int i = 0; i < 3; i++) {
            int heartX = startX + (heartBitmap.getWidth() + heartSpacing) * i;
            canvas.drawBitmap(heartBitmap, heartX, 0, null);
        }
    }
}


