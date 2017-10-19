package legacyCodeBase;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class excelReaderMapTable {

    private String Table = null;
    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private List<String> FromColumnList;
    private List<Map<String,String>> DataTableList;



    public excelReaderMapTable(String MoonExcelName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(MoonExcelName).getFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        workbook = new XSSFWorkbook(fileInputStream);
    }


    public List<Map<String,String>> getTable(String TableName, Boolean Transpose) {

        this.Table = TableName;
        sheet = workbook.getSheet(TableName);
        FromColumnList = new ArrayList<String>();


        if (Transpose == false) {
            Iterator<Row> rowIterator = sheet.rowIterator();
            XSSFRow metaDataRow = (XSSFRow) rowIterator.next();
            Iterator<Cell> metaDataCellIterator = metaDataRow.cellIterator();

            while (metaDataCellIterator.hasNext()) {
                Cell metaDataCell = metaDataCellIterator.next();
                FromColumnList.add(metaDataCell.getStringCellValue());
            }

            DataTableList = new ArrayList<Map<String, String>>();
            while (rowIterator.hasNext()) {
                XSSFRow dataRow = (XSSFRow) rowIterator.next();
                Iterator<Cell> dataCellIterator = dataRow.cellIterator();
                Map dataRowMap = new LinkedHashMap();
                Iterator FromColumnListIterator = FromColumnList.iterator();
                while (FromColumnListIterator.hasNext()) {
                    Cell dataCell = dataCellIterator.next();
                    dataRowMap.put(FromColumnListIterator.next(), dataCell.getStringCellValue());
                }
                DataTableList.add(dataRowMap);
            }
        }

        if (Transpose == true) {
            Iterator<Row> metaDataRowIterator = sheet.rowIterator();
            while (metaDataRowIterator.hasNext()) {
                Cell metaDataCell = metaDataRowIterator.next().cellIterator().next();
                FromColumnList.add(metaDataCell.getStringCellValue());
            }

            DataTableList = new ArrayList<Map<String, String>>();

            Integer i=1;
            Boolean cellHasNext=true;
            while (cellHasNext) {
                Iterator FromColumnListIterator = FromColumnList.iterator();
                Iterator<Row> dataRowIterator = sheet.rowIterator();
                Map dataRowMap = new LinkedHashMap();

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
                        Cell dataCell = dataCellIterator.next();
                        dataRowMap.put(FromColumnListIterator.next(), dataCell.getStringCellValue());
                    } else {
                        cellHasNext=false;
                        break;
                    }
                }
                i++;
                if (cellHasNext) {
                    DataTableList.add(dataRowMap);
                }
            }
        }
        return DataTableList;
    }
}
