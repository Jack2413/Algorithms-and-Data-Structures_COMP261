package renderer;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import renderer.Scene.Polygon;

/**
 * The Pipeline class has method stubs for all the major components of the
 * rendering pipeline, for you to fill in.
 * 
 * Some of these methods can get quite long, in which case you should strongly
 * consider moving them out into their own file. You'll need to update the
 * imports in the test suite if you do.
 */
public class Pipeline {

	/**
	 * Returns true if the given polygon is facing away from the camera (and so
	 * should be hidden), and false otherwise.
	 */
	public static boolean isHidden(Polygon poly) {
		// TODO fill this in.
		Vector3D A = poly.vertices[1].minus(poly.vertices[0]);
		Vector3D B = poly.vertices[2].minus(poly.vertices[1]);
		Vector3D normal = A.crossProduct(B);
		return normal.z > 0;

	}

	/**
	 * Computes the colour of a polygon on the screen, once the lights, their
	 * angles relative to the polygon's face, and the reflectance of the polygon
	 * have been accounted for.
	 * 
	 * @param lightDirection
	 *            The Vector3D pointing to the directional light read in from
	 *            the file.
	 * @param lightColor
	 *            The color of that directional light.
	 * @param ambientLight
	 *            The ambient light in the scene, i.e. light that doesn't depend
	 *            on the direction.
	 */
	public static Color getShading(Polygon poly, Vector3D lightDirection, Color lightColor, Color ambientLight) {
		Vector3D A = poly.vertices[1].minus(poly.vertices[0]);
		Vector3D B = poly.vertices[2].minus(poly.vertices[1]);
		Vector3D normal = A.crossProduct(B);

		Vector3D UnitNormal = normal.unitVector();

		float cos = UnitNormal.cosTheta(lightDirection);
		// System.out.printf("Cos: %f\n",cos);
		// System.out.printf("%f\n",((ambientLight.getRed()+lightColor.getRed())*cos));
		int newR, newG, newB;
		if (cos > 0) {
			newR = (int) ((ambientLight.getRed() + lightColor.getRed() * cos) * (poly.reflectance.getRed() / 255.0f));
			newG = (int) ((ambientLight.getGreen() + lightColor.getGreen() * cos)
					* (poly.reflectance.getGreen() / 255.0f));
			newB = (int) ((ambientLight.getBlue() + lightColor.getBlue() * cos)
					* (poly.reflectance.getBlue() / 255.0f));
		} else {
			newR = (int) (ambientLight.getRed() * (poly.reflectance.getRed() / 255.0f));
			newG = (int) (ambientLight.getGreen() * (poly.reflectance.getGreen() / 255.0f));
			newB = (int) (ambientLight.getBlue() * (poly.reflectance.getBlue() / 255.0f));
		}

		if (newR > 255)
			newR = 255;
		if (newG > 255)
			newG = 255;
		if (newB > 255)
			newB = 255;

		if (newR < 0)
			newR = 0;
		if (newG < 0)
			newG = 0;
		if (newB < 0)
			newB = 0;

		System.out.printf("RGB: %d %d %d\n", newR, newG, newB);
		return new Color(newR, newG, newB);

	}

	/**
	 * This method should rotate the polygons and light such that the viewer is
	 * looking down the Z-axis. The idea is that it returns an entirely new
	 * Scene object, filled with new Polygons, that have been rotated.
	 * 
	 * @param scene
	 *            The original Scene.
	 * @param xRot
	 *            An angle describing the viewer's rotation in the YZ-plane (i.e
	 *            around the X-axis).
	 * @param yRot
	 *            An angle describing the viewer's rotation in the XZ-plane (i.e
	 *            around the Y-axis).
	 * @return A new Scene where all the polygons and the light source have been
	 *         rotated accordingly.
	 */
	public static Scene rotateScene(Scene scene, float xRot, float yRot) {

		Transform rotationMatrix = Transform.newXRotation(xRot).compose(Transform.newYRotation(yRot));
		return processWithMatrix(scene, rotationMatrix);

	}

	private static Scene processWithMatrix(Scene scene, Transform rotationMatrix) {

		Vector3D newLightPos = rotationMatrix.multiply(scene.getLight());
		List<Polygon> newPolygons = new ArrayList<>();
		for (Polygon poly : scene.getPolygons()) {
			Vector3D[] Vectors = new Vector3D[3];
			for (int i = 0; i < Vectors.length; i++) { // for every polygon
														// doing the transfrom
				Vectors[i] = rotationMatrix.multiply(poly.vertices[i]);
			}
			Polygon newPolygon = new Polygon(Vectors[0], Vectors[1], Vectors[2], poly.reflectance);
			newPolygons.add(newPolygon);
		}

		return new Scene(newPolygons, newLightPos);
	}

	/**
	 * This should translate the scene by the appropriate amount.
	 * 
	 * @param scene
	 * @return
	 */
	public static Scene translateScene(Scene scene, Vector3D vector) {
		Transform translateMatrix = Transform.newTranslation(vector);
		return processWithMatrix(scene, translateMatrix);
	}

	/**
	 * This should scale the scene.
	 * 
	 * @param scene
	 * @return
	 */
	public static Scene scaleScene(Scene scene, Vector3D vector) {

		Transform sceneMatrix = Transform.newScale(vector);
		return processWithMatrix(scene, sceneMatrix);
	}

	/**
	 * Computes the edgelist of a single provided polygon, as per the lecture
	 * slides.
	 */
	public static EdgeList computeEdgeList(Polygon poly) {

		List<Float> leftx = new ArrayList<>();
		List<Float> leftz = new ArrayList<>();
		List<Float> rightx = new ArrayList<>();
		List<Float> rightz = new ArrayList<>();

		int k = findminY(poly);
		int j = k + 1;
		j = j > 2 ? 0 : j;
		for (int rotat = 0; rotat < 3; rotat++) {

			Vector3D a = poly.vertices[k];
			Vector3D b = poly.vertices[j];
			k++;
			j++;
			k = k > 2 ? 0 : k;
			j = j > 2 ? 0 : j;

			// System.out.printf("i: %d j: %d\n", k, j);

			float slopeX = (b.x - a.x) / (b.y - a.y);
			float slopeZ = (b.z - a.z) / (b.y - a.y);

			float x = a.x;
			float z = a.z;
			int y = (int) a.y;
			int by = (int) b.y;

			// System.out.printf("%d %d\n",y,by);
			if (a.y < b.y) {
				while (y <= by) {

					leftx.add(x);
					leftz.add(z);

					x = x + slopeX;
					z = z + slopeZ;
					y++;
				}
			} else {
				// System.out.println(by);
				while (y >= by) {

					rightx.add(x);
					rightz.add(z);

					x = x - slopeX;
					z = z - slopeZ;
					y--;
					// System.out.println(y);
				}
			}
		}
		System.out.printf("leftxSize:%d leftzSize:%d rightxSize:%drightzSize:%d\n", leftx.size(), leftz.size(),
				rightx.size(), rightz.size());
		int startY = (int) Math.min(poly.vertices[0].y, Math.min(poly.vertices[1].y, poly.vertices[2].y));
		int Ydepth = Math.min(leftx.size(), rightx.size());
		EdgeList edgeList = new EdgeList(startY, startY + Ydepth);
		for (int i = 0; i < Ydepth; i++) {
			edgeList.addRow(startY + i, leftx.get(i), rightx.get(Ydepth - 1 - i), leftz.get(i),
					rightz.get(Ydepth - 1 - i));
			// System.out.printf("%f %f %f %f\n",leftx.get(i), rightx.get(Ydepth
			// - 1 - i), leftz.get(i),
			// rightz.get(Ydepth - 1 - i));
		}

		// System.out.print(edgeList);
		return edgeList;

	}

	/**
	 * Fills a zbuffer with the contents of a single edge list according to the
	 * lecture slides.
	 * 
	 * The idea here is to make zbuffer and zdepth arrays in your main loop, and
	 * pass them into the method to be modified.
	 * 
	 * @param zbuffer
	 *            A double array of colours representing the Color at each pixel
	 *            so far.
	 * @param zdepth
	 *            A double array of floats storing the z-value of each pixel
	 *            that has been coloured in so far.
	 * @param polyEdgeList
	 *            The edgelist of the polygon to add into the zbuffer.
	 * @param polyColor
	 *            The colour of the polygon to add into the zbuffer.
	 */
	public static void computeZBuffer(Color[][] zbuffer, float[][] zdepth, EdgeList polyEdgeList, Color polyColor) {
		// System.out.print(polyEdgeList.getStartY());

		for (int y = polyEdgeList.getStartY(); y < polyEdgeList.getEndY(); y++) {

			float slope = (polyEdgeList.getRightZ(y) - polyEdgeList.getLeftZ(y))
					/ (polyEdgeList.getRightX(y) - polyEdgeList.getLeftX(y));

			float z = polyEdgeList.getLeftZ(y);
			int x = (int) polyEdgeList.getLeftX(y);
			int rightx = (int) (polyEdgeList.getRightX(y));
			while (x < rightx) {
				if (x < zbuffer[0].length && y < zbuffer.length && y >= 0 && x >= 0) {
					if (z < zdepth[x][Math.abs(y)]) {
						zbuffer[x][Math.abs(y)] = polyColor;
						zdepth[x][Math.abs(y)] = z;
					}
				}
				z = z + slope;
				x++;
			}
		}
	}

	public static float findmaxX(Scene scene) {
		float maxX = 0;
		for (Polygon poly : scene.getPolygons()) {
			Vector3D[] Vectors = poly.vertices;
			for (int i = 0; i < Vectors.length; i++) {
				if (Vectors[i].x > maxX)
					maxX = (int) Vectors[i].x;
			}
		}
		return maxX;
	}

	public static float findmaxY(Scene scene) {
		float maxY = 0;
		for (Polygon poly : scene.getPolygons()) {
			Vector3D[] Vectors = poly.vertices;
			for (int i = 0; i < Vectors.length; i++) {
				if (Vectors[i].y > maxY)
					maxY = (int) Vectors[i].y;
			}
		}
		return maxY;
	}

	public static float[] ImageEdge(Scene scene) {

		float maxX = Float.MIN_VALUE;
		float minX = Float.MAX_VALUE;
		float maxY = Float.MIN_VALUE;
		float minY = Float.MAX_VALUE;
		float[] ImageEdge = new float[4];

		for (Polygon poly : scene.getPolygons()) {
			Vector3D[] Vectors = poly.vertices;
			for (int i = 0; i < Vectors.length; i++) {
				if (Vectors[i].y > maxY)
					maxY = Vectors[i].y;
				if (Vectors[i].x > maxX) {
					maxX = Vectors[i].x;
				}
				if (Vectors[i].y < minY)
					minY = Vectors[i].y;
				if (Vectors[i].x < minX) {
					minX = Vectors[i].x;
				}
			}
		}
		ImageEdge[0] = maxX;
		ImageEdge[1] = minX;
		ImageEdge[2] = maxY;
		ImageEdge[3] = minY;
		return ImageEdge;
	}

	public static int findminY(Polygon poly) {

		if (poly.vertices[0].y < poly.vertices[1].y && poly.vertices[0].y < poly.vertices[2].y) {
			return 0;
		} else if (poly.vertices[1].y < poly.vertices[0].y && poly.vertices[1].y < poly.vertices[2].y) {
			return 1;
		} else {
			return 2;
		}
	}
}

// code for comp261 assignments
