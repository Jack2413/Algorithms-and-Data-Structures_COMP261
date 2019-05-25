
public class restrictions {
	
	public Node node1;
	public Node interNode;
	public Node node2;
	public Road road1;
	public Road road2;
	
	public restrictions(Graph graph, int node1ID, int road1ID, int IntnodeID, int node2ID, int road2ID) {
		super();
		this.node1 = graph.nodes.get(node1ID);
		this.interNode = graph.nodes.get(IntnodeID);
		this.node2 = graph.nodes.get(node2ID);
		this.road1 = graph.roads.get(road1ID);
		this.road2 = graph.roads.get(road2ID);
	}

	public boolean restriction(){
		
		return true;
	}
}