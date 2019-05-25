import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is the main class for the mapping program. It extends the GUI abstract
 * class and implements all the methods necessary, as well as having a main
 * function.
 * 
 * @author tony
 */
public class Mapper extends GUI {
	public static final Color NODE_COLOUR = new Color(77, 113, 255);
	public static final Color SEGMENT_COLOUR = new Color(130, 130, 130);
	public static final Color HIGHLIGHT_COLOUR = new Color(255, 219, 77);
	public static final Color STAEND_COLOUR = new Color(255,0,0);
	public static final Color ONEWAY_COLOUR = new Color(0,255,0);
	public static final Color ACLNODE_COLOUR = new Color(255,127,0);

	// these two constants define the size of the node squares at different zoom
	// levels; the equation used is node size = NODE_INTERCEPT + NODE_GRADIENT *
	// log(scale)
	public static final int NODE_INTERCEPT = 1;
	public static final double NODE_GRADIENT = 0.8;

	// defines how much you move per button press, and is dependent on scale.
	public static final double MOVE_AMOUNT = 100;
	// defines how much you zoom in/out per button press, and the maximum and
	// minimum zoom levels.
	public static final double ZOOM_FACTOR = 1.3;
	public static final double MIN_ZOOM = 1, MAX_ZOOM = 200;

	// how far away from a node you can click before it isn't counted.
	public static final double MAX_CLICKED_DISTANCE = 0.15;

	// these two define the 'view' of the program, ie. where you're looking and
	// how zoomed in you are.
	private Location origin;
	private double scale;

	// our data structures.
	private static Graph graph;
	private Trie trie;
	
	private Node Startnode=null;
	private Node Endnode=null;

	@Override
	protected void redraw(Graphics g) {
		if (getGraph() != null)
			getGraph().draw(g, getDrawingAreaDimension(), origin, scale);
	}

	@Override
	protected void onClick(MouseEvent e) {
		Location clicked = Location.newFromPoint(e.getPoint(), origin, scale);
		// find the closest node.
		double bestDist = Double.MAX_VALUE;
		Node closest = null;
		

		for (Node node : getGraph().nodes.values()) {
			double distance = clicked.distance(node.location);
			if (distance < bestDist) {
				bestDist = distance;
				closest = node;
			}
		}
			
		if(Startnode!=null&&Endnode!=null){ //reset the nodes
			Startnode=null;
			Endnode=null;
		}
		 if(Startnode==null){
			Startnode =closest;
		}else{
			Endnode=closest;
		}
		// if it's close enough, highlight it and show some information.
		if (clicked.distance(closest.location) < MAX_CLICKED_DISTANCE) {	
			getGraph().setHighlight(closest);
			findpath();
			}	
		}
	
	public void findpath() {
		if(Startnode!=null&&Endnode!=null&&Startnode!=Endnode){ // when they both not equals null will show some information.
			getGraph().setStartEndnode(Startnode,Endnode);
			List<Segment> seg = Pathfinding.Asearch(Startnode, Endnode);
			getGraph().setHighlightSegment(seg);
			
			getTextOutputArea().setText("The path is: \n");
			getTextOutputArea().append(totalcost(seg));		
		}	
	}
	
	

	public String totalcost(List<Segment> seg){
		
		double total=0;
		Set<Road> roads =new HashSet<>();
		for(Segment s : seg){
			
			s.road.length+=s.length;
			total+=s.length;
		}
		for(Road r : roads){
			getTextOutputArea().append(r.name + ": "+ String.format( "%.2f",r.length) +"km\n");
			r.length=0;
		}
		
		String totallength = "Total cost: " + String.format( "%.2f",total) + "km\n";
		return totallength;
	}

	@Override
	protected void onSearch() {
		if (trie == null)
			return;

		// get the search query and run it through the trie.
		String query = getSearchBox().getText();
		Collection<Road> selected = trie.get(query);

		// figure out if any of our selected roads exactly matches the search
		// query. if so, as per the specification, we should only highlight
		// exact matches. there may be (and are) many exact matches, however, so
		// we have to do this carefully.
		boolean exactMatch = false;
		for (Road road : selected)
			if (road.name.equals(query))
				exactMatch = true;

		// make a set of all the roads that match exactly, and make this our new
		// selected set.
		if (exactMatch) {
			Collection<Road> exactMatches = new HashSet<>();
			for (Road road : selected)
				if (road.name.equals(query))
					exactMatches.add(road);
			selected = exactMatches;
		}

		// set the highlighted roads.
		getGraph().setHighlight(selected);

		// now build the string for display. we filter out duplicates by putting
		// it through a set first, and then combine it.
		Collection<String> names = new HashSet<>();
		for (Road road : selected)
			names.add(road.name);
		String str = "";
		for (String name : names)
			str += name + "; ";

		if (str.length() != 0)
			str = str.substring(0, str.length() - 2);
			getTextOutputArea().setText(str);
	}

	@Override
	protected void onMove(Move m) {
		if (m == GUI.Move.NORTH) {
			origin = origin.moveBy(0, MOVE_AMOUNT / scale);
		} else if (m == GUI.Move.SOUTH) {
			origin = origin.moveBy(0, -MOVE_AMOUNT / scale);
		} else if (m == GUI.Move.EAST) {
			origin = origin.moveBy(MOVE_AMOUNT / scale, 0);
		} else if (m == GUI.Move.WEST) {
			origin = origin.moveBy(-MOVE_AMOUNT / scale, 0);
		} else if (m == GUI.Move.ZOOM_IN) {
			if (scale < MAX_ZOOM) {
				// yes, this does allow you to go slightly over/under the
				// max/min scale, but it means that we always zoom exactly to
				// the centre.
				scaleOrigin(true);
				scale *= ZOOM_FACTOR;
			}
		} else if (m == GUI.Move.ZOOM_OUT) {
			if (scale > MIN_ZOOM) {
				scaleOrigin(false);
				scale /= ZOOM_FACTOR;
			}
			
		}
		
		if(m==GUI.Move.ArticulationPoints){
			
			getGraph().ArticulationPoints = Pathfinding.ArticulationPoints(getGraph().nodes);
			getTextOutputArea().setText("Total Articulation Points: "  +  getGraph().ArticulationPoints.size());
			
			}
		
		if(m==GUI.Move.RESTRICTIONS){
			Pathfinding.off=false;
			findpath();
			
		}
		if(m == GUI.Move.FIND_FASTEST||m == GUI.Move.FIND_SHORTEST){
			findpath();	
		}else{
			findpath();	
		}
		
		}


	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons, File restriction) {
		setGraph(new Graph(nodes, roads, segments, polygons, restriction));
		trie = new Trie(getGraph().roads.values());
		origin = new Location(-250, 250); // close enough
		scale = 1;
	}

	/**
	 * This method does the nasty logic of making sure we always zoom into/out
	 * of the centre of the screen. It assumes that scale has just been updated
	 * to be either scale * ZOOM_FACTOR (zooming in) or scale / ZOOM_FACTOR
	 * (zooming out). The passed boolean should correspond to this, ie. be true
	 * if the scale was just increased.
	 */
	private void scaleOrigin(boolean zoomIn) {
		Dimension area = getDrawingAreaDimension();
		double zoom = zoomIn ? 1 / ZOOM_FACTOR : ZOOM_FACTOR;

		int dx = (int) ((area.width - (area.width * zoom)) / 2);
		int dy = (int) ((area.height - (area.height * zoom)) / 2);

		origin = Location.newFromPoint(new Point(dx, dy), origin, scale);
	}
	
	public static void main(String[] args) {
		new Mapper();
	}

	public static Graph getGraph() {
		return graph;
	}

	public static void setGraph(Graph graph) {
		Mapper.graph = graph;
	}
}

// code for COMP261 assignments