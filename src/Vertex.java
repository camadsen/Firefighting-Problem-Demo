
public class Vertex {
	private Coordinate c; //the coordinate of this vertex
	private boolean burned =false; //whether or not this vertex has been burned
	private boolean protect=false; //whether or not this vertex has been protected
	private boolean burnedFrom = false; //if this vertex is burned, whether or not it has been burned from
	private int timeBurned = -1; //when this vertex was burned (-1 if it has not yet been burned)
	private Coordinate burnOrigin=null; //if this vertex was burned, where it was burned from (otherwise null)

	/**
	 * A simple constructor for a vertex
	 * @param x
	 * @param y
	 */
	Vertex(int x, int y){
		c=new Coordinate(x,y);
	}
	
	/**
	 * An accessor for burned
	 * @return - true if this vertex has been burned, false if not
	 */
	public boolean isBurned(){
		return burned;
	}
	
	/**
	 * An accessor for protected
	 * @return - true if this vertex has been protected, false if not
	 */
	public boolean isProtected(){
		return protect;
	}
	
	/**
	 * An accessor for c, the Coordinate of the vertex
	 * @return - the Coordinate of this vertex
	 */
	public Coordinate getCoordinate(){
		return c;
	}

	/**
	 * A method that burns from this vertex. It attempts to burn any unburned vertex that is one step away
	 * and then removes this vertex from the list of vertices that can be burned.
	 */
	public void burnFrom(){
		if(up()!=null)
			up().burn(this);
		if(down()!=null)
			down().burn(this);
		if(left()!=null)
			left().burn(this);
		if(right()!=null)
			right().burn(this);
		Firefighter.burnableVertices.remove(c);
		
	}

	/**
	 * A method to burn a vertex v from while burning from this vertex
	 * @param v - the vertex that is being burned from this one
	 * @return - true if the vertex was successfully burned and false if it was already protected or burned
	 */
	public boolean burn(Vertex v){
		if(!v.isProtected() && !v.isBurned()){
			v.burned=true;
			v.timeBurned=Firefighter.time;
			Firefighter.burnableVertices.add(v.getCoordinate());
			Firefighter.justBurned.add(v.getCoordinate());
			burnOrigin=c;
			updateFireDistance();
			if(Math.abs(v.c.getX())+Math.abs(v.c.getY())==7){
				Firefighter.reachedD7=true;
				Firefighter.d7Unprotected.remove(v.c);
			}
			return true;
		}
		return false;
	}

	public void updateFireDistance(){
		if(Math.abs(this.c.x)+Math.abs(this.c.y)==6){
			if(up()!=null)
				up().updateSingleFireDistance(-1);
			if(down()!=null)
				down().updateSingleFireDistance(-1);
			if(left()!=null)
				left().updateSingleFireDistance(-1);
			if(right()!=null)
				right().updateSingleFireDistance(-1);
		}
		updateFireDistance(0);
	}

	public void updateSingleFireDistance(int distance){
		if(this instanceof D5){
			D5 current = (D5) this;
			if(current.fireDist>distance){
				current.fireDist=distance;
				current.timeChanged=Firefighter.time;
			}
		}
	}
	public void updateFireDistance(int distance){
		if(distance==5){
			return;
		}
		else{
			updateSingleFireDistance(distance);
			if(up()!=null)
				up().updateFireDistance(distance+1);
			if(down()!=null)
				down().updateFireDistance(distance+1);
			if(left()!=null)
				left().updateFireDistance(distance+1);
			if(right()!=null)
				right().updateFireDistance(distance+1);
		}
	}

	public boolean protect(){
		if(!protect && !burned){
			protect=true;
			return true;
		}
		return false;
	}

	public static Vertex getVertex(Coordinate c1){
		return Firefighter.grid[c1.x+9][c1.y+9];
	}

	public boolean canBeBurnedFrom(){
		if(protect||burnedFrom||(!burned))
			return false;
		if((up().burned||up().protect)&&(down().burned||down().protect)&&(left().burned||left().protect)&&(right().burned||right().protect))
			return false;
		return true;
	}

	public Vertex up(){
		if(c.y+1<=9)
			return Firefighter.grid[9+c.x][9+c.y+1];
		else
			return null;
	}

	public Vertex down(){
		if(c.y-1>=-9)
			return Firefighter.grid[9+c.x][9+c.y-1];
		else
			return null;
	}

	public Vertex left(){
		if(c.x-1>=-9)
			return Firefighter.grid[9+c.x-1][9+c.y];
		else
			return null;
	}

	public Vertex right(){
		if(c.x+1<=9)
			return Firefighter.grid[9+c.x+1][9+c.y];
		else
			return null;
	}



}
