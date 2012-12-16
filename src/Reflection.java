import Jama.Matrix;
import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;
import Jcg.polyhedron.Vertex;


public class Reflection {
	public double[] r;
	protected double weight;
	private Vertex v1,v2;
	public boolean valid;
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
	public int cluster=-1;
	public Reflection(Signature s1, Signature s2){
		r = new double[6];
		Vector_3 n = (Vector_3) s2.getVertex().getPoint().minus(s1.getVertex().getPoint());
		n=n.normalized();
		if (n.x<=0.){
			if(n.x<0.)
				n = n.opposite();
			else if (n.y<=0.){
				if(n.y<0.)
					n = n.opposite();
				else if (n.z <= 0.){
					if(n.z<0)
						n=n.opposite();
					else{ 
						System.out.println("Trying to compute a transformation with two identical points !");
						System.exit(1);
					}
				}
			}
		}

//		Vector_3 middle = (Vector_3) s2.getVertex().getPoint().minus(new Point_3()).sum(s1.getVertex().getPoint().minus(new Point_3())).divisionByScalar(2.);
		Point_3 mid = Point_3.linearCombination(new Point_3[]{s1.getVertex().getPoint(),s2.getVertex().getPoint()},
				new Number[]{0.5d,0.5d});
		Vector_3 middle = new Vector_3(new Point_3(0,0,0),mid);
		Vector_3 proj = n.multiplyByScalar(middle.innerProduct(n));
		r[0] = proj.x;
		r[1] = proj.y;
		r[2] = proj.z;
		r[3] = n.x;
		r[4] = n.y;
		r[5] = n.z;

		this.valid = n.crossProduct(s1.getNormale()).sum(n.crossProduct(s2.getNormale())).squaredLength().doubleValue() < Parameters.reflectionThreshold;
	}

	public Reflection(double[] ds) {
		this.r=ds.clone();
	}
	// display the reflection plane
	public void display(MeshViewer MV){
		
		double scale=MV.mesh.scaleFactor;				
				
		MV.noStroke();
		MV.fill(250,0,0);
		MV.translate((float) (r[0]*scale),(float) (r[1]*scale), (float) (r[2]*scale));
		MV.sphere((float) MV.mesh.scaleFactor/3);
		
		Vector_3 normal = new Vector_3(r[3],r[4],r[5]);
		MV.stroke(255,255,255);
		MV.line(0, 0, 0, (float) (normal.x*scale*10),(float) (normal.y*scale*10),(float) (normal.z*scale*10));
				
		MV.rectMode(2);
		MV.fill(255,255,255,150);	
		float alpha, beta, gamma;
		if(Math.abs(normal.z) != 1.f){
			// alpha = acos(z2/sqrt(1-z3²)
			alpha = (float) Math.acos(-normal.y/Math.sqrt(1-normal.z*normal.z));
			//alpha=Math.atan2(y, x)
			// beta = acos(z3);
			beta = (float) Math.acos(-normal.z);  //-z because Processing frame is left-handed
			// gamma = acos(y3/sqrt(1-z3²)
			gamma = (float) Math.acos(-normal.x/Math.sqrt(1-normal.z*normal.z));
		}
		else{
			// do nothing : the normal and the z axis are already aligned
			alpha=0.f;
			gamma=0.f;
			beta= (float) Math.acos(-normal.z);
		}
		
		Matrix m=new Matrix(3,3);
		Matrix origine=new Matrix(new double[]{1,0,0},3);
		Matrix dest=new Matrix(new double[]{normal.x,normal.y,normal.z},3);
		
		MV.rotateZ(alpha);
		MV.rotateX(beta);
		MV.rotateZ(gamma);
		
		MV.rect(0,0,(float) (scale*Parameters.maxDistance), (float) (scale *Parameters.maxDistance));
		
		MV.rotateZ(-gamma);
		MV.rotateX(-beta);
		MV.rotateZ(-alpha);
		MV.translate((float) (-r[0]*scale),(float) (-r[1]*scale), (float) (-r[2]*scale));
	}
	
	public void display2(MeshViewer mv){
		Point_3 p = new Point_3(r[0], r[1], r[2]);
		mv.stroke(this.hashCode()%255,(2*this.hashCode())%255,(3*this.hashCode())%255);
		mv.mesh.drawSegment(p, p.plus((new Vector_3(-r[4], r[3], 0)).multiplyByScalar(150.)));
		mv.mesh.drawSegment(p, p.plus((new Vector_3(-r[5], 0, r[3])).multiplyByScalar(150.)));
		mv.mesh.drawSegment(p, p.plus((new Vector_3(0, -r[5], r[4])).multiplyByScalar(150.)));
		mv.mesh.drawSegment(p, p.plus((new Vector_3(r[4], -r[3], 0)).multiplyByScalar(150.)));
		mv.mesh.drawSegment(p, p.plus((new Vector_3(r[5], 0, -r[3])).multiplyByScalar(150.)));
		mv.mesh.drawSegment(p, p.plus((new Vector_3(0, r[5], -r[4])).multiplyByScalar(150.)));

		mv.mesh.drawSegment(p, p.plus((new Vector_3(-r[4]-r[5], r[3], r[3])).multiplyByScalar(150.)));
		mv.mesh.drawSegment(p, p.plus((new Vector_3(r[4], -r[3]-r[5], r[4])).multiplyByScalar(150.)));
		mv.mesh.drawSegment(p, p.plus((new Vector_3(r[5], r[5], -r[3]-r[4])).multiplyByScalar(150.)));
		mv.mesh.drawSegment(p, p.plus((new Vector_3(r[4]+r[5], -r[3], -r[3])).multiplyByScalar(150.)));
		mv.mesh.drawSegment(p, p.plus((new Vector_3(-r[4], r[3]+r[5], -r[4])).multiplyByScalar(150.)));
		mv.mesh.drawSegment(p, p.plus((new Vector_3(-r[5], -r[5], r[3]+r[4])).multiplyByScalar(150.)));
				
	}
}

