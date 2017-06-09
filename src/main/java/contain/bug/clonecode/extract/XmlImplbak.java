package contain.bug.clonecode.extract;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
import bean.FileNameManage;
import bean.LogInformation;
import bean.RevisePath;

public class XmlImplbak implements XmlInterface{
	
	
	private Document document;
	List<CloneClass> listCloneClass;
	Element root;
	Element eleFirstLayer;//指的是root下的第一层
	Element eleSecondLayer;//指的是root下的第二层
	boolean writenAnalyticsResult = false;
	
	public void setCloneClassList(	List<CloneClass> listCloneClass) {
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
//	<clones>
//	<class classid="1" nclones="3" nlines="127" similarity="92">
//	<revision>
//
//	<bugid1>4455</bugid1>
//	<bugid2>4455</bugid2>
//	<revision1 id=222>描述信息</revision1>
//	<revision2 >描述信息</revision2>
//
//	</revision>
//	<!--这些路径一定是产生bug的文件-->
//	<clonecode >apache/coyote/http11/Http11Processor.java </clonecode>
//	<source file="apache/coyote/http11/Http11Processor.java" startline="532" endline="713" pcid="11208"></source>
//	<source file="apache/coyote/http11/Http11AprProcessor.java" startline="633" endline="810" pcid="11322"></source>
//	<source file="java/org/apache/coyote/http11/Http11NioProcessor.java" startline="713" endline="891" pcid="11337"></source>
//	</class>
//	</clones>
	

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
		
		//bug clone code
		//eleSecondLayer = this.document.createElement("bugclonecode");
		//eleFirstLayer.appendChild(eleSecondLayer);
		
		//
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
			//bug clone code
			//Element eleSecondLayer = this.document.createElement("bugclonecode");
			//eleSecondLayer.setTextContent(codeClone.getSourcePath());
			//eleFirstLayer.appendChild(eleSecondLayer);
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
			System.out.println("生成XML文件成功! ") ;
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
				
				//一克隆类id为目录保存克隆片段文件
				GenerateCloneCode fWriteText = new GenerateCloneCode();
				fWriteText.GenerateCodeFile(codeClone, cloneId);
				
				if (this.writenAnalyticsResult) {//log信息和bug信息写入一次即可
					AnalyticsResult analytics = codeClone.getAnalysisResult();
					if(analytics!=null) {//analytics过滤掉克隆代码片段中不含bugs
				
					BugInformation buInformation = analytics.getBugInformation();
					LogInformation logInformation = analytics.getLogInfomation();
					//设置克隆类的属性值
					eleFirstLayer.setAttribute("bugID", buInformation.getBugId());
					eleFirstLayer.setAttribute("revision", logInformation.getRevision());
					}
					else
					{
						//System.out.println("analytics value is:" + analytics);
					}
//				List<RevisePath> pathList = logInformation.getReviseList();		
//				for(RevisePath pathRevise : pathList) {
//					eleSecondLayer = this.document.createElement("revisepath") ;
//					eleSecondLayer.setTextContent(pathRevise.getContent());	
//					eleFirstLayer.appendChild(eleSecondLayer);
//				}
				this.writenAnalyticsResult = false;
				}
			}
			this.writenAnalyticsResult = true;		
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
		
//		eleBugInfo.setAttribute("Product", buInformation.getBugProduct());
//		eleBugInfo.setAttribute("Comp", buInformation.getBugComp());
//		eleBugInfo.setAttribute("Assignee", buInformation.getBugAssign());
		eleBugInfo.setAttribute("bugID", buInformation.getBugId());
//		eleBugInfo.setAttribute("Status", buInformation.getBugStatus());
//		eleBugInfo.setAttribute("Resolution", buInformation.getBugResolution());
//		eleBugInfo.setTextContent(buInformation.getBugSummary());		
		eleFirstLayer.appendChild(eleBugInfo);
	}
	// write log information
	public void writeLogInfromation(Element eleFirstLayer, LogInformation logInformation) {
		
		Element eleBugInfo = this.document.createElement("loggentry") ;
		eleBugInfo.setAttribute("revision", logInformation.getRevision());
//		eleBugInfo.setAttribute("author", logInformation.getCommitAuthor());
//		eleBugInfo.setAttribute("date", logInformation.getCommitDate());
		List<RevisePath> pathList = logInformation.getReviseList();		
		for(RevisePath pathRevise : pathList) {
			eleBugInfo.setTextContent(pathRevise.getContent());		
//			eleBugInfo.setAttribute("text-mods", pathRevise.getText_mods());
//			eleBugInfo.setAttribute("kind", pathRevise.getKind());
//			eleBugInfo.setAttribute("action", pathRevise.getAction());
//			eleBugInfo.setAttribute("prop-mods", pathRevise.getProp_mods());
		}
		eleFirstLayer.appendChild(eleBugInfo);
	}
	
	public void parserXml(String fileName) {
		
	}
	
	public static void main(String args[] ) {
		String bugsFileName   = FileNameManage.getBugsFileName();
		String commlogXmlFile = FileNameManage.getCommlogXmlFile();
		String cloneClassXml  = FileNameManage.getCloneClassXml();
		
		List<CloneClass> bugCloneList = ReviseBugCloneCode.getBugCloneClassList(bugsFileName, commlogXmlFile, cloneClassXml);
		XmlImplbak dd=new XmlImplbak() ;
		dd.setCloneClassList(bugCloneList);
		String str="e:/grade.xml";
		dd.init() ;
		dd.simpleInfo(str);
	}					
}
