/*
	MyDoubleStringConverter.java
	
	Copyright (C) 2019  Sriram C.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.gems.table;

import javafx.util.converter.DoubleStringConverter;

public class MyDoubleStringConverter extends DoubleStringConverter {

	public String format = "%.4f";
	


	public MyDoubleStringConverter() { 
		super();	
	}

	public MyDoubleStringConverter(String format) { 
		super();
		this.format = format;
	}
	
	@Override
	public Double fromString(final String value) {
		if (value.isEmpty()) {
			return (double)0;
		}
		return isNumber(value) ? super.fromString(value)
				:Double.NaN;
	}
	
	@Override
	public String toString(final Double value) {
		if (value == null) {
			return null;
		}
		
		if (value.isNaN()) {
			return "-";
		}

		return String.format(this.format, value.doubleValue());
	}

	public boolean isNumber(String value) {
		int size = value.length();
		boolean decimalPt = false;  // occurrence of decimal pt
		
		// exponent value
		boolean e = false;  // occurrence of E pt
		int epos = 0; // position of exponent value
		
		if (value.compareTo(".") == 0 || 
				value.compareTo("-") ==0 || 
				value.compareTo("-") ==0 || 
				value.compareTo("+") ==0 || 
				value.compareTo("e") ==0 || 
				value.compareTo("E") ==0 )			
			return false;
		
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
			
			// parse decimal pt
			if (value.charAt(i) == '.' && size > 1) {
				if(decimalPt == false) {
					decimalPt = true;
				} else {
					return false;
				}
			}
			
			// parse exponent
			if (value.charAt(i) == 'e' || value.charAt(i) == 'E') {
				if(e == false) {
					e = true;
					epos = i; // position of e in string.  
				} else {
					return false;
				}
			}
			
			// -ve and +ve sign can be only at the first position or 
			// after occurrence of E/e.  
			if(value.charAt(i) == '-' && (i != 0  && i != epos +1)) {
				return false;
			}

			if(value.charAt(i) == '+' &&  (i != 0  && i != epos+1))  {
				return false;
			}
			
		}
		
		return true;

	}
	
}
