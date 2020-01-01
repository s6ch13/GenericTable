/*
	TableEventObject.java
	
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
