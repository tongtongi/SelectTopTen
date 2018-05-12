import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TopTenSelectedFeature {
    private static String FILE_NAME = "/home/tong/Downloads/Last_Data_Results.xlsx";
    public static void main (String[] args){

        Workbook workbook = null;
        try {
            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            workbook = new XSSFWorkbook(excelFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            String selectedList = currentRow.getCell(3) != null ?currentRow.getCell(3).getStringCellValue() : null;
            if(selectedList != null && !selectedList.equalsIgnoreCase("All")){
                String[] lines = selectedList.split("\\r?\\n");
                for(int i = 0; i < lines.length; i++){
                    String selectedAttr = lines[i];
                    selectedAttr = selectedAttr.replaceAll("\\s","");
                    //System.out.println(selectedAttr);

                    if(map.containsKey(selectedAttr)){
                        map.put(selectedAttr, map.get(selectedAttr)+1);
                    }else{
                        map.put(selectedAttr, 1);
                    }
                }


            }
        }
        //System.out.println(map.size());
        /*Stream<Map.Entry<String,Integer>> sorted =
                map.entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));*/
        Map<String,Integer> topTen =
                map.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(10)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for(Map.Entry<String, Integer> entry : topTen.entrySet()){
            System.out.println(entry.getKey()+"\t"+entry.getValue());
        }
    }
}
