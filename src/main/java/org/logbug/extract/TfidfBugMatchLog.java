package org.logbug.extract;

import java.util.ArrayList;
import java.util.TreeMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import bean.AnalyticsResult;
import bean.BugInformation;
import bean.LogInformation;

public class TfidfBugMatchLog {
	
	
public static void main(String [] args) {
		
		String bugFileName = "e:/tmp/lianxi.txt";
		String logFileName = "E:/tmp/lianxi.xml";		
		
		/**
		 * key:revision value:Analytics Object
		 */
		Map<String, AnalyticsResult>TfIdfAnalyMap = getLogBugs(logFileName, bugFileName);
		for(String revision : TfIdfAnalyMap.keySet()) {
      		AnalyticsResult analyResult = TfIdfAnalyMap.get(revision);
      	}
      	System.out.println("余弦相似度计算完成!");
      	
//		Map<String, Map<String, Double>> bugsTfIdfMap  = BugTfidfAlgorithm.getBugTfidfMap(bugFileName);
//		Map<String, Map<String, Double>> logsTfIdfMap = LogTfidfAlgorithm.getLogTfidfMap(logFileName);
//  	
//		Map<String, BugInformation> mapBugsList = BugTfidfAlgorithm.getMapBugsList();
//		Map<String, LogInformation> mapLogsList = LogTfidfAlgorithm.getMapLogInfos();
//
//		Set<String> revisions=logsTfIdfMap.keySet();
      	
//      	for(String revision : revisions){
//      		
//      		Map<String, Double> logTfIdf=logsTfIdfMap.get(revision);
//      		List<Double> logBugTfIdfList = getLogBugSimList(logTfIdf, bugsTfIdfMap);
//      		int listSize = logBugTfIdfList.size();
//      		
//    		AnalyticsResult analyResult = new AnalyticsResult(); 
//    		
//    		double bugid = logBugTfIdfList.get(listSize-2);
//    		
//    		analyResult.setCommBugId(String.format("%d", (int)bugid));
//    		analyResult.setLogBugTfIdfList(logBugTfIdfList);
//    		analyResult.setBugInformation(mapBugsList.get(bugid));
//    		analyResult.setLogInfomation(mapLogsList.get(revision));
//    		TfIdfAnalyMap.put(revision, analyResult);
//      	}
	}
	
	
	public static Map<String, AnalyticsResult> getLogBugs(String logFileName, String bugFileName) {
		
		/**
		 * key:revision value:Analytics Object
		*/
		Map<String, AnalyticsResult>TfIdfAnalyMap = new TreeMap();
		
		Map<String, Map<String, Double>> bugsTfIdfMap  = BugTfidfAlgorithm.getBugTfidfMap(bugFileName);
		Map<String, Map<String, Double>> logsTfIdfMap  = LogTfidfAlgorithm.getLogTfidfMap(logFileName);
  	
		Map<String, BugInformation> mapBugsList = BugTfidfAlgorithm.getMapBugsList();
		Map<String, LogInformation> mapLogsList = LogTfidfAlgorithm.getMapLogInfos();

		Set<String> revisions=logsTfIdfMap.keySet();
      	
      	for(String revision : revisions){
      		
      		Map<String, Double> logTfIdf=logsTfIdfMap.get(revision);
      		String maxMatchBugid = getLogBugSimList(logTfIdf, bugsTfIdfMap);
      		
      		
    		AnalyticsResult analyResult = new AnalyticsResult();  
    		analyResult.setCommBugId(maxMatchBugid);
  		
    		BugInformation bugInformation = mapBugsList.get(maxMatchBugid);
    		analyResult.setBugInformation(bugInformation);
    		LogInformation logInformation = mapLogsList.get(revision);
    		analyResult.setLogInfomation(logInformation);
    		TfIdfAnalyMap.put(revision, analyResult);
    		System.out.println(maxMatchBugid+"-------------->" + revision);
    		
      	}
      	System.out.println("余弦相似度计算完成,Log与Bug匹配成功!");
      	return TfIdfAnalyMap;
	}
	
	public static String  getLogBugSimList(Map<String, Double> logTfIdfMap, Map<String, Map<String, Double>>bugsTfIdfMap) {
		
		/**
		 * log1->bug1,bug2,bug3,bug4,bug5
		 * log2->bug1,bug2,bug3,bug4,bug5,bug6,bug7
		 * log3->bug1,bug2,bug3,........
		 * <revision,AnalyticsResult>
		 */
		
		List<Double> allCos = new ArrayList<Double>();
		
		double cosValue = 0.000 ;
		double maxCosValue = cosValue;
		String maxValueBugId = "";
				
		Set<String> bugs=bugsTfIdfMap.keySet();
      	for(String bugid : bugs){  
      		maxValueBugId = bugid;
      		Map<String, Double> BugTfIdf=bugsTfIdfMap.get(bugid);
    			
    		cosValue = SimilarDegreeByCos.getSimilaryDegree(logTfIdfMap, BugTfIdf);
    		if (maxCosValue < cosValue ) {
    			maxCosValue = cosValue;
    			maxValueBugId = bugid;
    		}
    		allCos.add(cosValue);
      	}
        //确保最大的余弦相似度放在倒数第二存放bugid
      	allCos.add(Double.parseDouble(maxValueBugId));
      	//确保最大的余弦相似度放在最后
      	allCos.add(maxCosValue);   
	//返回相似度
	return maxValueBugId;	
	}
}
