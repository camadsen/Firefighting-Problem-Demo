import java.util.ArrayList;


public class D7 extends Vertex{
	public int maxAffected;
	public int allAffected;
	public int recentAffected;
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
