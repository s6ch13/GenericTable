/*
	EnhancedIntegerStringConverter.java
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

import java.math.BigDecimal;
import java.util.regex.Pattern;

import javafx.util.converter.IntegerStringConverter;

public class EnhancedIntegerStringConverter extends IntegerStringConverter {

	// exponents ("1e5") are deliberately not accepted
	private static final Pattern INTEGER_LIKE =
			Pattern.compile("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)");

	@Override
	public Integer fromString(final String value) {
		if (value == null) {
			return null;
		}
		if (value.trim().isEmpty()) {
			return Integer.valueOf(0);
		}
		return parse(value);
	}

	/**
	 * Check if the value is an integer. Decimals with an all-zero fraction such as
	 * 1.0 or 10.00 are accepted.
	 *
	 * @param value - string to check
	 * @return true if value can be converted to an Integer
	 */
	public boolean isNumber(String value) {
		return value != null && parse(value) != null;
	}

	/**
	 * Parse an integer, or a decimal whose fraction is all zeros ("10.0").
	 * trim() also drops the trailing \r left when pasting from Excel.
	 *
	 * @return the value, or null for a non-zero fraction ("1.5"), a value outside
	 *         the int range, or anything that is not a number
	 */
	private static Integer parse(String value) {
		String s = value.trim();
		if (!INTEGER_LIKE.matcher(s).matches()) {
			return null;
		}
		try {
			return new BigDecimal(s).intValueExact();
		} catch (ArithmeticException e) {
			return null;
		}
	}
}
