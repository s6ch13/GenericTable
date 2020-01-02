/*
	ThicknessTableController.java
	
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

public class ThicknessTableController extends GenericTableController implements Initializable{

	// Reference to the main application.
    private MainApp mainApp;
    private ResourceBundle bundle;

    @FXML public Button addRow;

    @FXML public Button delRow;

    
	@FXML
	protected TableView<ThicknessTableData> table;

	@FXML
	private TableColumn<ThicknessTableData, Integer> indexColumn;

	@FXML
	private TableColumn<ThicknessTableData, Float> thvColumn;

	@FXML
	private TableColumn<ThicknessTableData, Float> xthColumn;
	
	@FXML Label errorLabel;
	
	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
        bundle = rb;

		this.init();
	}
	
	public void init() {
		super.table = this.table;

		InputCommon iC = InputCommon.getInstance();
		
 		//initNthSpinner();
		
		table.setItems(iC.getThData());
		
		setupindexColumn();
		setupthvColumn();
		setupxthColumn();

		setTableEditable();

		
		super.init();
	}
	

	
	@Override
	public String getValue(int row,int col) {
		InputCommon iC = InputCommon.getInstance();
		return  iC.getThData().get(row).getCol(col);
	}
	
	@Override	
	public void setValue(int row,int col,String val) {
		System.out.println("set value - breadth table controller");

		InputCommon iC = InputCommon.getInstance();
		iC.setThData(row,col,val);
	}
	
	@Override	
	public void clearValue(int row,int col) {
		InputCommon iC = InputCommon.getInstance();
		iC.setThData(row,col,new String("0"));
	}	
	
		
	private void setupindexColumn() {
		
		indexColumn.setCellFactory(
				DragSelectionCell.<ThicknessTableData, Integer>forTableColumn(
						new MyIntegerStringConverter()));
		indexColumn.setEditable(false);
		indexColumn.setSortable(false);
	}

	private void setupthvColumn() {
		thvColumn.setCellFactory(
				EditCell.<ThicknessTableData, Float>forTableColumn(
						new MyFloatStringConverter("%.4f")));
		// updates the thv field on the ThicknessyTableData object to the
		// committed value
		thvColumn.setOnEditCommit(event -> {
			// Validate the input before committing
			
			InputCommon iC = InputCommon.getInstance();
			int editRow = event.getTablePosition().getRow();
			if (editRow >= iC.getNth() || editRow <0 ) {
				System.out.println("Edit event isssue, row count > rowmax, editrow=" + editRow);
				return;
			}			
			
			final float value = (event.getNewValue() != null) 
					? event.getNewValue() : event.getOldValue();
					
			table.getItems().get(editRow).setThv(value);
			refresh();
	
		});
	}

	private void setupxthColumn() {			
		xthColumn.setCellFactory(
				EditCell.<ThicknessTableData, Float>forTableColumn(
						new MyFloatStringConverter("%.3f")));
		// updates the xth field on the ThicknessTableData object to the
		// committed value
		xthColumn.setOnEditCommit(event -> {			
			// Validate the input before committing
			
			InputCommon iC = InputCommon.getInstance();
			int editRow = event.getTablePosition().getRow();
			if (editRow >= iC.getNth() || editRow <0 ) {
				System.out.println("Edit event isssue, row count > rowmax, editrow=" + editRow);
				return;
			}
			
			final float value = (event.getNewValue() != null) 
					? event.getNewValue() : event.getOldValue();

			table.getItems().get(editRow).setXth(value);
			
			refresh();

		});
	}
	
	@FXML
	public void addRowBelow(ActionEvent event) { 
		InputCommon iC = InputCommon.getInstance();

		iC.setNth(iC.getNth() + 1);
		iC.addTh(new Thickness(iC.getNth(),0.0,0.0));		
	}

	@FXML
	public void delLastRow(ActionEvent event) { 
		InputCommon iC = InputCommon.getInstance();
		if (iC.getNth() > 0) {
			iC.deleteTh(iC.getNth() -1 );
		}
	}
	

	/*
	 * public void initNthSpinner() { // Listen for Spinner nbrText changes
	 * InputCommon iC = InputCommon.getInstance(); MyIntegerStringConverter miC =
	 * new MyIntegerStringConverter(); MyFloatStringConverter mfC = new
	 * MyFloatStringConverter();
	 * 
	 * nthSpinner.focusedProperty().addListener((observable,oldV, newV) -> {
	 * 
	 * if (!newV) { final int value = (int)nthSpinner.getValue(); if(value !=
	 * iC.getNth()) { while (value > iC.getNth()) { iC.setNth(iC.getNth() + 1);
	 * iC.addTh(new Thickness(iC.getNth(),0.0,0.0)); } while ( value < iC.getNth())
	 * { iC.deleteTh(iC.getNth() -1 ); }
	 * 
	 * iC.setNth(value);
	 * 
	 * 
	 * } else { nthSpinner.getValueFactory().setValue(iC.getNth()); } } }); }
	 */
	

	
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
		
		iC.addTh(row, new Thickness(row+1,0,0));
		
		return row;
	}

	@Override
	protected int deleteRow() { 
		InputCommon iC = InputCommon.getInstance();					

		System.out.println("delete row - th table");
		int row = super.deleteRow();
		
		if (row < 0) 
			return row;
		
		iC.deleteTh(row);
		
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
