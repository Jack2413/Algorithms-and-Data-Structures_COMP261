package renderer;

import java.util.ArrayList;
import java.util.List;

/**
 * EdgeList should store the data for the edge list of a single polygon in your
 * scene. A few method stubs have been provided so that it can be tested, but
 * you'll need to fill in all the details.
 *
 * You'll probably want to add some setters as well as getters or, for example,
 * an addRow(y, xLeft, xRight, zLeft, zRight) method.
 */
public class EdgeList {
	private int StartY;
	private int EndY;
	private int i = 0;
	List<Row> rows = new ArrayList<>();

	public EdgeList(int startY, int endY) {
		this.StartY = startY;
		this.EndY = endY;
	}

	public int getStartY(){
		// TODO fill this in.
		return StartY;
	}

	public int getEndY() {
		// TODO fill this in.
		return EndY;
	}
	
	public float getLeftX(int y) {
		
		i =Math.abs(y-StartY);
		return rows.get(i).LeftX;
		
	}

	public float getRightX(int y) {
		
		i =Math.abs(y-StartY);
		return rows.get(i).RightX;
	}

	public float getLeftZ(int y) {
		
		i =Math.abs(y-StartY);
		return rows.get(i).LeftZ;
	}

	public float getRightZ(int y) {
		
		i =Math.abs(y-StartY);
		return rows.get(i).RightZ;
	}

	public void addRow(int y, float xLeft, float xRight, float zLeft, float zRight){
		Row row = new Row(y,xLeft,xRight,zLeft,zRight);
		this.rows.add(row);
	}
	
	class Row{
		
		private float LeftX;
		private float RightX;
		private float LeftZ;
		private float RightZ;
		private float CurrentY;
		
		public Row(int y, float LeftX, float RightX, float LeftZ, float RightZ ){
			
			this.CurrentY=y;
			this.LeftX=LeftX;
			this.RightX=RightX;
			this.LeftZ=LeftZ;
			this.RightZ=RightZ;
				
		}
			
	}
	}

// code for comp261 assignments
