import excel_Reader_POI.excelReader;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertTrue;

public class excelReaderTest {


//******Horizontal****************************************************************************************************
    //Page ObjectName ObjectPropetyType	ObjectPropetyValue	TestCase1	TestCase2

    @Test
    public void HorizontalExcel() throws IOException {
        List<List<String>> Result;
        excelReader excelReaderObj = new excelReader("Moon.xlsx");
        //eSQL Query with Select Set
        Result = excelReaderObj.getTable("MoonSheetHorizontal", false);
        System.out.println(Result);
    }

    @Test
    public void VerticalExcel() throws IOException {
        List<List<String>> Result;
        excelReader excelReaderObj = new excelReader("Moon.xlsx");
        //eSQL Query with Select Set
        Result = excelReaderObj.getTable("MoonSheetVertical", true);
        System.out.println(Result);
    }
}
