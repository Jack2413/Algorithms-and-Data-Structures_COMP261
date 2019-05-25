import java.util.Collection;
import java.util.HashSet;

/**
 * Road represents ... a road ... in our graph, which is some metadata and a
 * collection of Segments. We have lots of information about Roads, but don't
 * use much of it.
 * 
 * @author tony
 */
public class Road {
	public final int roadID;
	public final String name, city;
	public final Collection<Segment> components;
	public final boolean oneway;
	public final int speed;
	public double length;
	
	public int notforcar;
	public int notforpede;
	public int notforbicy;
	
	public Road(int roadID, int type, String label, String city, int oneway,
			int speed, int roadclass, int notforcar, int notforpede,
			int notforbicy) {
		this.roadID = roadID;
		this.city = city;
		this.name = label;
		this.components = new HashSet<Segment>();
		this.speed = speed;
		this.length = 0;
		
		this.notforbicy=notforbicy;
		this.notforcar=notforcar;
		this.notforpede=notforpede;
		
		if(oneway==1){this.oneway = true;}
		else{this.oneway = false;}
		
	}

	public void addSegment(Segment seg) {
		components.add(seg);
	}
}


// code for COMP261 assignments