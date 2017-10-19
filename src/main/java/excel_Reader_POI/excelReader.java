package excel_Reader_POI;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class excelReader {

    private String Table = null;
    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private List<List<String>> DataTableList;



    public excelReader(String MoonExcelName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(MoonExcelName).getFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        workbook = new XSSFWorkbook(fileInputStream);
    }


    public List<List<String>> getTable(String TableName, Boolean Transpose) {

        this.Table = TableName;
        sheet = workbook.getSheet(TableName);
        List<String> FromColumnList = new ArrayList<String>();


        if (Transpose == false) {
            DataTableList = new ArrayList<List<String>>();
            Iterator<Row> rowIterator = sheet.rowIterator();
            XSSFRow metaDataRow = (XSSFRow) rowIterator.next();
            Iterator<Cell> metaDataCellIterator = metaDataRow.cellIterator();

            while (metaDataCellIterator.hasNext()) {
                Cell metaDataCell = metaDataCellIterator.next();
                FromColumnList.add(metaDataCell.getStringCellValue());
            }

            DataTableList.add(FromColumnList);

            while (rowIterator.hasNext()) {
                XSSFRow dataRow = (XSSFRow) rowIterator.next();
                Iterator<Cell> dataCellIterator = dataRow.cellIterator();
                List<String> dataRowList = new ArrayList<String>();
                Iterator FromColumnListIterator = FromColumnList.iterator();
                while (FromColumnListIterator.hasNext()) {
                    FromColumnListIterator.next();
                    Cell dataCell = dataCellIterator.next();
                    dataRowList.add(dataCell.getStringCellValue());
                }
                DataTableList.add(dataRowList);
            }
        }

        if (Transpose == true) {
            DataTableList = new ArrayList<List<String>>();
            Iterator<Row> metaDataRowIterator = sheet.rowIterator();
            while (metaDataRowIterator.hasNext()) {
                Cell metaDataCell = metaDataRowIterator.next().cellIterator().next();
                FromColumnList.add(metaDataCell.getStringCellValue());
            }

            DataTableList.add(FromColumnList);

            Integer i=1;
            Boolean cellHasNext=true;
            while (cellHasNext) {
                Iterator FromColumnListIterator = FromColumnList.iterator();
                Iterator<Row> dataRowIterator = sheet.rowIterator();
                List<String> dataRowList = new ArrayList<String>();

                while (FromColumnListIterator.hasNext()) {
                    XSSFRow dataRow = (XSSFRow) dataRowIterator.next();
                    Iterator<Cell> dataCellIterator = dataRow.cellIterator();
                    Integer j = 1;
                    dataCellIterator.next();
                    while (j < i) {
                        dataCellIterator.next();
                        j++;
                    }
                    if (dataCellIterator.hasNext()) {
                        FromColumnListIterator.next();
                        Cell dataCell = dataCellIterator.next();
                        dataRowList.add(dataCell.getStringCellValue());
                    } else {
                        cellHasNext=false;
                        break;
                    }
                }
                i++;
                if (cellHasNext) {
                    DataTableList.add(dataRowList);
                }
            }
        }
        return DataTableList;
    }
}
