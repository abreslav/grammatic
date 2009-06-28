package org.abreslav.grammatic.query.interpreter;

import org.abreslav.grammatic.metadata.IdValue;
import org.abreslav.grammatic.metadata.IntegerValue;
import org.abreslav.grammatic.metadata.MultiValue;
import org.abreslav.grammatic.metadata.PunctuationValue;
import org.abreslav.grammatic.metadata.StringValue;
import org.abreslav.grammatic.metadata.TupleValue;
import org.abreslav.grammatic.metadata.Value;
import org.abreslav.grammatic.metadata.util.MetadataSwitch;

public class ValueMatcher {

	private static final ValueSwitch ourValueSwitch = new ValueSwitch();
	
	public static synchronized Boolean equals(Value a, Value b) {
		return ourValueSwitch.doSwitch(b, a);
	}

	private static class ValueSwitch extends MetadataSwitch<Boolean, Value> {
		
		@Override
		public Boolean caseIdValue(IdValue object, Value a) {
			if (false == a instanceof IdValue) {
				return false;
			}
			return object.getId().equals(((IdValue) a).getId());
		}
		
		@Override
		public Boolean caseIntegerValue(IntegerValue object, Value a) {
			if (false == a instanceof IntegerValue) {
				return false;
			}
			return object.getValue() == ((IntegerValue) a).getValue();
		}
		
		@Override
		public Boolean caseMultiValue(MultiValue object, Value a) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public Boolean casePunctuationValue(PunctuationValue object, Value a) {
			if (false == a instanceof PunctuationValue) {
				return false;
			}
			return object.getType() == ((PunctuationValue) a).getType();
		}
		
		@Override
		public Boolean caseStringValue(StringValue object, Value a) {
			if (false == a instanceof StringValue) {
				return false;
			}
			return object.getValue().equals(((StringValue) a).getValue());
		}
		
		@Override
		public Boolean caseTupleValue(TupleValue object, Value a) {
			throw new UnsupportedOperationException();
		}
	}
}
