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
}


