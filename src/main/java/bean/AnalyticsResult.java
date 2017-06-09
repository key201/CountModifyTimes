package bean;

import java.util.Map;
import java.util.List;


public class AnalyticsResult {
	
	/**bug信息与修复日志关联后，bugInformation和logInformation就不再为空，此时Clone和CloneClass仍然为空**/
	BugInformation  bugInformation; /** 含bugs的克隆代码片段的bug信息 **/
	LogInformation  logInfomation;  /** 含bugs的克隆代码片段的log信息 **/
	Clone 	        cloneCode;		/** 含bugs的克隆代码片段 **/
	CloneClass 		cloneClass;     /** 含bugs的克隆代码片段所在的克隆了群**/	
		
	
	String commBugId;
	Map<String, AnalyticsResult> mapAnalyResult;
	List<Double> logBugTfIdfList;	/** 通过TfIdf方式进行bug和log关联，每个log对应的bug **/
	
	
	
	
	public CloneClass getCloneClass() {
		return cloneClass;
	}
	public void setCloneClass(CloneClass cloneClass) {
		this.cloneClass = cloneClass;
	}
	public List<Double> getLogBugTfIdfList() {
		return logBugTfIdfList;
	}
	public void setLogBugTfIdfList(List<Double> logBugTfIdfList) {
		this.logBugTfIdfList = logBugTfIdfList;
	}
	public Clone getCloneCode() {
		return cloneCode;
	}
	public void setCloneCode(Clone cloneCode) {
		this.cloneCode = cloneCode;
	}
	public BugInformation getBugInformation() {
		return bugInformation;
	}
	public void setBugInformation(BugInformation bugInformation) {
		this.bugInformation = bugInformation;
	}
	public LogInformation getLogInfomation() {
		return logInfomation;
	}
	public void setLogInfomation(LogInformation logInfomation) {
		this.logInfomation = logInfomation;
	}
	public String getCommBugId() {
		return commBugId;
	}
	public void setCommBugId(String commBugId) {
		this.commBugId = commBugId;
	}
	public Map<String, AnalyticsResult> getMapAnalyResult() {
		return mapAnalyResult;
	}
	public void setMapAnalyResult(Map<String, AnalyticsResult> mapAnalyResult) {
		this.mapAnalyResult = mapAnalyResult;
	}	
}
