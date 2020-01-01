/*
	TableEventType.java
	
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
