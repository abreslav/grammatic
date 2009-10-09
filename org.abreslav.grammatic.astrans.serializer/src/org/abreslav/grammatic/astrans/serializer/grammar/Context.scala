package org.abreslav.grammatic.astrans.serializer.grammar

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

trait Context {
  def apply(attribute : Attribute) : Option[AnyRef]
  def setAttribute(attribute : Attribute, value : AnyRef) : Context
  def apply(obj : EObject, feature : EStructuralFeature) : Option[(Context, AnyRef)]
  def print(string : String) : Context
 
  def compose(collectionsAndOutput : Context) : Context
}

object Context {
  import scala.collection.immutable.Queue
  private class ContextImpl(
      private val environment : Map[Attribute, AnyRef],
      private val collectionEnvironment : Map[(AnyRef, EStructuralFeature), Int],
	  private val output : Queue[String]  
    ) extends Context {
    
    override def compose(collectionsAndOutput : Context) : Context = {
      val other = collectionsAndOutput.asInstanceOf[ContextImpl]
      new ContextImpl(environment, other.collectionEnvironment, other.output)
    }
    
    override def apply(attribute : Attribute) : Option[AnyRef] = 
      if (environment contains attribute)
        Some(environment(attribute))
      else None
    
	override def setAttribute(attribute : Attribute, value : AnyRef) : Context = {
	  assert(!value.isInstanceOf[Option[_]])
      new ContextImpl(
        environment + ((attribute, value)),
        collectionEnvironment,
        output
      )	  
	}
 
    override def apply(obj : EObject, feature : EStructuralFeature) : Option[(Context, AnyRef)] = {
	  if (!feature.isMany())
        throw new IllegalArgumentException();
      
      val values = obj.eGet(feature).asInstanceOf[java.util.List[AnyRef]] 
      val index = collectionEnvironment.getOrElse((obj, feature), 0)
    
      if (index >= values.size)
        None
      else
        Some((
          new ContextImpl(
            environment, 
            collectionEnvironment + ((obj, feature) -> (index + 1)), 
            output)
        , values.get(index)))
	}
 
	override def print(string : String) : Context = new ContextImpl(
	  environment,
      collectionEnvironment,
      output.enqueue(string)
	); 

    private def outputStr : String = output.foldLeft(new StringBuilder()){
        (sb, str) => sb.append(str)
      }.toString
    
    private def environmentStr : String = environment.foldLeft(new StringBuilder()){
        (sb, entry) => {
          val (attribute, value) = entry 
          sb.append(attribute.name).append("=").append(value).append("\n")
        }
      }.toString
    
    override def toString = environmentStr + "output:\"" + outputStr + "\""
  } 
  
  val emptyContext : Context = new ContextImpl(Map(), Map(), new Queue)
}
