package contain.bug.clonecode.extract;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.AnalyticsResult;
import bean.BugInformation;
import bean.LogInformation;
import bean.RevisePath;

public class BugMatchCommitInfo {
	
	private String bugsFileName ;
	private String commlogXmlFile;
	
	//private AnalyticsResult analyticsResult;
	// 开发人员的提交的修改信息
	private Map<String, LogInformation> commitLogInfoList ;	
	private Map<String, BugInformation> bugsInfoHashMap ;
	private Map<String, AnalyticsResult> mapMatchInfoList;
	
	public BugMatchCommitInfo(String bugsFileName, String commlogXmlFile) {
		
		this.bugsFileName   = bugsFileName;
		this.commlogXmlFile = commlogXmlFile;
		this.commitLogInfoList = new HashMap<String, LogInformation>();
		this.bugsInfoHashMap   = new HashMap<String, BugInformation>();
		this.mapMatchInfoList = new HashMap<String, AnalyticsResult>();
	}
	
	// 返回开发人员提交的日志信息map list
	public Map<String, LogInformation> getCommitLogInfoList() {
		
		DOMParserXml domParser = new DOMParserXml(commlogXmlFile);
		//domParser.readXmlFile();
		//Map<String, LogInformation> mapLogInfos= new HashMap<String, LogInformation>();
		this.commitLogInfoList = domParser.getMapLogInfos();;
		
		return commitLogInfoList;
	}

	public void setCommitLogInfoList(Map<String, LogInformation> commitLogInfoList) {
		this.commitLogInfoList = commitLogInfoList;
	}

	public Map<String, BugInformation> getMapBugsList() {		
		// <bugid,bug_Description>
		ParserBugInfo parseBugInfo = new ParserBugInfo();		 
		
		this.bugsInfoHashMap = parseBugInfo.getMapBugsList(this.bugsFileName);
		return this.bugsInfoHashMap;
	}

	public void setMapBugsList(Map<String, BugInformation> mapBugsList) {
		this.bugsInfoHashMap = mapBugsList;
	}
	
	public Map<String, AnalyticsResult> getMapMatchInfoList() {
		return getAnalyseResult();
	}
	public void setMapMatchInfoList(Map<String, AnalyticsResult> mapMatchInfoList) {
		this.mapMatchInfoList = mapMatchInfoList;
	}
	
	
	public Map<String, AnalyticsResult>getAnalyseResult() {
		
		Map<String, LogInformation>mapCommInfoList = getCommitLogInfoList(); 
		Map<String, BugInformation>bugsInfoHashMap = getMapBugsList();
		 
		for(String revise : mapCommInfoList.keySet()) {
			
			LogInformation logInformation = mapCommInfoList.get(revise);
			List<String> bugIdList = logInformation.getRevisedBugId();//获得bugs因为每次提交可能对应两个，或多个bug
			for(String bugId : bugIdList) {
				if (bugsInfoHashMap.containsKey(bugId)) {
					AnalyticsResult analyticsResult = new AnalyticsResult();
					analyticsResult.setBugInformation(bugsInfoHashMap.get(bugId));					
					analyticsResult.setLogInfomation(logInformation);
					//一个修复版本可能有多个分析结果，修复了多少个bug，则就有几个analyticsResult
					this.mapMatchInfoList.put(revise, analyticsResult);
				}
			}
			
		}
		return this.mapMatchInfoList;
	}	
	
	/**
	 * 通过bugsFileName和commlogXmlFile返回，bug和修复日志关联结果
	 * @param bugsFileName
	 * @param commlogXmlFile
	 * @return bug和修复日志关联结果
	 */
	public static Map<String, AnalyticsResult> getMatchResult(
			String bugsFileName, String commlogXmlFile) {	
		

		BugMatchCommitInfo bugMatchCommInfo  = new BugMatchCommitInfo(bugsFileName,commlogXmlFile);
		
		//bugs与logs关联的结果
		Map<String, AnalyticsResult> mapAnalyticsList = bugMatchCommInfo.getAnalyseResult();

//		for(String reviseId : mapAnalyticsList.keySet()) {
//			AnalyticsResult analyticRes = mapAnalyticsList.get(reviseId);
//			if(analyticRes!=null) {
//			bugInfo = mapAnalyticsList.get(reviseId).getBugInformation();
//			logInfo = mapAnalyticsList.get(reviseId).getLogInfomation();
//			}			
//		}
		return mapAnalyticsList;
	}
	public static void main(String[] args) { 
		
		String bugsFileName   ="e:/tmp/Tomcat7bugs_Info.txt" ;
		String commlogXmlFile = "e:/tmp/Tomcat7reviselog.xml";		
		
		Map<String, AnalyticsResult> analysisList = getMatchResult(bugsFileName, commlogXmlFile);
				
		String fileName = "e:/tmp/result.txt";
		File file = new File(fileName);
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			 fw = new FileWriter(file);
			 writer = new BufferedWriter(fw);
			 writer.write("vision    bugId       bugSummary						                                                    				logSummary\r\n\r\n");

			 for(Map.Entry<String, AnalyticsResult>resEntry : analysisList.entrySet())
			 {
				String revisionId = resEntry.getKey();
				AnalyticsResult analyReuslt = resEntry.getValue();
				String fileText = "";
				LogInformation logInfo = analyReuslt.getLogInfomation();
				BugInformation bugInfo = analyReuslt.getBugInformation();
				String bugId = bugInfo.getBugId();
				String bugSummary = bugInfo.getBugSummary();
				//List<RevisePath> logList = logInfo.getReviseList();
				String logSummary = logInfo.getCommitMsg();
//				fileText = String.format("%s   %s   %s   			%s", revisionId ,bugId, bugSummary , logSummary);
				fileText = String.format("%s   %s", revisionId ,bugId);
				System.out.println(fileText);
				writer.write(fileText);
				writer.newLine();//换行
				writer.flush();
			 } 
		}catch (FileNotFoundException e){  e.printStackTrace(); }
		catch (IOException e) 	{	  e.printStackTrace();	 }
		finally
		{
		  try {
		 	 writer.close();
		 	 fw.close();
		  } catch (IOException e) {	 e.printStackTrace();  }
		}
	}
	
}