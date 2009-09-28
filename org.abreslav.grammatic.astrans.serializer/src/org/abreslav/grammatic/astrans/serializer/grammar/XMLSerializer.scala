package org.abreslav.grammatic.astrans.serializer.grammar

import scala.xml.Elem

object XMLSerializer {

  def apply(g : Grammar) : Elem = <grammar>
    {g.symbols.map(apply(_))}
  </grammar>
  
  def apply(s : Symbol) : Elem = <symbol name={s.name} token={s.token.toString}>
    {s.inputs.map(a => <in name={a.name.name}/>)}
    {s.outputs.map(a => <out name={a.name.name}/>)}
    {s.productions.map(apply(_))}
  </symbol>
  
  def apply(p : Production) : Elem = <prod>
    {apply(p.body)}
  </prod>
  
  def apply(e : Expression) : Elem = e match {
    case SymbolReference(s) => <ref name={s.name}/>
    case Empty() => <empty/>
    case Iteration(it, l, u) => <itr low={l.toString} up={u.toString}>{apply(it)}</itr>
    case Alternative(exps) => <alt>
      {exps.map(apply(_))}
    </alt>
    case Sequence(exps) => <seq>
      {exps.map(apply(_))}
    </seq>
    case StringExpression(s) => <string s={s}/>
    case AnnotatedExpression(e, oD, aA, aO) => <annotated>
      {oD.map(a => <oD name={a.name.name}/>)}
      {apply(e)}
      {aA.map(apply(_))}
    </annotated>
    case AnnotatedSymbolReference(ref, assTo, args) => <annotatedSR>
      {assTo.map(r => apply(r))}
      {apply(ref)}
      {args.map(a => <arg name={a.name.name}/>)}
    </annotatedSR>
    case x => <unknown class={x.getClass.toString}/>
  }
  
  def apply(ref : Reference) : Elem = ref match {
    case AttributeReference(a) => <attr name={a.name.name}/>
    case FeatureReference(a, f) => <feature attr={a.name.name} feat={f.getName}/>
  }
  
  def apply(assignment : Assignment) : Elem = assignment match {
    case EnumLiteralAssignment(ref, lit) => <assign> {apply(ref)} <lit name={lit.getName}/></assign>
    case AttributeAssignment(ref, attr) => <assign> {apply(ref)} <attr name={attr.name.name}/> </assign>
  }
}
