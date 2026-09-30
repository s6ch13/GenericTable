package com.gems.table;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnhancedDoubleStringConverterTest {
	    @Test
	    public void testFromString_validDoubleString_returnsDoubleValue() {
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter();
	        String doubleString = "12.34";
	        Double expected = 12.34;
	        Double actual = converter.fromString(doubleString);
	        Assertions.assertEquals(expected, actual, 0.001);
	    }

	    @Test
	    public void testFromString_emptyString_returnsZero() {
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter();
	        String doubleString = "";
	        Double expected = 0.0;
	        Double actual = converter.fromString(doubleString);
	        Assertions.assertEquals(expected, actual, 0.001);
	    }

	    @Test
	    public void testFromString_null_returnsNaN() {
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter();
	        Double actual = assertDoesNotThrow(() -> converter.fromString(null));
	        Assertions.assertEquals(Double.NaN, actual);
	    }

	    @Test
	    public void testFromString_invalidDoubleString_returnsNaN() {
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter();
	        String doubleString = "12.34.56";
	        Double expected = Double.NaN;
	        Double actual = converter.fromString(doubleString);
	        Assertions.assertEquals(expected, actual);
	    }

	    @Test
	    public void testToString_validDouble_returnsFormattedString() {
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter();
	        Double doubleValue = 12.3456789;
	        String expected = "12.3457";
	        String actual = converter.toString(doubleValue);
	        Assertions.assertEquals(expected, actual);
	    }

	    @Test
	    public void testToString_nullDouble_returnsNull() {
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter();
	        Double doubleValue = null;
	        String expected = null;
	        String actual = converter.toString(doubleValue);
	        Assertions.assertEquals(expected, actual);
	    }

	    @Test
	    public void testToString_NaN_returnsDash() {
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter();
	        Double doubleValue = Double.NaN;
	        String expected = "-";
	        String actual = converter.toString(doubleValue);
	        Assertions.assertEquals(expected, actual);
	    }

	    @Test
	    public void testIsNumber_validDoubleString_returnsTrue() {
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter();

	        ArrayList<String> validNumbers = new ArrayList<String> (
	        		Arrays.asList("100","101.123","-102","-103.4567",
	        				"+100","+101.123","-102","-103.4567",
	        				".25","-.3","+.25",
	        				"1E5","1E-2","-2E5","-2E-3",
	        				"1.234E4","-1.456E5","1.345E-2","-1.234E-2"
	        				));
	        	
	        for (String doubleString : validNumbers) {
		        boolean actual = converter.isNumber(doubleString);
		        if (!actual) 
		        	fail("valid double declared invalid" + doubleString);
	        }
	    }

	    @Test
	    public void testIsNumber_invalidDoubleString_returnsFalse() {
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter();
	        ArrayList<String> inValidNumbers = new ArrayList<String> (
	        		Arrays.asList("11.22.33","a","abc",
	        				".","-","+","E","e",
	        				"..","--","++","+-","ee","-e","+e"
	        				,"33ee5","23.3e++4","@#@"
	        				));
	        	
	        for (String doubleString : inValidNumbers) {
		        boolean actual = converter.isNumber(doubleString);
		        if (actual) 
		        	fail("invalid double declared valid" + doubleString);
	        }
	    }

	    private static final String[] TRAILING_EXPONENT = { "1E", "1E+", "1.2E", "1E-" };

	    @Test
	    public void testIsNumber_trailingExponentWithoutDigits_returnsFalse() {
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter();
	        for (String doubleString : TRAILING_EXPONENT) {
		        if (converter.isNumber(doubleString))
		        	fail("invalid double declared valid " + doubleString);
	        }
	    }

	    @Test
	    public void testFromString_trailingExponentWithoutDigits_returnsNaN() {
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter();
	        for (String doubleString : TRAILING_EXPONENT) {
		        Double actual = assertDoesNotThrow(() -> converter.fromString(doubleString),
		        		"fromString threw for " + doubleString);
		        Assertions.assertEquals(Double.NaN, actual, "fromString(" + doubleString + ")");
	        }
	    }

	    @Test
	    public void testFromString_isNumberGap_returnsNaN() {
	        // simulate a future gap in isNumber: fromString must still not throw
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter() {
	        	@Override
	        	public boolean isNumber(String value) { return true; }
	        };
	        Double actual = assertDoesNotThrow(() -> converter.fromString("abc"));
	        Assertions.assertEquals(Double.NaN, actual);
	    }

	    private static final String[] DECIMAL_AFTER_EXPONENT = { "1e5.5", "1e.5", "2E3.0", "1.4E5.5" };

	    @Test
	    public void testIsNumber_decimalAfterExponent_returnsFalse() {
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter();
	        for (String doubleString : DECIMAL_AFTER_EXPONENT) {
		        if (converter.isNumber(doubleString))
		        	fail("invalid double declared valid " + doubleString);
	        }
	    }

	    @Test
	    public void testFromString_decimalAfterExponent_returnsNaN() {
	        EnhancedDoubleStringConverter converter = new EnhancedDoubleStringConverter();
	        for (String doubleString : DECIMAL_AFTER_EXPONENT) {
		        Double actual = assertDoesNotThrow(() -> converter.fromString(doubleString),
		        		"fromString threw for " + doubleString);
		        Assertions.assertEquals(Double.NaN, actual, "fromString(" + doubleString + ")");
	        }
	    }

	}
