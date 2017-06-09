package contain.bug.clonecode.extract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.logbug.extract.TfidfBugMatchLog;

import bean.AnalyticsResult;
import bean.BugInformation;
import bean.Clone;
import bean.CloneClass;
import bean.CloneClassList;
import bean.FileNameManage;
import bean.LogInformation;
import bean.RevisePath;

public class ReviseBugCloneCode {
	
	
	private String bugCloneCodePath;	//可能含有bug的克隆代码文件路径
	private AnalyticsResult analyticsResult;
	private CloneClass cloneClass;
	List<CloneClass> lsBugCloneClass; 		//以克隆片段的方式返回
	Map<String, CloneClass> mapBugCloneClass;//以克隆群的方式返回
	private String cloneClassXml;
	private String bugsFileName ;
	private String commlogXmlFile;
	
	public ReviseBugCloneCode() {
		
	}
	public ReviseBugCloneCode(String bugsFileName,String commlogXmlFile,String cloneClassXml){
		this.bugsFileName = bugsFileName;
		this.commlogXmlFile = commlogXmlFile;
		this.cloneClassXml = cloneClassXml;
		this.lsBugCloneClass = new ArrayList<CloneClass>(); 
	}

	public String getBugCloneCodePath() {
		return bugCloneCodePath;
	}

	public void setBugCloneCodePath(String bugCloneCodePath) {
		this.bugCloneCodePath = bugCloneCodePath;
	}

	public AnalyticsResult getAnalyticsResult() {
		return analyticsResult;
	}

	public void setAnalyticsResult(AnalyticsResult analyticsResult) {
		this.analyticsResult = analyticsResult;
	}

	public CloneClass getCloneClass() {
		return cloneClass;
	}

	public List<CloneClass> getLsBugCloneClass() {
		return getBugCloneClassList();//返回lsBugCloneClass含bugs的Clone对象列表
	}

	public void setLsBugCloneClass(List<CloneClass> lsBugCloneClass) {
		this.lsBugCloneClass = lsBugCloneClass;
	}
	public void setCloneClass(CloneClass cloneClass) {
		this.cloneClass = cloneClass;
	}

	public String getCloneClassXml() {
		return cloneClassXml;
	}

	public void setCloneClassXml(String cloneClassXml) {
		this.cloneClassXml = cloneClassXml;
	}
	public String getBugsFileName() {
		return bugsFileName;
	}
	public void setBugsFileName(String bugsFileName) {
		this.bugsFileName = bugsFileName;
	}

	public String getCommlogXmlFile() {
		return commlogXmlFile;
	}

	public void setCommlogXmlFile(String commlogXmlFile) {
		this.commlogXmlFile = commlogXmlFile;
	}



	public List<CloneClass> getBugCloneClassList() {
		
		//获取bugs与提交日志信息的结果
		Map<String, AnalyticsResult> mapAnalysis = null;
		mapAnalysis = BugMatchCommitInfo.getMatchResult(this.bugsFileName, this.commlogXmlFile);

		//得到克隆文件的路径
		List<CloneClass> lsCloneClass = CloneClassList.getListCloneClass(this.cloneClassXml);
		System.out.println("检测工具检测到的克隆类(群)数量:" +lsCloneClass.size());
		
		//遍历含有bugs的代码,查找含有bugs的克隆代码
		 for(Map.Entry<String, AnalyticsResult>resEntry : mapAnalysis.entrySet())
		 {
				AnalyticsResult analyReuslt = resEntry.getValue();

				LogInformation logInfo = analyReuslt.getLogInfomation();

				//获取文件修改路径,通过路径信息判断是否含有bugs的克隆代码
				List<RevisePath> logList = logInfo.getReviseList();
				for(RevisePath revisePath : logList) {
					
					int nlength = FileNameManage.getCommitPath().length();
					String fullPath = revisePath.getContent();
					if (fullPath.length()<nlength)
							continue;//修改路径信息的长度小于前缀信息则认为无效路径
					
					//删除前缀信息
					String subPath = fullPath.substring(nlength);
					
					boolean isBugCodeClass = false;//判断此克隆类是否是含有bug的克隆片段代码
					for (int i=0; i < lsCloneClass.size(); i++) {//克隆类的遍历
						
						CloneClass cloneClass = (CloneClass)lsCloneClass.get(i);
						List<Clone> listClone = cloneClass.getCloneList();
						
						for (Clone clone : listClone) {//克隆片段的遍历
							  
						  //System.out.println(clone.getSourcePath());
						  String codePath = clone.getSourcePath();
						  //删除文件的前缀信息
						  String subCodePath = codePath.substring(FileNameManage.getCloneDetectPath().length());
						  if (subPath.equals(subCodePath) && (analyReuslt!=null)){									  
							  clone.setAnalysisResult(analyReuslt);	//克隆代码对象中，若analyReuslt为null，
							  isBugCodeClass = true;				//说此克隆片段不是含bugs的克隆代码，而且说明同一个克隆群中的
							  										//克隆片段没有一致改变
							  }
						  }	//克隆片段遍历结束//list中存放的是一个克隆片段与AnalyticsResult对应
						
						if (isBugCodeClass)
							this.lsBugCloneClass.add(cloneClass);//这样是一克隆片段的方式返回
						//mapBugCloneClass
						isBugCodeClass = false;
					}//克隆类遍历结束
					//System.out.println(revisePath.getContent());
				}
		 } 

	
		 System.out.println("含有bug的克隆类(群)为："+lsBugCloneClass.size());
		 return this.lsBugCloneClass;
	}

	public static List<CloneClass> getBugCloneClassList(String bugsFileName,String commlogXmlFile,String cloneClassXml){
	
		ReviseBugCloneCode reviseBugsCloneCode = new ReviseBugCloneCode(bugsFileName,commlogXmlFile,cloneClassXml);
	
		return reviseBugsCloneCode.getLsBugCloneClass();
	}
	
	public static List<CloneClass> getBugCloneClassByTfIdf(String bugsFileName,String commlogXmlFile,String cloneClassXml){
		
		ReviseBugCloneCode reviseBugsCloneCode = new ReviseBugCloneCode(bugsFileName,commlogXmlFile,cloneClassXml);
	
		return reviseBugsCloneCode.getBugCloneClassByTfIdf();
	}
	public List<CloneClass> getBugCloneClassByTfIdf() {
//		String bugFileName = "e:/tmp/lianxi.txt";
//		String logFileName = "E:/tmp/lianxi.xml";		
		
		/**
		 * key:revision value:Analytics Object
		 */
		Map<String, AnalyticsResult>tfIdfAnalyMap = TfidfBugMatchLog.getLogBugs(this.commlogXmlFile, this.bugsFileName);
	
		//得到克隆文件的路径
		List<CloneClass> lsCloneClass = CloneClassList.getListCloneClass(this.cloneClassXml);
		System.out.println("检测工具检测到的克隆类(群)数量:" +lsCloneClass.size());
		
		//遍历含有bugs的代码,查找含有bugs的克隆代码
		 for(Map.Entry<String, AnalyticsResult>resEntry : tfIdfAnalyMap.entrySet())
		 {
				AnalyticsResult analyReuslt = resEntry.getValue();

				LogInformation logInfo = analyReuslt.getLogInfomation();

				//获取文件修改路径,通过路径信息判断是否含有bugs的克隆代码
				List<RevisePath> logList = logInfo.getReviseList();
				for(RevisePath revisePath : logList) {
					
					int nlength = FileNameManage.getCommitPath().length();
					String fullPath = revisePath.getContent();
					if (fullPath.length()<nlength)
							continue;//修改路径信息的长度小于前缀信息则认为无效路径
					
					//删除前缀信息
					String subPath = fullPath.substring(nlength);
					
					boolean isBugCodeClass = false;//判断此克隆类是否是含有bug的克隆片段代码
					for (int i=0; i < lsCloneClass.size(); i++) {//克隆类的遍历
						
						CloneClass cloneClass = (CloneClass)lsCloneClass.get(i);
						List<Clone> listClone = cloneClass.getCloneList();
						
						for (Clone clone : listClone) {//克隆片段的遍历
							  
						  //System.out.println(clone.getSourcePath());
						  String codePath = clone.getSourcePath();
						  //删除文件的前缀信息
						  String subCodePath = codePath.substring(FileNameManage.getCloneDetectPath().length());
						  if (subPath.equals(subCodePath) && (analyReuslt!=null)){									  
							  clone.setAnalysisResult(analyReuslt);	//克隆代码对象中，若analyReuslt为null，
							  isBugCodeClass = true;				//说此克隆片段不是含bugs的克隆代码，而且说明同一个克隆群中的
							  										//克隆片段没有一致改变
							  }
						  }	//克隆片段遍历结束//list中存放的是一个克隆片段与AnalyticsResult对应
						
						if (isBugCodeClass)
							this.lsBugCloneClass.add(cloneClass);//这样是一克隆片段的方式返回
						//mapBugCloneClass
						isBugCodeClass = false;
					}//克隆类遍历结束
					//System.out.println(revisePath.getContent());
				}
		 } 

	
		 System.out.println("含有bug的克隆类(群)为："+lsBugCloneClass.size());
		 return this.lsBugCloneClass;
	}
	
	
//	public static List<CloneClass> getBugCloneClassByBugId(String bugFileName, String logFileName) {
////		String bugFileName = "e:/tmp/lianxi.txt";
////		String logFileName = "E:/tmp/lianxi.xml";		
//		
//		/**
//		 * key:revision value:Analytics Object
//		 */
//		Map<String, AnalyticsResult>TfIdfAnalyMap = TfidfBugMatchLog.getLogBugs(logFileName, bugFileName);
//	}
	
	
	public static void main(String[]args) {
		
		String bugsFileName   = FileNameManage.getBugsFileName();
		String commlogXmlFile = FileNameManage.getCommlogXmlFile();
		String cloneClassXml  = FileNameManage.getCloneClassXml();
		
		List<CloneClass> bugCloneList = getBugCloneClassList(bugsFileName, commlogXmlFile, cloneClassXml);
		
		int nCountClone = 0;
		//XmlImpl 
		for(CloneClass cloneClass : bugCloneList) {
			List<Clone> listClone = cloneClass.getCloneList();
			for(Clone cloneCode : listClone) {
				if(cloneCode.getAnalysisResult()!=null)
				{
					nCountClone++;
				}		
			}
			
		}
		System.out.println("含bugs的克隆代代码片段为:" + nCountClone);
	}
}
