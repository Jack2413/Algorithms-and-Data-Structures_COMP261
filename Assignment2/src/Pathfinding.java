import java.util.*;
public class Pathfinding {
	
	public static List <Node> Nodes;
	public static List<Segment> Segments;
	public static PriorityQueue <Node> fringe;
	public static List<Node> Allnode;
	public static Set <Node> ArtPointNode;
	public static String condition=""; 
	public static String Vehicle = "";
	public static boolean off;
	
	static Set <BanRoad> BanRoads = new HashSet <>();
	/**
	 * the method is the main algorithm for the A*search
	 * 
	 * @param start The start node from first mouse chick 
	 * @param end   The target node from second mouse chick
	 * @return A path list back to the Graph Class
	 */
	public static List<Segment> Asearch(Node start, Node end){
		System.out.printf("startID: %d\n",start.nodeID);
		System.out.printf("endID: %d\n",end.nodeID);
		
		//Nodes = new ArrayList<>();
		Segments = new ArrayList<>();
		Allnode = new ArrayList<>();
		Node current = null;
		fringe = new  PriorityQueue<Node>();
		
		fringe.add(start);
		Allnode.add(start);
		while(!fringe.isEmpty()){
			current=fringe.poll();
			if(!current.visited){
				Allnode.add(current);
				current.visited=true;
				//current.pathfrom=start;
			    //current.total_cost=start.total_cost;
				
				if(current==end){break;}
				
					for(Node nei: current.neighbors){
						Segment s = current.getSegment(nei);

						if(!nei.visited&&s.CanTravelTo(nei)&&s.considerVehicle(Vehicle)&&(notbaned(s,nei,BanRoads)||off)){
							BanRoads = banlist(current, s, nei);//each time get into new neighbors update the path form path length and total cost
						    nei.pathlength = current.disbetween(nei) + current.pathlength;
						    nei.time = current.timebetween(nei) + current.time;
						    nei.weight=estimate(nei,end);
						    if(condition.equals("FindFastest")){nei.total_cost = current.time +  nei.weight;}
						    else{ nei.total_cost = current.pathlength +  nei.weight;}
						    nei.pathfrom = current;
							fringe.add(nei);
							Allnode.add(nei);		
						}	
					}
			}
		}
		//Nodes.add(current);
		
		do{
			if(current==null){break;}
			
			if(current.getSegment(current.pathfrom)!=null){Segments.add(current.getSegment(current.pathfrom));};
			current=current.pathfrom;
			//Nodes.add(current);
			//System.out.printf("nodeID: %d",current.nodeID);
	
		}while(current!=start); // add nodes into array list from current node until start node
		
		for(int i=0; i<Allnode.size(); i++ ){		
		 Allnode.get(i).resets();
		}	
		
		
		return Segments;
		
	}
	
	private static boolean notbaned(Segment s, Node nei, Set <BanRoad> BanRoads) {
		for(BanRoad br : BanRoads){
			if(br.Road==s.road&&br.node==nei)
				return false;	
		}
		return true;
	}

	private static Set<BanRoad> banlist(Node current, Segment seg, Node nei) {
		Set<BanRoad> ban = new HashSet<>();
		for(restrictions res :Mapper.getGraph().restriction){
			if(current==res.node1&&seg.road==res.road1&&nei==res.interNode)
		ban.add(new BanRoad(res.road2,res.node2));
		}
		return ban;
	}

	/**
	 * this method is estimate the distance between neighbor and end
	 * @param nei one of current node's neighbor
	 * @param end the target node
	 * @return the distance between neighbor and end
	 */
	public static double estimate(Node nei,Node end){ 
		return nei.location.distance(nei.location);
		
	}
	
	public static Set<Node> ArticulationPoints(Map<Integer,Node> nodes){
		
		ArtPointNode = new HashSet<Node>();
		for (Node node : nodes.values()){
		int subtree = 0;
		for(Node nei :node.neighbors){
			
			if(nei.count==Integer.MAX_VALUE){
				iterArtPts(nei, 1, node);
				subtree++;
			}
			
		if (subtree>1){ArtPointNode.add(node);}
		
		}
		}
		return ArtPointNode;	
	}
	
	public static void iterArtPts(Node nei, int count, Node node){
		
		
		Stack<ArtPtsNode> APnodes = new Stack<>();
		
		APnodes.push(new ArtPtsNode(nei,count,node));
		while(!APnodes.isEmpty()){
			
			Node current = APnodes.peek().node;
			
			if(current.count==Integer.MAX_VALUE){
				
				current.count=count;
				current.reachback=count;
				current.Children = new ArrayList<>();
				for(Node neibor:current.neighbors){
					if(neibor!=APnodes.peek().parent){
					current.Children.add(neibor);
					
					}
				}
			}	
			else if(!current.Children.isEmpty()){
				Node child = current.Children.remove(0);
				if(child.count<Integer.MAX_VALUE){
					current.reachback = min(current.reachback, child.count);
					//System.out.println(current.reachback);
					}
			
				else{				
					APnodes.push(new ArtPtsNode(child,count++,current));
					}
				
				
			}
			else{
				if(current!=nei){
					if(current.reachback>=APnodes.peek().parent.count){
						ArtPointNode.add(APnodes.peek().parent);	
					}
					APnodes.peek().parent.reachback= min(APnodes.peek().parent.reachback,current.reachback);
				}
				APnodes.pop();
			}
		}	
	}
	public static int min(int NodeReachBack,int ChildCount){
		
		if(NodeReachBack<ChildCount){return NodeReachBack;}
		else{return ChildCount;}
		
	}
	

}
