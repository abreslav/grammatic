import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.abreslav.grammatic.atf.types.ITypeSystem;
import org.abreslav.grammatic.atf.types.unification.IStringRepresentationProvider;
import org.abreslav.grammatic.atf.types.unification.ISubtypingRelation;
import org.abreslav.grammatic.atf.types.unification.ITypeErrorHandler;
import org.abreslav.grammatic.atf.types.unification.TypeVariable;
import org.abreslav.grammatic.atf.types.unification.graph.IGraph;
import org.abreslav.grammatic.atf.types.unification.graph.impl.Graph;
import org.abreslav.grammatic.atf.types.unification.impl.ConstraintSystem;
import org.abreslav.grammatic.atf.types.unification.impl.ConstraintSystemSolver;
import org.abreslav.grammatic.atf.types.unification.impl.FiniteTypeSystem;
import org.abreslav.grammatic.atf.types.unification.impl.FiniteTypeSystemClosedConstraintSolver;
import org.abreslav.grammatic.utils.IHashingStrategy;


public class TypeTest {

	public static void main(String[] args) {
//		example();
		simpleTypes();
	}

	@SuppressWarnings("unused")
	private static void example() {
		/*
		null() --> (Tnull result);
		Tnull <= Expression;
		Tm m;
		Tm >= Mult;
		Tmos multOrSum;
		Tmos >= Tm;
		Ts sign;
		Ts >= int;
		minusOne() --> (Tmo result);
		Ts >= Tmo;
		appendToSum(T1, T2, T3, T4) -> Tats;
		Tats <= Expression;
		T1 >= Expression;
		T2 >= Ts;
		T3 >= Tm;
		T4 >= Tmos;
		Tmos >= Expression;
		Tmos <= Expression; 		
		 */
				IGraph<String> typeGraph = new Graph<String>();
				typeGraph.addVertex("Expression");
				typeGraph.addVertex("Mult");
				typeGraph.addVertex("int");
				typeGraph.addEdge("Mult", "Expression");
				
				final FiniteTypeSystem<String> typeSystem = new FiniteTypeSystem<String>(typeGraph, IHashingStrategy.DEFAULT, new IStringRepresentationProvider<String>() {
		
					@Override
					public String getStringRepresentation(String type) {
						return type;
					}
					
				}, ITypeErrorHandler.ERROR_HANDLER);
				FiniteTypeSystemClosedConstraintSolver<String> solver = new FiniteTypeSystemClosedConstraintSolver<String>(typeSystem);
				
				TypeVariable Tnull = new TypeVariable("Tnull");
				TypeVariable Tm = new TypeVariable("Tm");
				TypeVariable Tmos = new TypeVariable("Tmos");
				TypeVariable Ts = new TypeVariable("Ts");
				TypeVariable Tmo = new TypeVariable("Tmo");
				TypeVariable T1 = new TypeVariable("T1");
				TypeVariable T2 = new TypeVariable("T2");
				TypeVariable T3 = new TypeVariable("T3");
				TypeVariable T4 = new TypeVariable("T4");
				TypeVariable Tats = new TypeVariable("Tats");
				
				ConstraintSystem<String, RuntimeException> constraintSystem = new ConstraintSystem<String, RuntimeException>(typeSystem, ITypeErrorHandler.ERROR_HANDLER);
				constraintSystem.addConstraint(Tnull, "Mult");
				constraintSystem.addConstraint(Tnull, "Expression");
				constraintSystem.addConstraint("Mult", Tm);
				constraintSystem.addConstraint(Tm, Tmos);
				constraintSystem.addConstraint("int", Ts);
				constraintSystem.addConstraint(Tmo, Ts);
				constraintSystem.addConstraint(Tats, "Expression");
				constraintSystem.addConstraint("Expression", T1);
				constraintSystem.addConstraint(Ts, T2);
				constraintSystem.addConstraint(Tm, T3);
				constraintSystem.addConstraint(Tmos, T4);
				constraintSystem.addConstraint("Expression", Tmos);
				constraintSystem.addConstraint(Tmos, "Expression");
				
				
				System.out.println(ConstraintSystemSolver.solve(constraintSystem, solver, ITypeErrorHandler.ERROR_HANDLER));
	}

	private static void simpleTypes() {
		TypeVariable a = new TypeVariable("a");
		TypeVariable b = new TypeVariable("b");
		TypeVariable c = new TypeVariable("c");
		TypeVariable d = new TypeVariable("d");
		
		ISubtypingRelation<Types> subtypingRelation = new ISubtypingRelation<Types>() {
			
			@Override
			public boolean isSubtypeOf(Types type, Types supertype) {
				return type.isSubtypeOf(supertype);
			}
			
		};
		ConstraintSystem<Types, RuntimeException> builder = new ConstraintSystem<Types, RuntimeException>(subtypingRelation, ITypeErrorHandler.ERROR_HANDLER);
		builder.addConstraint(a, b);
		builder.addConstraint(b, c);
		builder.addConstraint(c, a);
		builder.addConstraint(d, a);
		builder.addConstraint(Types.D, d);
		builder.addConstraint(Types.A, a);
		
		ITypeSystem<Types> typeSystem = new FiniteTypeSystem<Types>(EnumSet.allOf(Types.class), subtypingRelation, IHashingStrategy.DEFAULT, new IStringRepresentationProvider<Types>() {

			@Override
			public String getStringRepresentation(Types type) {
				return type.toString();
			}
			
		}, ITypeErrorHandler.ERROR_HANDLER);
		FiniteTypeSystemClosedConstraintSolver<Types> solver = new FiniteTypeSystemClosedConstraintSolver<Types>(typeSystem);

		System.out.println("result=" + ConstraintSystemSolver.solve(builder, solver, ITypeErrorHandler.ERROR_HANDLER));
	}

	enum Types {
		A {

			@Override
			protected Set<Types> getSupertypes() {
				return types();
			}
		}, 
		B {

			@Override
			protected Set<Types> getSupertypes() {
				return types(A);
			} 			
		}, 
		C {
			@Override
			protected Set<Types> getSupertypes() {
				return types(A);
			}
		}, 
		D {
			@Override
			protected Set<Types> getSupertypes() {
				return types(B, A);
			}
		};
		
		protected Set<Types> types(Types... types) {
			return new HashSet<Types>(Arrays.asList(types));
		}
		
		public boolean isSubtypeOf(Types t) {
			return getSupertypes().contains(t);
		}
		
		protected abstract Set<Types> getSupertypes();
	}
}
