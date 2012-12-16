import java.util.List;

import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;
import processing.core.PApplet;


/**
 * @author Antoine & NTheo (2012)
 *
 */
public class Main extends MeshViewer {

	// BE CAREFUL : if you launch MeshViewer as a Java Applet, remove "src/" from the filepath .
	//static String filename="src/OFF/tri_round_cube.off";
	static String filename="src/OFF/tri_triceratops.off";
	//static String filename="src/OFF/bunny.off";
	Sampling sample;
	SignatureMap signatures;
	ReflectionSpace reflections;
	MeanShiftClustering clusters;
	Reflection test2;
	Signature s1,s2;
	
	// This function will monitor all the steps of our program
	@Override
	public void setup() {
		super.setup();	
		
		Parameters.init(this);
		
		this.sample = new FarthestPointSampling(this.mesh.polyhedron3D);
		this.signatures = new SignatureMap(this.sample);
		this.reflections = new ReflectionSpace(signatures);
		this.clusters = new MeanShiftClustering(reflections);
				
		// test reflection
		List<Signature> list=this.signatures.getSignatures();
		this.s1=list.get((int)(Math.random()*list.size()));
		this.s2=list.get((int)(Math.random()*list.size()));
		this.test2 = new Reflection(s1,s2);
	}
	
	
	@Override
	public void draw(){
		super.draw();

		//this.mesh.drawAxis();
		this.mesh.draw();
		//this.sample.displaySpheres(this);
		//this.signatures.displayPoints(this);
		//this.signatures.displayNormalized(this);
		//this.signatures.displayCustom(this);
		//this.signatures.displaySpheres(this);
		
//		Vector_3 n = (new Vector_3(.2,.6,.3)).normalized();
//		Vector_3 p = n.multiplyByScalar(2);
//		Reflection test = new Reflection(new double[]{p.x,p.y,p.z,n.x,n.y,n.z});

//		Point_3 p1=s1.getVertex().getPoint();
//		Point_3 p2=s2.getVertex().getPoint();
//		
//		this.stroke(0,0,250);
//		Vector_3 direction = ((Vector_3) p2.minus(p1)).normalized();
//		direction = direction.multiplyByScalar(this.mesh.scaleFactor/6);
//		this.mesh.drawSegment(p1.plus(direction.opposite()), p2.plus(direction));
//		
//		this.noStroke();
//		this.fill(50, 200, 50);		
//		this.mesh.drawVertex(p1);
//		this.mesh.drawVertex(p2);

//		this.signatures.displayNeighborsNumber(this);
				
		//test.display(this);
		//this.test2.display(this);
		for(int i=0; i<this.clusters.clusters.size(); i++){
			this.clusters.clusters.get(i).r.display(this);
		}
		//this.clusters.clusters.get(0).r.display(this);

		for(Reflection r:this.reflections.getRange(
				new double[]{Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE},
				new double[]{Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE}
				)){
			if(r.valid){
				r.display3(this);
			}
		}
	}
	
	
	public static void main(String[] args) {
		//while(true){System.out.println("ôigbiypgiyv");}
		PApplet.main(new String[] { "Main",filename});		
	}

}
