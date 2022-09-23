# MyFirst-Ugly-Java-Selenium-Framework

Steps to use set up Framework:

  1.Install Eclipse and create a project with Name similar to Folder name of Framework.
  
  2.Place the Framework folder in the workspace of the Eclipse
  
  3.Download the required JAR files and build them to the project created.


Steps to Run Regression Test Cases:

  1.In DriverPack --> TestConfig.java class,
  
  --fill in the test sheet name as "TestSheet" ;
  
  --fill in the path for the Log files in reportPathGen() function and screenshots in imagePathGen() function;
  
  --Other details as environment name and DB credentials if required.
  
  2.In DriverPack --> TestSheet.xlsx , 
  
  Go to "TestRunner" sheet, 
  
  --fill in RunMode with Yes for those test cases which needs to be executed;
  
  Based on the Pre-Requisite for the test case,
  
  --fill in the data required in "TestData" sheet.
  
  
  3.Open DriverPack --> TestExecute.java class and click on RUN button.
  

Results will be saved in the Location mentioned in above steps.
