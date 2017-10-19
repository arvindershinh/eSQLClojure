/*

package legacyCodeBase;

import legacyCodeBase.excelReaderMapTable;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertTrue;

public class excelReaderMapTableTest {


//******Horizontal****************************************************************************************************
    //Page ObjectName ObjectPropetyType	ObjectPropetyValue	TestCase1	TestCase2

    @Test
    public void HorizontalExcelMapTable() throws IOException {
        List<Map<String, String>> Result;
        excelReaderMapTable excelReaderObj = new excelReaderMapTable("Moon.xlsx");
        //eSQL Query with Select Set
        Result = excelReaderObj.getTable("MoonSheetHorizontal", false);
        System.out.println(Result);
    }

    @Test
    public void VerticalExcelMapTable() throws IOException {
        List<Map<String, String>> Result;
        excelReaderMapTable excelReaderObj = new excelReaderMapTable("Moon.xlsx");
        //eSQL Query with Select Set
        Result = excelReaderObj.getTable("MoonSheetVertical", true);
        System.out.println(Result);
    }
}
*/