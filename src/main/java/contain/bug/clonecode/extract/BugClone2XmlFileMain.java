package contain.bug.clonecode.extract;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bean.AnalyticsResult;
import bean.BugInformation;
import bean.Clone;
import bean.CloneClass;
import bean.CloneClassList;
import bean.FileNameManage;
import bean.LogInformation;
import bean.RevisePath;


public class BugClone2XmlFileMain implements XmlInterface{
	
	
	private Document document;
	List<CloneClass> listCloneClass;
	Element root;
	Element eleFirstLayer;//指的是root下的第一层
	Element eleSecondLayer;//指的是root下的第二层
	boolean writenAnalyticsResult = false;
	
	public void setCloneClassList(List<CloneClass> listCloneClass) {
	this.listCloneClass = listCloneClass;
	}
	
	public void init() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;
		
			DocumentBuilder builder = factory.newDocumentBuilder() ;
			this.document = builder.newDocument() ;
			
			} catch (ParserConfigurationException e)
			{
			System.out.println(e.getMessage() ) ;
			}
	}

	public void writeCloneClass(CloneClass cloneClass){
		String cloneId;
		eleFirstLayer = this.document.createElement("cloneclass") ;
		String atrrValue = String.format("%d", cloneClass.getId());
		cloneId = atrrValue;
		eleFirstLayer.setAttribute("cloneclassid", atrrValue);
		atrrValue = String.format("%d", cloneClass.getNfragments());
		eleFirstLayer.setAttribute("nclones", atrrValue);			
		atrrValue = String.format("%d", cloneClass.getNlines());
		eleFirstLayer.setAttribute("nlines", atrrValue);
		root.appendChild(eleFirstLayer);
		
		List<Clone> cloneCodeList = cloneClass.getCloneList();
		for(Clone codeClone : cloneCodeList) {
			eleSecondLayer = this.document.createElement("clonecode");//添加克隆代码详细信息
			eleFirstLayer.appendChild(eleSecondLayer);
			//设置检测结果的克隆代码属性
			atrrValue = String.format("%s", codeClone.getSource());
			eleSecondLayer.setAttribute("file", codeClone.getSource());
			atrrValue = String.format("%d", codeClone.getStratLine());
			eleSecondLayer.setAttribute("startline", atrrValue);				
			atrrValue = String.format("%d", codeClone.getEndLine());
			eleSecondLayer.setAttribute("endline", atrrValue);
			atrrValue = String.format("%d", codeClone.getPcid());
			eleSecondLayer.setAttribute("pcid", atrrValue);
			eleSecondLayer.setTextContent(codeClone.getSourcePath());

			GenerateCloneCode fWriteText = new GenerateCloneCode();
			fWriteText.GenerateCodeFile(codeClone, cloneId);
			if (!this.writenAnalyticsResult) {//log信息和bug信息写入一次即可
			AnalyticsResult analytics = codeClone.getAnalysisResult();
			BugInformation buInformation = analytics.getBugInformation();
			LogInformation logInformation = analytics.getLogInfomation();
			writeBugInformation(eleFirstLayer, buInformation);
			writeLogInfromation(eleFirstLayer, logInformation);
			this.writenAnalyticsResult = false;
			}
		}
		this.writenAnalyticsResult = true;
		//use nicad detect result
		eleSecondLayer = this.document.createElement("clonecode");
		eleFirstLayer.appendChild(eleSecondLayer);		
		
		eleSecondLayer = this.document.createElement("revision");
		eleFirstLayer.appendChild(eleSecondLayer);			
	}
	
	public void save2XmlFile(String fileName) {
		TransformerFactory tf = TransformerFactory.newInstance() ;
		try {
			Transformer transformer = tf.newTransformer() ;
			DOMSource source = new DOMSource(document) ;
			
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8") ;
			transformer.setOutputProperty(OutputKeys.INDENT, "yes") ;
			
			PrintWriter pw = new PrintWriter(new FileOutputStream(fileName) ) ;
			StreamResult result = new StreamResult(pw) ;
			transformer.transform(source, result) ;
			System.out.println("含bugs的克隆代码写入文件成功！ ") ;
			} catch
			(TransformerConfigurationException e) {
			System.out.println(e.getMessage() ) ;
			} catch (IllegalArgumentException e) {
			System. out. println(e.getMessage() ) ;
			} catch (FileNotFoundException e) {
			System. out. println(e.getMessage() ) ;
			} catch (TransformerException e) {
			System. out. println(e.getMessage() ) ;
			}
	}
	public void simpleInfo(String fileName) {
		
		root =	this.document.createElement("clones") ;
		this.document.appendChild(root) ;
		
		for(CloneClass cloneClass : this.listCloneClass) {	
			//保存克隆类的id
			String cloneId;		
			eleFirstLayer = this.document.createElement("cloneclass") ;
			String atrrValue = String.format("%d", cloneClass.getId());
			cloneId = atrrValue;
			eleFirstLayer.setAttribute("cloneclassid", atrrValue);
			root.appendChild(eleFirstLayer);			
			//
			List<Clone> cloneCodeList = cloneClass.getCloneList();
			for(Clone codeClone : cloneCodeList) {
				
				eleSecondLayer = this.document.createElement("clonecode");//添加克隆代码详细信息
				eleFirstLayer.appendChild(eleSecondLayer);
				//设置检测结果的克隆代码的路径
				eleSecondLayer.setTextContent(codeClone.getSourcePath());	
				String attributeValue = String.format("%d", codeClone.getStratLine());
				eleSecondLayer.setAttribute("startline", attributeValue);
				attributeValue = String.format("%d", codeClone.getEndLine());
				eleSecondLayer.setAttribute("endline", attributeValue);
				
				//以克隆类id为目录保存克隆片段文件
				GenerateCloneCode fWriteText = new GenerateCloneCode();
				fWriteText.GenerateCodeFile(codeClone, cloneId);
				
				if (this.writenAnalyticsResult) {//log信息和bug信息写入一次即可
					AnalyticsResult analytics = codeClone.getAnalysisResult();
					if(analytics!=null) {//analytics过滤掉克隆代码片段中不含bugs
				
					BugInformation buInformation = analytics.getBugInformation();
					LogInformation logInformation = analytics.getLogInfomation();
					if(buInformation!=null &&logInformation!=null) {
					//设置克隆类的属性值
					eleFirstLayer.setAttribute("bugID", buInformation.getBugId());
					eleFirstLayer.setAttribute("revision", logInformation.getRevision());}
					}
					else
					{
						//System.out.println("analytics value is:" + analytics);
					}
				this.writenAnalyticsResult = false;
				}
			}
			this.writenAnalyticsResult = true;		
		}	
		save2XmlFile(fileName);		
	}
	//F36mV649aK	
	public void simpleInfo(String fileName, List<AnalyticsResult> analyResultList) {
		
		root =	this.document.createElement("clones") ;
		this.document.appendChild(root) ;
		
		for(AnalyticsResult analyticsResult : analyResultList) {
			if (analyticsResult==null)continue;
			CloneClass cloneClass = analyticsResult.getCloneClass();
			//保存克隆类的id
			String cloneId;		
			eleFirstLayer = this.document.createElement("cloneclass") ;
			String atrrValue = String.format("%d", cloneClass.getId());
			cloneId = atrrValue;
			eleFirstLayer.setAttribute("cloneclassid", atrrValue);
			root.appendChild(eleFirstLayer);			
			List<Clone> cloneCodeList = cloneClass.getCloneList();
			for(Clone codeClone : cloneCodeList) {
				
				eleSecondLayer = this.document.createElement("clonecode");//添加克隆代码详细信息
				eleFirstLayer.appendChild(eleSecondLayer);
				//设置检测结果的克隆代码的路径
				eleSecondLayer.setTextContent(codeClone.getSourcePath());	
				String attributeValue = String.format("%d", codeClone.getStratLine());
				eleSecondLayer.setAttribute("startline", attributeValue);
				attributeValue = String.format("%d", codeClone.getEndLine());
				eleSecondLayer.setAttribute("endline", attributeValue);
				
				//以克隆类id为目录保存克隆片段文件
				GenerateCloneCode fWriteText = new GenerateCloneCode();
				fWriteText.GenerateCodeFile(codeClone, cloneId);
				
				if (this.writenAnalyticsResult) {//log信息和bug信息写入一次即可
					AnalyticsResult analytics = codeClone.getAnalysisResult();
					if(analytics!=null) {//analytics过滤掉克隆代码片段中不含bugs
				
					BugInformation buInformation = analytics.getBugInformation();
					LogInformation logInformation = analytics.getLogInfomation();
					if(buInformation!=null &&logInformation!=null) {
					//设置克隆类的属性值
					eleFirstLayer.setAttribute("bugID", buInformation.getBugId());
					eleFirstLayer.setAttribute("revision", logInformation.getRevision());}
					}
					else
					{
						//System.out.println("analytics value is:" + analytics);
					}
				this.writenAnalyticsResult = false;
				}
			}
			this.writenAnalyticsResult = true;		
		}
		save2XmlFile(fileName);		
	}
/**
 * 去除list重复的元素，但没有使用
 * @param list
 */
	public static void  removeDuplicate000(List list) {
		
	for(int i=0; i<list.size()-1;i++) {
		for(int j=list.size()-1;j>i;j--) {
			if(list.get(j).equals(list.get(i)))
			{
				list.remove(j);
			}
		}
	}
	}
	public static List<AnalyticsResult>  removeDuplicate(List<AnalyticsResult> list) {
		Set<AnalyticsResult> analySet = new HashSet<AnalyticsResult>();
		analySet.addAll(list);
		List<AnalyticsResult> analyticsResultList = new ArrayList<AnalyticsResult>(); 
		
		for(AnalyticsResult analyResult : analyticsResultList) {
			analyticsResultList.add(analyResult);
		}
		return analyticsResultList;
	}
	
	/***通过代码路径匹配含bugs的克隆代码文件
	 * @param  filePath:文件的修改路径
	 * @return 返回AnalyticsResult的对象
	 * 
	*/
	public AnalyticsResult FindBugCloneByPath(String codePath) {
		
		AnalyticsResult  tmpAnalyReuslt = null;
		AnalyticsResult  analyReuslt    = null;/** 作为函数的返回值 */	
		
		/** 获取bugs与提交日志信息的结果,保存到成员变量mapAnalysis */
		Map<String, AnalyticsResult> analysisMap = null;/** <修改版本号---分析结果>  */
		String bugsFileName   = FileNameManage.getBugsFileName();
		String commlogXmlFile = FileNameManage.getCommlogXmlFile();

		analysisMap = BugMatchCommitInfo.getMatchResult(bugsFileName, commlogXmlFile);
		for(Map.Entry<String, AnalyticsResult>resEntry : analysisMap.entrySet())
		{
			tmpAnalyReuslt = resEntry.getValue();
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
			    	System.out.println(fullPath);
					}
				}/** 克隆片段遍历结束//list中存放的是一个克隆片段与AnalyticsResult对应 */
						  
			}/**  克隆类遍历结束 */
		return analyReuslt;
	}	
	/** 
	 * 获取clone code文件与bugs对应关系，删除	List<Map<String, AnalyticsResult>>
	 * 里面的路径与克隆代码中的文件路径不同的
	 * 剩下的就是clone code文件、bugs、修改日志信息三者关联的内容了
	 * 并检测的内容写入到xml文件
	 */	
	public Map<String, List<AnalyticsResult>>  getBugCloneListByBugId(String fileName) {
		
		Map<String, List<AnalyticsResult>> analyticsClassMap = new HashMap<String, List<AnalyticsResult>>();
		
		/** 获取bugs与提交日志信息的结果,保存到成员变量mapAnalysis */
		root =	this.document.createElement("clones") ;
		this.document.appendChild(root) ;
		
		/** 得到克隆文件的路径 */
		List<CloneClass> lsCloneClass = CloneClassList.getListCloneClass(FileNameManage.getCloneClassXml());

		for (int i=0; i < lsCloneClass.size(); i++) {/** 克隆类的遍历 */
			
		CloneClass cloneClass = (CloneClass)lsCloneClass.get(i);
		List<Clone> listClone = cloneClass.getCloneList();		
		List<AnalyticsResult> lsAnalyticsResult = new ArrayList<AnalyticsResult>();
		
		//保存克隆类的id
		String cloneClassId = String.format("%d", cloneClass.getId());
		eleFirstLayer = this.document.createElement("cloneclass") ;
		eleFirstLayer.setAttribute("cloneclassid", cloneClassId);
		root.appendChild(eleFirstLayer);
		/**每一个克隆片段对应一个修复日志中的文件路径**/
		for (Clone cloneCode : listClone) {/** 克隆片段的遍历 */

			  String codePath = cloneCode.getSourcePath();
			  AnalyticsResult analyResult = FindBugCloneByPath(codePath);

			  if (analyResult!=null) {
				  
				  analyResult.setCloneCode(cloneCode);
				  lsAnalyticsResult.add(analyResult);
				  
				  eleSecondLayer = this.document.createElement("clonecode");//添加克隆代码详细信息
				  eleFirstLayer.appendChild(eleSecondLayer);
					//设置检测结果的克隆代码的路径
				  eleSecondLayer.setTextContent(cloneCode.getSourcePath());	
				  String attributeValue = String.format("%d", cloneCode.getStratLine());
				  eleSecondLayer.setAttribute("startline", attributeValue);
				  attributeValue = String.format("%d", cloneCode.getEndLine());
				  eleSecondLayer.setAttribute("endline", attributeValue);

						
				  BugInformation buInformation = analyResult.getBugInformation();
				  LogInformation logInformation = analyResult.getLogInfomation();
				  if(buInformation!=null &&logInformation!=null) {
				  //设置克隆类的属性值
				  eleSecondLayer.setAttribute("bugID", buInformation.getBugId());
				  eleSecondLayer.setAttribute("revision", logInformation.getRevision());
				  }
				//以克隆类id为目录保存克隆片段文件
				GenerateCloneCode fWriteText = new GenerateCloneCode();
				fWriteText.GenerateCodeFile(cloneCode, cloneClassId);	
			  }
		  }	/** 克隆片段遍历结束//list中存放的是一个克隆片段与AnalyticsResult对应 */
		analyticsClassMap.put(cloneClassId, lsAnalyticsResult);
		} /** 克隆类遍历结束 */
		save2XmlFile(fileName);		
		return analyticsClassMap;
	}
	
	/****
	 ** @param fileName : 保存含bugs克隆代码的文件名
	 *  @param analyResultMap : 传入bug与log日志关联的结果
	 *  	   key:克隆id value: 含bug的克隆片段
	 *  @return 
	 */
	public void saveByCloneClass(String fileName, Map<String, List<AnalyticsResult>> analyResultMap) {
		
		root =	this.document.createElement("clones") ;
		this.document.appendChild(root) ;
		Set<String> classClones=analyResultMap.keySet();
      	for(String cloneClassId : classClones){
			//保存克隆类的id
			eleFirstLayer = this.document.createElement("cloneclass") ;
			eleFirstLayer.setAttribute("cloneclassid", cloneClassId);
			root.appendChild(eleFirstLayer);
	      	List<AnalyticsResult>analyResultList=analyResultMap.get(cloneClassId);
			for(AnalyticsResult analyticsResult : analyResultList) {
				if (analyticsResult==null)continue;
				Clone codeClone = analyticsResult.getCloneCode();
		
				eleSecondLayer = this.document.createElement("clonecode");//添加克隆代码详细信息
				eleFirstLayer.appendChild(eleSecondLayer);
				//设置检测结果的克隆代码的路径
				eleSecondLayer.setTextContent(codeClone.getSourcePath());	
				String attributeValue = String.format("%d", codeClone.getStratLine());
				eleSecondLayer.setAttribute("startline", attributeValue);
				attributeValue = String.format("%d", codeClone.getEndLine());
				eleSecondLayer.setAttribute("endline", attributeValue);
				
				BugInformation buInformation = analyticsResult.getBugInformation();
				LogInformation logInformation = analyticsResult.getLogInfomation();
				if(buInformation!=null &&logInformation!=null) {
				//设置克隆类的属性值
				eleSecondLayer.setAttribute("bugID", buInformation.getBugId());
				eleSecondLayer.setAttribute("revision", logInformation.getRevision());
				}
				//以克隆类id为目录保存克隆片段文件
				GenerateCloneCode fWriteText = new GenerateCloneCode();
				fWriteText.GenerateCodeFile(codeClone, cloneClassId);				
			}
		}	
		save2XmlFile(fileName);		
	}
	public void createXml(String fileName) {
		
		root =	this.document.createElement("clones") ;
		this.document.appendChild(root) ;
		for(CloneClass cloneClass : this.listCloneClass) {		
					
			writeCloneClass(cloneClass);
		}	
		save2XmlFile(fileName);
	}
	// write bug information
	public void writeBugInformation(Element eleFirstLayer, BugInformation buInformation) {
		Element eleBugInfo = this.document.createElement("bugentry") ;		

		eleBugInfo.setAttribute("bugID", buInformation.getBugId());	
		eleFirstLayer.appendChild(eleBugInfo);
	}
	// write log information
	public void writeLogInfromation(Element eleFirstLayer, LogInformation logInformation) {
		
		Element eleBugInfo = this.document.createElement("loggentry") ;
		eleBugInfo.setAttribute("revision", logInformation.getRevision());
		List<RevisePath> pathList = logInformation.getReviseList();		
		for(RevisePath pathRevise : pathList) {
			eleBugInfo.setTextContent(pathRevise.getContent());		
		}
		eleFirstLayer.appendChild(eleBugInfo);
	}
	
	public void parserXml(String fileName) {
		
	}
	
	public static void saveBugTfIdfCloneFile(String saveFile) {
		String bugsFileName   = FileNameManage.getBugsFileName();
		String commlogXmlFile = FileNameManage.getCommlogXmlFile();
		String cloneClassXml  = FileNameManage.getCloneClassXml();

		ReviseBugCloneCodeList bugCloneList = new ReviseBugCloneCodeList(bugsFileName,commlogXmlFile,cloneClassXml);		
		Map<String, List<AnalyticsResult>>analyticsClassMap = bugCloneList.getBugCloneMapByTfIdf();

		BugClone2XmlFileMain dd1=new BugClone2XmlFileMain() ;
		dd1.init() ;
		dd1.saveByCloneClass(saveFile, analyticsClassMap);
	}	
	
	public static void saveByBugId(String saveFile) {
		String bugsFileName   = FileNameManage.getBugsFileName();
		String commlogXmlFile = FileNameManage.getCommlogXmlFile();
		String cloneClassXml  = FileNameManage.getCloneClassXml();
		
		ReviseBugCloneCodeList bugCloneList = new ReviseBugCloneCodeList(bugsFileName,commlogXmlFile,cloneClassXml);		
		Map<String, List<AnalyticsResult>>analyticsClassMap = bugCloneList.getBugCloneListByBugId();

		BugClone2XmlFileMain dd1=new BugClone2XmlFileMain() ;
		dd1.init() ;
		dd1.saveByCloneClass(saveFile, analyticsClassMap);
		System.out.println("遍历完成");
	}
	
/**  
 * 
 * 使用TfIdf的方式提取含bugs的克隆代码  
 * 代码已经整理的很简洁了
 * @throws IOException 
 * */	
//	public static void main(String args[] ) {
//		String saveFile = "e:/grade.xml";
//		//saveBugTfIdfCloneFile(saveFile);	
//		saveByBugId(saveFile);
//		System.out.println("含bugs的克隆代码提取成功！");
//	}
	
	
	
	
	public static void initSettings()  {
		//从配置文件获取svn仓库地址,用户名和密码
		Properties prop = new Properties(); 
		String curPath = System.getProperty("user.dir");
											
		String iniFilePath = curPath + "\\" + "clonebug.properties";
		System.out.println(iniFilePath);
		
		//InputStream in = Object.class.getResourceAsStream(iniFilePath);   
	    try {
	    	InputStream in = new BufferedInputStream (new FileInputStream(iniFilePath));  
			prop.load(in);
			FileNameManage.setBugsFileName(prop.getProperty("bugsFileName").trim());
			System.out.println(prop.getProperty("commlogXmlFile").trim());
			FileNameManage.setCommlogXmlFile(prop.getProperty("commlogXmlFile").trim());
			FileNameManage.setCloneClassXml(prop.getProperty("cloneClassXml").trim());
			FileNameManage.setCloneDetectPath(prop.getProperty("cloneDetectPath").trim());
			FileNameManage.setCommitPath(prop.getProperty("commitPath").trim());
			//FileNameManage.setComputePath(prop.getProperty("computePath").trim());
	    }catch (IOException e) {
	        e.printStackTrace();
	        //return null;
	    }	
	}
	public static void main(String[]args) throws IOException {
		System.out.println(System.getProperty("user.dir"));
//		String bugsFileName   = FileNameManage.getBugsFileName();
//		String commlogXmlFile = FileNameManage.getCommlogXmlFile();
//		String cloneClassXml  = FileNameManage.getCloneClassXml();
			initSettings();
		if (args.length == 1) { // 没有向命令行输入参数的情况
			
			String saveFile = args[0];
//			String saveFile = "e:/s.xml";
			BugClone2XmlFileMain dd1=new BugClone2XmlFileMain() ;
			dd1.init() ;
			dd1.getBugCloneListByBugId(saveFile);			
		} else {
			System.out.println("参数不正确\n");
		}

		
	}
	
	
	public static Map<String, CloneClass> getCloneClassListByRevision(String bugsFileName,String commlogXmlFile,String cloneClassXml) {
		Map<String, CloneClass> mapRevision2Clone = new HashMap<String, CloneClass>();
		
		ReviseBugCloneCodeList lsReviseBugsCloneCode = new ReviseBugCloneCodeList(bugsFileName,commlogXmlFile,cloneClassXml);
		lsReviseBugsCloneCode.getLsReviseBugsCloneCode();
		//mapRevision2Clone = lsReviseBugsCloneCode.getCloneClassListByRevision(bugsFileName, commlogXmlFile, cloneClassXml);
		mapRevision2Clone = lsReviseBugsCloneCode.getMapRevision2CloneClass();
		return mapRevision2Clone;
	}
}