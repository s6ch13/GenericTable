/*  
 * 	TableEventListner.java
 *  This portion of code has been borrowed and modified from 
 *  https://www.javaworld.com/article/2077333/core-java/mr-happy-object-teaches-custom-events.html
 */

package com.gems.table;

public interface TableEventListener {
	public void tableEventReceived(TableEvent event);
}
