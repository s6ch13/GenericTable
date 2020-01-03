/*
	InputCommon.java
	
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

import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InputCommon {

	private static InputCommon inputCommon = new InputCommon();

	// Singleton initialization
	private InputCommon() {
	}

	public static InputCommon getInstance() {
		return inputCommon;
	}

	static int np; // number of Persons in table
	static ObservableList<PersonsTableData> personData = FXCollections.observableArrayList();

	
	public void init() {
		defaultValues();
	}



	public void setNp(int np) {
		InputCommon.np = np;
	}



	public int getNp() {
		return InputCommon.np;
	}

	
	
	public List<Persons> initPersonData() {
		return Arrays.asList();
	}
	
	public boolean addPerson(Persons thp) {
		
		personData.add(new PersonsTableData(thp));
		return true;
	}

	public boolean addPerson(int row,Persons pp) {
		
		np++;
		personData.add(row,new PersonsTableData(pp));
		
		// shift indices up
		for (int i = row; i < np; i++) {
			personData.get(i).setIndex(i+1);
		}
		
		return true;
	}
	


	public boolean deletePerson(int index) {
		if (index <= personData.size() && personData.size() > 0 && np > 0) {
			personData.remove(index);
			np--;
						
			// shift indices down
			for (int i = index; i < np; i++) {
				personData.get(i).setIndex(i+1);
			}
				
			return true;

		} else {
			return false;
		}		
	}


	
	public ObservableList<PersonsTableData> getPersonData() {
		return InputCommon.personData;
	}



	public void setPersonData(int row, int col, String val) {
		PersonsTableData ptd = personData.get(row);
		ptd.updateCol(col, val);
		InputCommon.personData.set(row, ptd);
	}

	
	public void clearValues() {

		
		np = 0;
		
		personData.clear();
	}

    public void defaultPerson(final List<Persons> pp) {
        pp.forEach(p -> personData.add(new PersonsTableData(p)));
    }
	
	public void defaultValues() {

		// Clear existing values
		clearValues();

		// set default values;
		np =0;
		defaultPerson(initPersonData());
	}

	

	public Object[][] getPersonTable() {
		if (getNp() == 0)
			return null;

		Object obj[][] = new Object[getNp()][3];
		for (int i = 0; i < getNp(); i++) {
			obj[i][0] = personData.get(i).getIndex();
			obj[i][1] = personData.get(i).getName();
			obj[i][2] = personData.get(i).getAge();
		}
		return obj;
	}



	

	public void printInputs() {
		System.out.println("Inputs");
		System.out.println("------");
		

		System.out.println("Number of Persons=" + InputCommon.np );
		System.out.println("Persons:");
		for (int i = 0; i < personData.size(); i++) {
			personData.get(i).printThPoint();
		}

	}

}
