/*
	InputCommon.java
	
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

	static int np; // number of Person in table
	static ObservableList<PersonTableData> personData = FXCollections.observableArrayList();

	
	public void init() {
		defaultValues();
	}

	public void setNp(int np) {
		InputCommon.np = np;
	}

	public int getNp() {
		return InputCommon.np;
	}

	public List<Person> initPersonData() {
		return Arrays.asList();
	}
	
	public boolean addPerson(Person thp) {
		
		personData.add(new PersonTableData(thp));
		return true;
	}

	public boolean addPerson(int row,Person pp) {
		
		np++;
		personData.add(row,new PersonTableData(pp));
		
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


	
	public ObservableList<PersonTableData> getPersonData() {
		return InputCommon.personData;
	}



	public void setPersonData(int row, int col, String val) {
		PersonTableData ptd = personData.get(row);
		ptd.updateCol(col, val);
		InputCommon.personData.set(row, ptd);
	}

	
	public void clearValues() {

		
		np = 0;
		
		personData.clear();
	}

    public void defaultPerson(final List<Person> pp) {
        pp.forEach(p -> personData.add(new PersonTableData(p)));
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
		

		System.out.println("Number of Person=" + InputCommon.np );
		System.out.println("Person:");
		for (int i = 0; i < personData.size(); i++) {
			personData.get(i).printPersonPoint();
		}

	}

}
