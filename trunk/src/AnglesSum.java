import Jcg.polyhedron.Halfedge;
import Jcg.polyhedron.Vertex;

/**
 * an easy to compute, classic way to caracterize the points for rigid transformations
 * @author Antoine & NTheo (2012)
 *
 */
public class AnglesSum extends Sign {

	private double sum;
	
	public void computeSign(SurfaceMesh m, Vertex v) {
		Halfedge e = v.getHalfedge();
		sum = 0.;
		Halfedge f = v.getHalfedge();
		do{
			sum += f.angle();
			f = f.getNext().getOpposite();
		}while(f!=e);
	}

	public boolean isValid() {
		return true;
	}

}
