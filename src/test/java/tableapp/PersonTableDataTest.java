package tableapp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PersonTableDataTest {

	    private static PersonTableData mary() {
	        return new PersonTableData(new Person(1, "Mary", 23, "Engineering",
	        		PersonTableData.DATE_CONVERTER.fromString("15-03-2021"), 72.50));
	    }

	    @Test
	    public void testGetCol_formatsEveryColumn() {
	        PersonTableData p = mary();
	        Assertions.assertEquals("1", p.getCol(0));
	        Assertions.assertEquals("Mary", p.getCol(1));
	        Assertions.assertEquals("23", p.getCol(2));
	        Assertions.assertEquals("Engineering", p.getCol(3));
	        Assertions.assertEquals("15-03-2021", p.getCol(4));
	        Assertions.assertEquals("72.50", p.getCol(5));
	    }

	    @Test
	    public void testGetColUpdateCol_roundTripsEveryColumn() {
	        PersonTableData source = mary();
	        PersonTableData target = new PersonTableData(9, "", 0);
	        for (int col = 1; col <= 5; col++) {
	        	target.updateCol(col, source.getCol(col));
	        	Assertions.assertEquals(source.getCol(col), target.getCol(col), "column " + col);
	        }
	    }

	    @Test
	    public void testUpdateCol_ageWholeNumberDecimal_setsAge() {
	        PersonTableData p = mary();
	        assertDoesNotThrow(() -> p.updateCol(2, "30.0"));
	        Assertions.assertEquals(Integer.valueOf(30), p.getAge());
	    }

	    @Test
	    public void testUpdateCol_invalidSalary_keepsOldValue() {
	        PersonTableData p = mary();
	        p.updateCol(5, "abc");
	        Assertions.assertEquals(72.50, p.getSalary(), 0.0);
	    }

	    @Test
	    public void testUpdateCol_departmentTrimsExcelCarriageReturn() {
	        PersonTableData p = mary();
	        p.updateCol(3, "Sales\r");
	        Assertions.assertEquals("Sales", p.getDepartment());
	    }

	    @Test
	    public void testUpdateCol_clearValues() {
	        // values written by PersonTableController.clearValue
	        PersonTableData p = mary();
	        p.updateCol(1, "");
	        p.updateCol(2, "0");
	        p.updateCol(3, "");
	        p.updateCol(4, "");
	        p.updateCol(5, "0");
	        Assertions.assertEquals("", p.getName());
	        Assertions.assertEquals(Integer.valueOf(0), p.getAge());
	        Assertions.assertEquals("", p.getDepartment());
	        Assertions.assertNull(p.getJoined());
	        Assertions.assertEquals("", p.getCol(4));
	        Assertions.assertEquals(0.0, p.getSalary(), 0.0);
	    }

	}
