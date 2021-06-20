# JavaFX Generic Table Library

The goal of this project is to bring some of the basic excel like capabilities to javafx tables.  This work is an offshoot of the development work at https://www.gemsoftware.org  Geotechnical Engineering Modelling Software (GEMS)

## Supports : 
1) Editing of cells in tableview
2) drag and select a rectangle with mouse
3) cut copy, paste of cells using keyboard (Ctrl-X, Ctrl-C, Ctrl-V)
4) context menu for cut, copy, paste, delete, insertrow, delete row.  Right click on table shows context menu
5) menubar with cut, copy, paste & delete functionality
6) Validation of float, integer, double values

## Structure : 
'''

src/main/java ->com.gems.table - contains the generic table library

              ->tableapp       - example app that uses the generic library 
                                 to create an editable table
                                 
              ->View           - fxml and css files 
              
Settings.gradle

build.gradle

'''

## Building

gradle.build

This will create a jar file called tableapp.jar

## Running

java -jar tableApp.jar

## Reference:
This work is based on a few of the works below and extends them to form a common library.

1) Tableview documentation from oracle 
https://docs.oracle.com/javafx/2/ui_controls/table-view.htm

2) example of setting an editable tableview
https://dzone.com/articles/editable-tables-in-javafx
https://github.com/lankydan/JavaFX-Table-Tutorial/tree/master/JavaFXTableTutorial

3) Copy and paste on tabeview by Roland
https://gist.github.com/Roland09/6fb31781a64d9cb62179

4) Custom events by Tony Sintes
https://www.javaworld.com/article/2077333/core-java/mr-happy-object-teaches-custom-events.html

## License
This work is licensed under FREEBSD license. (2 Clause BSD license)
