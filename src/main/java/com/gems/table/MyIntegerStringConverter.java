/*
	MyIntegerStringConverter.java
	
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

import javafx.util.converter.IntegerStringConverter;

public class MyIntegerStringConverter extends IntegerStringConverter {

	@Override
	public Integer fromString(final String value) {
		if (value.isEmpty()) {
			return (int)0;
		}
		return isNumber(value) ? super.fromString(value)
				: null;
	}

	
	// FIXME - should also accept numbers like 1.0, 10.0 
	public boolean isNumber(String value) {
		int size = value.length();
		for (int i = 0; i < size; i++) {
			
			if(value.charAt(i) == '-' && i >0) {
				return false;
			}
			
			if(value.charAt(i) == '+' && i >0) {
				return false;
			}			
			
			// when pasting from excel, the last char is /r.  
			// other valid digits are  0-9 + -
			
			if (!(Character.isDigit(value.charAt(i)) ||
					(i == (size-1) && value.charAt(i) == '\r' ) || 
					value.charAt(i) == '-' ||
					value.charAt(i) == '+' ) ) {
				return false;
			}
			
		}
		return true;
	}
}
