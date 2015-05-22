
public class Vertex {
	public Coordinate c;
	public boolean burned =false;
	public boolean protect=false;
	public boolean burnedFrom = false;
	public int timeBurned = -1;
	public Coordinate burnOrigin=null;

	Vertex(int x, int y){
		c=new Coordinate(x,y);
	}

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

	public boolean burn(Vertex v){
		if(!protect && !burned){
			burned=true;
			timeBurned=Firefighter.time;
			Firefighter.burnableVertices.add(c);
			Firefighter.justBurned.add(c);
			burnOrigin=v.c;
			updateFireDistance();
			if(Math.abs(v.c.x)+Math.abs(v.c.y)==7){
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
