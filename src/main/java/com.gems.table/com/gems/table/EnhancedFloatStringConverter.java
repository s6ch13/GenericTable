/*
	EnhancedFloatStringConverter.java
	Copyright (C) 2019  Sriram C.

	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions are met:
	
	1. Redistributions of source code must retain the above copyright notice, this
	   list of conditions and the following disclaimer.
	2. Redistributions in binary form must reproduce the above copyright notice,
	   this list of conditions and the following disclaimer in the documentation
	   and/or other materials provided with the distribution.
	
	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
	DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
	ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
	ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/ 

/* Reference:
 * Some portions of the code was initially developed by DanNewton and enhanced
 * https://dzone.com/articles/editable-tables-in-javafx
 */


package com.gems.table;

import javafx.util.converter.FloatStringConverter;

public class EnhancedFloatStringConverter extends FloatStringConverter {

	private String format = "%.4f";

	public EnhancedFloatStringConverter() { 
		super();
	}

	public EnhancedFloatStringConverter(String format) { 
		super();
		this.format = format;
	}
	
	@Override
	public Float fromString(final String value) {
		if (value.trim().isEmpty()) {
			return Float.valueOf(0);
		}
		return isNumber(value) ? super.fromString(value)
				: Float.NaN;
	}

	@Override
	public String toString(final Float value) {
		if (value == null) {
			return null;
		}
		
		if (value.isNaN()) {
			return "-";
		}

		return String.format(this.format, value.floatValue());
	}
	
	public boolean isNumber(String value) {
		value = value.trim();

		int size = value.length();
		boolean decimalPt = false;  // occurrence of decimal pt
		
		// exponent value
		boolean e = false;  // occurrence of E pt
		int epos = 0; // position of exponent value
		boolean digitFound = false;	// ensure that atleast a number is present
		
		for (int i = 0; i < size; i++) {
			
			// when pasting from excel, the last char is /r.  
			// other valid digits are  0-9 . + - e E
			if (!(Character.isDigit(value.charAt(i)) ||
					(i == (size-1) && value.charAt(i) == '\r' ) || 
					value.charAt(i) == '.' || 
					value.charAt(i) == '-' ||
					value.charAt(i) == '+' ||
					value.charAt(i) == 'e' ||
					value.charAt(i) == 'E') ) {
				return false;
			}
			
			if (Character.isDigit(value.charAt(i)))
				digitFound = true;
			
			// parse decimal pt
			if (value.charAt(i) == '.' && size > 1 ) {
				if(decimalPt == false) {
					decimalPt = true;
				} else {
					return false;
				}
			}
			
			// parse exponent
			if (value.charAt(i) == 'e' || value.charAt(i) == 'E') {
				if(e == false && i > 0 && Character.isDigit(value.charAt(i-1))) {
					e = true;
					epos = i; // position of e in string.  
				} else {
					return false;
				}
			}
			
			// -ve and +ve sign can be only at the first position or 
			// immediately after occurrence of E/e.  
			if(value.charAt(i) == '-' || value.charAt(i) == '+') { 
				if (i == 0)
					continue;
				if (e == true && i == epos+1) {
					continue;
				} else { 
					return false;
				}
			}
		}
		
		if (!digitFound) 
			return false;
		else 
			return true;
	}	
}
