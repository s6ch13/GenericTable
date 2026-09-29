/*
	DragSelectionComboBoxCell.java
	Copyright (C) 2026  Sriram C.

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

package com.gems.table;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

// A ComboBoxTableCell that also supports mouse-drag range selection, the same
// way DragSelectionCell does for TextFieldTableCell-based columns. This reads
// and writes DragSelectionCell's per-table drag state directly (package-private,
// same package) so a drag gesture can flow seamlessly between this cell type
// and DragSelectionCell-backed columns in the same row.
public class DragSelectionComboBoxCell<S, T> extends ComboBoxTableCell<S, T> {

	public DragSelectionComboBoxCell(final ObservableList<T> items) {
		super(items);
		dragEvent();
	}

	public DragSelectionComboBoxCell(final StringConverter<T> converter,
			final ObservableList<T> items) {
		super(converter, items);
		dragEvent();
	}

	public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(
			final ObservableList<T> items) {
		return list -> new DragSelectionComboBoxCell<S, T>(items);
	}

	public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(
			final StringConverter<T> converter, final ObservableList<T> items) {
		return list -> new DragSelectionComboBoxCell<S, T>(converter, items);
	}

	public void selectRectangle() {

		TableView<S> tableView = getTableColumn().getTableView();
		DragSelectionCell.DragState state = DragSelectionCell.dragStateFor(tableView);
		ObservableList<TableColumn<S, ?>> list = tableView.getColumns();
		int endCol = list.indexOf(getTableColumn());
		int endRow = getIndex();

		int startRow = state.startRow;
		int startCol = state.startCol;

		// Clear old rectangle
		// FIXME - only the rectangle selection should be cleared and not the previous
		// selection
		tableView.getSelectionModel().clearSelection();

		tableView.getSelectionModel().setCellSelectionEnabled(true);
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		int minRow = Math.min(startRow, endRow);
		int maxRow = Math.max(startRow, endRow);
		int minCol = Math.min(startCol, endCol);
		int maxCol = Math.max(startCol, endCol);

		tableView.getSelectionModel().selectRange(minRow, list.get(minCol), maxRow, list.get(maxCol));
	}

	public void dragEvent() {
		setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				startFullDrag();

				TableView<S> tableView = getTableColumn().getTableView();
				DragSelectionCell.DragState state = DragSelectionCell.dragStateFor(tableView);
				tableView.getSelectionModel().select(getIndex(), getTableColumn());

				ObservableList<TableColumn<S, ?>> columnList = tableView.getColumns();
				state.startCol = columnList.indexOf(getTableColumn());
				state.startRow = getIndex();
			}
		});

		setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {

			@Override
			public void handle(MouseDragEvent event) {
				selectRectangle();
			}

		});

		setOnMouseDragExited(new EventHandler<MouseDragEvent>() {

			@Override
			public void handle(MouseDragEvent event) {
			}

		});
	}

}
