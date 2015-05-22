import java.util.ArrayList;


public class D7 extends Vertex{
	private int maxAffected; //number of vertices in n5 (D5 vertices that are two steps away from this vertex) that have the current maximum threat value
	private int allAffected; //number of D5 vertices in n5 that have 
	private int recentAffected; //number of D5 vertices in n5 whose threat values have increased in the last time step
	public ArrayList<Coordinate> n5 = new ArrayList<Coordinate>();
	
	D7(int x, int y){
		super(x,y);
		maxAffected=0;
		recentAffected=0;
		for(int dy=2; dy>=-2; dy--){
			int dx=Math.abs(2-dy); 
			while(dx>=-Math.abs(2-dy)){
				if(Math.abs(dx)+Math.abs(dy)==2 && Math.abs(x+dx)+Math.abs(y+dy)==5){
					n5.add(new Coordinate(x+dx, y+dy));
				}
				dx--;
			}
		}
		allAffected=n5.size();
	}
	
	
}
