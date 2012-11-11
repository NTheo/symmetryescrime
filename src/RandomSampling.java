import java.util.LinkedList;

import Jcg.geometry.Point_3;
import Jcg.polyhedron.Polyhedron_3;
import Jcg.polyhedron.Vertex;


/**
 * This class performs a random sampling of the model
 * @author Antoine & NTheo (2012)
 *
 */
public class RandomSampling extends Sampling {
	
	public RandomSampling(Polyhedron_3<Point_3> polyhedron) {
		this.sample=new LinkedList<Vertex<Point_3>>();
		int vertices=polyhedron.sizeOfVertices();
		for(int i=0;i<vertices*Parameters.samplingRatio;i++){
			sample.add(polyhedron.vertices.get((int) Math.random()*vertices));
		}
	}

	@Override
	void displaySample() {
		// TODO Auto-generated method stub
	}


}
