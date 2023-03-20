package com.mygdx.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.game.UI.MainUI;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;

public class AwesomeGame extends ApplicationAdapter {
	
	private static Stage stage;
	private static Group group;
	private static Table table;

	// copied code from http://acamara.es/blog/2012/02/keep-screen-aspect-ratio-with-different-resolutions-using-libgdx/
	private static final int VIRTUAL_WIDTH = 1920;
    private static final int VIRTUAL_HEIGHT = 1080;
    private static final float ASPECT_RATIO =
        (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
    private Camera camera;
    private Rectangle viewport;



	public static Table getTable(){
		return table;
	}

	public static Stage getStage(){
		return stage;
	}

	public static Group getGroup(){
		return group;
	}


	//Start of ApplicationListener life-cycle
	@Override
	public void create() {
		MainUI MainUI = new MainUI();
		stage = MainUI.getStage();
		table = MainUI.getTable();

		// copied code from http://acamara.es/blog/2012/02/keep-screen-aspect-ratio-with-different-resolutions-using-libgdx/
		camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);


	}
		
	@Override
	public void render () {
		// update camera,  copied code from http://acamara.es/blog/2012/02/keep-screen-aspect-ratio-with-different-resolutions-using-libgdx/
        camera.update();
        // camera.apply,  copied code from http://acamara.es/blog/2012/02/keep-screen-aspect-ratio-with-different-resolutions-using-libgdx/
		Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                          (int) viewport.width, (int) viewport.height);
		// clear previous frame
		ScreenUtils.clear(0, 0, 0, 1);
		stage.act();
		stage.draw();
		
		
	}
	@Override
    public void resize(int width, int height){
        // calculate new viewport, copied code from http://acamara.es/blog/2012/02/keep-screen-aspect-ratio-with-different-resolutions-using-libgdx/
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
		if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)height/(float)VIRTUAL_HEIGHT;
            crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
            crop.y = (height - VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
        }

        float w = (float)VIRTUAL_WIDTH*scale;
        float h = (float)VIRTUAL_HEIGHT*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
    }
	@Override
	public void dispose () {
		stage.dispose();
		
	}
}
