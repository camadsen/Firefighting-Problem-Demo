import java.util.ArrayList;


public class FirefighterModel {
	private static boolean burnTurn=false; //whether or not it is the turn of the fire
	private static boolean reachedD7=false; //whether or not the fire has reached D7 (a distance of 7 from the origin)
	private static int time; //the current time step of the simulation
	private static int maxThtVal; //the maximum threat value of any D5 vertex
	private static Vertex[][] grid= new Vertex[19][19]; //the array of all vertices on the grid that are displayed in the simulation
	private static ArrayList<Coordinate> burnableVertices= new ArrayList<Coordinate>(); // the list of all vertices that can be burned from
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
	 * @return - the list of vertices which can be burned from
	 */
	public static ArrayList<Coordinate> getBurnableVertices(){
		return burnableVertices;
	}
	
	/**
	 * An accessor for time
	 * @return - the current time of the simulation
	 */
	public static int getTime(){
		return time;
	}

	/**
	 * An accessor for justBurned
	 * @return - the list of vertices that were burned in the last time step
	 */
	public static ArrayList<Coordinate> getJustBurned(){
		return justBurned;
	}
	
	/**
	 * An accessor for d7Unprotected
	 * @return - the list of vertices on D7 that have not been protected
	 */
	public static ArrayList<D7> getD7Unprotected(){
		return d7Unprotected;
	}
	
	/**
	 * A method to record when D7 has been reached
	 */
	public static void reachedD7(){
		reachedD7=true;
	}

	/**
	 * A method to set up the grid of Vertices for the simulation
	 */
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

	/**
	 * A method to begin burning from a certain vertex
	 * @param v - the vertex that is being burned from
	 * @return - true if the vertex was burned from and false if it cannot be burned from
	 */
	public static boolean burnFrom(Vertex v){
		if(burnableVertices.contains(v.getCoordinate())){
			if(v.canBeBurnedFrom()){
				FirefighterModel.justBurned.clear();
				v.burnFrom();
				return true;
			}
			else{
				burnableVertices.remove(v.getCoordinate());
				return false;
			}
		}
		return false;
	}

	/**
	 * A method to protect a certain vertex
	 * @param v
	 * @return
	 */
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

	//CURRENTLY MAKING CHANGES FROM HERE FORWARD
	public static D7 algorithmicD7Choice(){
		//updating D5 threat values
		int max=-5;
		for(D5 vert: FirefighterModel.d5Vertices){
			vert.updateThtValue();
			if(vert.getThreatValue()>max){
				max=vert.getThreatValue();
			}
		}
		FirefighterModel.maxThtVal=max;
		
		ArrayList<D5> maxD5 = new ArrayList<D5>();
		ArrayList<D5> justChanged = new ArrayList<D5>();
		for(D5 d: d5Vertices){
			if(d.getThreatValue()==maxThtVal){
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
