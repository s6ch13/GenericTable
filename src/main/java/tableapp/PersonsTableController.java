/*
	PersonsTableController.java
	
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

import java.net.URL;
import java.util.ResourceBundle;

import com.gems.table.CustomTableColumn;
import com.gems.table.DragSelectionCell;
import com.gems.table.EditCell;
import com.gems.table.GenericTableController;
import com.gems.table.MyFloatStringConverter;
import com.gems.table.MyIntegerStringConverter;

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

public class PersonsTableController extends GenericTableController implements Initializable{

	// Reference to the main application.
    private MainApp mainApp;
    private ResourceBundle bundle;

    @FXML public Button addRow;

    @FXML public Button delRow;

    
	@FXML
	protected TableView<PersonsTableData> table;

	@FXML
	private TableColumn<PersonsTableData, Integer> indexColumn;

	@FXML
	private TableColumn<PersonsTableData, String> nameColumn;

	@FXML
	private TableColumn<PersonsTableData, Integer> ageColumn;
	
	
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
		System.out.println("set value - persons controller");

		InputCommon iC = InputCommon.getInstance();
		iC.setPersonData(row,col,val);
	}
	
	@Override	
	public void clearValue(int row,int col) {
		InputCommon iC = InputCommon.getInstance();
		iC.setPersonData(row,col,new String("0"));
	}	
	
		
	private void setupindexColumn() {
		
		indexColumn.setCellFactory(
				DragSelectionCell.<PersonsTableData, Integer>forTableColumn(
						new MyIntegerStringConverter()));
		indexColumn.setEditable(false);
		indexColumn.setSortable(false);
	}

	private void setupNameColumn() {
		nameColumn.setCellFactory(
				EditCell.<PersonsTableData, String>forTableColumn(
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
				EditCell.<PersonsTableData, Integer>forTableColumn(
						new MyIntegerStringConverter()));
		// updates the xth field on the ThicknessTableData object to the
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
	
	@FXML
	public void addRowBelow(ActionEvent event) { 
		InputCommon iC = InputCommon.getInstance();

		iC.setNp(iC.getNp() + 1);
		iC.addPerson(new Persons(iC.getNp(),"",0));		
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
		MyIntegerStringConverter mIC = new MyIntegerStringConverter();
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
			
		default:
			// max columns is 2.  If trying to paste outside table, 
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
		System.out.println("insert row above - th table, rownum=" + row);

		if (row < 0) 
			return row;
		
		iC.addPerson(row, new Persons(row+1,"",0));
		
		return row;
	}

	@Override
	protected int deleteRow() { 
		InputCommon iC = InputCommon.getInstance();					

		System.out.println("delete row - th table");
		int row = super.deleteRow();
		
		if (row < 0) 
			return row;
		
		iC.deletePerson(row);
		
		return row;
	}

	
	/**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

}
