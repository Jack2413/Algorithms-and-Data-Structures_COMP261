import java.awt.Graphics;
import java.awt.Point;


/**
 * A Segment is the most interesting class making up our graph, and represents
 * an edge between two Nodes. It knows the Road it belongs to as well as the
 * Nodes it joins, and contains a series of Locations that make up the length of
 * the Segment and can be used to render it.
 * 
 * @author tony
 */
public class Segment {

	public final Road road;
	public final Node start, end;
	public final double length;
	public final Location[] points;
	private boolean oneway;
	private int speed;
	private int notforcar;
	private int notforbic;
	private int notforped;
	public Segment(Graph graph, int roadID, double length, int node1ID,
			int node2ID, double[] coords) {

		this.road = graph.roads.get(roadID);
		this.start = graph.nodes.get(node1ID);
		this.end = graph.nodes.get(node2ID);
		this.length = length;

		points = new Location[coords.length / 2];
		
		for (int i = 0; i < points.length; i++) {
			points[i] = Location
					.newFromLatLon(coords[2 * i], coords[2 * i + 1]);
		}
		
		
		this.road.addSegment(this);
		this.start.addSegment(this);
		this.end.addSegment(this);
		
		this.notforcar=this.road.notforcar;
		this.notforbic=this.road.notforbicy;
		this.notforped=this.road.notforpede;
		
		this.oneway = this.road.oneway; //for start to end is one way
		this.speed = this.road.speed;
		
		start.addnNeighbors(end); //add neighbors to each other
		end.addnNeighbors(start);
	}
	
	public boolean considerVehicle(String vehicle) {
		if(vehicle.equals("car")&&notforcar==1){return false;}
		else if(vehicle.equals("bicycle")&&notforbic==1){return false;}
		else if(vehicle.equals("pedestrians")&&notforped==1){return false;}
		return true;
	}


	public boolean contain(Node nei){
		return (nei==start||nei==end);
		
	}

	public void draw(Graphics g, Location origin, double scale) {
		for (int i = 1; i < points.length; i++) {
			Point p = points[i - 1].asPoint(origin, scale);
			Point q = points[i].asPoint(origin, scale);
			g.drawLine(p.x, p.y, q.x, q.y);
		}
	}
	public  String toString() {
		String str = "\n" + this.road.name+":  " + String.format( "%.2f",this.length) + "km";
		return str;
	}

	public boolean CanTravelTo(Node targetnode) {
		
		return (!oneway || targetnode==this.end);
	}

	public boolean getOneway(){ 
		return this.oneway;
	}

	public int getSpeed() {
		return speed;
	}


	
	
	
	
	
	
}

// code for COMP261 assignments