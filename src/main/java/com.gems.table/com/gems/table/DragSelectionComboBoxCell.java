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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

// A ComboBoxTableCell that also supports mouse-drag range selection, the same
// way DragSelectionCell does for TextFieldTableCell-based columns. This reads
// and writes DragSelectionCell.startRow/startCol directly (package-private,
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

		ObservableList<TableColumn<S, ?>> list = getTableColumn().getTableView().getColumns();
		int endCol = list.indexOf(getTableColumn());
		int endRow = getIndex();

		int startRow = DragSelectionCell.startRow;
		int startCol = DragSelectionCell.startCol;

		// Clear old rectangle
		// FIXME - only the rectangle selection should be cleared and not the previous
		// selection
		getTableColumn().getTableView().getSelectionModel().clearSelection();

		getTableColumn().getTableView().getSelectionModel().setCellSelectionEnabled(true);
		getTableColumn().getTableView().getSelectionModel()
				.setSelectionMode(SelectionMode.MULTIPLE);

		// Select Rectangle
		for (int i = (startRow <= endRow) ? startRow
				: endRow; i <= ((startRow < endRow) ? endRow : startRow); i++) {
			for (int j = (startCol <= endCol) ? startCol
					: endCol; j <= ((startCol < endCol) ? endCol : startCol); j++) {

				getTableColumn().getTableView().getSelectionModel().select(i, list.get(j));

			}
		}
	}

	public void dragEvent() {
		setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				startFullDrag();
				getTableColumn().getTableView().getSelectionModel().select(getIndex(),
						getTableColumn());

				ObservableList<TableColumn<S, ?>> columnList = getTableColumn().getTableView()
						.getColumns();
				DragSelectionCell.startCol = columnList.indexOf(getTableColumn());
				DragSelectionCell.startRow = getIndex();
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
