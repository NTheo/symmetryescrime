import java.util.LinkedList;

import Jcg.geometry.Point_3;
import Jcg.polyhedron.Vertex;


/**
 * Abstract class for extracting a sample of vertices from the model 
 * @author Antoine & NTheo (2012)
 *
 */
public abstract class Sampling {

	// NB: we should use Iterator.remove for the pruning, in O(1) instead of O(n) for remove()
	protected LinkedList<Vertex<Point_3>> vertices;  
	
	// Constructor : extract a sample of vertices from the given polyhedron
	// Sampling(){}	
	
	// Display on the model the points of the sample
	abstract void displaySample();
	
	LinkedList<Vertex<Point_3>> sample(){
		return this.vertices;
	}
}
