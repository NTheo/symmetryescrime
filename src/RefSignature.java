import Jcg.geometry.Vector_3;
import Jcg.polyhedron.Halfedge;
import Jcg.polyhedron.Vertex;

/**
 * This class computes and stores principal directions and curvatures for a vertex
 * @author Antoine & NTheo (2012)
 *
 */
public class RefSignature {
	private Vertex v;
	private Vector_3 normale;
	private double angleSum;
	public boolean isValid(){return true;}
	public Vertex getVertex(){return v;}
	public RefSignature(Vertex vRoot){
		this.angleSum = 0.;
		this.normale = new Vector_3(0., 0., 0.);
		Halfedge eRoot = vRoot.getHalfedge();
		Halfedge e = vRoot.getHalfedge();
		do{
			angleSum+=e.angle();
			normale = normale.sum(e.getFace().normale());
			e = e.getNext().getOpposite();
		}while(e!=eRoot);
		normale = normale.divisionByScalar(Math.sqrt(this.normale.squaredLength().doubleValue()));
		v = vRoot;
		System.out.println(angleSum);
	}
	public double getAngleSum(){return angleSum;}
	public Vector_3 getNormale(){return normale;}
}
