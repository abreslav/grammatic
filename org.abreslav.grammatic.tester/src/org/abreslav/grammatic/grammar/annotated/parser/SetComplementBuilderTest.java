package org.abreslav.grammatic.grammar.annotated.parser;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.abreslav.grammatic.grammar.template.parser.SetComplementBuilder;
import org.abreslav.grammatic.grammar.template.parser.SetComplementBuilder.Range;
import org.junit.Test;

public class SetComplementBuilderTest {

	private final SetComplementBuilder myBuilder = new SetComplementBuilder(); 
	
	@Test
	public void testRemoveItemInTheMiddle() {
		myBuilder.removeItem(5);
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(0, 4),
				new Range(6, Character.MAX_VALUE)
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveTwoItemsInTheMiddle() {
		myBuilder.removeItem(5);
		myBuilder.removeItem(9);
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(0, 4),
				new Range(6, 8),
				new Range(10, Character.MAX_VALUE)
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveItemInTheStart() {
		myBuilder.removeItem(0);
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(1, Character.MAX_VALUE)
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveItemInTheEnd() {
		myBuilder.removeItem(Character.MAX_VALUE);
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(0, Character.MAX_VALUE - 1)
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveIsolatedItem() {
		myBuilder.removeItem(5);
		myBuilder.removeItem(7);
		myBuilder.removeItem(6);
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(0, 4),
				new Range(8, Character.MAX_VALUE),
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveAbsent() {
		myBuilder.removeItem(5);
		myBuilder.removeItem(5);
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(0, 4),
				new Range(6, Character.MAX_VALUE),
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveCRLF() {
		myBuilder.removeItem('\n');
		myBuilder.removeItem('\r');
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(0, '\n' - 1),
				new Range('\n' + 1, '\r' - 1),
				new Range('\r' + 1, Character.MAX_VALUE),
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveFromEmpty() {
		myBuilder.removeRange(0, Character.MAX_VALUE);
		myBuilder.removeEmptyRanges();
		myBuilder.removeItem(5);
		assertEquals(
			Arrays.asList(new Range[] {
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveRange() {
		myBuilder.removeRange(1, 2);
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(0, 0),
				new Range(3, Character.MAX_VALUE),
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveTwoRanges() {
		myBuilder.removeRange(1, 2);
		myBuilder.removeRange(5, 8);
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(0, 0),
				new Range(3, 4),
				new Range(9, Character.MAX_VALUE),
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveAbsentRange() {
		myBuilder.removeRange(1, 10);
		myBuilder.removeRange(5, 8);
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(0, 0),
				new Range(11, Character.MAX_VALUE),
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveLeftIntersection() {
		myBuilder.removeRange(1, 10);
		myBuilder.removeRange(5, 12);
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(0, 0),
				new Range(13, Character.MAX_VALUE),
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveRightIntersection() {
		myBuilder.removeRange(3, 10);
		myBuilder.removeRange(2, 5);
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(0, 1),
				new Range(11, Character.MAX_VALUE),
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveFullIntersection() {
		myBuilder.removeRange(3, 10);
		myBuilder.removeRange(12, 15);
		myBuilder.removeRange(9, 14);
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(0, 2),
				new Range(16, Character.MAX_VALUE),
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveRangeOfSingleItem() {
		myBuilder.removeRange(3, 3);
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
				new Range(0, 2),
				new Range(4, Character.MAX_VALUE),
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveRangeFromEmpty() {
		myBuilder.removeRange(0, Character.MAX_VALUE);
		myBuilder.removeEmptyRanges();
		myBuilder.removeRange(2, 5);
		assertEquals(
			Arrays.asList(new Range[] {
			}
			),
			myBuilder.getRanges()
		);
	}

	@Test
	public void testRemoveCrLfSlashQuote() {
		myBuilder.removeItem('\\');
		myBuilder.removeItem('\n');
		myBuilder.removeItem('\r');
		myBuilder.removeItem('\'');
		myBuilder.removeEmptyRanges();
		assertEquals(
			Arrays.asList(new Range[] {
					new Range(0, '\n' - 1),
					new Range('\n' + 1, '\r' - 1),
					new Range('\r' + 1, '\'' - 1),
					new Range('\'' + 1, '\\' - 1),
					new Range('\\' + 1, Character.MAX_VALUE),
			}
			),
			myBuilder.getRanges()
		);
	}

}