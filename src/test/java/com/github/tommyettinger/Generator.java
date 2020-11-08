package com.github.tommyettinger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import make.some.noise.Noise;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Should generate noise textures and output them as PNG files to images/ .
 */
public class Generator extends ApplicationAdapter {
	Noise noise;
    @Override
    public void create() {
    	noise = new Noise(1, 2f, Noise.SIMPLEX_FRACTAL, 2);
        Gdx.files.local("images").mkdirs();	       
        renderPNG();
        Gdx.app.exit();
    }
    
    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
    }

    public void renderPNG() {
        PixmapIO.PNG png = new PixmapIO.PNG();
        png.setFlipY(false);
        png.setCompression(7);
        String noiseType = "simplex", fractal = "fbm";
        int octaves = 2, size = 512, seed = 1;
        float frequency = 2f;
        Pixmap map = new Pixmap(size, size, Pixmap.Format.RGB888);
		ByteBuffer buf = map.getPixels();
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				byte b = (byte)((int)((noise.seamless2D(x, y, size, size, seed) + 1f) * 127.999));
				buf.put(b);
				buf.put(b);
				buf.put(b);
			}
		}
		try {
			png.write(Gdx.files.local("images/" + noiseType + "-" + fractal + "-" + octaves + "-octaves-" + frequency + "-frequency-" + size + "x" + size + "_" + seed + ".png"), map);
		} catch (IOException e) {
			Gdx.app.error("png", e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		createApplication();
	}

	private static Lwjgl3Application createApplication() {
		return new Lwjgl3Application(new StillImageDemo(), getDefaultConfiguration());
	}

	private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("Noisefarm Generator");
		configuration.setWindowedMode(256, 256);
		configuration.useVsync(true);
		configuration.setIdleFPS(20);
		return configuration;
	}

}
