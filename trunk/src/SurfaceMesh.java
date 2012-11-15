import processing.core.PConstants;
import Jcg.geometry.*;
import Jcg.polyhedron.*;


/**
 * 
 * Class for rendering a surface triangle mesh
 * @author Luca Castelli Aleardi (INF555, 2012)
 *
 */
public class SurfaceMesh {
	
	double scaleFactor=50; // scaling factor: useful for 3d rendering
	MeshViewer view;
	public Polyhedron_3 polyhedron3D; // triangle mesh
	
	/**
	 * Create a surface mesh from an OFF file
	 */	
	public SurfaceMesh(MeshViewer view, String filename) {
		this.view=view;

		// shared vertex representation of the mesh
    	SharedVertexRepresentation sharedVertex=new SharedVertexRepresentation(filename);
    	LoadMesh load3D=new LoadMesh();
    	
    	polyhedron3D=load3D.createTriangleMesh(sharedVertex.points,sharedVertex.faceDegrees,
				sharedVertex.faces,sharedVertex.sizeHalfedges);

    	//System.out.println(polyhedron3D.verticesToString());   	
    	//System.out.println(polyhedron3D.facesToString());
    	polyhedron3D.isValid(false);
    	    	
    	this.scaleFactor=this.computeScaleFactor();
	}
	
	/**
	 * Draw a segment between two points
	 */	
	public void drawSegment(Point_3 p, Point_3 q) {
		float s=(float)this.scaleFactor;
		this.view.line(	(float)p.getX().doubleValue()*s, (float)p.getY().doubleValue()*s, 
				(float)p.getZ().doubleValue()*s, (float)q.getX().doubleValue()*s, 
				(float)q.getY().doubleValue()*s, (float)q.getZ().doubleValue()*s);
	}

	/**
	 * Draw a triangle face
	 */	
	public void drawTriangle(Point_3 p, Point_3 q, Point_3 r) {
		float s=(float)this.scaleFactor;
		view.vertex( (float)(p.getX().doubleValue()*s), (float)(p.getY().doubleValue()*s), (float)(p.getZ().doubleValue()*s));
		view.vertex( (float)(q.getX().doubleValue()*s), (float)(q.getY().doubleValue()*s), (float)(q.getZ().doubleValue()*s));
		view.vertex( (float)(r.getX().doubleValue()*s), (float)(r.getY().doubleValue()*s), (float)(r.getZ().doubleValue()*s));
	}

	/**
	 * Draw a vertex (as a small sphere)
	 */	
	public void drawVertex(Point_3 p) {
		float s=(float)this.scaleFactor;
		float x1=(float)p.getX().doubleValue()*s;
		float y1=(float)p.getY().doubleValue()*s;
		float z1=(float)p.getZ().doubleValue()*s;
		
		view.translate(x1, y1, z1);
		view.sphere(s/25f);
		view.translate(-x1, -y1, -z1);
	}
	
	/**
	 * Draw the entire mesh
	 */
	public void draw() {
		this.drawAxis();
		
		view.beginShape(PConstants.TRIANGLES);
		for(Face f: this.polyhedron3D.facets) {
			Halfedge e=f.getEdge();
			Point_3 p=e.getVertex().getPoint();
			Point_3 q=e.getNext().getVertex().getPoint();
			Point_3 r=e.getNext().getNext().getVertex().getPoint();
			
			view.noStroke();
			view.fill(200,200,200,255); // color of the triangle
			this.drawTriangle(p, q, r); // draw a triangle face
		}
		view.endShape();
		
		view.strokeWeight(2); // line width (for edges)
		view.stroke(20);
		for(Halfedge e: this.polyhedron3D.halfedges) {
			Point_3 p=e.getVertex().getPoint();
			Point_3 q=e.getOpposite().getVertex().getPoint();
			
			this.drawSegment(p, q); // draw edge (p,q)
		}
		view.strokeWeight(1);
	}
	
	/**
	 * Draw the X, Y and Z axis
	 */
	public void drawAxis() {
		double s=1;
		Point_3 p000=new Point_3(0., 0., 0.);
		Point_3 p100=new Point_3(s, 0., 0.);
		Point_3 p010=new Point_3(0.,s, 0.);
		Point_3 p011=new Point_3(0., 0., s);
		
		drawSegment(p000, p100);
		drawSegment(p000, p010);
		drawSegment(p000, p011);
	}


	/**
	 * Return the value after truncation
	 */
	public static double round(double x, int precision) {
		return ((int)(x*precision)/(double)precision);
	}
	
	/**
	 * Compute the scale factor (depending on the max distance of the point set)
	 */
	public double computeScaleFactor() {
		if(this.polyhedron3D==null || this.polyhedron3D.vertices.size()<1)
			return 1;
		double maxDistance=0.;
		Point_3 origin=new Point_3(0., 0., 0.);
		for(Vertex v: this.polyhedron3D.vertices) {
			double distance=Math.sqrt(v.getPoint().squareDistance(origin).doubleValue());
			maxDistance=Math.max(maxDistance, distance);
		}
		return Math.sqrt(3)/maxDistance*300;
	}
	
}
