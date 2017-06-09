package contain.bug.clonecode.extract;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import org.logbug.extract.TfidfBugMatchLog;

import bean.AnalyticsResult;
import bean.Clone;
import bean.CloneClass;
import bean.CloneClassList;
import bean.FileNameManage;
import bean.LogInformation;
import bean.RevisePath;

/***每个修订版本中，修改的文件是多个，暂且认为就是修改了一个bug
 *修订版本号与bugid是一一对应的，但是可能对应多个文件路径
 *克隆代码：
 *一个克隆群中可能有多个克隆片段，这几个克隆片段可能都在同一个文件中
 *bugid在那个修订版本中修复的
 * 
 * 
 * **/



public class ReviseBugCloneCodeList {
	
	private String cloneClassXml;
	private String bugsFileName ;
	private String commlogXmlFile;
	
	
	private List<ReviseBugCloneCode> lsReviseBugsCloneCode;
	
	Map<String, Clone> mapRevision2Clone;			/** <修改版本号---克隆片段>  */
	Map<String, CloneClass> mapRevision2CloneClass;	/** <修改版本号---克隆群>   */
	Map<String, AnalyticsResult> analysisMap = null;/** <修改版本号---分析结果>  */
	Map<String, AnalyticsResult> contianBugCloneMap = null; /**<修改版本号---分析结果>*/
	
	
	public ReviseBugCloneCodeList(String bugsFileName ,String commlogXmlFile,
			String cloneClassXml) {
		
		this.bugsFileName   = bugsFileName;
		this.commlogXmlFile = commlogXmlFile;
		this.cloneClassXml  = cloneClassXml;
		
		lsReviseBugsCloneCode = new ArrayList<ReviseBugCloneCode>();
		/** 让含有bug的克隆代码，分别以版本粒度返回 */
		mapRevision2Clone  = new TreeMap<String, Clone>();
		mapRevision2CloneClass = new TreeMap<String, CloneClass>();		
	}
	
	/***
	 * 获取log与bug关联的map列表
	 * @param matchType:通过什么方式获得(TfIdf匹配方式和bugid方式)
	 * @return 返回bug和log关联的结果即mapList
	 */
	public Map<String, AnalyticsResult> getAnalysisMapList(String matchType) {
		
		if (matchType.equals("TfIdf")) {
			analysisMap = TfidfBugMatchLog.getLogBugs(this.commlogXmlFile, this.bugsFileName);
		} else if (matchType.equals("bugid")) {
			analysisMap = BugMatchCommitInfo.getMatchResult(this.bugsFileName, this.commlogXmlFile);
		}
		return analysisMap;
	}	
	
	public Map<String, AnalyticsResult> getNoneClones() {
		for(Map.Entry<String, AnalyticsResult>resEntry : analysisMap.entrySet())
		{}
		return contianBugCloneMap;
	}
	
	/***通过代码路径匹配含bugs的克隆代码文件
	 * @param  filePath:文件的修改路径
	 * @return 返回AnalyticsResult的对象
	 * 
	*/
	public AnalyticsResult FindBugCloneByPath(String codePath) {
		
		AnalyticsResult  tmpAnalyReuslt = null;
		AnalyticsResult  analyReuslt    = null;/** 作为函数的返回值 */		
		for(Map.Entry<String, AnalyticsResult>resEntry : analysisMap.entrySet())
		{
			tmpAnalyReuslt = resEntry.getValue();
			//tmpAnalyReuslt.setFileType(fileType);
			LogInformation logInfo = tmpAnalyReuslt.getLogInfomation();
			
			/** 获取文件修改路径 */
			List<RevisePath> logList = logInfo.getReviseList();
			for(RevisePath revisePath : logList) {
				
				String fullPath = revisePath.getContent();
				if (fullPath.length()<FileNameManage.getCommitPath().length())
						continue;	/** 修改路径信息的长度小于前缀信息则认为无效路径 */				
				
				/** 删除前缀信息 */
				String subPath = fullPath.substring(FileNameManage.getCommitPath().length());
					
				/** 删除文件的前缀信息 */
			    String subCodePath = codePath.substring(FileNameManage.getCloneDetectPath().length());
			    if (subPath.equals(subCodePath)){									  
			    	analyReuslt = tmpAnalyReuslt;
					}
				}/** 克隆片段遍历结束//list中存放的是一个克隆片段与AnalyticsResult对应 */
						  
			}/**  克隆类遍历结束 */
		return analyReuslt;
	}	
	/** 
	 * 获取clone code文件与bugs对应关系，删除	List<Map<String, AnalyticsResult>>
	 * 里面的路径与克隆代码中的文件路径不同的，一片段形式返回
	 * 剩下的就是clone code文件、bugs、修改日志信息三者关联的内容了
	 * @param bug与log日志关联的结果map表
	 */	
	public List<ReviseBugCloneCode>  getBugCloneCodeList() {
		
		AnalyticsResult analyReuslt = null;
		List<AnalyticsResult> lsAnalyticsResult = new ArrayList<AnalyticsResult>();
		
		/** 获取bugs与提交日志信息的结果,保存到成员变量mapAnalysis */
		analysisMap = BugMatchCommitInfo.getMatchResult(this.bugsFileName, this.commlogXmlFile);
		
		/** 得到克隆文件的路径 */
		List<CloneClass> lsCloneClass = CloneClassList.getListCloneClass(this.cloneClassXml);
		for (int i=0; i < lsCloneClass.size(); i++) {/** 克隆类的遍历 */
			
		CloneClass cloneClass = (CloneClass)lsCloneClass.get(i);
		List<Clone> listClone = cloneClass.getCloneList();
			
		for (Clone clone : listClone) {/** 克隆片段的遍历 */
			  
			  String codePath = clone.getSourcePath();
			  AnalyticsResult analyResult = FindBugCloneByPath(codePath);
			  if (analyResult!=null)
				  lsAnalyticsResult.add(analyReuslt);
		  }	/** 克隆片段遍历结束//list中存放的是一个克隆片段与AnalyticsResult对应 */				  
		} /** 克隆类遍历结束 */
		return this.lsReviseBugsCloneCode;
	}
	/***
	 * key:克隆群id value:AnalyticsResult
	 * @return 通过词频向量返回的
	 */
	public Map<String, List<AnalyticsResult>>  getBugCloneMapByTfIdf() {
		
		Map<String, List<AnalyticsResult>> analyticsClassMap = new TreeMap<String, List<AnalyticsResult>>();
		/** 获取bugs与提交日志信息的结果,保存到成员变量mapAnalysis */
		analysisMap = getAnalysisMapList("TfIdf");
		
		/** 得到克隆文件的路径 */
		List<CloneClass> lsCloneClass = CloneClassList.getListCloneClass(this.cloneClassXml);
		System.out.println("检测结果中的克隆群:" + lsCloneClass.size());
		for (int i=0; i < lsCloneClass.size(); i++) {/** 克隆类的遍历 */
			
		CloneClass cloneClass = (CloneClass)lsCloneClass.get(i);
		List<Clone> listClone = cloneClass.getCloneList();		
		List<AnalyticsResult> lsAnalyticsResult = new ArrayList<AnalyticsResult>();
		for (Clone cloneCode : listClone) {/** 克隆片段的遍历 */			  
			  String codePath = cloneCode.getSourcePath();
			  AnalyticsResult analyResult = FindBugCloneByPath(codePath);

			  if (analyResult!=null) {
				  lsAnalyticsResult.add(analyResult);
				  analyResult.setCloneCode(cloneCode);
			  }
		  }	/** 克隆片段遍历结束//list中存放的是一个克隆片段与AnalyticsResult对应 */
		String cloneClassId;
		cloneClassId = String.format("%d", cloneClass.getId());
		analyticsClassMap.put(cloneClassId, lsAnalyticsResult);
		} /** 克隆类遍历结束 */
		return analyticsClassMap;
	}
	
	/** 
	 * 获取clone code文件与bugs对应关系，删除	List<Map<String, AnalyticsResult>>
	 * 里面的路径与克隆代码中的文件路径不同的,每一个克隆群中，含bugs克隆片段的文件
	 * 剩下的就是clone code文件、bugs、修改日志信息三者关联的内容了
	 * 提取的Map<String, List<AnalyticsResult>>maplist的内容出现错误，还没有找到原因
	 * analyticsClassMap中的克隆代码信息出现错误
	 * key：value-->cloneclassid:List<AnalyticsResult>
	 */	
	public Map<String, List<AnalyticsResult>>  getBugCloneListByBugId() {
		
		Map<String, List<AnalyticsResult>> analyticsClassMap = new TreeMap<String, List<AnalyticsResult>>();
		/** 获取bugs与提交日志信息的结果,保存到成员变量mapAnalysis */
		analysisMap = getAnalysisMapList("bugid");
		
		/** 得到克隆文件的路径 */
		List<CloneClass> lsCloneClass = CloneClassList.getListCloneClass(this.cloneClassXml);
		System.out.println("检测结果中的克隆群:" + lsCloneClass.size());
		
		for (int i=0; i < lsCloneClass.size(); i++) {/** 克隆类的遍历 */
			
			CloneClass cloneClass = (CloneClass)lsCloneClass.get(i);
			List<Clone> listClone = cloneClass.getCloneList();		
			List<AnalyticsResult> lsAnalyticsResult = new ArrayList<AnalyticsResult>();
			for (Clone cloneCode : listClone) {/** 克隆片段的遍历 */

				  String codePath = cloneCode.getSourcePath();
				  AnalyticsResult analyResult = FindBugCloneByPath(codePath);
	
				  if (analyResult!=null) {
					  analyResult.setCloneCode(cloneCode);
					  lsAnalyticsResult.add(analyResult);				  						
				  }
			  }	/** 克隆片段遍历结束//list中存放的是一个克隆片段与AnalyticsResult对应 */
			String cloneClassId;
			cloneClassId = String.format("%d", cloneClass.getId());
			analyticsClassMap.put(cloneClassId, lsAnalyticsResult);
		} /** 克隆类遍历结束 */
		return analyticsClassMap;
	}
	
	/** 
	 * 通过TfIdf方式获取含bugs的克隆代码
	 * 里面的路径与克隆代码中的文件路径不同的
	 * 剩下的就是clone code文件、bugs、修改日志信息三者关联的内容了
	 * List里面存放的AnalyticsResult
	 */	
	public List<AnalyticsResult>  getBugCloneListByTfIdf() {
		List<AnalyticsResult> lsAnalyticsResult = new ArrayList<AnalyticsResult>();
		Map<String, List<AnalyticsResult>> analyticsClassMap = new TreeMap<String, List<AnalyticsResult>>();
		/** 获取bugs与提交日志信息的结果,保存到成员变量mapAnalysis */
		analysisMap = getAnalysisMapList("TfIdf");
		
		/** 得到克隆文件的路径 */
		List<CloneClass> lsCloneClass = CloneClassList.getListCloneClass(this.cloneClassXml);
		System.out.println("检测结果中的克隆群" + lsCloneClass.size());
		for (int i=0; i < lsCloneClass.size(); i++) {/** 克隆类的遍历 */
			
		CloneClass cloneClass = (CloneClass)lsCloneClass.get(i);
		List<Clone> listClone = cloneClass.getCloneList();		
		
		for (Clone clone : listClone) {/** 克隆片段的遍历 */
			  
			  String codePath = clone.getSourcePath();
			  AnalyticsResult analyResult = FindBugCloneByPath(codePath);

			  if (analyResult!=null) {
				  lsAnalyticsResult.add(analyResult);
				  analyResult.setCloneClass(cloneClass);
			  }
		  }	/** 克隆片段遍历结束//list中存放的是一个克隆片段与AnalyticsResult对应 */
		String cloneClassId;
		cloneClassId = String.format("%d", cloneClass.getId());
		analyticsClassMap.put(cloneClassId, lsAnalyticsResult);
		} /** 克隆类遍历结束 */
		return lsAnalyticsResult;
	}
	
	/***
	 * 	
	 * @param bugsFileName
	 * @param commlogXmlFile
	 * @param cloneClassXml
	 * @return 
	 * 为方便使用重新编写静态的函数
	 */
	public static Map<String, List<AnalyticsResult>> getBugCloneListByTfIdf(String bugsFileName, String commlogXmlFile, String cloneClassXml) {		
		ReviseBugCloneCodeList bugCloneList = new ReviseBugCloneCodeList(bugsFileName,commlogXmlFile,cloneClassXml);		
		
		Map<String, List<AnalyticsResult>> listRevise= bugCloneList.getBugCloneMapByTfIdf();
		System.out.println("匹配成功的克隆片段"+listRevise.size());
		System.out.println("遍历完成");
		return listRevise;
	}
		
	public static void main(String[]args) {
		
		String bugsFileName   = FileNameManage.getBugsFileName();
		String commlogXmlFile = FileNameManage.getCommlogXmlFile();
		String cloneClassXml  = FileNameManage.getCloneClassXml();
		
		ReviseBugCloneCodeList bugCloneList = new ReviseBugCloneCodeList(bugsFileName,commlogXmlFile,cloneClassXml);	
		Map<String, List<AnalyticsResult>> analyticsClassMap;
		
		analyticsClassMap = bugCloneList.getBugCloneListByBugId();
      	for(String cloneClassId : analyticsClassMap.keySet()){
			//保存克隆类的id
	      	List<AnalyticsResult>analyResultList=analyticsClassMap.get(cloneClassId);
	      	for(AnalyticsResult analyResult : analyResultList) {
	      		Clone bugClone = analyResult.getCloneCode();
	      		//System.out.println(bugClone.getSourcePath());
	      		String commitPath = FileNameManage.getCloneDetectPath();

	      		String fullPathLog = bugClone.getSourcePath();
	      		commitPath = fullPathLog.substring(fullPathLog.indexOf("trunk"));
	      		System.out.println(commitPath);
	      		//System.out.println(analyResult.getLogInfomation().getRevision());
	      	}
      	}
		System.out.println("匹配成功的克隆片段"+analyticsClassMap.size());
		System.out.println("遍历完成");
//		Map<String, AnalyticsResult>analysisMap = bugCloneList.getAnalysisMap();
//		for(Map.Entry<String, AnalyticsResult>resEntry : analysisMap.entrySet())
//		{
//			Clone clonecode = resEntry.getValue().getCloneCode();
//			if (clonecode!=null)
//				System.out.println(clonecode.getSourcePath());
//			else
//				System.out.println(clonecode);
//		}
	}
	
	
	public void setAnalysisMap(Map<String, AnalyticsResult> analysisMap) {
		this.analysisMap = analysisMap;
	}
	public Map<String, AnalyticsResult> getAnalysisMap() {
		return this.analysisMap;
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
	public void setLsReviseBugsCloneCode(List<ReviseBugCloneCode> lsReviseBugsCloneCode) {
		this.lsReviseBugsCloneCode = lsReviseBugsCloneCode;
	}	
	
	public List<ReviseBugCloneCode> getLsReviseBugsCloneCode()
	{		
		return getBugCloneCodeList();
	}	
	public Map<String, Clone> getMapRevision2Clone() {
		return this.mapRevision2Clone;
	}
	public Map<String, CloneClass>getMapRevision2CloneClass () {
		
		return this.mapRevision2CloneClass;
	}
}
