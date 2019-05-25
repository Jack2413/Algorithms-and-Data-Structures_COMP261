

import java.util.List;


public class Edges { 
// Segment
	private int RoadID;
	private double Length;
	private int node1; //First node		
	private int node2;  //Second node
	private List<Double> coords;
	


	

	public int getRoadID() {
		return RoadID;
	}

	public void setRoadID(int roadID) {
		RoadID = roadID;
	}

	public double getLength() {
		return Length;
	}

	public void setLength(int length) {
		Length = length;
	}

	public int getNode1() {
		return node1;
	}

	public void setNode1(int node1) {
		this.node1 = node1;
	}

	public int getNode2() {
		return node2;
	}

	public void setNode2(int node2) {
		this.node2 = node2;
	}

	public Edges(int roadID, double length, int node1, int node2, List<Double> croods) {
		super();
		RoadID = roadID;
		Length = length;
		this.node1 = node1;
		this.node2 = node2;
		this.coords = croods;
		
	}

	public List<Double>  getCoords() {
		return coords;
	}

	public void setCoords(List<Double> coords){
		this.coords = coords;
	}





}
