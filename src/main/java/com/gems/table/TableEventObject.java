/*
	TableEventObject.java
	
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
/*  Some portions of code has been borrowed and modified from 
 *  https://www.javaworld.com/article/2077333/core-java/mr-happy-object-teaches-custom-events.html
 */

package com.gems.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableEventObject {

	private static TableEventObject tableEventObject = new TableEventObject();
	
	// Singleton initialization
	public TableEventObject() {
	}	
	public static TableEventObject getInstance() {
		return tableEventObject;
	}
	
	
	private TableEventType _tet;
	private Object _teo;
    private List _listeners = new ArrayList();

    
    public synchronized void copy() {
    	_tet = TableEventType.COPY;
    	_fireAppEvent();
    }     

    public synchronized void paste() {
    	_tet = TableEventType.PASTE;
    	_fireAppEvent();
    }     

    public synchronized void cut() {
    	_tet = TableEventType.CUT;
    	_fireAppEvent();
    }  
    
    public synchronized void delete() {
    	_tet = TableEventType.DELETE;
    	_fireAppEvent();
    }  
    
    
    
    public synchronized void tableFocusChanged(String id) {
    	_tet = TableEventType.TABLE_FOCUS_CHANGED;
    	_teo = id;
    	_fireAppEvent();
    }      

    public synchronized void insertRowAbove() {
    	_tet = TableEventType.INSERT_ROW_ABOVE;
    	_fireAppEvent();
    }  
    
    public synchronized void deleteRow() {
    	_tet = TableEventType.DELETE_ROW;
    	_fireAppEvent();
    }  
    
    
    public synchronized void addTableEventListener( TableEventListener l ) {
        _listeners.add( l );
    }
    
    public synchronized void removeTableEventListener( TableEventListener l ) {
        _listeners.remove( l );
    }

    
    
    private synchronized void _fireAppEvent() {
        TableEvent tet = new TableEvent( this, _tet,_teo );
        Iterator listeners = _listeners.iterator();
        while( listeners.hasNext() ) {
            ( (TableEventListener) listeners.next() ).tableEventReceived( tet );
        }
    }
    
}
