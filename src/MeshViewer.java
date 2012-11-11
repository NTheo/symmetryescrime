import Jcg.geometry.Point_3;
import Jcg.polyhedron.Vertex;
import processing.core.*;


/**
 * A simple 3d viewer for visualizing surface meshes
 * 
 * @author Luca Castelli Aleardi (INF555, 2012)
 *
 */
@SuppressWarnings("serial")
public class MeshViewer extends PApplet {

	SurfaceMesh mesh;
	// BE CAREFUL : if you launch MeshViewer as a Java Applet, remove "src/" from the filepath
	//String filename="src/OFF/tri_round_cube.off";
	String filename="src/OFF/tri_triceratops.off";
	Sampling sample;

	public void setup() {
		size(800,600,P3D);

		@SuppressWarnings("unused")
		ArcBall arcball = new ArcBall(this);

		this.mesh=new SurfaceMesh(this, filename);
		this.mesh.polyhedron3D.isValid(false);
		
		this.sample = new RandomSampling(this.mesh.polyhedron3D);
	}

	public void displaySample(){
		float s = (float) this.mesh.scaleFactor;
		for(Vertex<Point_3> v : this.sample.vertices){		
			pushMatrix();
			translate((float)(double) v.getPoint().x*s, (float)(double) v.getPoint().y*s,(float)(double) v.getPoint().z*s);
			stroke(255);
			sphere(s/5.f);
			popMatrix();
		}
	}
	
	public void displaySample2(){
		for(Vertex<Point_3> v : this.sample.vertices){
			this.mesh.drawSegment(v.getPoint(), new Point_3(v.getPoint().x*1.05,v.getPoint().y*1.05,v.getPoint().z*1.05));
		}
	}
	
	public void draw() {
		background(0);
		//this.lights();
		directionalLight(101, 204, 255, -1, 0, 0);
		directionalLight(51, 102, 126, 0, -1, 0);
		directionalLight(51, 102, 126, 0, 0, -1);
		directionalLight(102, 50, 126, 1, 0, 0);
		directionalLight(51, 50, 102, 0, 1, 0);
		directionalLight(51, 50, 102, 0, 0, 1);

		translate(width/2.f,height/2.f,-1*height/2.f);
		this.strokeWeight(1);
		stroke(150,150,150);

		displaySample2();
		
		this.mesh.draw();
	}

}
