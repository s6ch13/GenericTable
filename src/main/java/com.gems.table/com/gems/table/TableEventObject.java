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

package com.gems.table;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TableEventObject {

	private static TableEventObject tableEventObject = new TableEventObject();
	
	// Singleton initialization
	private TableEventObject() {
	}
	public static TableEventObject getInstance() {
		return tableEventObject;
	}
	
	
    // Thread safe version of ArrayList.
    private List<TableEventListener> _listeners = new CopyOnWriteArrayList<TableEventListener>();


    public void copy() {
    	_fireAppEvent(TableEventType.COPY, null);
    }

    public void paste() {
    	_fireAppEvent(TableEventType.PASTE, null);
    }

    public void cut() {
    	_fireAppEvent(TableEventType.CUT, null);
    }

    public void delete() {
    	_fireAppEvent(TableEventType.DELETE, null);
    }



    public void tableFocusChanged(String id) {
    	_fireAppEvent(TableEventType.TABLE_FOCUS_CHANGED, id);
    }

    public void insertRowAbove() {
    	_fireAppEvent(TableEventType.INSERT_ROW_ABOVE, null);
    }

    public void deleteRow() {
    	_fireAppEvent(TableEventType.DELETE_ROW, null);
    }
    
    
    public synchronized void addTableEventListener( TableEventListener l ) {
        _listeners.add( l );
    }
    
    public synchronized void removeTableEventListener( TableEventListener l ) {
        _listeners.remove( l );
    }

    
    
    private synchronized void _fireAppEvent(TableEventType type, Object payload) {
        TableEvent tet = new TableEvent( this, type, payload );
        Iterator<TableEventListener> listeners = _listeners.iterator();
        while( listeners.hasNext() ) {
            ( (TableEventListener) listeners.next() ).tableEventReceived( tet );
        }
    }
    
}
