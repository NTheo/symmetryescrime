import java.util.LinkedList;
import java.util.List;

import Jcg.geometry.Point_3;
import Jcg.polyhedron.Polyhedron_3;
import Jcg.polyhedron.Vertex;


/**
 * Abstract class for extracting a sample of vertices from the model 
 * @author Antoine & NTheo (2012)
 *
 */
public abstract class Sampling {

	// NB: we should use Iterator.remove for the pruning, in O(1) instead of O(n) for remove()
	public List<Vertex> vertices; 
	public int size;
	public double radius;
	protected Polyhedron_3 polyhedron;
	
	// Constructor : extract a sample of vertices from the given polyhedron
	public Sampling(Polyhedron_3 polyhedron){
		this.polyhedron=polyhedron;
		
		
		// NB: we should use Iterator.remove for the pruning, in O(1) instead of O(n) for remove()
		this.vertices=new LinkedList<Vertex>();
		int psv=polyhedron.sizeOfVertices();
		this.size=(int) (psv*Parameters.samplingRatio);
		
		// We set the minimum radius of the sphere in which each selected vertex should be isolated
		double distance=(minDistance()+maxDistance())/2;
		this.radius=Math.sqrt(4*distance*distance/this.size);
		Parameters.radius=this.radius;
		System.out.println("radius = "+this.radius);
		System.out.print("Extracting sample...");
	}	
	
	protected double minDistance(){
		double min=Double.MAX_VALUE;
		double distance;
		Point_3 origin=new Point_3(0., 0., 0.);
		for(Vertex v:this.polyhedron.vertices){
			distance=Math.sqrt(v.getPoint().squareDistance(origin).doubleValue());
			min=Math.min(min,distance);
		}
		return min;
	}
	
	protected double maxDistance(){
		double max=0;
		double distance;
		Point_3 origin=new Point_3(0., 0., 0.);
		for(Vertex v:this.polyhedron.vertices){
			distance=Math.sqrt(v.getPoint().squareDistance(origin).doubleValue());
			max=Math.max(max,distance);
		}
		return max;
	}
	
	List<Vertex> sample(){
		return this.vertices;
	}
	
	// Display on the model the points of the sample
	// Display a white segment for each vertex in the sample 
	public void displaySegments(MeshViewer MV){
		for(Vertex v : this.vertices){
			MV.mesh.drawSegment(v.getPoint(), new Point_3(v.getPoint().x*1.05,v.getPoint().y*1.05,v.getPoint().z*1.05));
		}
	}
	
	// Display a little sphere for each vertex in the sample - too slow !
	public void displayPoints(MeshViewer MV){
		MV.noStroke();
		MV.fill(250f, 250f, 0f);
		for(Vertex v: this.vertices) {
			MV.mesh.drawVertex(v.getPoint());
		}
		MV.strokeWeight(1);
	}
	
	// Display a sphere the size of Parameters.radius at each point
	public void displaySpheres(MeshViewer MV){
		Point_3 p;
		float scale = (float) MV.mesh.scaleFactor;
		MV.noStroke();
		MV.fill(250.f,250.f,250.f);
		for(Vertex v: this.vertices){
			p=v.getPoint();
			MV.translate(scale*p.x.floatValue(), scale*p.y.floatValue(), scale*p.z.floatValue());
			MV.sphere(scale * ((float) Parameters.radius));
			MV.translate(-scale*p.x.floatValue(), -scale*p.y.floatValue(), -scale*p.z.floatValue());
		}
	}
}
