import java.util.LinkedList;

import Jcg.geometry.Point_3;
import Jcg.polyhedron.Polyhedron_3;
import Jcg.polyhedron.Vertex;


/**
 * This class performs a uniform sampling of the model
 * @author Antoine & NTheo (2012)
 *
 */
public class UniformSampling extends Sampling {
	
	private double minRadius;
	
	private double minDistance(){
		double min=Double.MAX_VALUE;
		double distance;
		Point_3 origin=new Point_3(0., 0., 0.);
		for(Vertex v:this.vertices){
			distance=Math.sqrt(v.getPoint().squareDistance(origin).doubleValue());
			min=Math.min(min,distance);
		}
		return min;
	}
	
	private double maxDistance(){
		double max=0;
		double distance;
		Point_3 origin=new Point_3(0., 0., 0.);
		for(Vertex v:this.vertices){
			distance=Math.sqrt(v.getPoint().squareDistance(origin).doubleValue());
			max=Math.max(max,distance);
		}
		return max;
	}
	
	private boolean isTooClose(Vertex v){
		
		for(Vertex u : this.vertices){
			if(u.getPoint().distanceFrom(v.getPoint()).doubleValue() < this.minRadius)
				return true;
		}
		return false;
	}
	
	public UniformSampling(Polyhedron_3 polyhedron) {	
		// NB: we should use Iterator.remove for the pruning, in O(1) instead of O(n) for remove()
		this.vertices=new LinkedList<Vertex>();
		int psv=polyhedron.sizeOfVertices();
		this.size=(int) (psv*Parameters.samplingRatio);
		
		// First, we set the minimum radius of the sphere in which each selected vertex should be isolated
		double distance=(minDistance()+maxDistance())/2;
		this.minRadius=Math.sqrt(4*distance*distance/this.size);
		
		Vertex v;
		while(this.vertices.size()<this.size){
			v=polyhedron.vertices.get((int) (Math.random()*psv));
			int retry=0;
			while(vertices.contains(v)||isTooClose(v)){
				v=polyhedron.vertices.get((int) (Math.random()*psv));
				retry++;
				if(retry>50){
					// Can't find points that far from each others => diminish the radius to the value
					// for which we are sure to find enough points
					this.minRadius=Math.sqrt(4*minDistance()*minDistance()/this.size);
				}
			}
			vertices.add(v);
		}
		System.out.println("Sampling : "+vertices.size()+" points selected ("+Parameters.samplingRatio*100+" %)");
	}

}
