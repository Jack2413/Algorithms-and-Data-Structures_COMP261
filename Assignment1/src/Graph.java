import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
//import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import java.util.Set;
//import java.util.Map.Entry;

public class Graph {
	Color color;
	Set<Node> nodes = new HashSet<Node>();
	Set<Edges> edges = new HashSet<Edges>();
	Set<Polygon> polygons = new HashSet<Polygon>();

	static Map<Integer, Node> intersection = new HashMap<Integer, Node>();
	static Collection<Edges> segments;

	Map<Integer, Road> roads = new HashMap<Integer, Road>();
	Map<String, Integer> RoadNames = new HashMap<String, Integer>();
	Map<Integer, Edges> SelectEdges = new HashMap<Integer, Edges>();

	Set<Edges> SelRoad = new HashSet<Edges>();
	Tries Tri = new Tries();
	int num = 100;
	// private static Color color;

	public double scale = 100;

	public double latitude = -36.847622;
	public double longitude = 174.763444;
	public Location origin = Location.newFromLatLon(latitude, longitude);

	Dimension d = new Dimension();

	Graphics g;

	public void onload(File nodesfile, File roadsfile, File segmentsfile, File polygonsfile) {

		File file = nodesfile;

		String line;
		BufferedReader data = null;

		try { // load nodes

			data = new BufferedReader(new FileReader(file));

			// line = data.readLine();
			while ((line = data.readLine()) != null) {

				String[] values = line.split("\t");
				int ID = Integer.parseInt(values[0]);
				double Latitude = Double.parseDouble(values[1]);
				double Longitude = Double.parseDouble(values[2]);

				Node node = new Node(ID, Latitude, Longitude);
				nodes.add(node);

				intersection.put(ID, node);
			}
			System.out.println(data);// for test

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);

		}

		file = roadsfile; // load rodas

		try {

			data = new BufferedReader(new FileReader(file));

			line = data.readLine();

			while ((line = data.readLine()) != null) {

				String[] values = line.split("\t");

				int RoadId = Integer.parseInt(values[0]);
				int Type = Integer.parseInt(values[1]);
				String Lable = (values[2]);
				String City = (values[3]);
				int Oneway = Integer.parseInt(values[4]);
				int Speed_limit = Integer.parseInt(values[5]);
				int Road_class = Integer.parseInt(values[6]);
				int notforcar = Integer.parseInt(values[7]);
				int notforpede = Integer.parseInt(values[8]);
				int notforbicy = Integer.parseInt(values[9]);

				Road road = new Road(RoadId, Type, Lable, City, Oneway, Speed_limit, Road_class, notforcar, notforpede,
						notforbicy);
				roads.put(RoadId, road);

				RoadNames.put(Lable, RoadId);

				Tri.add(Lable);
				// System.out.println(data); //for test
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		file = segmentsfile;

		try {

			data = new BufferedReader(new FileReader(file));

			line = data.readLine();

			while ((line = data.readLine()) != null) {
				// System.out.println(line);

				String[] values = line.split("\t");
				int RoadID = Integer.parseInt(values[0]);
				double length = Double.parseDouble(values[1]);
				int nodeID1 = Integer.parseInt(values[2]);
				int nodeID2 = Integer.parseInt(values[3]);

				List<Double> coords = new ArrayList<Double>();

				for (int i = 4; i < values.length; i++) {

					coords.add(Double.parseDouble(values[i]));
				}

				Edges edge = new Edges(RoadID, length, nodeID1, nodeID2, coords);

				edges.add(edge);

				// segments.add(edge);

				intersection.get(nodeID1).getConnectRoads().add(roads.get(RoadID));
				// add the Roads in to node class
				// System.out.println("nodeid2 = " + nodeID2);
				intersection.get(nodeID2).getConnectRoads().add(roads.get(RoadID));
				// therefore every intersections knows every roads around him
				// System.out.println(data);//for test
				SelectEdges.put(RoadID, edge); // put all the edges in the a map
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);

		}

		file = polygonsfile;
		int count = 0;
		try {

			data = new BufferedReader(new FileReader(file));

			while ((line = data.readLine()) != null) {
				if (line.startsWith("[")) {
					line = data.readLine();
				}
				// System.out.println(line);

				if (line.startsWith("T")) {
					String type = line.substring(7, 8);
					if (!type.equals("a") && !type.equals("b") && !type.equals("c") && !type.equals("e")) {
						num = Integer.parseInt(line.substring(7, 8));
					}
				}
				// System.out.println(count++);
				// System.out.println("line:" + line);
				// System.out.println("num:" + num);

				if (num == 100) {
					color = null;

				} else if (num == 2 || num == 3 || num == 4) {
					color = new Color(0, 255, 255);
				} else if (num == 0) {
					color = new Color(210, 210, 210);
				} else if (num == 1) {
					color = new Color(128, 194, 105);
				} else if (num == 5) {
					color = new Color(106, 57, 6);
				}

				// System.out.println(color);
				// System.out.println();

				while (!line.startsWith("D")) {
					line = data.readLine();
				}

				String[] values = line.substring(6).replace("(", "").replace(")", "").split(",");
				List<Location> loc = new ArrayList<Location>();

				for (int i = 0; i < values.length; i++) {
					double lat = Double.parseDouble(values[i]);
					double lon = Double.parseDouble(values[i]);
					loc.add(Location.newFromLatLon(lat, lon));
				}

				Polygon poly = new Polygon(color, loc);
				polygons.add(poly);

				data.readLine();
				data.readLine();

				List<Double> coords = new ArrayList<Double>();

				for (int i = 4; i < values.length; i++) {

					coords.add(Double.parseDouble(values[i]));
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);

		}

	}

	public void draw(Graphics g) {

		this.g = g;

		origin = Location.newFromLatLon(latitude, longitude);
		// Point MidPoint = new Point();

		// MidPoint.x = (int) d.getDrawingAreaDimension().getWidth();

		// MidPoint.x = (int) (d.getWidth()/2);
		// MidPoint.y = (int) (d.getHeight()/2);
		// System.out.println(MidPoint.x);
		// System.out.println(MidPoint.y);
		// Location MidLocation = Location.newFromPoint(MidPoint, origin,
		// scale);
		// origin=MidLocation;

		g.setColor(Color.blue);
		int size = (int) scale / 20; // node size
		int x;
		int y;

		for (Node n : nodes) {

			Location newLOC = Location.newFromLatLon(n.getLatitude(), n.getLongitude());

			x = newLOC.asPoint(origin, scale).x; // Convert location into point
													// --> into x,y
			y = newLOC.asPoint(origin, scale).y;
			g.fillOval(x - (size / 2), y - (size / 2), size, size);

		}

		g.setColor(Color.black);
		int x1;
		int y1;
		int x2;
		int y2;

		for (Edges e : edges) {

			for (int i = 0; i < e.getCoords().size() - 3; i += 2) {

				Location newLOC = Location.newFromLatLon(e.getCoords().get(i), e.getCoords().get(i + 1));

				x1 = newLOC.asPoint(origin, scale).x; // Convert location into
														// point --> into x,y

				y1 = newLOC.asPoint(origin, scale).y;

				Location newLOC2 = Location.newFromLatLon(e.getCoords().get(i + 2), e.getCoords().get(i + 3));

				x2 = newLOC2.asPoint(origin, scale).x;
				y2 = newLOC2.asPoint(origin, scale).y;

				// g.setColor(Color.black);
				g.drawLine(x1, y1, x2, y2);

				// g.setColor(Color.red);
				// g.fillOval(origin.asPoint(origin,
				// scale).x,origin.asPoint(origin, scale).y, 10, 10);
				// g.fillOval(MidPoint.x,MidPoint.y,50,50);

			}
		}
	}

	public void DrawSelectNode(Location Nloc) {

		int x;
		int y;

		g.setColor(Color.red);
		int size = (int) scale / 15;
		x = Nloc.asPoint(origin, scale).x; // Convert location into point -->
											// into x,y
		y = Nloc.asPoint(origin, scale).y;
		g.fillOval(x - (size / 2), y - (size / 2), size, size);

	}

	public void DrawSelectRoad(Set<Edges> SelEdgs) {

		// System.out.println(SelEdgs+"\n");

		g.setColor(Color.RED);

		int x1;
		int y1;
		int x2;
		int y2;

		for (Edges e : SelEdgs) {

			for (int i = 0; i < e.getCoords().size() - 3; i += 2) {

				Location newLOC = Location.newFromLatLon(e.getCoords().get(i), e.getCoords().get(i + 1));

				x1 = newLOC.asPoint(origin, scale).x; // Convert location into
														// point --> into x,y

				y1 = newLOC.asPoint(origin, scale).y;

				Location newLOC2 = Location.newFromLatLon(e.getCoords().get(i + 2), e.getCoords().get(i + 3));

				x2 = newLOC2.asPoint(origin, scale).x;
				y2 = newLOC2.asPoint(origin, scale).y;

				g.drawLine(x1, y1, x2, y2);

			}
		}
	}

	public Set<Edges> TriesSerch(String Roadname) {
		SelRoad.clear();

		HashSet<String> AllRoadName = new HashSet<String>();
		AllRoadName = Tri.getall(Roadname);
		// System.out.print(AllRoadName + "\n");

		for (String roadName : AllRoadName) { // Transform Road name --> road ID
												// --> edges
			if (SelectEdges.get(RoadNames.get(roadName)) != null) {
				SelRoad.add(SelectEdges.get(RoadNames.get(roadName)));
			}

		}

		return SelRoad; // return the selected road then print.

		// System.out.println(SelRoad);
	}

	public void drawPoly(Graphics g) {

		for (Polygon p : polygons) {

			g.setColor(p.getColor());

			int x[] = new int[p.getData().size()];
			int y[] = new int[p.getData().size()];
			// System.out.println(p.getData().size());
			int i = 0;
			for (Location L : p.getData()) {
				// System.out.println(i);
				x[i] = L.asPoint(origin, scale).x;
				y[i] = L.asPoint(origin, scale).y;
				i++;
			}
			g.fillPolygon(x, y, p.getData().size());
		}

	}

}
