/*
	PersonTableData.java
	
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

package tableapp;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class PersonTableData {

	private SimpleIntegerProperty index;
	private SimpleStringProperty name;
	private SimpleIntegerProperty age;


	public PersonTableData(Person pp) {
		this.index = new SimpleIntegerProperty(pp.getIndex());
		this.name = new SimpleStringProperty(pp.getName());
		this.age = new SimpleIntegerProperty(pp.getAge());
	}

	public PersonTableData(final Integer index, final String name,
			final Integer age) {
		this.index = new SimpleIntegerProperty(index);
		this.name = new SimpleStringProperty(name);
		this.age = new SimpleIntegerProperty(age);
	}

	public Integer getIndex() {
		return index.get();
	}

	public void setIndex(final Integer index) {
		this.index.set(index);
	}

	public String getName() {
		return name.get();
	}

	public void setName(final String name) {
		this.name.set(name);
	}
	
	public Integer getAge() {
		return age.get();
	}

	public void setAge(final Integer age) {
		this.age.set(age);
	}	
	
	public String getCol(int col) {
		String ret = new String();

		switch (col) {
		case 0:
			ret = Integer.toString(index.get());
			break;
		case 1:
			ret = name.get();
			break;
		case 2:
			ret = Integer.toString(age.get());
			break;			
		}
		return ret;
	}
	
	public void updateCol(int col,String val) {
		switch(col) {
		case 0:
			setIndex(Integer.valueOf(val));
			break;
		case 1:
			setName(val);
			break;
		case 2:
			setAge(Integer.valueOf(val));
			break;
		}
	}
	
	public void printPersonPoint() {
		System.out.println(getIndex()+"\t"+getName()+"\t"+getAge());
		
	}
	

}
