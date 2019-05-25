import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Node represents an intersection in the road graph. It stores its ID and its
 * location, as well as all the segments that it connects to. It knows how to
 * draw itself, and has an informative toString method.
 * 
 * @author tony
 */
public class Node implements Comparable<Node>{

	public final int nodeID;
	public final Location location;
	public final Collection<Segment> segments;

	
	public boolean visited;
	public Node pathfrom;
	public double total_cost;
	public Set<Node> neighbors;
	public List<Node> Children;
	public double pathlength;
	public double weight;
	public int count;
	public int reachback;
	public double time;
	
	public static Node root;
	
	public String conditions;

	public Node(int nodeID, double lat, double lon) {
		this.nodeID = nodeID;
		this.location = Location.newFromLatLon(lat, lon);
		this.segments = new HashSet<Segment>();
		
		this.visited = false;
		this.pathfrom = null;
		this.total_cost = 0;
		this.pathlength = 0;
		this.weight = 0;
		this.time = 0;
		this.neighbors = new HashSet<Node>();
		
		this.count= Integer.MAX_VALUE;
		this.reachback = 0;
		Node.root = this;
	}

	public void addSegment(Segment seg) {
		segments.add(seg);
	}
	
	public void addnNeighbors(Node nei) {
		neighbors.add(nei);
	}

	public void draw(Graphics g, Dimension area, Location origin, double scale) {
		Point p = location.asPoint(origin, scale);

		// for efficiency, don't render nodes that are off-screen.
		if (p.x < 0 || p.x > area.width || p.y < 0 || p.y > area.height)
			return;

		int size = (int) (Mapper.NODE_GRADIENT * Math.log(scale) + Mapper.NODE_INTERCEPT);
		g.fillRect(p.x - size / 2, p.y - size / 2, size, size);
	}
	
	public String toString() {
		Set<String> edges = new HashSet<String>();
		for (Segment s : segments) {
			if (!edges.contains(s.road.name))
				edges.add(s.road.name);
		}

		String str = "ID: " + nodeID + "  loc: " + location + "\nroads: ";
		for (String e : edges) {
			str += e + ", ";
		}
		return str.substring(0, str.length() - 2);
	}


	public double disbetween(Node nei){
		for(Segment seg : segments){
			if(seg.contain(nei)){ // to check which string contains the given node
				return seg.length;
			}
		}
		return 0;
	}
	
	public double timebetween(Node nei){
		for(Segment seg : segments){
			if(seg.contain(nei)){ // to check which string contains the given node
				return seg.length/seg.getSpeed();
			}
		}
		return 0;
	}
	
	
	public Segment getSegment(Node node){
		for(Segment seg : segments){
			if(seg.contain(node)){ 
				return seg;
			}
			
		}
		return null;
	}
	
	
	public void resets(){
		this.visited = false;
		this.pathfrom = null;
		this.total_cost = 0;
		this.pathlength = 0;
		this.weight = 0;
		this.time = 0;
		this.count= Integer.MAX_VALUE;
		this.reachback = 0;
		//this.neighbors = new HashSet<Node>();
		
	}
  @Override
    public int compareTo(Node other) {
        
        if (this.total_cost > other.total_cost) {
            return 1;
        } else if (this.total_cost <other.total_cost) {
            return -1;
        } else {
            return 0;
        }
    }

public double getCondition(String condition) {
	
	if(condition.equals("FindFastest")){return time;}
	else{return pathlength;}
	
}
}

// code for COMP261 assignments