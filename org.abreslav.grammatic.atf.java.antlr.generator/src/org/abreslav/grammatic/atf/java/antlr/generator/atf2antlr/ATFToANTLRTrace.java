package org.abreslav.grammatic.atf.java.antlr.generator.atf2antlr;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.abreslav.grammatic.atf.FunctionSignature;
import org.abreslav.grammatic.atf.java.antlr.LexicalRule;
import org.abreslav.grammatic.atf.java.antlr.SyntacticalRule;
import org.abreslav.grammatic.atf.java.antlr.generator.IModuleImplementationBuilderTrace;
import org.abreslav.grammatic.atf.java.antlr.semantics.ImplementationPoolField;
import org.abreslav.grammatic.atf.java.antlr.semantics.Method;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementation;
import org.abreslav.grammatic.atf.java.antlr.semantics.ModuleImplementationProvider;
import org.abreslav.grammatic.emfutils.EMFProxyUtil;
import org.abreslav.grammatic.grammar.Grammar;
import org.abreslav.grammatic.grammar.Symbol;
import org.abreslav.grammatic.metadata.Namespace;

/*package*/ class ATFToANTLRTrace implements IModuleImplementationBuilderTrace {

	private final Map<FunctionSignature, SyntacticalRule> myFunctionToRule = new LinkedHashMap<FunctionSignature, SyntacticalRule>();
	private final Map<Symbol, ModuleImplementation> mySymbolToModuleImpl = EMFProxyUtil.customHashMap();
	private final Map<FunctionSignature, ModuleImplementation> myFunctionToModuleImpl = new HashMap<FunctionSignature, ModuleImplementation>();
	private final Map<FunctionSignature, Namespace> myFunctionToNamespace = new HashMap<FunctionSignature, Namespace>();
	private final Map<Symbol, LexicalRule> myTokenToRule = new LinkedHashMap<Symbol, LexicalRule>();
	private final Map<SyntacticalRule, Symbol> mySyntacticalRuleToSymbol = new HashMap<SyntacticalRule, Symbol>();
	private final Map<FunctionSignature, Method> myFunctionToMethod = new HashMap<FunctionSignature, Method>();
	private final Map<Grammar, ModuleImplementationProvider> myModuleImplementationProviders = new HashMap<Grammar, ModuleImplementationProvider>();
	private final Map<ModuleImplementationProvider, ImplementationPoolField> myPoolFields = new HashMap<ModuleImplementationProvider, ImplementationPoolField>();

	public SyntacticalRule getRuleByFunction(FunctionSignature key) {
		return myFunctionToRule.get(key);
	}
	
	public ModuleImplementation getModuleImplBySymbol(Symbol key) {
		return mySymbolToModuleImpl.get(key);
	}
	
	public ModuleImplementation getModuleImplByFunction(FunctionSignature key) {
		return myFunctionToModuleImpl.get(key);
	}
	
	public Namespace getNamespaceByFunction(FunctionSignature key) {
		return myFunctionToNamespace.get(key);
	}
	
	public LexicalRule getRuleByToken(Symbol key) {
		return myTokenToRule.get(key);
	}
	
	public Symbol getSymbolBySyntacticalRule(SyntacticalRule key) {
		return mySyntacticalRuleToSymbol.get(key);
	}
	
	public Method getMethodByFunction(FunctionSignature key) {
		return myFunctionToMethod.get(key);
	}
	
	public ImplementationPoolField getPoolField(ModuleImplementationProvider key) {
		return myPoolFields.get(key);
	}
	
	public ModuleImplementationProvider getModuleImplementationProvider(Grammar key) {
		return myModuleImplementationProviders.get(key);
	}
	
	public Set<Map.Entry<Symbol, LexicalRule>> getAllTokenRulePairs() {
		return Collections.unmodifiableSet(myTokenToRule.entrySet());
	}
	
	public Set<Map.Entry<FunctionSignature, SyntacticalRule>> getAllFunctionRulePairs() {
		return Collections.unmodifiableSet(myFunctionToRule.entrySet());
	}
	
	public void putFunctionToRule(FunctionSignature key, SyntacticalRule value) {
		myFunctionToRule.put(key, value);
	}

	public void putSymbolToModuleImpl(Symbol key, ModuleImplementation value) {
		mySymbolToModuleImpl.put(key, value);
	}

	public void putFunctionToModuleImpl(FunctionSignature key, ModuleImplementation value) {
		myFunctionToModuleImpl.put(key, value);
	}

	public void putFunctionToNamespace(FunctionSignature key, Namespace value) {
		myFunctionToNamespace.put(key, value);
	}

	public void putTokenToRule(Symbol key, LexicalRule value) {
		myTokenToRule.put(key, value);
	}

	public void putSyntacticalRuleToSymbol(SyntacticalRule key, Symbol value) {
		mySyntacticalRuleToSymbol.put(key, value);
	}

	public void putFunctionToMethod(FunctionSignature key, Method value) {
		myFunctionToMethod.put(key, value);
	}

	public void putModuleImplementationProvider(Grammar key, ModuleImplementationProvider value) {
		myModuleImplementationProviders.put(key, value);
	}

	public void putPoolField(ModuleImplementationProvider key, ImplementationPoolField value) {
		myPoolFields.put(key, value);
	}

}
