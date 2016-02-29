package testSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;



public class ReadWriteExcelProperties { 

	// Create a HashMap which will store keys and values 
	// from xls file provided
	HashMap< String, String > properties = new HashMap< String, String >();


	public void readExcelFile(String fileName)     { 

		HSSFCell cell1 =null;
		HSSFCell cell2 =null;

		try{                      

			// Create a FileInputStream by passing the location of excel
			FileInputStream input = new FileInputStream(fileName);         

			// Create a POIFSFileSystem by passing it FileInputStream 
			//POIFSFileSystem fileSystem = new POIFSFileSystem(input);         

			// Create a Workbook using HSSFWorkbook object
			HSSFWorkbook workBook = new HSSFWorkbook(input);

			// get the sheet at location 0 by calling 
			// getSheetAt() method of the Workbook
			HSSFSheet sheet = workBook.getSheetAt(0);   

			// Create a Iterator object by calling 
			// sheet's method as rowIterator() which 
			// will return back us with Iterator, which will 
			// loop through each row.
			Iterator rowIterator = sheet.rowIterator();   

			// Iterating row by row
			while(rowIterator.hasNext()){       

				// Creating a reference to row by calling 
				// next method of the iterator
				HSSFRow row = (HSSFRow) rowIterator.next(); 

				// Creating a iterator which will contain each
				// cell info related to that particular row
				Iterator cellIterator = row.cellIterator(); 

				// Iterating over each cell
				while(cellIterator.hasNext()){    

					// Creating a cell by calling next method of the iterator 
					cell1 = (HSSFCell) cellIterator.next(); 

					System.out.println("Cell One ... " + cell1.getRichStringCellValue());

					// extracting key from the xls
					String key = cell1.getRichStringCellValue().toString();

					// if cellIterator.hasNext() returns false than perform the
					// below task
					if(!cellIterator.hasNext()){

						System.out.println("No Such Element");

						String value = "";

						// put default key and key to it
						properties.put(key, value);  

					} 
					else {

						// if not than create yet another cell from the
						// cell iterator by calling its next method
						cell2 = (HSSFCell) cellIterator.next();

						System.out.println("Cell Two ... " + cell2.getRichStringCellValue());

						// extracting values from the cell2 
						String value = cell2.getRichStringCellValue().toString();

						// storing each properties into the HashMap
						properties.put(key, value);        
					}

					System.out.println("The properties are " + properties);                              

				}          
			}     
		}

		catch (Exception e){

			System.out.println("No Such Element Exception Occured ..... ");

			e.printStackTrace();

		}                
	}


	public void writeToPropertiesFile(String propertiesPath) {

		// Creating a new Properties object
		Properties props = new Properties();

		// Creating a File object which will point to location of 
		// properties file
		File propertiesFile = new File(propertiesPath);

		try {

			// create a FileOutputStream by passing above properties file 
			FileOutputStream xlsFos = new FileOutputStream(propertiesFile);

			// Taking hashMaps keys by first converting it to Set and than
			// taking iterator over it.
			Iterator mapIterator = properties.keySet().iterator();

			// looping over iterator properties
			while(mapIterator.hasNext()) {

				// extracting keys and values based on the keys
				String key = mapIterator.next().toString();

				String value = properties.get(key);

				// setting each properties file in props Object 
				// created above
				props.setProperty(key, value);

			}

			// Finally storing the properties to real 
			// properties file.
			props.store(xlsFos, null);

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}
}
