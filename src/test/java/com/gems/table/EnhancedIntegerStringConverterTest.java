package com.gems.table;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnhancedIntegerStringConverterTest {
	    @Test
	    public void testFromString_validIntegerString_returnsIntegerValue() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        Assertions.assertEquals(Integer.valueOf(1234), converter.fromString("1234"));
	        Assertions.assertEquals(Integer.valueOf(-1234), converter.fromString("-1234"));
	        Assertions.assertEquals(Integer.valueOf(1234), converter.fromString("+1234"));
	    }

	    @Test
	    public void testFromString_surroundingWhitespace_returnsIntegerValue() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        Assertions.assertEquals(Integer.valueOf(42), converter.fromString("  42  "));
	    }

	    @Test
	    public void testFromString_trailingCarriageReturn_returnsIntegerValue() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        Assertions.assertEquals(Integer.valueOf(42), converter.fromString("42\r"));
	    }

	    @Test
	    public void testFromString_integerLimits_returnsIntegerValue() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        Assertions.assertEquals(Integer.valueOf(Integer.MAX_VALUE), converter.fromString("2147483647"));
	        Assertions.assertEquals(Integer.valueOf(Integer.MIN_VALUE), converter.fromString("-2147483648"));
	    }

	    @Test
	    public void testFromString_emptyString_returnsZero() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        Assertions.assertEquals(Integer.valueOf(0), converter.fromString(""));
	        Assertions.assertEquals(Integer.valueOf(0), converter.fromString("   "));
	    }

	    @Test
	    public void testFromString_null_returnsNull() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        Integer actual = assertDoesNotThrow(() -> converter.fromString(null));
	        Assertions.assertNull(actual);
	    }

	    @Test
	    public void testFromString_invalidIntegerString_returnsNull() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        ArrayList<String> inValidNumbers = new ArrayList<String> (
	        		Arrays.asList("abc","12a","1.5","1e5","1-","--1","+-1","1 2","@#@"));

	        for (String intString : inValidNumbers) {
		        Integer actual = assertDoesNotThrow(() -> converter.fromString(intString),
		        		"fromString threw for " + intString);
		        Assertions.assertNull(actual, "fromString(" + intString + ")");
	        }
	    }

	    @Test
	    public void testFromString_loneSign_returnsNull() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        for (String intString : new String[] { "-", "+" }) {
		        Integer actual = assertDoesNotThrow(() -> converter.fromString(intString),
		        		"fromString threw for " + intString);
		        Assertions.assertNull(actual, "fromString(" + intString + ")");
	        }
	    }

	    @Test
	    public void testFromString_integralDecimal_returnsIntegerValue() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        Assertions.assertEquals(Integer.valueOf(10), converter.fromString("10.0"));
	        Assertions.assertEquals(Integer.valueOf(10), converter.fromString("10.00"));
	        Assertions.assertEquals(Integer.valueOf(10), converter.fromString("10.0000"));
	        Assertions.assertEquals(Integer.valueOf(10), converter.fromString("10."));
	        Assertions.assertEquals(Integer.valueOf(-3), converter.fromString("-3.0"));
	        Assertions.assertEquals(Integer.valueOf(3), converter.fromString("+3.0"));
	        Assertions.assertEquals(Integer.valueOf(0), converter.fromString(".0"));
	        Assertions.assertEquals(Integer.valueOf(10), converter.fromString("10.0\r"));
	    }

	    @Test
	    public void testFromString_nonZeroFraction_returnsNull() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        for (String intString : new String[] { "1.5", "10.01", "-3.5", ".5", "10.0001" }) {
		        Integer actual = assertDoesNotThrow(() -> converter.fromString(intString),
		        		"fromString threw for " + intString);
		        Assertions.assertNull(actual, "fromString(" + intString + ")");
	        }
	    }

	    @Test
	    public void testFromString_exponent_returnsNull() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        for (String intString : new String[] { "1e5", "1E5", "1.0E2" }) {
		        Assertions.assertNull(converter.fromString(intString), "fromString(" + intString + ")");
	        }
	    }

	    @Test
	    public void testFromString_outOfRange_returnsNull() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        for (String intString : new String[] { "2147483648", "-2147483649", "99999999999", "2147483648.0" }) {
		        Integer actual = assertDoesNotThrow(() -> converter.fromString(intString),
		        		"fromString threw for " + intString);
		        Assertions.assertNull(actual, "fromString(" + intString + ")");
	        }
	    }

	    @Test
	    public void testToString_validInteger_returnsString() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        Assertions.assertEquals("1234", converter.toString(1234));
	        Assertions.assertEquals("-1234", converter.toString(-1234));
	    }

	    @Test
	    public void testToString_nullInteger_returnsEmptyString() {
	        // inherited from javafx IntegerStringConverter
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        Assertions.assertEquals("", converter.toString(null));
	    }

	    @Test
	    public void testIsNumber_validIntegerString_returnsTrue() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();

	        ArrayList<String> validNumbers = new ArrayList<String> (
	        		Arrays.asList("0","100","-102","+100","  7  ","100\r",
	        				"10.0","10.00","-3.0","10."));

	        for (String intString : validNumbers) {
		        if (!converter.isNumber(intString))
		        	fail("valid integer declared invalid " + intString);
	        }
	    }

	    @Test
	    public void testIsNumber_invalidIntegerString_returnsFalse() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        ArrayList<String> inValidNumbers = new ArrayList<String> (
	        		Arrays.asList("abc","12a","1.5","1e5","1-","--1","+-1","1 2","@#@",
	        				"-","+","","   ",
	        				"10.01",".","-.","1.0.0","2147483648"));

	        for (String intString : inValidNumbers) {
		        if (converter.isNumber(intString))
		        	fail("invalid integer declared valid " + intString);
	        }
	    }

	    @Test
	    public void testIsNumber_null_returnsFalse() {
	        EnhancedIntegerStringConverter converter = new EnhancedIntegerStringConverter();
	        Assertions.assertFalse(assertDoesNotThrow(() -> converter.isNumber(null)));
	    }

	}
