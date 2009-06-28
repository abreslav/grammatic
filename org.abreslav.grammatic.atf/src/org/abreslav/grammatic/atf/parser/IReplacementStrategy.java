/**
 * 
 */
package org.abreslav.grammatic.atf.parser;

import org.abreslav.grammatic.grammar.CharacterRange;
import org.abreslav.grammatic.grammar.Expression;
import org.abreslav.grammatic.grammar.StringExpression;

public interface IReplacementStrategy<D> {
	IReplacementStrategy<Character> CHARACTER_REPLACEMENT_STRATEGY = new IReplacementStrategy<Character>() {

		@Override
		public String getName(Character data) {
			return "'" + data + "'";
		}

		@Override
		public boolean needsToBeReplaced(Expression expression, Character data) {
			if (false == expression instanceof CharacterRange) {
				return false;
			}
			CharacterRange range = (CharacterRange) expression;
			return range.getLowerBound() == data.charValue()
					&& range.getUpperBound() == data.charValue();
		}
		
	};
	
	IReplacementStrategy<String> STRING_REPLACEMENT_STRATEGY = new IReplacementStrategy<String>() {

		@Override
		public String getName(String data) {
			return "'" + data + "'";
		}

		@Override
		public boolean needsToBeReplaced(Expression expression, String data) {
			if (false == expression instanceof StringExpression) {
				return false;
			}
			StringExpression stringExpression = (StringExpression) expression;
			return data.equals(stringExpression.getValue());
		}
		
	};
	
	String getName(D data);
	boolean needsToBeReplaced(Expression expression, D data);
}