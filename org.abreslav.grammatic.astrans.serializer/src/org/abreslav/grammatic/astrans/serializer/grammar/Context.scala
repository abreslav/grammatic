package org.abreslav.grammatic.astrans.serializer.grammar

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

trait Context {
	def apply(semanticalAttribute : Attribute) : EObject
	def setAttribute(semanticalAttribute : Attribute, value : EObject) : Context
	def apply(obj : EObject, feature : EStructuralFeature) : (Option[EObject], Context)
	def print(string : String) : Context
	def print(strings : String*) : Context = {
	  strings.foldLeft(this)((cont, str) => cont.print(str))
	} 
}

object Context {
  import scala.collection.immutable.Queue
  private class ContextImpl(
      private val environment : Map[Attribute, EObject],
      private val collectionEnvironment : Map[(EObject, EStructuralFeature), Int],
	  private val output : Queue[String]  
    ) extends Context {
    
    override def apply(semanticalAttribute : Attribute) : EObject = environment(semanticalAttribute)
    
	override def setAttribute(semanticalAttribute : Attribute, value : EObject) : Context = {
      new ContextImpl(
        environment + ((semanticalAttribute, value)),
        collectionEnvironment,
        output
      )	  
	}
 
    override def apply(obj : EObject, feature : EStructuralFeature) : (Option[EObject], Context) = {
	  if (!feature.isMany())
        throw new IllegalArgumentException();
      
      val values = obj.eGet(feature).asInstanceOf[java.util.List[EObject]] 
      val index = if (collectionEnvironment.contains(obj, feature))
          collectionEnvironment(obj, feature)
        else 
          0
        ;
      if (values.size <= index)
        (None, this)
      else
        (
          Some(values.get(index)),
          new ContextImpl(
            environment, 
            collectionEnvironment + ((obj, feature) -> (index + 1)), 
            output)
        )
	}
 
	override def print(string : String) : Context = new ContextImpl(
	  environment,
      collectionEnvironment,
      output.enqueue(string)
	); 

    override def print(strings : String*) : Context = new ContextImpl( 
    		environment,
    		collectionEnvironment,
    		strings.foldLeft(output)((out, str) => out.enqueue(str))
    );
    
    override def toString : String = output.foldLeft(new StringBuilder())((sb, str) => sb.append(str)).toString
  } 
  
  val emptyContext : Context = new ContextImpl(Map(), Map(), new Queue)
}
