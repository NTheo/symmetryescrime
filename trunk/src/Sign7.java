import Jcg.geometry.Point_3;
import Jcg.polyhedron.Vertex;

/**
 * signatures for rigid transformations
 * @author Antoine & NTheo (2012)
 *
 */
public class Sign7 implements Sign {

	private double r;	// r=k1/k2

	public void computeSign(SurfaceMesh m, Vertex v) {
		double[] k = v.curvatures(r);
		if(k[0]>k[1]){
			if(k[0]>0.){
				r = k[1]/k[0];
			} else {
				r = 0.;
			}
		} else {
			if(k[1]>0.){
				r = k[0]/k[1];
			} else {
				r = 0.;
			}
	}
	}

	public boolean isValid() {
		return r<threshold;
	}

}
