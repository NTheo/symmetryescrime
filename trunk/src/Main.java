import Jcg.geometry.Point_3;
import Jcg.polyhedron.Face;
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
	Sampling sample;
	SignatureMap signatures;
	
	// This function will monitor all the steps of our program
	@Override
	public void setup() {
		super.setup();	
		
		this.sample = new UniformSampling(this.mesh.polyhedron3D);

		this.signatures = new SignatureMap(this.sample);
	}
	
	
	@Override
	public void draw(){
		super.draw();

		this.mesh.draw();
		//this.sample.display2(this);
		//this.signatures.displayPoints(this);
		//this.signatures.displayNormalized(this);
		this.strokeWeight(1);
		this.stroke(0.f, 1250.f, 150.f);
		for(Face f: this.mesh.polyhedron3D.facets){
			Point_3 p = new Point_3();
			Point_3[] tri = new Point_3[3];
			tri[0] = f.getEdge().getVertex().getPoint();
			tri[1] = f.getEdge().getNext().getVertex().getPoint();
			tri[2] = f.getEdge().getNext().getNext().getVertex().getPoint();
			p.barycenter(tri);
			this.mesh.drawSegment(p, p.plus(f.normale()));
		}
	}
	
	
	public static void main(String[] args) {
		PApplet.main(new String[] { "Main",filename});		
	}

}
