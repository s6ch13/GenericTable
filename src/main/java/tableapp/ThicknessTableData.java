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


public class ThicknessTableData {

	private SimpleIntegerProperty index;
	private SimpleFloatProperty thv;
	private SimpleFloatProperty xth;


	public ThicknessTableData(Thickness thp) {
		this.index = new SimpleIntegerProperty(thp.getIndex());
		this.thv = new SimpleFloatProperty(thp.getthv());
		this.xth = new SimpleFloatProperty(thp.getxth());
	}

	public ThicknessTableData(final Integer index, final float thv,
			final float xth) {
		this.index = new SimpleIntegerProperty(index);
		this.thv = new SimpleFloatProperty(thv);
		this.xth = new SimpleFloatProperty(xth);
	}

	public Integer getIndex() {
		return index.get();
	}

	public void setIndex(final Integer index) {
		this.index.set(index);
	}

	public float getThv() {
		return thv.get();
	}

	public void setThv(final float thv) {
		this.thv.set(thv);
	}
	public float getXth() {
		return xth.get();
	}

	public void setXth(final float xth) {
		this.xth.set(xth);
	}	
	
	public String getCol(int col) {
		String ret = new String();

		switch (col) {
		case 0:
			ret = Integer.toString(index.get());
			break;
		case 1:
			ret = Float.toString(thv.get());
			break;
		case 2:
			ret = Float.toString(xth.get());
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
			setThv(Float.valueOf(val));
			break;
		case 2:
			setXth(Float.valueOf(val));
			break;
		}
	}
	
	public void printThPoint() {
		System.out.println(getIndex()+"\t"+getThv()+"\t"+getXth());
		
	}
	

}
