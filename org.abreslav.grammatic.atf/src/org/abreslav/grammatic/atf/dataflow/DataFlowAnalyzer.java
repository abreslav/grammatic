package org.abreslav.grammatic.atf.dataflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.ATFAttribute;
import org.abreslav.grammatic.atf.ATFAttributeAssignment;
import org.abreslav.grammatic.atf.ATFAttributeReference;
import org.abreslav.grammatic.atf.ATFExpression;
import org.abreslav.grammatic.atf.ATFMetadata;
import org.abreslav.grammatic.atf.Block;
import org.abreslav.grammatic.atf.CollectionAppending;
import org.abreslav.grammatic.atf.FunctionCall;
import org.abreslav.grammatic.atf.FunctionSignature;
import org.abreslav.grammatic.atf.ISyntacticFunctionHandler;
import org.abreslav.grammatic.atf.Statement;
import org.abreslav.grammatic.atf.SyntacticFunctionTraverser;
import org.abreslav.grammatic.atf.util.AtfSwitch;
import org.abreslav.grammatic.cfgraph.BodyVertex;
import org.abreslav.grammatic.cfgraph.ControlFlowEdge;
import org.abreslav.grammatic.cfgraph.ControlFlowGraph;
import org.abreslav.grammatic.cfgraph.ControlFlowVertex;
import org.abreslav.grammatic.cfgraph.EndVertex;
import org.abreslav.grammatic.cfgraph.StartVertex;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.Production;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.grammar.SymbolReference;
import org.abreslav.grammatic.graph.TopologicalOrder;
import org.abreslav.grammatic.graph.VertexUtil;
import org.abreslav.grammatic.metadata.aspects.manager.IMetadataProvider;
import org.abreslav.grammatic.metadata.util.IMetadataStorage;
import org.abreslav.grammatic.metadata.util.MetadataStorage;
import org.abreslav.grammatic.utils.IErrorHandler;
import org.abreslav.grammatic.utils.INull;
import org.eclipse.emf.ecore.EObject;


public class DataFlowAnalyzer<E extends RuntimeException> {
	
	private static final BackwardDirection BACKWARD_DIRECTION = new BackwardDirection();
	private static final ForwardDirection FORWARD_DIRECTION = new ForwardDirection();

	public static <E extends RuntimeException> void analyzeDefiniteAssignments(
			ControlFlowGraph graph,
			GrammarGraphBuilderTrace trace,
			IMetadataProvider metadataProvider, 
			IErrorHandler<E> errorHandler) throws E {
		DataFlowAnalyzer<E> dataFlowAnalyzer = new DataFlowAnalyzer<E>(graph, trace, metadataProvider, errorHandler);
		dataFlowAnalyzer.buildOperations();
		dataFlowAnalyzer.analyze(FORWARD_DIRECTION, new DefiniteAssignmentOperationHandler<E>(errorHandler));
	}
	
	public static <E extends RuntimeException> void analyzeUnreadValues(
			ControlFlowGraph graph,
			GrammarGraphBuilderTrace trace,
			IMetadataProvider metadataProvider, 
			IErrorHandler<E> errorHandler) throws E {
		DataFlowAnalyzer<E> dataFlowAnalyzer = new DataFlowAnalyzer<E>(graph, trace, metadataProvider, errorHandler);
		dataFlowAnalyzer.buildOperations();
		dataFlowAnalyzer.analyze(BACKWARD_DIRECTION, new UnreadValuesOperationHandler<E>(errorHandler));
	}
	
	public static <E extends RuntimeException> void completeAnalysis(
			ControlFlowGraph graph,
			GrammarGraphBuilderTrace trace,
			IMetadataProvider metadataProvider, 
			IErrorHandler<E> definitiveAssignmentErrorHandler,
			IErrorHandler<E> unreadValuesErrorHandler) throws E {
		DataFlowAnalyzer<E> dataFlowAnalyzer = new DataFlowAnalyzer<E>(graph, trace, metadataProvider, definitiveAssignmentErrorHandler);
		dataFlowAnalyzer.buildOperations();
		dataFlowAnalyzer.analyze(FORWARD_DIRECTION, new DefiniteAssignmentOperationHandler<E>(definitiveAssignmentErrorHandler));
		dataFlowAnalyzer.analyze(BACKWARD_DIRECTION, new UnreadValuesOperationHandler<E>(unreadValuesErrorHandler));
	}
	
	private interface IDirection {
		ControlFlowVertex vertexToStartFrom(FunctionModel functionModel);
		Iterator<ControlFlowVertex> iterator(TopologicalOrder topologicalOrder, ControlFlowVertex startVertex);
		Collection<ControlFlowEdge> outgoingEdges(ControlFlowVertex vertex);
		Collection<ControlFlowEdge> incomingEdges(ControlFlowVertex vertex);
		void composeContexts(Set<ATFAttribute> context, Set<ATFAttribute> toBeComposed);
		<E extends Throwable> void process(List<DataFlowAnalyzer.Operation> operations, IOperationHandler<E> handler, Set<ATFAttribute> context) throws E;
	}
	
	private interface IOperationHandler<E extends Throwable> {
		void read(ATFAttribute attribute, Set<ATFAttribute> context) throws E; 
		void write(ATFAttribute attribute, Set<ATFAttribute> context) throws E;
		void setFunctionName(String name);
	}
	
	private final ControlFlowGraph myGraph;
	private final TopologicalOrder myTopologicalOrder;
	private final IMetadataProvider myRootMetadataProvider;
	private final GrammarGraphBuilderTrace myTrace;
	private final Collection<FunctionModel> myFunctionModels = new ArrayList<FunctionModel>();
	
	private DataFlowAnalyzer(ControlFlowGraph graph,
			GrammarGraphBuilderTrace trace, IMetadataProvider metadataProvider, IErrorHandler<E> errorHandler) {
		myGraph = graph;
		myRootMetadataProvider = metadataProvider;
		myTopologicalOrder = new TopologicalOrder(graph, true);
		myTrace = trace;
	}

	public void analyze(IDirection direction, IOperationHandler<E> operationHandler) {
		for (FunctionModel functionModel : myFunctionModels) {
			ControlFlowVertex start = direction.vertexToStartFrom(functionModel);
			Symbol symbol = myTrace.getSymbol(start);
			operationHandler.setFunctionName(symbol.getName() + "." + functionModel.ffFunction.getName());

			Map<ControlFlowVertex, List<Operation>> vertexToOperations = functionModel.ffVertexToOperations;
			List<Operation> startOperations = vertexToOperations.get(start);			
			
			Map<ControlFlowEdge, Set<ATFAttribute>> contextOnEdge = new HashMap<ControlFlowEdge, Set<ATFAttribute>>();
			
			// entering start vertex
			Set<ATFAttribute> initialContext = new HashSet<ATFAttribute>();
			direction.process(startOperations, operationHandler, initialContext);
			
			// write on edges going from start
			propagateToOutoingEdges(direction, start, initialContext,
					contextOnEdge);
			
			Iterator<ControlFlowVertex> iterator = direction.iterator(myTopologicalOrder, start);
			while (iterator.hasNext()) {
				ControlFlowVertex vertex = iterator.next();
				if (vertex == start) {
					continue;
				}
				
				// incoming context
				Collection<ControlFlowEdge> incomingEdges = direction.incomingEdges(vertex);
				Set<ATFAttribute> context = null;
				for (ControlFlowEdge edge : incomingEdges) {
					if (edge.isBackward()) {
						continue;
					}
					Set<ATFAttribute> set = contextOnEdge.remove(edge);
					if (context == null) {
						context = set;
					} else {
						direction.composeContexts(context, set);
					}
				}
				
				if (context == null) {
					// We are in a vertex which has no incoming edges
					context = new HashSet<ATFAttribute>();
				}
				
				// operations in this vertex
				List<Operation> list = vertexToOperations.get(vertex);
				direction.process(list, operationHandler, context);
				
				// outgoing edges
				propagateToOutoingEdges(direction, vertex, context,
						contextOnEdge);
			}
			
		}
	}

	private void propagateToOutoingEdges(IDirection direction,
			ControlFlowVertex vertex, Set<ATFAttribute> context,
			Map<ControlFlowEdge, Set<ATFAttribute>> contextOnEdge) {
		for (ControlFlowEdge edge : direction.outgoingEdges(vertex)) {
			if (edge.isBackward()) {
				continue;
			}
			contextOnEdge.put(edge, new HashSet<ATFAttribute>(context));
		}
	}
	
	public void buildOperations() {
		ISyntacticFunctionHandler<StartVertex> handler = new ISyntacticFunctionHandler<StartVertex>() {
			@Override
			public void handleSyntacticFunction(IMetadataProvider projection,
					StartVertex data) {
				buildFunctionModel(data, projection);
				
			}
		};
		for (StartVertex startVertex : myGraph.getStartVertices()) {
			Symbol symbol = myTrace.getSymbol(startVertex);
			IMetadataProvider rootMetadataProvider = myRootMetadataProvider;
			SyntacticFunctionTraverser.processSyntacticFunctions(symbol, rootMetadataProvider, handler, startVertex);
		}
	}

	private void buildFunctionModel(StartVertex startVertex,
			IMetadataProvider metadataProvider) throws E {
		Symbol symbol = myTrace.getSymbol(startVertex);
		IMetadataStorage symbolMetadata = MetadataStorage.getMetadataStorage(symbol, metadataProvider);

		Map<ControlFlowVertex, List<Operation>> vertexToOperations = new HashMap<ControlFlowVertex, List<Operation>>();
		
		FunctionSignature function = (FunctionSignature) symbolMetadata.readEObject(ATFMetadata.SYNTACTIC_FUNCTION);
		List<Operation> initialOperations = createOperations(startVertex,
				vertexToOperations);
		for (ATFAttribute attribute : function.getInputAttributes()) {
			Operation.write(attribute, initialOperations);
		}
//		HashSet<ATFAttribute> initiallyWritten = new HashSet<ATFAttribute>(function.getInputAttributes());
		// symbol.before
		Statement beforeSymbol = readStatement(symbolMetadata, ATFMetadata.BEFORE);
		readWriteAttributes(beforeSymbol, initialOperations);

//		Map<ControlFlowEdge, Set<ATFAttribute>> writtenOnEdge = new HashMap<ControlFlowEdge, Set<ATFAttribute>>();
//		ControlFlowEdge firstEdge = startVertex.getNext();
//		writtenOnEdge.put(firstEdge, initiallyWritten);
		
		EndVertex endVertex = null;
		
		Iterator<ControlFlowVertex> iterator = myTopologicalOrder.iterator(startVertex);
		while (iterator.hasNext()) {
			ControlFlowVertex vertex = iterator.next();
			if (vertex instanceof StartVertex) {
				continue;
			}
			if (vertex instanceof EndVertex) {
				endVertex = (EndVertex) vertex;
				continue;
			}

			List<Operation> operations = createOperations(vertex, vertexToOperations);
			
			// production.before
			Production production = myTrace.getProductionByFirstVertex(vertex);
			processStatement(production, ATFMetadata.BEFORE, metadataProvider, operations);
			// expression.before
			List<Expression> beforeExpressions = myTrace.getBeforeExpressions(vertex);
			if (beforeExpressions != null) {
				for (Expression expression : beforeExpressions) {
					processStatement(expression, ATFMetadata.BEFORE, metadataProvider, operations);
				}
				// symbolReference.mapping
				Expression last = beforeExpressions.get(beforeExpressions.size() - 1);
				if (last instanceof SymbolReference) {
					IMetadataStorage metadata = MetadataStorage.getMetadataStorage(last, metadataProvider);
					Collection<ATFExpression> args = metadata.readEObjects(ATFMetadata.ASSOCIATED_CALL_ARGUMENTS);
					if (args != null) {
						for (ATFExpression expression : args) {
							readWriteAttributes(expression, operations);
						}
					}
					Collection<ATFAttributeReference> assignedTo = metadata.readEObjects(ATFMetadata.ASSIGNED_TO_ATTRIBUTES);
					if (assignedTo != null) {
						for (ATFAttributeReference attributeReference : assignedTo) {
//							myReadWrites.write(attributeReference.getAttribute(), written);
							Operation.write(attributeReference.getAttribute(), operations);
						}
					}
				}
			}
			// expression.after
			List<Expression> afterExpressions = myTrace.getAfterExpressions(vertex);
			if (afterExpressions != null) {
				for (Expression expression : afterExpressions) {
					IMetadataStorage metadata = MetadataStorage.getMetadataStorage(expression, metadataProvider);
					ATFAttributeReference assignTextTo = (ATFAttributeReference) metadata.readEObject(ATFMetadata.ASSIGN_TEXT_TO_ATTRIBUTE);
					if (assignTextTo != null) {
//						myReadWrites.write(assignTextTo.getAttribute(), written);
						Operation.write(assignTextTo.getAttribute(), operations);
					}
					processStatement(metadata, ATFMetadata.AFTER, operations);
				}
			}
			// production.after
			Production endingProduction = myTrace.getProductionByLastVertex(vertex);
			processStatement(endingProduction, ATFMetadata.AFTER, metadataProvider, operations);
			
//			Collection<ControlFlowEdge> outgoingEdges = VertexUtil.getOutgoingEdges(vertex);
//			for (ControlFlowEdge edge : outgoingEdges) {
//				writtenOnEdge.put(edge, new HashSet<ATFAttribute>(written));
//			}
		}
		
//		Set<ATFAttribute> written = getIncomingWritten(endVertex, writtenOnEdge);
		List<Operation> operations = createOperations(endVertex, vertexToOperations);
		
		// symbol.after
		Statement symbolAfter = readStatement(symbolMetadata, ATFMetadata.AFTER);
		readWriteAttributes(symbolAfter, operations);
		//		myReadWrites.readAll(outputAttributes, written);
		for (ATFAttribute attribute : function.getOutputAttributes()) {
			Operation.read(attribute, operations);
		}

		myFunctionModels.add(new FunctionModel(function, startVertex, endVertex, vertexToOperations));
	}

	private List<Operation> createOperations(ControlFlowVertex vertex,
			Map<ControlFlowVertex, List<Operation>> vertexToOperations) {
		List<Operation> initialOperations = new ArrayList<Operation>();
		vertexToOperations.put(vertex, initialOperations);
		return initialOperations;
	}

	private IMetadataStorage processStatement(EObject object, String name,
			IMetadataProvider metadataProvider, List<Operation> operations) {
		if (object != null) {
			IMetadataStorage metadata = MetadataStorage.getMetadataStorage(object, metadataProvider);
			return processStatement(metadata, name, operations);
		}
		return null;
	}

	private IMetadataStorage processStatement(IMetadataStorage metadata,
			String name, List<Operation> operations) {
		Statement readStatement = readStatement(metadata, name);
		readWriteAttributes(readStatement, operations);
		return metadata;
	}

	private Statement readStatement(IMetadataStorage metadata, String name) {
		return (Statement) metadata.readEObject(name);
	}

	private void readWriteAttributes(EObject subject, final List<Operation> operations) {
		if (subject == null) {
			return;
		}
		new AtfSwitch<INull>() {
			@Override
			public INull caseATFAttributeReference(ATFAttributeReference object) {
//				myReadWrites.read(object.getAttribute(), written);
				Operation.read(object.getAttribute(), operations);
				return INull.NULL;
			}
			
			@Override
			public INull caseFunctionCall(FunctionCall object) {
				List<ATFExpression> arguments = object.getArguments();
				for (ATFExpression expression : arguments) {
					doSwitch(expression);
				}
				return INull.NULL;
			}
			
			@Override
			public INull caseBlock(Block object) {
				for (Statement statement : object.getStatements()) {
					doSwitch(statement);
				}
				return INull.NULL;
			}
			
			@Override
			public INull caseATFAttributeAssignment(
					ATFAttributeAssignment object) {
				doSwitch(object.getRightSide());
				for (ATFAttributeReference attributeReference : object.getLeftSide()) {
//					myReadWrites.write(attributeReference.getAttribute(), written);
					Operation.write(attributeReference.getAttribute(), operations);
				}				
				return INull.NULL;
			}
			
			@Override
			public INull caseCollectionAppending(CollectionAppending object) {
				doSwitch(object.getRightSide());
//				myReadWrites.read(object.getLeftSide().getAttribute(), written);
				Operation.read(object.getLeftSide().getAttribute(), operations);
				return INull.NULL;
			}
			
			@Override
			public INull defaultCase(EObject object) {
				throw new IllegalStateException("Unknowsn expression type: " + object);
			}
		}.doSwitch(subject);
	}
	
	private static <E extends Throwable> void handleOperation(IOperationHandler<E> handler,
			Operation operation, Set<ATFAttribute> context) throws E {
		if (operation.ffRead) {
			handler.read(operation.ffAttribute, context);
		} else {
			handler.write(operation.ffAttribute, context);
		}
	}

	static final class FunctionModel {
		public final FunctionSignature ffFunction;
		public final StartVertex ffStartVertex;
		public final EndVertex ffEndVertex;
		public final Map<ControlFlowVertex, List<Operation>> ffVertexToOperations;
		
		public FunctionModel(FunctionSignature function,
				StartVertex startVertex, EndVertex endVertex,
				Map<ControlFlowVertex, List<Operation>> vertexToOperations) {
			this.ffEndVertex = endVertex;
			this.ffFunction = function;
			this.ffStartVertex = startVertex;
			this.ffVertexToOperations = vertexToOperations;
		}
	}
	
	static final class Operation {
		public static void read(ATFAttribute attribute, List<Operation> operations) {
			operations.add(new Operation(attribute, true));
		}
		
		public static void write(ATFAttribute attribute, List<Operation> operations) {
			operations.add(new Operation(attribute, false));
		}
		
		private final boolean ffRead;
		private final ATFAttribute ffAttribute;
		
		public Operation(ATFAttribute ffAttribute, boolean ffRead) {
			this.ffAttribute = ffAttribute;
			this.ffRead = ffRead;
		}
		
		@Override
		public String toString() {
			return (ffRead ? "read " : "write ") + ffAttribute.getName();
		}
	}
	
	private static abstract class OperationHandler<E extends Throwable> implements IOperationHandler<E> {

		protected final IErrorHandler<E> myErrorHandler;
		private String myFunctionName;
		
		public OperationHandler(IErrorHandler<E> errorHandler) {
			myErrorHandler = errorHandler;
		}

		@Override
		public void setFunctionName(String symbolName) {
			myFunctionName = symbolName;
		}
		
		protected final String getFunctionName() {
			return myFunctionName;
		}
	}
	
	private static class DefiniteAssignmentOperationHandler<E extends Throwable> extends OperationHandler<E> {
		
		public DefiniteAssignmentOperationHandler(IErrorHandler<E> errorHandler) {
			super(errorHandler);
		}

		@Override
		public void read(ATFAttribute attribute, Set<ATFAttribute> context) throws E{
			if (!context.contains(attribute)) {
				myErrorHandler.reportError("'%s': Attribute '%s' is read but might be never written before", getFunctionName(), attribute.getName());
			}
		}

		@Override
		public void write(ATFAttribute attribute, Set<ATFAttribute> context) throws E {
			context.add(attribute);
		}
	}
	
	private static class UnreadValuesOperationHandler<E extends Throwable> extends OperationHandler<E> {
		
		public UnreadValuesOperationHandler(IErrorHandler<E> errorHandler) {
			super(errorHandler);
		}

		@Override
		public void read(ATFAttribute attribute, Set<ATFAttribute> context) throws E{
			context.add(attribute);
		}

		@Override
		public void write(ATFAttribute attribute, Set<ATFAttribute> context) throws E {
			if (!context.contains(attribute)) {
				myErrorHandler.reportError("'%s': Value assigned to attribute '%s' is never read", getFunctionName(), attribute.getName());
			}
			context.remove(attribute);
		}
	}

	private static final class ForwardDirection implements IDirection {
		@Override
		public Collection<ControlFlowEdge> incomingEdges(
				ControlFlowVertex vertex) {
			if (vertex instanceof StartVertex) {
				return Collections.emptySet();
			}
			return ((BodyVertex) vertex).getIncomingEdges();
		}

		@Override
		public Iterator<ControlFlowVertex> iterator(TopologicalOrder topologicalOrder,
				ControlFlowVertex startVertex) {
			return topologicalOrder.iterator((StartVertex) startVertex);
		}

		@Override
		public Collection<ControlFlowEdge> outgoingEdges(
				ControlFlowVertex vertex) {
			return VertexUtil.getOutgoingEdges(vertex);
		}

		@Override
		public <E extends Throwable> void process(
				List<Operation> operations, IOperationHandler<E> handler,
				Set<ATFAttribute> context) throws E {
			for (Operation operation : operations) {
				handleOperation(handler, operation, context);
			}
		}

		@Override
		public ControlFlowVertex vertexToStartFrom(
				FunctionModel functionModel) {
			return functionModel.ffStartVertex;
		}

		@Override
		public void composeContexts(Set<ATFAttribute> context,
				Set<ATFAttribute> toBeComposed) {
			context.retainAll(toBeComposed);
		}

	}

	private static final class BackwardDirection implements IDirection {
		@Override
		public Collection<ControlFlowEdge> incomingEdges(
				ControlFlowVertex vertex) {
			return VertexUtil.getOutgoingEdges(vertex);
		}

		@Override
		public Iterator<ControlFlowVertex> iterator(TopologicalOrder topologicalOrder,
				ControlFlowVertex startVertex) {
			return topologicalOrder.iterator((EndVertex) startVertex);
		}

		@Override
		public Collection<ControlFlowEdge> outgoingEdges(
				ControlFlowVertex vertex) {
			if (vertex instanceof StartVertex) {
				return Collections.emptySet();
			}
			return ((BodyVertex) vertex).getIncomingEdges();
		}

		@Override
		public <E extends Throwable> void process(
				List<Operation> operations, IOperationHandler<E> handler,
				Set<ATFAttribute> context) throws E {
			if (operations.isEmpty()) {
				return;
			}
			ListIterator<Operation> listIterator = operations.listIterator(operations.size() - 1);
			listIterator.next();
			while (listIterator.hasPrevious()) {
				Operation operation = listIterator.previous();
				handleOperation(handler, operation, context);
			}
		}

		@Override
		public ControlFlowVertex vertexToStartFrom(
				FunctionModel functionModel) {
			return functionModel.ffEndVertex;
		}

		@Override
		public void composeContexts(Set<ATFAttribute> context,
				Set<ATFAttribute> toBeComposed) {
			context.addAll(toBeComposed);
		}

	}

}
