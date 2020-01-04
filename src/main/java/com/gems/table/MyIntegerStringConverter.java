/*
	MyIntegerStringConverter.java
	
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
