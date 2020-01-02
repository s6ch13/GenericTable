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


import javafx.beans.value.ObservableValue;
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

	static int nth; // number of thickness specified.  ( also equal to nfr)
	static ObservableList<ThicknessTableData> thData = FXCollections.observableArrayList();

	
	public void init() {
		defaultValues();
	}



	public void setNth(int nth) {
		InputCommon.nth = nth;
	}



	public int getNth() {
		return InputCommon.nth;
	}

	
	
	public List<Thickness> initThData() {
		return Arrays.asList(new Thickness(1, 0.0, 0.0));
	}
	public boolean addTh(Thickness thp) {
		
		thData.add(new ThicknessTableData(thp));
		return true;
	}

	public boolean addTh(int row,Thickness thp) {
		
		nth++;
		thData.add(row,new ThicknessTableData(thp));
		
		// shift indices up
		for (int i = row; i < nth; i++) {
			thData.get(i).setIndex(i+1);
		}
		
		return true;
	}
	


	public boolean deleteTh(int index) {
		if (index <= thData.size() && thData.size() > 0 && nth > 1) {
			thData.remove(index);
			nth--;
						
			// shift indices down
			for (int i = index; i < nth; i++) {
				thData.get(i).setIndex(i+1);
			}
				
			return true;

		} else {
			return false;
		}		
	}


	
	public ObservableList<ThicknessTableData> getThData() {
		return InputCommon.thData;
	}



	public void setThData(int row, int col, String val) {
		ThicknessTableData thtd = thData.get(row);
		thtd.updateCol(col, val);
		InputCommon.thData.set(row, thtd);
	}

	
	public void clearValues() {

		
		nth = 0;
		
		thData.clear();
	}

    public void defaultTh(final List<Thickness> thp) {
        thp.forEach(p -> thData.add(new ThicknessTableData(p)));
    }
	
	public void defaultValues() {

		// Clear existing values
		clearValues();

		// set default values;
		nth =1;
		defaultTh(initThData());
	}

	

	public Object[][] getThicknessTable() {
		if (getNth() == 0)
			return null;

		Object obj[][] = new Object[getNth()][3];
		for (int i = 0; i < getNth(); i++) {
			obj[i][0] = thData.get(i).getIndex();
			obj[i][1] = thData.get(i).getThv();
			obj[i][2] = thData.get(i).getXth();
		}
		return obj;
	}



	

	public void printInputs() {
		System.out.println("Inputs");
		System.out.println("------");
		

		System.out.println("NTH=" + InputCommon.nth );
		System.out.println("Thickness");
		for (int i = 0; i < thData.size(); i++) {
			thData.get(i).printThPoint();
		}


	}

}
