package exampleInstance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import bean.RevisePath;



public class SAXOperateXmlDemo {
	
	//计算content内容的一个hash值
	public class RevisePath {
		private String action;
		private String prop_mods;
		private String text_mods;
		private String kind;
		private String content;
		Map<String, String> mapReviseAct;
		public RevisePath() {
			
		}
		public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
		}
		public String getProp_mods() {
			return prop_mods;
		}
		public void setProp_mods(String prop_mods) {
			this.prop_mods = prop_mods;
		}
		public String getText_mods() {
			return text_mods;
		}
		public void setText_mods(String text_mods) {
			this.text_mods = text_mods;
		}
		public String getKind() {
			return kind;
		}
		public void setKind(String kind) {
			this.kind = kind;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public Map<String, String> getMapReviseAct() {
			return mapReviseAct;
		}
		public void setMapReviseAct(Map<String, String> mapReviseAct) {
			this.mapReviseAct = mapReviseAct;
		}
		
		
	}
public class LogInfomation {		
		
		private List<RevisePath> reviseList;
		private String revision;
		private String commitAuthor;
		private String commitDate;
		private String commitMsg;		
		//private  List<String> pathList;
		public String getRevision() {
			return revision;
		}
		public void setRevision(String revision) {
			this.revision = revision;
		}
		public String getCommitAuthor() {
			return commitAuthor;
		}
		public void setCommitAuthor(String commitAuthor) {
			this.commitAuthor = commitAuthor;
		}
		public String getCommitDate() {
			return commitDate;
		}
		public void setCommitDate(String commitDate) {
			this.commitDate = commitDate;
		}
		public String getCommitMsg() {
			return commitMsg;
		}
		public void setCommitMsg(String commitMsg) {
			this.commitMsg = commitMsg;
		}
		/*public List<String> getPathList() {
			return pathList;
		}
		public void setPathList(List<String> pathList) {
			this.pathList = pathList;
		}*/
		public List<RevisePath> getReviseList() {
			return reviseList;
		}
		public void setReviseList(List<RevisePath> reviseList) {
			this.reviseList = reviseList;
		}
		
		
	}

public static List<LogInfomation> userList = null;
public static LogInfomation user = null;
	public void parseXml04(String xmlPath){


		//String xmlPath = "e:/user01.xml";
		
		try {
		//获取SAX分析器的工厂实例,专门负责创建SAXParser分析器
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		//获取SAXParser分析器的实例
		SAXParser saxParser = saxParserFactory.newSAXParser();
		InputStream inputStream = new FileInputStream(new File(xmlPath));		
		
		//解析xml文档						 XmlSAXHandler03
		saxParser.parse(inputStream, new XmlSAXHandler04());
		
		//迭代list
		if (SAXOperateXmlDemo.userList.size() > 0)
		{
			for (LogInfomation user  : SAXOperateXmlDemo.userList) {
				
				System.out.println("---------------------------");
				System.out.println("[revision]" + user.getRevision());
				System.out.println("[author]" + user.getCommitAuthor());
				System.out.println("[date]" + user.getCommitDate());
				System.out.println("[msg]" + user.getCommitMsg());
			
/*			List<String> pathList = user.getPathList();
			if (pathList.size()>0) {
				for (String logPaht: pathList)
				System.out.println("[path]" + logPaht);
			}*/
			//修改路径信息都在此	
			List<RevisePath> revisePathList = user.getReviseList();
			for(RevisePath revisePath : revisePathList)	{
				if (revisePath != null) {					
					System.out.println("[path]" + revisePath.getContent());
					Map<String, String> mapReviseTypes = revisePath.getMapReviseAct();
					System.out.print("[moditype]");
					for(String modiKey : mapReviseTypes.keySet())
					System.out.print(modiKey + ":" + mapReviseTypes.get(modiKey)+" ");
					}
				}
			}
			
		}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	class XmlSAXHandler04 extends DefaultHandler {
		//因为startElemnt()才能获取到标签名称，但是标签的内容在characters()获取，而且他们的执行顺序
		//一直是顺序的,所以可以使用currentQName来过滤一下获取上一次的标签名
		private String currentQName;
		private boolean isLogPath = false;
		//private  List<String> pathList  ;
		private List<RevisePath> revisePathList;
		
		
		@Override
		public void startDocument() throws SAXException{
			SAXOperateXmlDemo.userList = new ArrayList<LogInfomation>();
			
		}
		@Override
		public void endDocument() throws SAXException {
		
		}
		RevisePath revisePath ;
		
		public RevisePath getRevisePath() {
			return revisePath;
		}
		public void setRevisePath(RevisePath revisePath) {
			this.revisePath = revisePath;
		}
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

			if(qName.equals("logentry")){
				SAXOperateXmlDemo.user = new LogInfomation();//每次解析到了user标签才会创建user对象实例
				//添加标签中的id属性
				if(attributes.getLength() > 0){
					SAXOperateXmlDemo.user.setRevision((attributes.getValue("revision")));
				}
			}
			if(qName.equals("paths")) {
				//pathList = new ArrayList<String>();
				revisePathList = new ArrayList<RevisePath>();
				
				//System.out.println(attributes.getValue("qq"));
			}
			if (qName.equals("path")) {
				isLogPath = true;
				revisePath = new RevisePath(); 
				Map<String, String> mapReviseAct = new HashMap<String, String>();
				if (attributes.getLength()>0) {
					//根据下标获取属性name和value，也可以直接传递属性name获取属性值:attributes.getValue("id")
					//System.out.println("element属性值--->" + attributes.getQName(0) + ":" + attributes.getValue(0));
					revisePath.setAction(attributes.getValue(0));
					revisePath.setProp_mods(attributes.getValue(1));
					revisePath.setText_mods(attributes.getValue(2));
					revisePath.setKind(attributes.getValue(3));
					for(int i=0; i<attributes.getLength();i++){
						mapReviseAct.put(attributes.getQName(i), attributes.getValue(i));
						revisePath.setMapReviseAct(mapReviseAct);
					}
					//System.out.println("element属性值--->" + attributes.getValue(0));
				}
				revisePathList.add(revisePath);
			}
			this.currentQName = qName;
		}
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
		/**
		 * 需要说明的是，因为每一个非空标签都有characters(),那么无法知道user子标签循环完了
		 * 但是可以这样，如果不考虑子标签顺序可以判断是否解析到了最后一个子标签来判断或者直接在user标签的endElement中添加即可
		 */
			if (qName.equals("logentry")){
				SAXOperateXmlDemo.userList.add(SAXOperateXmlDemo.user);
				SAXOperateXmlDemo.user = null;

			}
			if (qName.equals("paths"))
			{
				//SAXOperateXmlDemo.user.setPathList(pathList);
				SAXOperateXmlDemo.user.setReviseList(revisePathList);
				
			}
			if (qName.equals("path")) {
				this.isLogPath = false;
			}
				
			this.currentQName = null;		
		}
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
		
			String content = new String(ch, start, length);

			//if (isLogPath){				
				//pathList.add(content);		
				//System.out.print(content);
			//}
			if (SAXOperateXmlDemo.user !=null && currentQName != null){
				if(currentQName.equals("author")){
					SAXOperateXmlDemo.user.setCommitAuthor(content);
				}else if(currentQName.equals("date")){
					SAXOperateXmlDemo.user.setCommitDate(content);
				}else if(currentQName.equals("msg")){
					SAXOperateXmlDemo.user.setCommitMsg(content);
				} else if(currentQName.equals("path")) {
					revisePath.setContent(content);
				}
				
			}
				
		}
	}
	
	public static void main(String[] args) {
		String xmlPath = "e:/user01.xml";
		SAXOperateXmlDemo demo = new SAXOperateXmlDemo();
		demo.parseXml04(xmlPath);
	}
}
