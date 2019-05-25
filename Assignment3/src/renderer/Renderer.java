package renderer;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import renderer.Scene.Polygon;

public class Renderer extends GUI {
	
	Scene scene;
	float xRot = 0f;
	float yRot = 0f;
	float width=0;
	float height=0;
	float ImgMaxX = 0;
	float ImgMaxY = 0;
	float realmidX =0;
	float realmidY =0;
	
	float []ImageEdge;
	
	Vector3D scaleVector = new Vector3D(1.0f, 1.0f, 1.0f);
	Vector3D translateVector = new Vector3D(100f, 100f, 0f);
	Color DirectLight = new Color(0, 0, 0);
	Color ambientColor = new Color(0, 0, 0);
	Vector3D centerVector = new Vector3D(0f,0f,0f);

	@Override
	protected void onLoad(File file) {
		reset();
		Vector3D lightPos;
		float[] points = new float[9];
		int[] color = new int[3];
		float x, y, z;
		List<Polygon> polygons = new ArrayList<>();

		try {
			// make a reader
			BufferedReader br = new BufferedReader(new FileReader(file));

			String line = br.readLine();
			String[] Vectors = line.split(" ");

			x = Float.parseFloat(Vectors[0]);
			y = Float.parseFloat(Vectors[1]);
			z = Float.parseFloat(Vectors[2]);
			lightPos = new Vector3D(x, y, z);
			// read in each line of the file
			while ((line = br.readLine()) != null) {

				String[] values = line.split(" ");
				int c = 0;
				for (int i = 0; i < values.length; i++) {
					if (i < points.length) {
						points[i] = Float.parseFloat(values[i]);
					} else {

						color[c] = Integer.parseInt(values[i]);
						c++;
						
					}
				}
				ImgMaxX=Math.max(Math.max(ImgMaxX,points[0]),Math.max(points[3],points[6]));
				ImgMaxY=Math.max(Math.max(ImgMaxY,points[1]),Math.max(points[3],points[7]));
				
				Polygon poly = new Polygon(points, color);
				polygons.add(poly);

			}
			br.close();
		} catch (IOException e) {
			throw new RuntimeException("file reading failed.");
		}
		realmidX = 100f+ImgMaxX/2;
		realmidY = 100f+ImgMaxY/2;
		scene = new Scene(polygons, lightPos);
		render();
		/*
		 * This method should parse the given file into a Scene object, which
		 * you store and use to render an image.
		 */
	}

	private void reset() {
		xRot = 0f;
		yRot = 0f;
		scaleVector = new Vector3D(1.0f, 1.0f, 1.0f);
		translateVector = new Vector3D(150f, 150f, 0f);
		DirectLight = new Color(0, 0, 0);
		ambientColor = new Color(0, 0, 0);

	}

	@Override
	protected void onKeyPress(KeyEvent ev) {
		char move = ev.getKeyChar();

		// Translation
		if ((move == 'w' || move == 'W') && translateVector.y > 0) {
			translateVector = new Vector3D(translateVector.x, translateVector.y - 10f, translateVector.z);
		} else if ((move == 's' || move == 'S') && translateVector.y + height < CANVAS_HEIGHT)  {
			translateVector = new Vector3D(translateVector.x, translateVector.y + 10f, translateVector.z);
		} else if ((move == 'a' || move == 'A') && translateVector.x > 0) {
			translateVector = new Vector3D(translateVector.x - 10f, translateVector.y, translateVector.z);
		} else if ((move == 'd' || move == 'D') && translateVector.x + width < CANVAS_WIDTH ) {
			translateVector = new Vector3D(translateVector.x + 10f, translateVector.y, translateVector.z);
		}

		if (move == 'i' || move == 'I') {
			xRot -= 0.25;
		} else if (move == 'k' || move == 'K') {
			xRot += 0.25;
		} else if (move == 'j' || move == 'J') {
			yRot -= 0.25;
		} else if (move == 'l' || move == 'L') {
			yRot += 0.25;
		}
		
	  if ((move == 'e' || move == 'E')&(width>100f&&height>100f)) {
		  scaleVector = new Vector3D(scaleVector.x-0.1f, scaleVector.y-0.1f, scaleVector.z-0.1f);
     } else if ((move == 'q' || move == 'Q')&&(width<CANVAS_WIDTH&&height<CANVAS_HEIGHT)) {
    	 scaleVector = new Vector3D(scaleVector.x+0.1f, scaleVector.y+0.1f, scaleVector.z+0.1f);
     }
		/*
		 * } This method should be used to rotate the user's viewpoint.
		 */
	}

	@Override
	protected BufferedImage render() {

		Color[][] zbuffer = new Color[CANVAS_WIDTH][CANVAS_HEIGHT];
		float[][] zdepth = new float[CANVAS_WIDTH][CANVAS_HEIGHT];

		Color backgroundColor = new Color(200, 200, 200); 
		for (int i = 0; i < zbuffer.length; i++) {
			for (int j = 0; j < zbuffer[i].length; j++) {
				zbuffer[i][j] = backgroundColor;
			}
		}

		if (scene == null) {
			return convertBitmapToImage(zbuffer);
		}

		Scene rotatedScene = Pipeline.rotateScene(scene, xRot, yRot);
		Scene scaledScene = Pipeline.scaleScene(rotatedScene, scaleVector);
		
		do{
			ImageEdge = Pipeline.ImageEdge(scaledScene);
			width = ImageEdge[0]-ImageEdge[1];
			height = ImageEdge[2]-ImageEdge[3];
			
			scaledScene = Pipeline.scaleScene(scaledScene, scaleVector);
	
			if(width<80f|height<80f){
			scaleVector = new Vector3D(scaleVector.x+0.1f,scaleVector.y+0.1f,scaleVector.z+0.1f);
			}
			System.out.printf("width:%f height:%f\n",width,height);
			}while(width<80f||height<80f);
			
		//float midX = 100f+maxX/2;
		//float midY = 100f+maxY/2;
		//centerVector = new Vector3D(100f+(midX-realmidX),100f+100f+(midY-realmidY),0f);
		//Scene centerScene = Pipeline.translateScene(scaledScene,centerVector);
		Scene translatedScene = Pipeline.translateScene(scaledScene, translateVector);
		
		
		for (int i = 0; i < zdepth.length; i++) {
			for (int j = 0; j < zdepth[i].length; j++) {
				zdepth[i][j] = Float.POSITIVE_INFINITY;
			}
		}

		int[] dlRBG = getDirectLight();
		int[] alRBG = getAmbientLight();
		Color ambientColor = new Color(alRBG[0], alRBG[1], alRBG[2]);
		Color directColor = new Color(dlRBG[0], dlRBG[1], dlRBG[2]);
		Vector3D lightVector = translatedScene.getLight();
		List<Polygon> polygons = translatedScene.getPolygons();
		for (Polygon poly : polygons) {
			if (!Pipeline.isHidden(poly)) {
				Color shading = Pipeline.getShading(poly, lightVector, directColor, ambientColor);
				EdgeList edgeList = Pipeline.computeEdgeList(poly);
				Pipeline.computeZBuffer(zbuffer, zdepth, edgeList, shading);
				//System.out.print(poly.toString());
			}
		}

		return convertBitmapToImage(zbuffer);

		/*
		 * This method should put together the pieces of your renderer, as
		 * described in the lecture. This will involve calling each of the
		 * static method stubs in the Pipeline class, which you also need to
		 * fill in.
		 */
	}

	/**
	 * Converts a 2D array of Colors to a BufferedImage. Assumes that bitmap is
	 * indexed by column then row and has imageHeight rows and imageWidth
	 * columns. Note that image.setRGB requires x (col) and y (row) are given in
	 * that order.
	 */
	private BufferedImage convertBitmapToImage(Color[][] bitmap) {
		BufferedImage image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < CANVAS_WIDTH; x++) {
			for (int y = 0; y < CANVAS_HEIGHT; y++) {
				image.setRGB(x, y, bitmap[x][y].getRGB());
			}
		}
		return image;
	}

	public static void main(String[] args) {
		new Renderer();
	}
}

// code for comp261 assignments
