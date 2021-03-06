package org.xacml4j.v30.policy.function;

/*
 * #%L
 * Xacml4J Core Engine Implementation
 * %%
 * Copyright (C) 2009 - 2014 Xacml4J.org
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.xacml4j.v30.EvaluationContext;
import org.xacml4j.v30.types.BooleanExp;
import org.xacml4j.v30.types.StringExp;
import org.xacml4j.v30.types.TimeExp;




public class NonNumericComparisionFunctionTest
{
	private EvaluationContext context;

	@Before
	public void init(){
		this.context = createStrictMock(EvaluationContext.class);
	}

	@Test
	public void testStringGreaterThan()
	{
		StringExp a = StringExp.valueOf("ab");
		StringExp b = StringExp.valueOf("aa");
		assertEquals(BooleanExp.valueOf(true),
				NonNumericComparisonFunctions.greaterThan(a, b));
		a = StringExp.valueOf("aaa");
		b = StringExp.valueOf("aa");
		assertEquals(BooleanExp.valueOf(true),
				NonNumericComparisonFunctions.greaterThan(a, b));
	}

	@Test
	public void testTimeLessThan()
	{
		TimeExp t1 = TimeExp.valueOf("08:23:47-05:00");
		TimeExp t2 = TimeExp.valueOf("08:23:48-05:00");
		assertEquals(BooleanExp.valueOf(true), NonNumericComparisonFunctions.lessThan(t1, t2));
		t2 = TimeExp.valueOf("08:23:47-05:00");
		assertEquals(BooleanExp.valueOf(false), NonNumericComparisonFunctions.lessThan(t1, t2));
		t2 = TimeExp.valueOf("08:23:46-05:00");
		assertEquals(BooleanExp.valueOf(false), NonNumericComparisonFunctions.lessThan(t1, t2));
	}

	@Test
	public void testTimeInRangeNoTimeZonesAndTimeIsInRange()
	{
		replay(context);
		TimeExp a = TimeExp.valueOf("09:30:10");
		TimeExp b = TimeExp.valueOf("08:30:10");
		TimeExp c = TimeExp.valueOf("09:30:11");
		assertEquals(BooleanExp.valueOf(true),
				NonNumericComparisonFunctions.timeInRange(context, a, b, c));
		verify(context);
	}

	@Test
	public void testTimeInRangeWithTimeZonesAndTimeIsInRange()
	{
		TimeExp a = TimeExp.valueOf("09:30:10Z");
		TimeExp b = TimeExp.valueOf("08:30:10Z");
		TimeExp c = TimeExp.valueOf("09:30:11Z");
		assertEquals(BooleanExp.valueOf(true),
				NonNumericComparisonFunctions.timeInRange(context, a, b, c));
	}

	@Test
	public void testTimeInRangeNoTimeZonesAndTimeIsNotInRange()
	{
		replay(context);
		TimeExp a = TimeExp.valueOf("09:30:10");
		TimeExp b = TimeExp.valueOf("08:30:10");
		TimeExp c = TimeExp.valueOf("09:30:09");
		assertEquals(BooleanExp.valueOf(false),
				NonNumericComparisonFunctions.timeInRange(context, a, b, c));
		verify(context);
	}

	@Test
	public void testTimeInRangeNoTimeZonesAndTimeIsEqualToUpperBound()
	{
		replay(context);
		TimeExp a = TimeExp.valueOf("09:30:10");
		TimeExp b = TimeExp.valueOf("08:30:10");
		TimeExp c = TimeExp.valueOf("09:30:10");
		assertEquals(BooleanExp.valueOf(true),
				NonNumericComparisonFunctions.timeInRange(context, a, b, c));
		verify(context);
	}
}
