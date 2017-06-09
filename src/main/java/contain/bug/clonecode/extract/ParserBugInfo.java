package contain.bug.clonecode.extract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bean.BugInformation;

public class ParserBugInfo {

	List<String> fileContentList;
	Map<String, BugInformation> mapBugsList;
	StringBuffer fileContentBuffer;
	
	public ParserBugInfo(){		
		
		fileContentList = new ArrayList<String>();
		
		// <bugId,BugInformation>
		mapBugsList = new HashMap<String, BugInformation>() ;
		fileContentBuffer = new StringBuffer("");
	}
	
	
	public Map<String, BugInformation> getMapBugsList(String bugsFileName) {
		// <bugid,bug_Description>
		readFileByLines(bugsFileName);//读文件并将bugs信息保存到map中
		return this.mapBugsList;
		/*
		 * 打印测试信息用的,一般测试使用的 for (String keyBugid : mapBugsList.keySet()) {
		 * String strKeyValue = mapBugsList.get(keyBugid);
		 * System.out.println(keyBugid + ":" + strKeyValue); }
		 */
	}
	
//ID	Product		    Comp			Assignee	Status	Resolution				Summary					
	public void setMapBugsList(Map<String, BugInformation> mapBugsList) {
		this.mapBugsList = mapBugsList;
	}

	public String getWholeFileContent(String bugsFileName) {

		StringBuffer fileContent = readFileByLines(bugsFileName);
		this.fileContentBuffer = fileContent;

		return fileContentBuffer.toString();
	}
	public void setWholeFileContent(StringBuffer wholeFileContent) {
		this.fileContentBuffer = wholeFileContent;
	}
	
	public BugInformation getBugInfo(String bugInfoString) {
		
		BugInformation bugInformation = new BugInformation();
		String resultString[] = bugInfoString.split("\t+");
		int nlenth = resultString.length;
		
		if(resultString.length==8){
			bugInformation.setBugId(resultString[nlenth-8].replace("\r\n", "")); 
			bugInformation.setBugProduct(resultString[nlenth-7].replace("\r\n", "")); 
			bugInformation.setBugComp(resultString[nlenth-6].replace("\r\n", "")); 
			bugInformation.setBugAssign(resultString[nlenth-5].replace("\r\n", "")); 
			bugInformation.setBugResolution(resultString[nlenth-4].replace("\r\n", "")); 
			bugInformation.setBugStatus(resultString[nlenth-3].replace("\r\n", "")); 
			bugInformation.setBugSummary(resultString[nlenth-2].replace("\r\n", "")); 
			bugInformation.setBugChangeDate(resultString[nlenth - 1].replace("\r|\n", "")); 
		}
		else
		{
			System.out.println("存在格式不正确的bug");
			System.out.println(bugInfoString);
		}
		//mapBugsList.put(subString[0].replace("\r\n", ""), );
		return bugInformation;
	}

	public StringBuffer readFileByLines(String fileName) {
		
		String tempString = null;
		BufferedReader reader = null;
		
		File file = new File(fileName);
		try {
			reader = new BufferedReader(new FileReader(file));			
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				
				tempString = tempString.replaceAll("\r|\n", "");
				BugInformation bugInfo = getBugInfo(tempString);
				mapBugsList.put(bugInfo.getBugId(), bugInfo);
				//保存文件内容
				fileContentBuffer.append(tempString);			
			}
			reader.close();
		} catch (IOException e) {e.printStackTrace();} 
		finally 
		{	if (reader != null) {
				try {reader.close();} 
				catch (IOException e1) {}
			}
		}
		
		return fileContentBuffer;
	}
	
	public static void main(String[] args) {

		String fileName = "e:/tmp/Tomcat7bugs_Info.txt";
		ParserBugInfo rFile = new ParserBugInfo();

		// <bugid,bug_Description>
		Map<String, BugInformation> mapBugsList = rFile.getMapBugsList(fileName);
		System.out.println("bugs总数:" + mapBugsList.size());
		//StringBuffer FileContent = rFile.readFileByLines(fileName);

		File file = new File("E:/dic_general.txt");
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			 fw = new FileWriter(file);
			 writer = new BufferedWriter(fw);
				for (String keyBugid : mapBugsList.keySet()) {
			 
			//System.out.println(keyBugid + ":  " + mapBugsList.get(keyBugid).getBugSummary());
			writer.write(keyBugid + ":  " + mapBugsList.get(keyBugid).getBugSummary());
			 writer.newLine();//换行
			 writer.flush();
			 } 
		}catch (FileNotFoundException e) { e.printStackTrace();	 }
		 catch (IOException e) {  e.printStackTrace(); }
		finally
		{
		  try {	writer.close();fw.close(); } 
		  catch (IOException e) { e.printStackTrace();  } 
		}			//BugInformation bugInformation = mapBugsList.get(keyBugid);
	}
}