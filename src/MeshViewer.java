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

	public void setup() {
		size(800,600,P3D);

		@SuppressWarnings("unused")
		ArcBall arcball = new ArcBall(this);
		
		this.mesh=new SurfaceMesh(this, args[0]);
		//this.mesh.polyhedron3D.isValid(false);
	}
	
	public void draw(){
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
	}
}
