package com.gems.table;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnhancedDateStringConverterTest {
	    @Test
	    public void testFromString_validDate_parses() {
	        EnhancedDateStringConverter converter = new EnhancedDateStringConverter("yyyy-MM-dd");
	        Date date = converter.fromString("2021-03-15");
	        Assertions.assertNotNull(date);
	        Assertions.assertEquals("2021-03-15", converter.toString(date));
	    }

	    @Test
	    public void testFromString_surroundingWhitespace_parses() {
	        EnhancedDateStringConverter converter = new EnhancedDateStringConverter("yyyy-MM-dd");
	        for (String dateString : new String[] { "  2021-03-15  ", "2021-03-15\r", "\t2021-03-15\n" }) {
		        Assertions.assertNotNull(converter.fromString(dateString), "fromString(" + dateString + ")");
	        }
	    }

	    @Test
	    public void testFromString_invalidDate_returnsNull() {
	        EnhancedDateStringConverter converter = new EnhancedDateStringConverter("yyyy-MM-dd");
	        for (String dateString : new String[] {
	        		"2021-03-15xyz", "2021-03-15 xyz", "2021-02-30", "2021-13-01",
	        		"21-03-05",			// would otherwise be read as the year 21
	        		"2021-3-5", "15-03-2021", "abc", "" }) {
		        Date actual = assertDoesNotThrow(() -> converter.fromString(dateString),
		        		"fromString threw for " + dateString);
		        Assertions.assertNull(actual, "fromString(" + dateString + ")");
	        }
	    }

	    @Test
	    public void testFromString_null_returnsNull() {
	        EnhancedDateStringConverter converter = new EnhancedDateStringConverter("yyyy-MM-dd");
	        Assertions.assertNull(assertDoesNotThrow(() -> converter.fromString(null)));
	    }

	    @Test
	    public void testToString_null_returnsEmptyString() {
	        // inherited from javafx DateStringConverter
	        EnhancedDateStringConverter converter = new EnhancedDateStringConverter("yyyy-MM-dd");
	        Assertions.assertEquals("", converter.toString(null));
	    }

	}
