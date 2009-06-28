package org.abreslav.grammatic.utils;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.Assert;

import org.abreslav.grammatic.parsingutils.JavaUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ApplyVarNameConventionsTest {

	@Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][] {
				{"some", null},
				{"someSome", null},
				{"someS", null},
				{"someSomeS", null},
				{"someSomeSome", null},
				{"Some", "some"},
				{"SOME", "some"},
				{"SOME_", "some"},
				{"SOME_SOME", "someSome"},
				{"_SOME_SOME", "someSome"},
				{"SOMESome", "someSome"},
				{"SOMESome_SOME", "someSomeSome"},
				{"SOMESome_SOME123co", "someSomeSome123Co"},
				{"123some", "n123Some"},
				{"1", "n1"},
				{"", "v"},
				{"int", "int_"},
				{"As#you_WHISH...", "asYouWhish"},
				{"As#you_WHISH", "asYouWhish"},
				{"---As#you_WHI SH", "asYouWhiSh"},
				{".", "dot"},
				{"..", "dotDot"},
				{"..-!", "dotDotMinusExclamation"},
		});
	}

	private String myName;
	private String myExpected;
	
	public ApplyVarNameConventionsTest(String name, String expected) {
		myName = name;
		myExpected = expected;
		if (myExpected == null) {
			myExpected = myName;
		}
	}
	
	@Test
	public void test() throws Exception {
		String varName = JavaUtils.applyVarNameConventions(myName);
		Assert.assertEquals(myExpected, varName);
	}
}
