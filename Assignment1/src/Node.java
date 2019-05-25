import java.util.HashSet;
import java.util.Set;

public class Node { //intersection
	private int ID;
	private double Latitude;
	private double Longitude;
    private Set<Road> ConnectRoads = new HashSet<Road>() ;
	
	


	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}
			


public Node (int ID, double Latitude,double Longitude){
	 
	 this.ID = ID;
	 this.Latitude = Latitude;
	 this.Longitude = Longitude;

	 
	
	
 }

public Set<Road> getConnectRoads() {
	return ConnectRoads;
}

public void setConnectRoads(Set<Road> connectRoads) {
	ConnectRoads = connectRoads;
}


	
	
	
	
} 

	
	

