package Jcg.polyhedron;

import java.awt.image.SampleModel;
import java.security.Policy.Parameters;
import java.util.LinkedList;
import java.util.Queue;

import Jcg.geometry.*;


public class Vertex{

Halfedge halfedge=null;
Point_3 point=null;
public int tag;
public int index;

    public Vertex(Point_3 point) { this.point=point; }
    public Vertex() {}

    public void setEdge(Halfedge halfedge) { this.halfedge=halfedge; }
    public void setPoint(Point_3 point) { this.point=point; }  
    
    public Point_3 getPoint() { return this.point; } 
    public Halfedge getHalfedge() { return this.halfedge; } 
    
    public String toString(){
        return "v"+point.toString();
    }
    /**
     * computes the K (but does not return the principal directions)
     * @return the k
     */
    public double[] curvatures(double radius) {
    	//TODO return the k
    	//the queue of vertexes we need to see in our BFS.
    	Queue<Vertex> q = new LinkedList<Vertex>();
    	//the current position of our algorithm
    	Halfedge e; Vertex v; Face f;
    	q.add(this);
    	while(!q.isEmpty()){
    		v = q.poll();
    		e = v.halfedge;
    		f = e.face;
    		
    	}
    	return new double[2];
    	
    }
}


