package exampleInstance;

import java.util.regex.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import bean.LogInformation;
import bean.RevisePath;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

public class DOMParserXml {

	private String xmlFileName;
	Document document;
	DocumentBuilder builder;

	LogInformation logInformation;
	// private String xmlFileName = "";
	private Map<String, LogInformation> mapLogInfos = new HashMap<String, LogInformation>();

	private DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

	DOMParserXml(String xmlFileName) {
		super();
		this.xmlFileName = xmlFileName;
	}

	public Map<String, LogInformation> getMapLogInfos() {
		readXmlFile();
		return this.mapLogInfos;
	}

	public void readXmlFile() {
		try {
			// 通过DocumentBuilderFactory,得到一个DocumentBuilder
			DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
			// 指定解析哪个xml文件
			Document document = dBuilder.parse(xmlFileName);
			// System.out.println(document);//获取logentry的节点，注意空白符
			
			 Node firstchild= document.getFirstChild();
			 System.out.println("doc下第一个孩子节点" + firstchild.getNodeName());
			 firstchild = document.getFirstChild().getFirstChild();
			 System.out.println("doc下第一个孩子节点的第一个孩子" + firstchild.getNodeName());
			
			Node logentryNode = document.getFirstChild().getFirstChild().getNextSibling();

			listSibling(logentryNode);
			// listAllNodes(logentryNode);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// 传入logentry节点，遍历所有的logentry的兄弟节点
	public Map<String, LogInformation> listSibling(Node node) {
		
		if (node.getNodeType() == Document.ELEMENT_NODE) {		
			
			while (node != null) {
				
				if (node.getNodeType() == Document.ELEMENT_NODE) {
					System.out.println(node.getNodeName());
					logInformation = new LogInformation();
					logInformation = getInformation(node);
					String revision = ((Element) node).getAttribute("revision");
					logInformation.setRevision(revision);
					this.mapLogInfos.put(revision, logInformation);
					listAllNodes(node);//遍历logentry revision="1736287"下的所有子节点
				}
				node = node.getNextSibling();
				System.out.println("---------------");
			}
		}
		return mapLogInfos;
	}
	// 遍历xml文件//传入logentry节点，就能遍历的所有节点
	public LogInformation listAllNodes(Node node) {
		if (node.getNodeType() == Document.ELEMENT_NODE) {
			
			// 取出子节点
			NodeList nodeList = node.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node subNode = nodeList.item(i);
				if (subNode.getNodeType() == Document.ELEMENT_NODE) {
					System.out.println(subNode.getNodeName());
					// logInfomation.setCommitAuthor();
					// 在去显示
					logInformation = getInformation(subNode);
//					if (subNode.getNodeName().equals("paths")){						
//						listAllNodes(subNode);
//					}
					
				}
			}
		}
		return logInformation;
	}
	public LogInformation getInformation(Node node) {
		
		String nodeName = node.getNodeName();
		String nodeValue = node.getTextContent();
		nodeValue = nodeValue.replace("\r|\n|\r\n", "");
		
		
		if (nodeName.equals("author")) {
			logInformation.setCommitAuthor(nodeValue);

		} else if (nodeName.equals("date")) {
			logInformation.setCommitDate(nodeValue);

		} else if (nodeName.equals("paths")) {
			List<RevisePath>reviseList = getRevisePathList(node);
			logInformation.setReviseList(reviseList);
		}else if (nodeName.equals("msg")) {
			logInformation.setCommitMsg(nodeValue);				
			logInformation.setRevisedBugId(getBugId(nodeValue));
		}
		return logInformation;
	}
	public List<RevisePath> getRevisePathList(Node nodePaths) {
		
		String nodeValue = "";
		List<RevisePath>reviseList = new ArrayList<RevisePath>();		
		if (nodePaths.getNodeType() == Document.ELEMENT_NODE) {		
			
			NodeList nodeList = nodePaths.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				if(nodeList.item(i).getNodeType()== Document.ELEMENT_NODE)  {
				nodeValue = nodeList.item(i).getTextContent();
				RevisePath revisePath = new RevisePath();
				revisePath.setContent(nodeValue);
				reviseList.add(revisePath);	}	
			}
		}
		return reviseList;
	}
	//一般认为一条修改日志信息中，只能匹配出一条bugid
	public List<String> getBugId(String piContent){
		String bugId = "";
		List<String> listMatchStr = regularMatch("id=[0-9]{5}", piContent);
				
		return listMatchStr;
	}
	public List<String> regularMatch(String regExpression, String piContent) {
		if (piContent==null)return null;
		List<String> listMatchStr = new ArrayList<String>();

		Pattern p = Pattern.compile(regExpression);
		Matcher m = p.matcher(piContent);
		while (m.find()) {
			String tmpBugId = m.group();
			tmpBugId = tmpBugId.substring(3);// 删除掉id=这三个字符}
			listMatchStr.add(tmpBugId);
		}	
	return listMatchStr;
	}
	
	public  void WriteXml(Object object) {
		
	}
	
	public static void main(String[] args) {
		
		String xmlFileName = "E:/tmp/lianxi.xml";
		DOMParserXml domParser = new DOMParserXml(xmlFileName);
		//domParser.readXmlFile();
		
		Map<String, LogInformation> mapLogInfos= new HashMap<String, LogInformation>();
		mapLogInfos = domParser.getMapLogInfos();
		System.out.println(mapLogInfos.size());
		
		for(String bugId : mapLogInfos.keySet())
		{
			System.out.println(bugId);
			if(mapLogInfos.get(bugId).getRevisedBugId().size()>0)
			{
				List<RevisePath> lsRevisePath = mapLogInfos.get(bugId).getReviseList();
				for(RevisePath rvisePath : lsRevisePath) 
				{
					//System.out.println(rvisePath.getContent());
				}
			}
		}
	}
}
