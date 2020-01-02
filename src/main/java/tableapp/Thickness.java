/*
	Thickness.java
	
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

public class Thickness {
	
	private Integer index;
	private float thv;
	private float xth;

	public Thickness(Integer index, float thv, float xth) {
		super();
		this.index = index;
		this.thv = thv;
		this.xth = xth;
	}

	public Thickness(Integer index, double thv, double xth) { 
		super();
		this.index = index;
		this.thv = (float)thv;
		this.xth = (float)xth;
	}
	
	
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public float getthv() {
		return thv;
	}

	public void setbrv(float thv) {
		this.thv = thv;
	}

	public float getxth() {
		return xth;
	}

	public void setxth(float xth) {
		this.xth = xth;
	}

}
