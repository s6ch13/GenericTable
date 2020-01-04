/*  
 * 	TableEvent.java
 *  This portion of code has been borrowed and modified from 
 *  https://www.javaworld.com/article/2077333/core-java/mr-happy-object-teaches-custom-events.html
 */

package com.gems.table;

import java.util.EventObject;


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
