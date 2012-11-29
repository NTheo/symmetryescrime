import processing.core.PApplet;


/**
 * @author Antoine & NTheo (2012)
 *
 */
@SuppressWarnings("serial")
public class Main extends MeshViewer {

	// BE CAREFUL : if you launch MeshViewer as a Java Applet, remove "src/" from the filepath
	//String filename="src/OFF/tri_round_cube.off";
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
		this.signatures.display(this);
	}
	
	
	public static void main(String[] args) {
		PApplet.main(new String[] { "Main",filename});		
	}

}
