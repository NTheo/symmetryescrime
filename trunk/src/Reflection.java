import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;


public class Reflection {
	protected double px;
	protected double py;
	protected double pz;
	protected double nx;
	protected double ny;
	protected double nz;
	protected double weight;
	public double getPx() {
		return px;
	}
	public double getPy() {
		return py;
	}
	public double getPz() {
		return pz;
	}
	public double getNx() {
		return nx;
	}
	public double getNy() {
		return ny;
	}
	public double getNz() {
		return nz;
	}
	public Reflection(Signature s1, Signature s2) throws Exception{
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
					else throw new Exception("ouegrzipyrgpZYIFTÜZGÖUZherouHEORUHRQETOUzhtÖUZÖPPP");
				}
			}
		}
		Vector_3 middle = (Vector_3) s2.getVertex().getPoint().minus(new Point_3()).sum(s1.getVertex().getPoint().minus(new Point_3())).divisionByScalar(2.);
		Vector_3 point = n.multiplyByScalar(middle.innerProduct(n));
		px = point.x;
		py = point.y;
		pz = point.z;
		nx = n.x;
		ny = n.y;
		nz = n.z;
	}
}
