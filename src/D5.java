import java.util.ArrayList;


public class D5 extends Vertex{

	private int thtValue; //the computed threat value based on the algorithm from my research
	private int n7; //the number of vertices in N2(Vertex) on D7 with N2(Vertex) being all vertices 2 steps away
	private int protN7; //the number of vertices in N2(Vertex) on D7 protected by the end of time t-1
	public ArrayList<Coordinate> n7UnprotVertices = new ArrayList<Coordinate>();
	private int fireDist; //the distance from the fire to this vertex (can be negative if the fire passes this vertex towards D7)
	private int timeChanged; //the last time at which the 


	D5(int x, int y){
		super(x,y);
		n7=0;
		protN7=0;
		fireDist=5; 
		timeChanged=0;
		for(int dy=2; dy>=-2; dy--){
			int dx=Math.abs(2-dy); 
			while(dx>=-Math.abs(2-dy)){
				if(Math.abs(dx)+Math.abs(dy)==2 && Math.abs(x+dx)+Math.abs(y+dy)==7){
					n7UnprotVertices.add(new Coordinate(x+dx, y+dy));
					n7++;
				}
				dx--;
			}
		}
	}


	public void updateThtValue(){
		if(n7UnprotVertices.size()>0){
			for(int i =n7UnprotVertices.size()-1; i>=0; i--){
				Coordinate c = n7UnprotVertices.get(i);
				D7 vert=(D7) Firefighter.grid[c.x+9][c.y+9];
				if(vert.protect){
					n7UnprotVertices.remove(c);
				}
			}
		}
		protN7=n7-n7UnprotVertices.size();
		thtValue=(n7-protN7)-fireDist-1;
	}

}