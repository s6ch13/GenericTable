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

import java.util.Date;

import com.gems.table.EnhancedDateStringConverter;
import com.gems.table.EnhancedDoubleStringConverter;
import com.gems.table.EnhancedIntegerStringConverter;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;


public class PersonTableData {

	// shared by the table cells and copy/paste so copied values paste back unchanged
	public static final String DATE_PATTERN = "dd-MM-yyyy";
	public static final String SALARY_FORMAT = "%.2f";

	static final EnhancedIntegerStringConverter AGE_CONVERTER = new EnhancedIntegerStringConverter();
	static final EnhancedDateStringConverter DATE_CONVERTER = new EnhancedDateStringConverter(DATE_PATTERN);
	static final EnhancedDoubleStringConverter SALARY_CONVERTER = new EnhancedDoubleStringConverter(SALARY_FORMAT);

	private SimpleIntegerProperty index;
	private SimpleStringProperty name;
	private SimpleIntegerProperty age;
	private SimpleStringProperty department;
	private SimpleObjectProperty<Date> joined;
	private SimpleDoubleProperty salary;


	public PersonTableData(Person pp) {
		this(pp.getIndex(), pp.getName(), pp.getAge(),
				pp.getDepartment(), pp.getJoined(), pp.getSalary());
	}

	public PersonTableData(final Integer index, final String name,
			final Integer age) {
		this(index, name, age, "", null, 0.0);
	}

	public PersonTableData(final Integer index, final String name,
			final Integer age, final String department, final Date joined,
			final Double salary) {
		this.index = new SimpleIntegerProperty(index);
		this.name = new SimpleStringProperty(name);
		this.age = new SimpleIntegerProperty(age);
		this.department = new SimpleStringProperty(department);
		this.joined = new SimpleObjectProperty<Date>(joined);
		this.salary = new SimpleDoubleProperty(salary);
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

	public String getDepartment() {
		return department.get();
	}

	public void setDepartment(final String department) {
		this.department.set(department);
	}

	public Date getJoined() {
		return joined.get();
	}

	public void setJoined(final Date joined) {
		this.joined.set(joined);
	}

	public Double getSalary() {
		return salary.get();
	}

	public void setSalary(final Double salary) {
		this.salary.set(salary);
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
		case 3:
			ret = department.get();
			break;
		case 4:
			ret = DATE_CONVERTER.toString(joined.get());
			break;
		case 5:
			ret = SALARY_CONVERTER.toString(salary.get());
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
			// not Integer.valueOf: "30.0" is a valid age
			Integer ageVal = AGE_CONVERTER.fromString(val);
			if (ageVal != null)
				setAge(ageVal);
			break;
		case 3:
			setDepartment(val.trim());	// drop the \r Excel leaves on a row's last cell
			break;
		case 4:
			setJoined(DATE_CONVERTER.fromString(val));
			break;
		case 5:
			Double salaryVal = SALARY_CONVERTER.fromString(val);
			if (!salaryVal.isNaN())
				setSalary(salaryVal);
			break;
		}
	}

	public void printPersonPoint() {
		System.out.println(getIndex()+"\t"+getName()+"\t"+getAge()
				+"\t"+getCol(3)+"\t"+getCol(4)+"\t"+getCol(5));

	}
	

}
