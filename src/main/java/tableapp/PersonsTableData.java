/*
	ThicknessTableData.java
	
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

package tableapp;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class PersonsTableData {

	private SimpleIntegerProperty index;
	private SimpleStringProperty name;
	private SimpleIntegerProperty age;


	public PersonsTableData(Persons pp) {
		this.index = new SimpleIntegerProperty(pp.getIndex());
		this.name = new SimpleStringProperty(pp.getName());
		this.age = new SimpleIntegerProperty(pp.getAge());
	}

	public PersonsTableData(final Integer index, final String name,
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
	
	public void printThPoint() {
		System.out.println(getIndex()+"\t"+getName()+"\t"+getAge());
		
	}
	

}
