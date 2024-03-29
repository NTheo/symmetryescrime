package Jcg.polyhedron;

import Jcg.geometry.*;
//import Jcg.viewer.*;

/**
 * A class for testing the Half-edge data structure for representing polyhedral surfaces.
 *
 * @author Luca Castelli Aleardi
 * @author modified by Antoine & NTheo
 *
 */
public class TestPolyhedron {
 
	/**
	 * Testing a 3D surface mesh (loaded from an OFF file)
	 */    
    public static void test3D() {   	
    	SharedVertexRepresentation m=new SharedVertexRepresentation("OFF/cube.off");
    	LoadMesh load3D=new LoadMesh();
    	
    	Polyhedron_3 polyhedron3D=
    		load3D.createPolyhedron(m.points,m.faceDegrees,m.faces,m.sizeHalfedges);
    	System.out.println(polyhedron3D.verticesToString());   	
    	System.out.println(polyhedron3D.facesToString());
    	polyhedron3D.isValid(false);
    	
    	//new MeshViewer(polyhedron3D);
    }

    public static void main (String[] args) {
    	if (args.length != 1 || (!args[0].equals("2") && !args[0].equals("3"))) {
    		System.out.println("Usage : java testPolyhedron <dim>\n ou <dim> est la dimension (2 ou 3)");
    		return;
    	}
    	if (args[0].equals("2"))
    		{}//test2D();
    	else  if (args[0].equals("3"))
    		test3D();	
    }
}
