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
        renderPNG(512);
//        renderEqualizedPNG(512);
        Gdx.app.exit();
    }
    
    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
    }

    public void renderPNG(int size) {
		PixmapIO.PNG png = new PixmapIO.PNG();
		png.setFlipY(false);
		png.setCompression(7);
		
		Pixmap map = new Pixmap(size, size, Pixmap.Format.RGB888);
		ByteBuffer buf = map.getPixels();

		String[] noiseTypes = {"value", "perlin", "simplex", "foam", "honey"};
		int[] noiseTypeIndices = {Noise.VALUE_FRACTAL, Noise.PERLIN_FRACTAL, Noise.SIMPLEX_FRACTAL, Noise.FOAM_FRACTAL, Noise.HONEY_FRACTAL};
		String[] fractals = {"fbm", "billow", "ridged"};
		String noiseType, fractal;
		for (int nti = 0; nti < noiseTypeIndices.length; nti++) {
			noiseType = noiseTypes[nti];
			noise.setNoiseType(noiseTypeIndices[nti]);
			for (int fi = 0; fi < fractals.length; fi += 2) {
				fractal = fractals[fi];
				noise.setFractalType(fi);
				for (int octaves = 1; octaves <= 3; octaves++) {
					noise.setFractalOctaves(octaves);
					for (int frequency = 1; frequency <= 8; frequency *= 2) {
						noise.setFrequency(frequency);
						for (int seed = 0; seed < 16; seed++) {
							noise.setSeed(seed);
							for (int y = 0; y < size; y++) {
								for (int x = 0; x < size; x++) {
									byte b = (byte)((int)((noise.seamless2D(x, y, size, size, seed) + 1f) * 127.999));
									buf.put(b);
									buf.put(b);
									buf.put(b);
								}
							}
							try {
								png.write(Gdx.files.local(String.format("generated/%6$dx%6$d/%s/%s/%d-octaves-%d-frequency_%02d.png", noiseType, fractal, octaves, frequency, seed, size)), map);
							} catch (IOException e) {
								Gdx.app.error("png", e.getMessage());
							}
							buf.rewind();
						}
					}
				}
			}
		}
	}
    public void renderEqualizedPNG(int size) {
		PixmapIO.PNG png = new PixmapIO.PNG();
		png.setFlipY(false);
		png.setCompression(7);
		
		float[][] raw = new float[size][size];

		Pixmap map = new Pixmap(size, size, Pixmap.Format.RGB888);
		ByteBuffer buf = map.getPixels();

		String[] noiseTypes = {"value", "perlin", "simplex", "foam", "honey"};
		int[] noiseTypeIndices = {Noise.VALUE_FRACTAL, Noise.PERLIN_FRACTAL, Noise.SIMPLEX_FRACTAL, Noise.FOAM_FRACTAL, Noise.HONEY_FRACTAL};
		String[] fractals = {"fbm", "billow", "ridged"};
		String noiseType, fractal;
		for (int nti = 0; nti < noiseTypeIndices.length; nti++) {
			noiseType = noiseTypes[nti];
			noise.setNoiseType(noiseTypeIndices[nti]);
			for (int fi = 0; fi < fractals.length; fi += 2) {
				fractal = fractals[fi];
				noise.setFractalType(fi);
				for (int octaves = 1; octaves <= 3; octaves++) {
					noise.setFractalOctaves(octaves);
					for (int frequency = 1; frequency <= 8; frequency *= 2) {
						noise.setFrequency(frequency);
						for (int seed = 0; seed < 16; seed++) {
							noise.setSeed(seed);
							float min = Float.MAX_VALUE, max = -Float.MAX_VALUE, t;
							for (int y = 0; y < size; y++) {
								for (int x = 0; x < size; x++) {
									raw[y][x] = t = noise.seamless2D(x, y, size, size, seed);
									min = Math.min(min, t);
									max = Math.max(max, t);
								}
							}
							float range = 255.999f / (max - min);
							for (int y = 0; y < size; y++) {
								for (int x = 0; x < size; x++) {
									byte b = (byte)(range * (raw[y][x] - min));
									buf.put(b);
									buf.put(b);
									buf.put(b);
								}
							}
							try {
								png.write(Gdx.files.local(String.format("generated/%6$dx%6$dEq/%s/%s/%d-octaves-%d-frequency_%02d.png", noiseType, fractal, octaves, frequency, seed, size)), map);
							} catch (IOException e) {
								Gdx.app.error("png", e.getMessage());
							}
							buf.rewind();
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new Lwjgl3Application(new Generator(), getDefaultConfiguration());
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
