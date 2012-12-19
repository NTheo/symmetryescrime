import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;
import Jcg.polyhedron.Vertex;


/**
 * Computes a Reflection form two Signatures, stores it and displays it
 * @author Antoine & NTheo (2012)
 *
 */
public class Reflection {
	public double[] r;
	protected double weight;
	private Vertex v1,v2;
	public boolean valid;
	public double validityValue;   // debug
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
		v1 = s1.getVertex();
		v2 = s2.getVertex();
		Vector_3 n2i = new Vector_3(new Point_3(2*r[0], 2*r[1], 2*r[2]), this.image(new Point_3(0,0,0).plus(s2.getNormale())));		
		this.valid = (n2i.difference(s1.getNormale())).squaredLength().doubleValue() < Parameters.reflectionThreshold;
		this.validityValue=(n2i.difference(s1.getNormale())).squaredLength().doubleValue();
	}
	
	public Reflection(RefSignature s1, RefSignature s2){
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
		v1 = s1.getVertex();
		v2 = s2.getVertex();
		Vector_3 n2i = new Vector_3(new Point_3(2*r[0], 2*r[1], 2*r[2]), this.image(new Point_3(0,0,0).plus(s2.getNormale())));
		this.valid = (n2i.difference(s1.getNormale())).squaredLength().doubleValue() < Parameters.reflectionThreshold;
	}
	
	// Must be used carefully !
    public Reflection(double[] ds) {
		this.r=ds.clone();
	}
    
	public Vertex getV1() {
		return v1;
	}

	public Vertex getV2() {
		return v2;
	}
    
    public Point_3 image(Point_3 p){
    	Vector_3 n = new Vector_3(r[3], r[4], r[5]);
    	Point_3 proj = new Point_3(r[0], r[1], r[2]);
    	return p.plus(n.multiplyByScalar(n.innerProduct(new Vector_3(proj, p)).doubleValue()*(-2.)));
    }
    
	// display the reflection plane
	public void displayReflectionPlane(MeshViewer MV){
		
		double scale=MV.mesh.scaleFactor;				
			
		MV.translate((float) (r[0]*scale),(float) (r[1]*scale), (float) (r[2]*scale));		
		Vector_3 normal = new Vector_3(r[3],r[4],r[5]);

		MV.rectMode(2);
		MV.fill(128+this.hashCode()%127,128+this.hashCode()%127,128+(2*this.hashCode())%127,100);
		MV.stroke(128+this.hashCode()%127,128+this.hashCode()%127,128+(2*this.hashCode())%127);
		float alpha, beta;
		
		if(Math.abs(normal.z) != 1.f){
			// alpha = acos(-z2/sqrt(1-z3²)
			alpha = (float) Math.acos(-normal.y/Math.sqrt(1-normal.z*normal.z));
			// beta = acos(z3);
			beta = (float) Math.acos(normal.z); 
		}
		else{
			// the normal and the z axis are already aligned
			alpha=0.f;
			beta= (float) Math.acos(normal.z);
		}
		
		MV.rotateZ(alpha);
		MV.rotateX(beta);
		
		MV.rect(0,0,(float) (scale*Parameters.maxDistance*.7), (float) (scale *Parameters.maxDistance*.7));

		MV.rotateX(-beta);
		MV.rotateZ(-alpha);
		MV.translate((float) (-r[0]*scale),(float) (-r[1]*scale), (float) (-r[2]*scale));
	}
	
	// Display some segments to provide a rough visualization of the reflection plane
	public void displayQuickPlane(MeshViewer mv){
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
	
	// Display the two points that have been used to compute this reflection
	public void displayPairOfPoints(MeshViewer mv){
		mv.noStroke();
		mv.fill(this.hashCode()%255,(2*this.hashCode())%255,(3*this.hashCode())%255);
		mv.mesh.drawVertex(v1.getPoint());
		mv.mesh.drawVertex(v2.getPoint());
		
		mv.stroke(this.hashCode()%255,(2*this.hashCode())%255,(3*this.hashCode())%255);
		mv.mesh.drawSegment(v1.getPoint(), v2.getPoint());
	}

	// Display the two points that have been used to compute this reflection, their normals and the reflection plane
	public void displayPointsAndNormals(MeshViewer mv){
		
		Point_3 p1=v1.getPoint();
		Point_3 p2=v2.getPoint();
		
		mv.stroke(0,0,250);
		Vector_3 direction = ((Vector_3) p2.minus(p1)).normalized();
		direction = direction.multiplyByScalar(mv.mesh.scaleFactor/6);
		mv.mesh.drawSegment(p1.plus(direction.opposite()), p2.plus(direction));
		
		mv.noStroke();
		mv.fill(50, 200, 50);		
		mv.mesh.drawVertex(p1);
		mv.mesh.drawVertex(p2);
		
		Signature s1 = new Signature(v1);
		Signature s2 = new Signature(v2);
		
		Vector_3 n1=s1.getNormale().normalized();
		Vector_3 n2=s2.getNormale().normalized();
		
		//Recompute the image of the 2 points
		Vector_3 n2i = new Vector_3(new Point_3(2*r[0], 2*r[1], 2*r[2]), this.image(new Point_3(0,0,0).plus(s2.getNormale())));
		double scale = mv.mesh.scaleFactor/5;
		
		n1=n1.multiplyByScalar(scale);
		n2=n2.multiplyByScalar(scale);
		n2i=n2i.multiplyByScalar(scale);

		mv.stroke(250.0f,250.0f,250.0f);
		mv.mesh.drawSegment(p1, p1.plus(n1));
		mv.mesh.drawSegment(p2, p2.plus(n2));
		mv.stroke(255,255,0);
		mv.mesh.drawSegment(p1, p1.plus(n2i));
		
		//System.out.println("Reflection has a validity value of : "+this.validityValue);
		
		this.displayReflectionPlane(mv);
	}
}

