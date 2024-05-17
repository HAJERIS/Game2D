package com.example.game2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "GameView";
    private GameThread gameThread;
    private Bitmap heroBitmap;
    private Bitmap frogBitmap;
    private Bitmap frogDeadBitmap;
    private Hero hero;
    private List<Frog> frogs;
    private int screenWidth;
    private int screenHeight;
    private Joystick joystick;
    private Hearts hearts;
    private int numLives = 3;
    private BackgroundManager backgroundManager;// Initialize the hero with 3 lives



    private static final float HERO_SPEED = 0.01f; // Lower value for slower speed

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        hearts = new Hearts(getContext(), 0.2f);

        backgroundManager = new BackgroundManager(context, screenWidth, screenHeight);// Adjust the scaling factor as needed


        // Load and scale bitmaps
        heroBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        frogBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.frogfront);
        frogDeadBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.frogdead);

        // Scale down the hero bitmap
        int heroNewWidth = heroBitmap.getWidth() / 4; // Adjust the divisor to change the scale
        int heroNewHeight = heroBitmap.getHeight() / 4;
        heroBitmap = Bitmap.createScaledBitmap(heroBitmap, heroNewWidth, heroNewHeight, false);

// Scale down the frog bitmaps
        int frogNewWidth = frogBitmap.getWidth() / 2; // Adjust the divisor to change the scale
        int frogNewHeight = frogBitmap.getHeight() / 2;
        frogBitmap = Bitmap.createScaledBitmap(frogBitmap, frogNewWidth, frogNewHeight, false);


        int frogDeadNewWidth = frogDeadBitmap.getWidth() / 2; // Adjust the divisor to change the scale
        int frogDeadNewHeight = frogDeadBitmap.getHeight() / 2;
        frogDeadBitmap = Bitmap.createScaledBitmap(frogDeadBitmap, frogDeadNewWidth, frogDeadNewHeight, false);


        // Create game objects
        hero = new Hero(heroBitmap, 100, 100);
        frogs = new ArrayList<>();
        frogs.add(new Frog(frogBitmap, frogDeadBitmap, 200, 200));

        joystick = new Joystick(150, 150, 100, 50);

        gameThread = new GameThread(getHolder(), this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenWidth = getWidth(); // Initialize screenWidth
        screenHeight = getHeight(); // Initialize screenHeight

        backgroundManager = new BackgroundManager(getContext(), screenWidth, screenHeight);

        gameThread.setRunning(true);
        gameThread.start();
        Log.d(TAG, "Surface created and game thread started.");
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Handle surface changes if necessary
        Log.d(TAG, "Surface changed.");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
                Log.d(TAG, "Game thread stopped.");
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(TAG, "Error stopping game thread: " + e.getMessage());
            }
        }
    }

    public void update() {


        List<Frog> newFrogs = new ArrayList<>();
        for (Frog frog : frogs) {
            if (!frog.isAlive() && frog.getDeadTime() > 0) {
                frog.decrementDeadTime();
            } else if (!frog.isAlive() && frog.getDeadTime() <= 0) {
                frog.respawn(screenWidth, screenHeight, hero.getX(), hero.getY(), heroBitmap.getWidth(), heroBitmap.getHeight());
                newFrogs.add(new Frog(frogBitmap, frogDeadBitmap, (float) (Math.random() * screenWidth), (float) (Math.random() * screenHeight)));
                newFrogs.add(new Frog(frogBitmap, frogDeadBitmap, (float) (Math.random() * screenWidth), (float) (Math.random() * screenHeight)));
            }


        }

        for (Frog frog : frogs) {
            frog.move(screenWidth, screenHeight);
        }

        // Ensure the maximum number of frogs is five
        if (frogs.size() < 5) {
            frogs.addAll(newFrogs);
        }

        // Remove excess frogs if the total number exceeds five
        while (frogs.size() > 5) {
            frogs.remove(frogs.size() - 1);
        }

        if (joystick.isPressed()) {
            float xPercent = joystick.getXPercent();
            float yPercent = joystick.getYPercent();
            hero.move(hero.getX() + xPercent * HERO_SPEED * screenWidth, hero.getY() + yPercent * HERO_SPEED * screenHeight, screenWidth, screenHeight); // Adjust speed as necessary
        }

        Log.d(TAG, "Game updated.");
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);
            // Draw background using BackgroundManager
            backgroundManager.draw(canvas);
            hero.draw(canvas);
            for (Frog frog : frogs) {
                if (frog.isAlive()) {
                    frog.draw(canvas);
                }
            }
            joystick.draw(canvas);

            // Draw hearts based on the number of lives
            for (int i = 0; i < numLives; i++) {
                hearts.draw(canvas, screenWidth, screenHeight);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        // Check if the touch event intersects with any frogs
        for (Frog frog : frogs) {
            if (frog.intersects(touchX, touchY) && event.getAction() == MotionEvent.ACTION_DOWN) {
                frog.kill();
                Log.d(TAG, "Frog killed at: (" + touchX + ", " + touchY + ")");
                return true; // Stop processing the event if a frog is killed
            }
        }

        // Only update the joystick if the touch is within its bounds or if it's already pressed
        if (joystick.isTouchWithinBounds(event) || joystick.isPressed()) {
            joystick.update(event);
            Log.d(TAG, "Joystick updated.");
        }

        return true;
    }
}