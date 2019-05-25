
import java.util.ArrayList;

public class Road {
	private int RoadId;
	private int Type;
	private String Lable;
	private String City;
	private int Oneway;
	private int Speed_limit;
	private int Road_class;
    private int notforcar;
    private int notforpede;
    private int notforbicy;
	public ArrayList<Edges> edges;
    
    
	public Road(int roadId, int type, String lable, String city, int oneway, int speed_limit, int road_class,
			int notforcar, int notforpede, int notforbicy) {
		super();
		RoadId = roadId;
		Type = type;
		Lable = lable;
		City = city;
		Oneway = oneway;
		Speed_limit = speed_limit;
		Road_class = road_class;
		this.notforcar = notforcar;
		this.notforpede = notforpede;
		this.notforbicy = notforbicy;

  edges = new ArrayList<Edges>();
	}

	public int getRoadId() {
		return RoadId;
	}

	public void setRoadId(int roadId) {
		RoadId = roadId;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public String getLable() {
		return Lable;
	}

	public void setLable(String lable) {
		Lable = lable;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public int getOneway() {
		return Oneway;
	}

	public void setOneway(int oneway) {
		Oneway = oneway;
	}

	public int getSpeed_limit() {
		return Speed_limit;
	}

	public void setSpeed_limit(int speed_limit) {
		Speed_limit = speed_limit;
	}

	public int getRoad_class() {
		return Road_class;
	}

	public void setRoad_class(int road_class) {
		Road_class = road_class;
	}

	public int getNotforcar() {
		return notforcar;
	}

	public void setNotforcar(int notforcar) {
		this.notforcar = notforcar;
	}

	public int getNotforpede() {
		return notforpede;
	}

	public void setNotforpede(int notforpede) {
		this.notforpede = notforpede;
	}

	public int getNotforbicy() {
		return notforbicy;
	}

	public void setNotforbicy(int notforbicy) {
		this.notforbicy = notforbicy;
	}
	

}
