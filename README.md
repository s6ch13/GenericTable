JavaFX Generic Table Library

The goal of this project is to bring some of the basic excel like capabilities to javafx tables.  

Supports : 
a) Editing of cells in tableview
b) drag and select a rectangle with mouse
c) cut copy, paste of cells
d) context menu for cut, copy, paste, delete, insertrow, delete row
e) menubar with cut, copy, paste & delete functionality
f) Validation of float, integer, double values

Structure : 
src/main/java ->com.gems.table - contains the generic table library
              ->tableapp       - example app that uses the generic library 
                                 to create an editable table
              ->View           - fxml and css files 



Reference:
This work is based on a few of the works below and extends them to form a common library.

a) Tableview documentation from oracle 
https://docs.oracle.com/javafx/2/ui_controls/table-view.htm

b) example of setting an editable tableview
https://dzone.com/articles/editable-tables-in-javafx
https://github.com/lankydan/JavaFX-Table-Tutorial/tree/master/JavaFXTableTutorial

c) Copy and paste on tabeview by Roland
https://gist.github.com/Roland09/6fb31781a64d9cb62179

d) Custom events by Tony Sintes
https://www.javaworld.com/article/2077333/core-java/mr-happy-object-teaches-custom-events.html
