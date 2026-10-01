/*
	PersonTableController.java
	
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

/* Reference:
 * Some portions of the code was initially developed by DanNewton and enhanced
 * https://dzone.com/articles/editable-tables-in-javafx
 */

package tableapp;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.gems.table.CustomTableColumn;
import com.gems.table.DragSelectionCell;
import com.gems.table.DragSelectionComboBoxCell;
import com.gems.table.EditCell;
import com.gems.table.GenericTableController;
import com.gems.table.EnhancedFloatStringConverter;
import com.gems.table.EnhancedIntegerStringConverter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

public class PersonTableController extends GenericTableController implements Initializable {

	// Reference to the main application.
    private TableApp tableApp;
    private ResourceBundle bundle;

    @FXML public Button addRow;

    @FXML public Button delRow;

    
	@FXML
	protected TableView<PersonTableData> table;

	@FXML
	private TableColumn<PersonTableData, Integer> indexColumn;

	@FXML
	private TableColumn<PersonTableData, String> nameColumn;

	@FXML
	private TableColumn<PersonTableData, Integer> ageColumn;

	@FXML
	private TableColumn<PersonTableData, String> departmentColumn;

	@FXML
	private TableColumn<PersonTableData, Date> joinedColumn;

	@FXML
	private TableColumn<PersonTableData, Double> salaryColumn;

	static final ObservableList<String> DEPARTMENTS = FXCollections.observableArrayList(
			"Engineering", "Sales", "Marketing", "Finance", "HR");


	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
        bundle = rb;

		this.init();
	}
	
	public void init() {
		super.table = this.table;

		InputCommon iC = InputCommon.getInstance();
		
		
		table.setItems(iC.getPersonData());
		
		setupindexColumn();
		setupNameColumn();
		setupAgeColumn();
		setupDepartmentColumn();
		setupJoinedColumn();
		setupSalaryColumn();

		setTableEditable();

		
		super.init();
	}
	

	
	@Override
	public String getValue(int row,int col) {
		InputCommon iC = InputCommon.getInstance();
		return  iC.getPersonData().get(row).getCol(col);
	}
	
	@Override	
	public void setValue(int row,int col,String val) {

		InputCommon iC = InputCommon.getInstance();
		iC.setPersonData(row,col,val);
	}
	
	@Override
	public void clearValue(int row,int col) {
		InputCommon iC = InputCommon.getInstance();
		switch (col) {
		case 1:		// name
		case 3:		// department
		case 4:		// joined
			iC.setPersonData(row,col,"");
			break;
		default:
			iC.setPersonData(row,col,new String("0"));
		}
	}
	
		
	private void setupindexColumn() {
		
		indexColumn.setCellFactory(
				DragSelectionCell.<PersonTableData, Integer>forTableColumn(
						new EnhancedIntegerStringConverter()));
		indexColumn.setEditable(false);
		indexColumn.setSortable(false);
	}

	private void setupNameColumn() {
		nameColumn.setCellFactory(
				EditCell.<PersonTableData, String>forTableColumn(
						new StringConverter<String>() {
							@Override
							public String toString(String s) {
								return s;
							}

							@Override
							public String fromString(String s) {
								return s;
							}
						}											
						));
		// updates the thv field on the ThicknessyTableData object to the
		// committed value
		nameColumn.setOnEditCommit(event -> {
			// Validate the input before committing
			
			InputCommon iC = InputCommon.getInstance();
			int editRow = event.getTablePosition().getRow();
			if (editRow >= iC.getNp() || editRow <0 ) {
				System.out.println("Edit event isssue, row count > rowmax, editrow=" + editRow);
				return;
			}			
			
			final String value = (event.getNewValue() != null) 
					? event.getNewValue() : event.getOldValue();
					
			table.getItems().get(editRow).setName(value);
			refresh();
	
		});
	}

	private void setupAgeColumn() {			
		ageColumn.setCellFactory(
				EditCell.<PersonTableData, Integer>forTableColumn(
						new EnhancedIntegerStringConverter()));
		// updates the age field on the PersonTableData object to the
		// committed value
		ageColumn.setOnEditCommit(event -> {			
			// Validate the input before committing
			
			InputCommon iC = InputCommon.getInstance();
			int editRow = event.getTablePosition().getRow();
			if (editRow >= iC.getNp() || editRow <0 ) {
				System.out.println("Edit event isssue, row count > rowmax, editrow=" + editRow);
				return;
			}
			
			final Integer value = (event.getNewValue() != null) 
					? event.getNewValue() : event.getOldValue();

			table.getItems().get(editRow).setAge(value);

			refresh();

		});
	}

	private void setupDepartmentColumn() {
		departmentColumn.setCellFactory(
				DragSelectionComboBoxCell.<PersonTableData, String>forTableColumn(DEPARTMENTS));
		departmentColumn.setOnEditCommit(event -> {
			InputCommon iC = InputCommon.getInstance();
			int editRow = event.getTablePosition().getRow();
			if (editRow >= iC.getNp() || editRow <0 ) {
				System.out.println("Edit event isssue, row count > rowmax, editrow=" + editRow);
				return;
			}

			final String value = (event.getNewValue() != null)
					? event.getNewValue() : event.getOldValue();

			table.getItems().get(editRow).setDepartment(value);
			refresh();
		});
	}

	private void setupJoinedColumn() {
		joinedColumn.setCellFactory(
				EditCell.<PersonTableData, Date>forTableColumn(PersonTableData.DATE_CONVERTER));
		joinedColumn.setOnEditCommit(event -> {
			InputCommon iC = InputCommon.getInstance();
			int editRow = event.getTablePosition().getRow();
			if (editRow >= iC.getNp() || editRow <0 ) {
				System.out.println("Edit event isssue, row count > rowmax, editrow=" + editRow);
				return;
			}

			final Date value = (event.getNewValue() != null)
					? event.getNewValue() : event.getOldValue();

			table.getItems().get(editRow).setJoined(value);
			refresh();
		});
	}

	private void setupSalaryColumn() {
		salaryColumn.setCellFactory(
				EditCell.<PersonTableData, Double>forTableColumn(PersonTableData.SALARY_CONVERTER));
		salaryColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
		salaryColumn.setOnEditCommit(event -> {
			InputCommon iC = InputCommon.getInstance();
			int editRow = event.getTablePosition().getRow();
			if (editRow >= iC.getNp() || editRow <0 ) {
				System.out.println("Edit event isssue, row count > rowmax, editrow=" + editRow);
				return;
			}

			final Double value = (event.getNewValue() != null && !event.getNewValue().isNaN())
					? event.getNewValue() : event.getOldValue();

			table.getItems().get(editRow).setSalary(value);
			refresh();
		});
	}

	@FXML
	public void addRowBelow(ActionEvent event) { 
		InputCommon iC = InputCommon.getInstance();

		iC.setNp(iC.getNp() + 1);
		iC.addPerson(new Person(iC.getNp(),"",0));		
	}

	@FXML
	public void delLastRow(ActionEvent event) { 
		InputCommon iC = InputCommon.getInstance();
		if (iC.getNp() > 0) {
			iC.deletePerson(iC.getNp() -1 );
		}
	}
	
	@Override
	protected boolean isCellContentValid(int row, int col, String val) {
		EnhancedIntegerStringConverter mIC = new EnhancedIntegerStringConverter();
		switch (col) {
		
		case 0:
			// index column cannot be edited.  prevent pasting of data here
			return false;
		case 1:
			// any text for name is valid
			 return true;
		case 2:
			// Age should be an integer
			return mIC.isNumber(val)? true: false;
		case 3:
			// Department must be one of the combo box choices
			return DEPARTMENTS.contains(val.trim());
		case 4:
			// Joined must be a date in PersonTableData.DATE_PATTERN
			return PersonTableData.DATE_CONVERTER.fromString(val) != null;
		case 5:
			// Salary should be a number
			return PersonTableData.SALARY_CONVERTER.isNumber(val);

		default:
			// max columns is 5.  If trying to paste outside table,
			// return false
			return false;
		}

	}
	
	@Override
	protected  void pasteFromClipboard( ) {
		// abort if there's not cell selected to start with
		if( table.getSelectionModel().getSelectedCells().size() == 0) {
			return;
		}
		
		InputCommon iC = InputCommon.getInstance();					

		super.pasteFromClipboard();
	}
	
	@Override
	protected int insertRowAbove() { 
		InputCommon iC = InputCommon.getInstance();					

		int row = super.insertRowAbove();

		if (row < 0) 
			return row;
		
		iC.addPerson(row, new Person(row+1,"",0));
		
		return row;
	}

	@Override
	protected int deleteRow() { 
		InputCommon iC = InputCommon.getInstance();					

		int row = super.deleteRow();
		
		if (row < 0) 
			return row;
		
		iC.deletePerson(row);
		
		return row;
	}

	
	/**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param tableApp
     */
    public void setMainApp(TableApp tableApp) {
        this.tableApp = tableApp;
    }

}
