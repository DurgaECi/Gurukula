package DriverPack;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author - Durga Suresh
 * Description - Objects and methods to access and retrive data from excel sheet
 *
 */
public class TestData {
	
	//Declare excel variables
	public XSSFSheet ExcelWSheet = null;
	public XSSFWorkbook ExcelWBook= null;
	public XSSFCell Cell= null;	
		
    //(@Author:DurgaPlaceOrder,fileName,sheetName;@Return:Excelsheet)This method is to set the File path and to open the Excel file
    public TestData(String filePath,String fileName,String sheetName)
    {
    	try
    	{
    		//Create a object of File class to open xlsx file
			File path = new File(filePath+"\\"+fileName);
			//Create an object of FileInputStream class to read excel file
			FileInputStream excelFile = new FileInputStream(path);
		    // Access the required test data sheet
			ExcelWBook = new XSSFWorkbook(excelFile);
	        ExcelWSheet= ExcelWBook.getSheet(sheetName);
    	 }catch (Exception e){
    		 e.printStackTrace();
		}
	}
    
    //(@Author:Durga;@Param:rowNum,colNum;@Return:cellData)This method is to read the test data from the Excel cell
    public String getCellData(int rowNum, int colNum) 
    {   String cellData=null;
    	try{
    		Cell = ExcelWSheet.getRow(rowNum).getCell(colNum);    	
    		cellData = Cell.getStringCellValue();        		  		
    		}catch (NullPointerException n){ 
    			//n.printStackTrace();
    			cellData = null;    	
    		}catch (Exception e){
    			e.printStackTrace();
           }
		return cellData;    	
    }
    
    //(@Author:Durga;@Param:colName;@Return:Col Index)To read the Column Index for a particular Column Name from testSheet
    public int getColIndex(String colName)
    {
 	   int colIndex = 0;
 	   try {
	 		int colCount = ExcelWSheet.getRow(0).getLastCellNum();
	 		for(colIndex=0;colIndex<colCount;colIndex++)
	 		   {
	 			   if  (getCellData(0,colIndex).equalsIgnoreCase(colName)){
	 				break;   	}  
	 		   } 
 		  }catch (Exception e) {
 			  e.printStackTrace();
 	   	  }return colIndex;
    }
    
    //(@Author:Durga;@Return:Row count)This method is to find the used rows / Row Count
  	public int getRowUsed() 
  	{	int rowCount = 0;
  		try{
  				rowCount =ExcelWSheet.getLastRowNum();    				 			
  		    }catch (Exception e){
  				 e.printStackTrace();
  			}return rowCount+1;
  	} 
  
  	//(@Author:Durga;@Param:rowName;@Return:rowIndex)To read the Row Index for a particular Row Name from testSheet
    public int getRowIndex(String rowName)
    {
 	   int rowIndex = 0;
 	   try {
	 		   int rowCount = getRowUsed();
	 		   for(rowIndex=0;rowIndex<rowCount;rowIndex++)
	 		   {
	 			   if  (getCellData(rowIndex,0).equalsIgnoreCase(rowName)){
	 				break;   	}
	 		   }
	 		   }catch (Exception e){
	 			  e.printStackTrace();
 	   	  }return rowIndex;	
    }
    
    
 	//(@Author:Durga;@Param:rowName,colName,sheetName;@Return:cellData)This is to retrive data from the excel
     public String getData(String rowName,String colName,String sheetName) 
     {	String cellVal = null;
     	try{
 	    		ExcelWSheet= ExcelWBook.getSheet(sheetName);
 	  			int colNum = getColIndex(colName);
 	  			int rowNum = getRowIndex(rowName);  			
 	  			cellVal = getCellData(rowNum,colNum);	
 	  			if (cellVal.isEmpty())
 	  			{
 	  				System.out.println("Data is empty for Test case - "+rowName+" coressponding to "+colName+" column in "+sheetName+" sheet");
 	  			}	  			
   	  		}catch(Exception e){
   	  			e.printStackTrace();
 	  		}return cellVal;
      }
	   
	//(@Author:Durga;@Param:rowName,colName;@Return:cellData)This is to retrive data from the Testdata excelsheet
    public String getTestData(String rowName,String colName)
    {	String data = null;
    	try{
	    		ExcelWSheet=ExcelWBook.getSheet("TestData");
	  			int colNum = getColIndex(colName);
	  			int rowNum = getRowIndex(rowName);
	  			data = getCellData(rowNum,colNum); 
	  			if (data.isEmpty())
	  			{
	  				System.out.println("Data is empty for Test case - "+rowName+" coressponding to "+colName+" column in Test Data sheet");
	  			}
	  			
	  		}catch(Exception e){
	  			//System.out.println("Please fill in data or format numeric data with 'in front for Test case - "+rowName+" coressponding to "+colName+" column in Test Data sheet");
	  			//e.printStackTrace();	  			
	  		}return data;    	
    }
    
    
  	//(@Author:Durga;@Param:ColName;@Return:Column values)This method is to read the column list (Ex.List of Testcases in TestCaseId column)
  	public Vector getColList(String colName)
  	{
  		Vector colLists = new Vector();
  		try {
	  			int colIndex = getColIndex(colName);
	  			int rowCount = getRowUsed();
	  			for ( int i=0 ; i<rowCount; i++)
	  			{
	  				String colData;
					try {
						colData = getCellData(i, colIndex);

		  				if(!colData.equals(null))
		  				{
		  					colLists.add(colData); 
		  				}		  				  	
					} catch (NullPointerException e) {
						continue;
						//e.printStackTrace();
					}
	  						
	  			}
	  	    }catch (Exception e) {
	  	    	 e.printStackTrace();
	  		}
		return (colLists);
    }
  	
  //(@Author:Durga;@Param:String to Be Fetched,colNum;@Return:RowIndexes)This method is to read the row indexes of a particular text(RunMode or TestcaseID) from the Excel cell
  	public Vector getRowIndexes(String toBeFetched, int colNum)
  	{
  		Vector rowIndexes = new Vector();
  		try {
	  			int rowCount = getRowUsed();
	  			for ( int rowIndex=0 ; rowIndex<rowCount; rowIndex++)
	  			{
	  				String excelData = getCellData(rowIndex,colNum);
	  				if  (excelData.equalsIgnoreCase(toBeFetched))	  					
	  				  rowIndexes.add(rowIndex);   		
	  			}
  	    	}catch(NullPointerException n){
  	    		//n.printStackTrace();
  	    	}catch (Exception e) {
  			 e.printStackTrace();
  	    	}
		return (rowIndexes);	
    }
  	
	
  	//(@Author:Durga;@Param:rowIndexes,ColName;@Return:Column values)This method is to return a particular column list
  	public Vector getResColList(Vector rowIndexes,String ColName)
  	{
  		Vector colList = new Vector();
  		Object[] colListV =rowIndexes.toArray();  		
		try 
		{
			int colIndex = getColIndex(ColName);
			int rowCount = colListV.length;
			for (int i=0 ; i<rowCount; i++)
			{	
				String excelData = getCellData((int) colListV[i],colIndex);
				colList.add(excelData);   		
			}
	    } catch (Exception e) {
	    	 e.printStackTrace();
	    }
		return (colList);
	}
	        
  //(@Author:Durga;@Param:sheetName)To switch between Sheets in Excel with Sheet name as parameter    
    public XSSFSheet setSheet(String sheetName)
    {
    	try{
    		//To set value in excel
    		ExcelWSheet= ExcelWBook.getSheet(sheetName);	    		
	       }catch(Exception e){
	    	   e.printStackTrace();
	        }
    	return ExcelWSheet;
    }
    
	//(@Author:Durga;@Param:currentTcRow;@Return:keywords)To retrive the list of keywords(functions) for a testcase from excel
  	public Vector getKeywords(int currentTcRow)
  	{
  		Vector keywords = new Vector();  		 
  		try{
	  			int colCount = ExcelWSheet.getRow(currentTcRow).getLastCellNum();
	  			for(int i=1;i<colCount;i++)
	  				{
	  				String keyword = getCellData(currentTcRow, i);	  						
		  			if (!keyword.isEmpty())
		  				keywords.add(keyword);
	  				}
  			}catch(Exception e){
  				 e.printStackTrace();  				 
  			}
  		return (keywords);	  			
  	 }  	
}
	    
	    
	    
	    
	    
	    
	    
		








