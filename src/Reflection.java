import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;
import Jcg.polyhedron.Vertex;


public class Reflection {
	public double[] r;
	protected double weight;
	private Vertex v1,v2;

	/**
	 * @return the v1
	 */
	public Vertex getV1() {
		return v1;
	}
	/**
	 * @return the v2
	 */
	public Vertex getV2() {
		return v2;
	}
	public int cluster;
	public Reflection(Signature s1, Signature s2) throws Exception{
		r = new double[6];
		Vector_3 n = (Vector_3) s2.getVertex().getPoint().minus(s1.getVertex().getPoint());
		n.normalize();
		if (n.x<=0.){
			if(n.x<0.)
				n = n.opposite();
			else if (n.y<=0.){
				if(n.y<0.)
					n = n.opposite();
				else if (n.z <= 0.){
					if(n.z<0)
						n=n.opposite();
					else throw new Exception("ouegrzipyrgpZYIFT�ZG�UZherouHEORUHRQETOUzht�UZ�PPP");
				}
			}
		}
		Vector_3 middle = (Vector_3) s2.getVertex().getPoint().minus(new Point_3()).sum(s1.getVertex().getPoint().minus(new Point_3())).divisionByScalar(2.);
		Vector_3 point = n.multiplyByScalar(middle.innerProduct(n));
		r[0] = point.x;
		r[1] = point.y;
		r[2] = point.z;
		r[3] = n.x;
		r[4] = n.y;
		r[5] = n.z;
		v1 = s1.getVertex();
		v2 = s2.getVertex();
		cluster = -1;
	}
}
