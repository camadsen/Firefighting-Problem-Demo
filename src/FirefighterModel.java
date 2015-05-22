import java.util.ArrayList;


public class FirefighterModel {
	private static boolean burnTurn=false; //whether or not it is the turn of the fire
	private static boolean reachedD7=false; //whether or not the fire has reached D7 (a distance of 7 from the origin)
	private static int time; //the current time step of the simulation
	private static int maxThtVal; //the maximum threat value of any D5 vertex
	private static Vertex[][] grid= new Vertex[19][19]; //the array of all vertices on the grid that are displayed in the simulation
	private static ArrayList<Coordinate> burnableVertices= new ArrayList<Coordinate>(); // the list of all vertices that can be burned
	private static ArrayList<Coordinate> justBurned = new ArrayList<Coordinate>(); //the list of all vertices that were burned during the previous time step
	private static ArrayList<D7> d7Unprotected = new ArrayList<D7>(); //the list of all vertices on D7 that haven't been protected yet; used by the algorithm to determine where to protect next
	private static ArrayList<D5> d5Vertices = new ArrayList<D5>(); //the list of all vertices on D5


	/**
	 * A constructor for a model of the Firefighting (1-pyro) problem
	 */
	FirefighterModel(){
		time=0;
		maxThtVal=-1;
		setUpGrid();
	}
	
	/**
	 * An accessor for grid
	 * @return - the grid of vertices
	 */
	public static Vertex[][] getGrid(){
		return grid;
	}
	
	/**
	 * An accessor for burnableVertices
	 * @return - the list of 
	 */
	public static ArrayList<Coordinate> getBurnableVertices(){
		return burnableVertices;
	}
	
	public static int getTime(){
		return time;
	}

	public static ArrayList<Coordinate> getJustBurned(){
		return justBurned;
	}
	
	public static ArrayList<D7> getD7Unprotected(){
		return d7Unprotected;
	}
	
	public static void reachedD7(){
		reachedD7=true;
	}

	public static void setUpGrid(){
		for(int i=0; i<19; i++){
			for(int j=0; j<19; j++){
				if(Math.abs(i-9)+Math.abs(j-9)==5){
					grid[i][j]=new D5(i-9, j-9);
					d5Vertices.add((D5)grid[i][j]);
				}
				else if(Math.abs(i-9)+Math.abs(j-9)==7){
					grid[i][j]=new D7(i-9, j-9);
					d7Unprotected.add((D7) grid[i][j]);
				}
				else{
					grid[i][j]=new Vertex(i-9,j-9);
				}
			}
		}
		grid[9][9].burn(grid[9][9]);
	}


	public static boolean burnFrom(Vertex v){
		if(burnableVertices.contains(v.c)){
			if(v.canBeBurnedFrom()){
				Firefighter.justBurned.clear();
				v.burnFrom();
				return true;
			}
			else{
				burnableVertices.remove(v.c);
				return false;
			}
		}
		return false;
	}

	public static boolean protect(Vertex v){
		if(v.protect()){
			time++;
			if(v instanceof D7){
				d7Unprotected.remove(v);
			}
			return true;
		}
		else{
			return false;
		}
	}

	public static D7 algorithmicD7Choice(){
		//updating D5 threat values
		int max=-5;
		for(D5 vert: Firefighter.d5Vertices){
			vert.updateThtValue();
			if(vert.thtValue>max){
				max=vert.thtValue;
			}
		}
		Firefighter.maxThtVal=max;
		
		ArrayList<D5> maxD5 = new ArrayList<D5>();
		ArrayList<D5> justChanged = new ArrayList<D5>();
		for(D5 d: d5Vertices){
			if(d.thtValue==maxThtVal){
				maxD5.add(d);
			}
			if(d.timeChanged==time){
				justChanged.add(d);
			}
		}
		ArrayList<D7> algorithmChoices=new ArrayList<D7>();
		int maxMax=0;
		for(D7 v: d7Unprotected){
			v.maxAffected=0;
			v.recentAffected=0;
			for(Coordinate c:v.n5){
				D5 vert5=(D5)Vertex.getVertex(c);
				if(maxD5.contains(vert5)){
					v.maxAffected++;
				}
				if(justChanged.contains(vert5)){
					v.recentAffected++;
				}
			}
			if(v.maxAffected>=maxMax){
				if(v.maxAffected>maxMax){
					algorithmChoices.clear();
				}
				algorithmChoices.add(v);
				maxMax=v.maxAffected;
			}
		}
		for(Vertex v:algorithmChoices){
			System.out.print(v.c);
		}
		//algorithmChoices now holds all D7 vertices that will affect a maximum number of
		//D5 vertices that currently have the maximum threat value
		if(algorithmChoices.size()>1){
			//next want to limit to those that will affect a maximum number of total D5 vertices
			int maxAll=0;
			ArrayList<D7> algorithmChoices2=new ArrayList<D7>();
			for(D7 d: algorithmChoices){
				if(d.allAffected>=maxAll){
					if(d.allAffected>maxAll){
						maxAll=d.allAffected;
						algorithmChoices2.clear();
					}
					algorithmChoices2.add(d);
				}
			}
			System.out.println();
			for(Vertex v:algorithmChoices2){
				System.out.print(v.c);
			}
			if(algorithmChoices2.size()>1){
				int maxRecent=0;
				ArrayList<D7> algorithmChoices3=new ArrayList<D7>();
				for(D7 d: algorithmChoices2){
					if(d.recentAffected>=maxRecent){
						if(d.recentAffected>maxRecent){
							maxRecent=d.recentAffected;
							algorithmChoices3.clear();
						}
						algorithmChoices3.add(d);
					}
				}
				System.out.println();
				for(Vertex v:algorithmChoices3){
					System.out.print(v.c);
				}
				return algorithmChoices3.get(0);
			}
			else{
				return algorithmChoices2.get(0);
			}
		}

		return algorithmChoices.get(0);
	}

}
