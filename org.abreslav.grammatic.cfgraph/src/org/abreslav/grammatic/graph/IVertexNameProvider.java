package org.abreslav.grammatic.graph;

import org.abreslav.grammatic.cfgraph.ControlFlowVertex;

public interface IVertexNameProvider {
	
	IVertexNameProvider DEFAULT = new IVertexNameProvider() {

		@Override
		public String getVertexName(ControlFlowVertex vertex) {
			return String.format("%s_%x", vertex.getClass().getSimpleName(), System.identityHashCode(vertex));
		}
		
	};
	
	String getVertexName(ControlFlowVertex vertex);
}
