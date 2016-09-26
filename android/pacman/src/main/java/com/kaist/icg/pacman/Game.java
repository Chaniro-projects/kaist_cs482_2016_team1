package com.kaist.icg.pacman;

import android.opengl.GLES20;
import android.os.SystemClock;

import com.kaist.icg.pacman.graphic.Mesh;
import com.kaist.icg.pacman.graphic.android.PacManActivity;
import com.kaist.icg.pacman.graphic.android.PacManGLSurfaceView;
import com.kaist.icg.pacman.manager.InputManager;
import com.kaist.icg.pacman.manager.LevelManager;

/**
 * Main game class
 */
public class Game {

    private InputManager inputManager;
    private LevelManager levelManager;

    private long lastUpdate;
    private long elapsedTime;
    private PacManGLSurfaceView glView;

    //FPS stuff
    private long lastFPSupdate;
    private int nbFrameSinceLastFPSupdate;

    //Test 3D mesh
    private Mesh suzanne;


    /**
     * Load assets etc...
     * @param mGLView
     */
    public  Game(PacManGLSurfaceView mGLView) {
        this.glView = mGLView;
        this.glView.setGame(this);

        inputManager = InputManager.getInstance();
        levelManager = LevelManager.getInstance();
    }

    public void init() {
        suzanne = new Mesh("suzanne.obj");
    }

    /**
     * Called every frame
     */
    public void loop() {
        onRender();
        onUpdate();
    }

    /**
     * Update objects positions, player input....
     */
    private void onUpdate() {
        //Compute time from last onUpdate
        elapsedTime = SystemClock.uptimeMillis() - lastUpdate;

        levelManager.update(elapsedTime);
        inputManager.update(elapsedTime);

        nbFrameSinceLastFPSupdate++;
        if(SystemClock.uptimeMillis() - lastFPSupdate > 1000) {
            //Compute FPS: number_frame_drew / (elapsed_time / 1000)
            ((PacManActivity) glView.getContext()).setLogText(
                    nbFrameSinceLastFPSupdate / ((SystemClock.uptimeMillis() - lastFPSupdate) / 1000) + " fps");

            nbFrameSinceLastFPSupdate = 0;
            lastFPSupdate = SystemClock.uptimeMillis();
        }

        lastUpdate = SystemClock.uptimeMillis();
    }

    /**
     * Draw all the scene
     */
    private void onRender() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        suzanne.draw(glView.getRenderer().getProjMatrix(), glView.getRenderer().getViewMatrix());
    }

    public void onPause() {
        inputManager.onPause();
    }

    public void onResume() {
        inputManager.onResume();
    }

    /**
     * Clean memory
     */
    public void cleanup() {

    }
}
