package org.abreslav.grammatic.graph;

import java.util.Collection;
import java.util.Collections;

import org.abreslav.grammatic.cfgraph.BranchingVertex;
import org.abreslav.grammatic.cfgraph.ControlFlowEdge;
import org.abreslav.grammatic.cfgraph.ControlFlowVertex;
import org.abreslav.grammatic.cfgraph.EndVertex;
import org.abreslav.grammatic.cfgraph.StartVertex;
import org.abreslav.grammatic.cfgraph.StatementVertex;
import org.abreslav.grammatic.cfgraph.util.CfgraphSwitch;
import org.eclipse.emf.ecore.EObject;

public class VertexUtil {

	private static final CfgraphSwitch<Collection<ControlFlowEdge>> OUTGOING_EDGE_COLLECTOR = new CfgraphSwitch<Collection<ControlFlowEdge>>() {
		@Override
		public Collection<ControlFlowEdge> caseBranchingVertex(BranchingVertex object) {
			return Collections.unmodifiableCollection(object.getBranches());
		}
		
		@Override
		public Collection<ControlFlowEdge> caseStatementVertex(StatementVertex object) {
			return Collections.singleton(object.getNext());
		}
		
		@Override
		public Collection<ControlFlowEdge> caseStartVertex(StartVertex object) {
			return Collections.singleton(object.getNext());
		}
		
		@Override
		public Collection<ControlFlowEdge> caseEndVertex(EndVertex object) {
			return Collections.emptySet();
		}
		
		@Override
		public Collection<ControlFlowEdge> defaultCase(EObject object) {
			throw new UnsupportedOperationException();
		}
	};
	
	public static Collection<ControlFlowEdge> getOutgoingEdges(ControlFlowVertex vertex) {
		return OUTGOING_EDGE_COLLECTOR.doSwitch(vertex);
	}
}
