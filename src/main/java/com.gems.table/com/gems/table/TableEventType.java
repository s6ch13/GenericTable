/*
	TableEventType.java
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

package com.gems.table;

public class TableEventType {
	
	
	public static final TableEventType COPY = new  TableEventType("copy");
	public static final TableEventType PASTE = new  TableEventType("paste");
	public static final TableEventType CUT = new  TableEventType("cut");
	public static final TableEventType DELETE = new  TableEventType("delete");
		
	public static final TableEventType TABLE_FOCUS_CHANGED = new  TableEventType("tableFocusChanged");
	
	public static final TableEventType INSERT_ROW_ABOVE = new  TableEventType("insertRowAbove");
	public static final TableEventType DELETE_ROW = new  TableEventType("deleteRow");
	
	private String tableEventType;
	
	private TableEventType(String type) {
		tableEventType = type;
	}
	
	public String toString() {
		return tableEventType;
	}

}
