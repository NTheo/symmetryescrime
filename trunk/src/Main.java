import java.util.List;

import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;
import processing.core.PApplet;


/**
 * @author Antoine & NTheo (2012)
 *
 */
@SuppressWarnings("serial")
public class Main extends MeshViewer {

	// BE CAREFUL : if you launch MeshViewer as a Java Applet, remove "src/" from the filepath .
	//static String filename="src/OFF/tri_round_cube.off";
	static String filename="src/OFF/tri_triceratops.off";
	//static String filename="src/OFF/bunny.off";
	//static String filename="src/OFF/OFF/OFF_various/cow.off";
	Sampling sample;
	SignatureMap signatures;
	//RefSignatureMap signatures;
	ReflectionSpace reflections;
	MeanShiftClustering clustering;
	static int viewIndex=0;
	
	// test reflection
	Reflection test2;
	Signature s1,s2;
	//RefSignature s1,s2;
	
	// This function will monitor all the steps of our program
	@Override
	public void setup() {
		super.setup();	
		
		Parameters.init(this);
		
		System.out.println("The scaleFactor is "+this.mesh.scaleFactor);
		
		this.sample = new FarthestPointSampling(this.mesh.polyhedron3D);
		this.signatures = new SignatureMap(this.sample);
		//this.signatures = new RefSignatureMap(this.sample);
		this.reflections = new ReflectionSpace(signatures);
		this.clustering = new MeanShiftClustering(reflections);
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

//		this.signatures.displayNeighborsNumber(this);	
//		this.signatures.displayCorrespondingPointsInSignatureSpace(this);

//		this.displayAllReflectionPairsOfPoints();
//		this.displayOneReflectionPairOfPointsAndNormals();

		this.clustering.displayOneCluster(this);
//		this.displayAllReflectionPlanes();
	}
	
	public void displayAllReflectionPlanes(){
		for(int i=0; i<Math.min(this.clustering.clusters.size(), viewIndex+1);i++){
			this.clustering.clusters.get(i).r.display(this);
		}
	}
	
	// Display the pairs of points that have been used to generate Reflections in ReflectionSPace
	public void displayAllReflectionPairsOfPoints(){
		for(Reflection r:this.reflections.getAll()){
				r.display3(this);
		}
	}
	
	// Display the 2 points that have been used to compute a Reflection, and their normals
	public void displayOneReflectionPairOfPointsAndNormals(){
		List<Reflection> lr = this.reflections.getAll();
		if(lr.size()>0){
			Reflection r = lr.get(viewIndex%lr.size());
			if(r.valid){ 
				r.display4(this);
				System.out.println("validity value = "+r.validityValue);
			}
			else
				System.out.println("This reflection is not valid.");
		}else{
			System.out.println("There are no reflections in ReflectionSpace !");
		}
	}
	
	// Test the display of a plane corresponding to an arbitrary reflection
	public void testDisplay(){
		Vector_3 n = (new Vector_3(.2,.6,.3)).normalized();
		Vector_3 p = n.multiplyByScalar(2);
		Reflection test = new Reflection(new double[]{p.x,p.y,p.z,n.x,n.y,n.z});
		test.display(this);
	}
	
	// Test the display of a reflection computed from two Signatures (taken from SignatureMap)
	public void testDisplay2(){
		// Get the Signatures
		List<Signature> list=this.signatures.getSignatures();
		//List<RefSignature> list=this.signatures.getSignatures();
		this.s1=list.get((int)(Math.random()*list.size()));
		this.s2=list.get((int)(Math.random()*list.size()));
		this.test2 = new Reflection(s1,s2);
		
		
		Point_3 p1=s1.getVertex().getPoint();
		Point_3 p2=s2.getVertex().getPoint();
		
		this.stroke(0,0,250);
		Vector_3 direction = ((Vector_3) p2.minus(p1)).normalized();
		direction = direction.multiplyByScalar(this.mesh.scaleFactor/6);
		this.mesh.drawSegment(p1.plus(direction.opposite()), p2.plus(direction));
		
		this.noStroke();
		this.fill(50, 200, 50);		
		this.mesh.drawVertex(p1);
		this.mesh.drawVertex(p2);
		
		this.test2.display(this);
	}

	public void keyPressed(){
		viewIndex++;
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "Main",filename});		
	}

}
