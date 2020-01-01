/*
	TableEvent.java
	
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

import java.util.EventObject;

// https://www.javaworld.com/article/2077333/core-java/mr-happy-object-teaches-custom-events.html

public class TableEvent extends EventObject {

	private TableEventType tet;
	private Object teo;
	
	public TableEvent(Object source, TableEventType tableEventType) {
		super(source);
		tet = tableEventType;
	}

	public TableEvent(Object source, TableEventType tableEventType, Object obj) {
		super(source);
		tet = tableEventType;
		teo = obj;
	}
	
	public TableEventType tableEventType() {
		return tet;
	}
	
	public Object tableEventObject() {
		return teo;
	}

}
