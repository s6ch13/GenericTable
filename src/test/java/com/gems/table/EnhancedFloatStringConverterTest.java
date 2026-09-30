package com.gems.table;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnhancedFloatStringConverterTest {
	    @Test
	    public void testFromString_validFloatString_returnsFloatValue() {
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter();
	        String floatString = "12.34";
	        Float expected = (float) 12.34;
	        Float actual = converter.fromString(floatString);
	        Assertions.assertEquals(expected, actual, 0.001);
	    }

	    @Test
	    public void testFromString_emptyString_returnsZero() {
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter();
	        String floatString = "";
	        Float expected = (float) 0.0;
	        Float actual = converter.fromString(floatString);
	        Assertions.assertEquals(expected, actual, 0.001);
	    }

	    @Test
	    public void testFromString_null_returnsNaN() {
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter();
	        Float actual = assertDoesNotThrow(() -> converter.fromString(null));
	        Assertions.assertEquals(Float.NaN, actual);
	    }

	    @Test
	    public void testFromString_invalidFloatString_returnsNaN() {
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter();
	        String floatString = "12.34.56";
	        Float expected = Float.NaN;
	        Float actual = converter.fromString(floatString);
	        Assertions.assertEquals(expected, actual);
	    }

	    @Test
	    public void testToString_validFloat_returnsFormattedString() {
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter();
	        Float floatValue = (float) 12.3456789;
	        String expected = "12.3457";
	        String actual = converter.toString(floatValue);
	        Assertions.assertEquals(expected, actual);
	    }

	    @Test
	    public void testToString_nullFloat_returnsNull() {
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter();
	        Float floatValue = null;
	        String expected = null;
	        String actual = converter.toString(floatValue);
	        Assertions.assertEquals(expected, actual);
	    }

	    @Test
	    public void testToString_NaN_returnsDash() {
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter();
	        Float floatValue = Float.NaN;
	        String expected = "-";
	        String actual = converter.toString(floatValue);
	        Assertions.assertEquals(expected, actual);
	    }

	    @Test
	    public void testIsNumber_validFloatString_returnsTrue() {
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter();

	        ArrayList<String> validNumbers = new ArrayList<String> (
	        		Arrays.asList("100","101.123","-102","-103.4567",
	        				"+100","+101.123","-102","-103.4567",
	        				".25","-.3","+.25",
	        				"1E5","1E-2","-2E5","-2E-3",
	        				"1.234E4","-1.456E5","1.345E-2","-1.234E-2"
	        				));
	        	
	        for (String floatString : validNumbers) {
		        boolean actual = converter.isNumber(floatString);
		        if (!actual) 
		        	fail("valid float declared invalid" + floatString);
	        }
	    }

	    @Test
	    public void testIsNumber_invalidFloatString_returnsFalse() {
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter();
	        ArrayList<String> inValidNumbers = new ArrayList<String> (
	        		Arrays.asList("11.22.33","a","abc",
	        				".","-","+","E","e",
	        				"..","--","++","+-","ee","-e","+e"
	        				,"33ee5","23.3e++4","@#@"
	        				));
	        	
	        for (String floatString : inValidNumbers) {
		        boolean actual = converter.isNumber(floatString);
		        if (actual) 
		        	fail("invalid float declared valid" + floatString);
	        }
	    }

	    private static final String[] TRAILING_EXPONENT = { "1E", "1E+", "1.2E", "1E-" };

	    @Test
	    public void testIsNumber_trailingExponentWithoutDigits_returnsFalse() {
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter();
	        for (String floatString : TRAILING_EXPONENT) {
		        if (converter.isNumber(floatString))
		        	fail("invalid float declared valid " + floatString);
	        }
	    }

	    @Test
	    public void testFromString_trailingExponentWithoutDigits_returnsNaN() {
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter();
	        for (String floatString : TRAILING_EXPONENT) {
		        Float actual = assertDoesNotThrow(() -> converter.fromString(floatString),
		        		"fromString threw for " + floatString);
		        Assertions.assertEquals(Float.NaN, actual, "fromString(" + floatString + ")");
	        }
	    }

	    @Test
	    public void testFromString_isNumberGap_returnsNaN() {
	        // simulate a future gap in isNumber: fromString must still not throw
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter() {
	        	@Override
	        	public boolean isNumber(String value) { return true; }
	        };
	        Float actual = assertDoesNotThrow(() -> converter.fromString("abc"));
	        Assertions.assertEquals(Float.NaN, actual);
	    }

	    private static final String[] DECIMAL_AFTER_EXPONENT = { "1e5.5", "1e.5", "2E3.0", "1.4E5.5" };

	    @Test
	    public void testIsNumber_decimalAfterExponent_returnsFalse() {
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter();
	        for (String floatString : DECIMAL_AFTER_EXPONENT) {
		        if (converter.isNumber(floatString))
		        	fail("invalid float declared valid " + floatString);
	        }
	    }

	    @Test
	    public void testFromString_decimalAfterExponent_returnsNaN() {
	        EnhancedFloatStringConverter converter = new EnhancedFloatStringConverter();
	        for (String floatString : DECIMAL_AFTER_EXPONENT) {
		        Float actual = assertDoesNotThrow(() -> converter.fromString(floatString),
		        		"fromString threw for " + floatString);
		        Assertions.assertEquals(Float.NaN, actual, "fromString(" + floatString + ")");
	        }
	    }

	}
