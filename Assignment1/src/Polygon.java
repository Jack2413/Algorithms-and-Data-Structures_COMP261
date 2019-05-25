import java.awt.Color;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;




public class Polygon {

	//public final int x, y;
	private Color color;
	private String Label;
	private int EndLevel;
	private int CityIdx;
	private  List <Location> data = new ArrayList<Location>();
	

	


	public Polygon(Color color,  List<Location> data) {
		super();
		this.setColor(color);

		this.data = data;
	}



	public String getLabel() {
		return Label;
	}



	public void setLabel(String label) {
		Label = label;
	}



	public int getEndLevel() {
		return EndLevel;
	}



	public void setEndLevel(int endLevel) {
		EndLevel = endLevel;
	}



	public int getCityIdx() {
		return CityIdx;
	}



	public void setCityIdx(int cityIdx) {
		CityIdx = cityIdx;
	}





	

	

	public void draw(Graphics g) {
		//g.setColor(color);
	
	}



	public List <Location> getData() {
		return data;
	}



	public void setData(List <Location> data) {
		this.data = data;
	}



	public Color getColor() {
		return color;
	}



	public void setColor(Color color) {
		this.color = color;
	}

	//public boolean contains(int x, int y) {
		
	}
	


