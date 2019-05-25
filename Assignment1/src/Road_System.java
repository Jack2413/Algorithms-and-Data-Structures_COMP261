import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.HashSet;
//import java.util.Map;
//import java.util.Map.Entry;
import java.util.Set;

public class Road_System extends GUI  {
	
	Graph graph = new Graph();
	Location Nloc;
	Set<Edges> SelRoad = new HashSet<Edges>();
	String duplicate;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Road_System();
	}
	
	@Override
	protected void MouseWheel(MouseWheelEvent e) {
		int num = e.getWheelRotation();
		if(num>0){graph.scale+=(0.025*graph.scale);}
		else{graph.scale-=(0.025*graph.scale);	}
		
	}


	@Override
	protected void redraw(Graphics g) {
		graph.drawPoly(g);
		
		graph.draw(g);
		if(Nloc!=null){
		graph.DrawSelectNode(Nloc);	
		}
		graph.DrawSelectRoad(SelRoad);
		
		
	}

	@Override
	protected void onClick(MouseEvent e) {
		
			Point ClickedPoint = e.getPoint();		
			Location ClickedLocation = Location.newFromPoint(ClickedPoint, graph.origin, graph.scale); // chick point to location
			System.out.print(ClickedLocation);
			
			
			for(Node n : graph.nodes){
				
			Location Nloc =	Location.newFromLatLon(n.getLatitude(), n.getLongitude());
			
				double dist =0.07;//Nloc.distance(ClickedLocation);
				
				if(Nloc.isClose(ClickedLocation, dist)){ //find the closest distance between the chick point and node
					
					this.Nloc=Nloc;
					//System.out.print(Nloc);
					int ID = n.getID();
					getTextOutputArea().append("\nRoad ID: " + ID + "\n");
					getTextOutputArea().append("Roads that connect to intersections are: \n");
					String dupilacte = "dupilacte";
					for (Road Nroad : n.getConnectRoads()){
						
						if(!dupilacte.equals(Nroad.getLable())){  //Avoid print the segment name twice
						getTextOutputArea().append( Nroad.getLable()+"\n");
						 dupilacte = Nroad.getLable();
						 
					}
					}
					break;
				}
				
				
			}
			}
			
			
	
		
	

	@Override
	protected void onSearch() {
		
			SelRoad.clear();
			String roadName = getSearchBox().getText();
				
			/*for(Entry<Integer, Road> road : graph.roads.entrySet()){ 
				Road r = road.getValue(); 
				if(r.getLable().equals(roadName)){ //for every road Check if this road  match the roadName have been given
					for(Edges e : graph.edges){
						if(graph.roads.get(e.getRoadID()).equals(r)){
							 //check for every edges if they are any connect to the road
							SelRoad.add(e);	                          //by using the road map get key from edge to calling roads
							System.out.print(e);
							}
						}
					}
				}
				*/
			if(roadName!=duplicate){
			getTextOutputArea().append("Roadname: "+ roadName + "\n");
			duplicate = roadName;
			}
			SelRoad=graph.TriesSerch(roadName);
		}
		
	
	

	@Override
	protected void onMove(Move m) {
		if(m==Move.ZOOM_IN){graph.scale+=(0.1*graph.scale);}
		if(m==Move.ZOOM_OUT){graph.scale-=(0.1*graph.scale);}
		if(m==Move.NORTH){graph.latitude += (0.01*graph.scale/75);}
		if(m==Move.EAST){graph.longitude+=(0.01*graph.scale/75);}
		if(m==Move.SOUTH){graph.latitude-=(0.01*graph.scale/75);}
		if(m==Move.WEST){graph.longitude-=(0.01*graph.scale/75);}
		
		
		
	}

	@Override
	protected void onLoad(File nodesf, File roads, File segments, File polygons) {
	
	
	graph.onload( nodesf, roads, segments,  polygons);
	 redraw();
	}



	

	
	}



