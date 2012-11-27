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
	
	// This function will monitor all the steps of our program
	@Override
	public void setup() {
		super.setup();	
		
		this.sample = new RandomSampling(this.mesh.polyhedron3D);
	}
	
	
	@Override
	public void draw(){
		super.draw();

		sample.display2(this);	
		this.mesh.draw();
	}
	
	
	public static void main(String[] args) {
		PApplet.main(new String[] { "Main",filename});		
	}

}
