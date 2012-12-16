import java.util.List;


public class ReflectionSpace extends KDTree2<Reflection> {
	public ReflectionSpace(SignatureMap SM){
		super(6);
		System.out.println("Pairing...");
		List<Signature> neighbors;
		Reflection r;
		for(Signature s: SM.p){
			neighbors = SM.q.getRange(new double[]{s.getPrincipalCurvature1()-Parameters.pairingRange/2,
					s.getPrincipalCurvature2()-Parameters.pairingRange/2},
					new double[]{s.getPrincipalCurvature1()+Parameters.pairingRange/2,
					s.getPrincipalCurvature2()+Parameters.pairingRange/2});
			System.out.print("neighbor.size() : "+neighbors.size()+" -> ");
			for(Signature t: neighbors){
				r=new Reflection(s, t);
				if(r.valid)
					this.add(r.r, r);
			}
			System.out.println(neighbors.size());
		}
	}
	
}
