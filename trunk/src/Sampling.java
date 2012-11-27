import java.util.List;

import Jcg.geometry.Point_3;
import Jcg.polyhedron.Vertex;


/**
 * Abstract class for extracting a sample of vertices from the model 
 * @author Antoine & NTheo (2012)
 *
 */
public abstract class Sampling {

	// NB: we should use Iterator.remove for the pruning, in O(1) instead of O(n) for remove()
	public List<Vertex> vertices;  
	
	// Constructor : extract a sample of vertices from the given polyhedron
	// Sampling(){}	
	
	// Display on the model the points of the sample
	// Display a white segment for each vertex in the sample 
	public void display(MeshViewer MV){
		for(Vertex v : this.vertices){
			MV.mesh.drawSegment(v.getPoint(), new Point_3(v.getPoint().x*1.05,v.getPoint().y*1.05,v.getPoint().z*1.05));
		}
	}
	
	// Display a little sphere for each vertex in the sample - too slow !
	public void display2(MeshViewer MV){
		MV.noStroke();
		MV.fill(250f, 250f, 0f);
		for(Vertex v: this.vertices) {
			MV.mesh.drawVertex(v.getPoint());
		}
		MV.strokeWeight(1);
	}
	
	List<Vertex> sample(){
		return this.vertices;
	}
}
