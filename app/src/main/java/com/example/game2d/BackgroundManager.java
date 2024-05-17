package com.example.game2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class BackgroundManager {
    private Bitmap sandTileBitmap;
    private Bitmap waterTileBitmap;
    private int[][] backgroundMap; // Define your background layout here
    private static final int TILE_SIZE = 150; // Adjust as needed

    public BackgroundManager(Context context, int screenWidth, int screenHeight) {
        // Load tile bitmaps
        sandTileBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sandtile);
        waterTileBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.watertile);

        // Scale tile bitmaps
        sandTileBitmap = scaleBitmap(sandTileBitmap, TILE_SIZE);
        waterTileBitmap = scaleBitmap(waterTileBitmap, TILE_SIZE);

        // Initialize background map with the desired layout
        backgroundMap = new int[][] {
                {1, 1, 1, 1, 1, 1, 1, 1,1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 1,1, 1, 1, 0, 0, 0, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 1,1, 1, 1, 0, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 1, 1, 1,1, 1, 1, 0, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1,1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1,1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1,1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1,1, 1, 1, 1, 1, 1, 1, 1},
        };
    }

    public void draw(Canvas canvas) {
        int numRows = backgroundMap.length;
        int numCols = backgroundMap[0].length;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                // Determine which tile to draw based on the background map
                Bitmap tileBitmap;
                if (backgroundMap[row][col] == 0) {
                    tileBitmap = waterTileBitmap;
                } else {
                    tileBitmap = sandTileBitmap;
                }

                // Calculate the position to draw the tile
                int left = col * TILE_SIZE;
                int top = row * TILE_SIZE;

                // Draw the tile
                canvas.drawBitmap(tileBitmap, left, top, null);
            }
        }
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int size) {
        return Bitmap.createScaledBitmap(bitmap, size, size, false);
    }
}
