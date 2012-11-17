 package Jcg.polyhedron;

import Jama.Matrix;
import Jcg.geometry.Vector_3;


/**
 * Class for representing half-edges
 *
 * @author Code by Luca Castelli Aleardi (INF555, 2012)
 * @author modified by Antoine & NTheo

 */
public class Halfedge{

Halfedge next;
Halfedge opposite;
Vertex vertex;

Halfedge prev;
Face face;
public int tag;
public int index;

	public void setNext(Halfedge e) { this.next=e; }
	public void setOpposite(Halfedge e) { this.opposite=e; }
	public void setPrev(Halfedge e) { this.prev=e; }
	public void setVertex(Vertex v) { this.vertex=v; }
	public void setFace(Face f) { this.face=f; }
	
	public Halfedge getNext() { return this.next; }
	public Halfedge getOpposite() { return this.opposite; }
	public Halfedge getPrev() { return this.prev; }
	public Vertex getVertex() { return this.vertex; }
	public Face getFace() { return this.face; }
	
	/**
	 * @author NTheo & Antoine
	 * @return the associated vector
	 */
	public Vector_3 vector(){
		return new Vector_3(this.getOpposite().getVertex().getPoint(), this.getVertex().getPoint());
	}
	

	public double angle(){
		return this.vector().opposite().angle(this.getNext().vector());
	}
    public Halfedge() {}
    
    public String toString(){
    	return "("+opposite.getVertex().getPoint()+" - "+vertex.getPoint()+")";
    }
    public Matrix toColumn(){
    	return this.vector().toColumn();
    }
    public Matrix toLine(){
    	return this.vector().toLine();
    }
}


