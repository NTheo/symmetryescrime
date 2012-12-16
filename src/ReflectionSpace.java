import java.util.List;


public class ReflectionSpace extends KDTree2<Reflection> {
	public ReflectionSpace(SignatureMap SM){
		super(6);
		System.out.print("Pairing...");
		List<Signature> neighbors;
		Reflection r;
		for(Signature s: SM.p){
			neighbors = SM.q.getRange(new double[]{s.getPrincipalCurvature1()-Parameters.pairingRange/2,
					s.getPrincipalCurvature2()-Parameters.pairingRange/2},
					new double[]{s.getPrincipalCurvature1()+Parameters.pairingRange/2,
					s.getPrincipalCurvature2()+Parameters.pairingRange/2});
			System.out.print(neighbors.size()+" ");
			for(Signature t: neighbors){
				r=new Reflection(s, t);
				if(r.valid)
					this.add(r.r, r);
			}
		}
		System.out.println("ok");
	}
	
	public static void display(SignatureMap SM, MeshViewer mv){
		List<Signature> neighbors;
		for(Signature s: SM.p){
			neighbors = SM.q.getRange(new double[]{s.getPrincipalCurvature1()-Parameters.pairingRange/2,
					s.getPrincipalCurvature2()-Parameters.pairingRange/2},
					new double[]{s.getPrincipalCurvature1()+Parameters.pairingRange/2,
					s.getPrincipalCurvature2()+Parameters.pairingRange/2});
			mv.noStroke();
			mv.fill(6*neighbors.size()%255, 2*neighbors.size()%255, 6*neighbors.size()%255);		
			mv.mesh.drawVertex(s.getVertex().getPoint());
		}
	}
}
