package exampleInstance;
import java.io.BufferedOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.util.ArrayList;  
import java.util.List;  
  
import javax.xml.parsers.ParserConfigurationException;  
import javax.xml.parsers.SAXParser;  
import javax.xml.parsers.SAXParserFactory;  
import javax.xml.transform.OutputKeys;  
import javax.xml.transform.Result;  
import javax.xml.transform.Transformer;  
import javax.xml.transform.TransformerConfigurationException;  
import javax.xml.transform.sax.SAXTransformerFactory;  
import javax.xml.transform.sax.TransformerHandler;  
import javax.xml.transform.stream.StreamResult;  
  
import org.xml.sax.Attributes;  
import org.xml.sax.InputSource;  
import org.xml.sax.SAXException;  
import org.xml.sax.helpers.AttributesImpl;  
import org.xml.sax.helpers.DefaultHandler;


public class SAXOperateXmlDemo2 {
	
	public static void main(String[] args) {
		SAXOperateXmlDemo2 demo = new SAXOperateXmlDemo2();
		//demo.parseXml101();
		//demo.parseXml02();
		demo.parseXml04();
	}
	
public void parseXml101(){
	String xmlPath = "e:/user01.xml";
	//String xmlName = xmlPath.substring(xmlPath.lastIndexOf("\\"));
	try{
		//获取SAX分析器的工厂实例，专门负责创建SAXParser分析器
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		
		//获取SAXParser分析器的实例
		SAXParser saxParser = saxParserFactory.newSAXParser();
		// 和其他解析方式一样，也要间接通过InputStream输入流对象获取xml信息
		InputStream inputStream = new FileInputStream(new File(xmlPath));
		//解析xml文档
		//这里传递了自定义的XmlSAXHandler()管理者参数来解析xml,不像以前都是直接调用返回的Document对象
		saxParser.parse(inputStream, new XmlSAXHandler01());
		//1、直接地指定绝对路径获取文件输入流对象
		//InputStream inputStream = new FileInputStream(xmlPath);
		//2、使用类的相对路径查找xml路径
		//InputStream inputStream = this.getClass().getResourceAsStream(xmlName);
		//3、也可以指定路径完成InputStream输入流的实例化操作
		
		//4、使用InputSource输入源作为参数也可以转化xml
		//InputSource inputSource = new InputSource(inputStream);		
	} catch (ParserConfigurationException e) {
		e.printStackTrace();
	} catch (SAXException e) {
		e.printStackTrace();
	} catch (FileNotFoundException e){
		e.printStackTrace();
	} catch (IOException e){
		e.printStackTrace();
	}
}

	
	/**
	 * 
	 * 解析SAX的处理者01
	 * 
	 * 
	 */	
	class XmlSAXHandler01 extends DefaultHandler {
		
		@Override
		public void startDocument() throws SAXException {
			System.out.println("---->startDocument() is invoked....");
			super.startDocument();
		}
		@Override
		public void endDocument() throws SAXException {
			System.out.println("---->endDocument() is invoked...");
			super.endDocument();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
			System.out.println("-------->startElement() is invoked...");
			super.startElement(uri, localName, qName, attributes);
		}
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			System.out.println("-------->endElement() is invoked....");
			super.endElement(uri, localName, qName);
		}
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			System.out.println("-------------->characters () is invoked.....");
			super.characters(ch, start, length);
		}
	}
	public void parseXml02() {
		String xmlPath = "e:/user01.xml";
		try {
			//获取SAX分析器的工厂实例,专门负责创建SAXParser分析器
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			//获取SAXParser分析器的实例
			SAXParser saxParser = saxParserFactory.newSAXParser();
			InputStream inputStream = new FileInputStream(new File(xmlPath));
			
			//解析xml文档
			saxParser.parse(inputStream, new XmlSAXHandler02());
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
	
	/**
	 * 解析SAX的处理者02
	 * 
	 * 
	 */
	class XmlSAXHandler02 extends DefaultHandler {
		@Override
		public void startDocument() throws SAXException {
			System.out.println("---->startDocument() is invoked...");			
		}
		
		@Override
		public void endDocument() throws SAXException {
			System.out.print("---->endDocument() is invoked...");
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
			System.out.println("-------->startElement() is invoked...");
			System.out.println("uri的属性值:" + uri);
			System.out.println("localName的属性值:" + localName);
			System.out.println("qName的属性值:" + qName);
			if (attributes.getLength()>0) {
				//根据下标获取属性name和value，也可以直接传递属性name获取属性值:attributes.getValue("id")
				System.out.println("element属性值--->" + attributes.getQName(0) + ":" + attributes.getValue(0));
			}
			
		}
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			System.out.print("-------->endElement() is invoked...");
			System.out.println("uri的属性值:" + uri);
			System.out.print("localName的属性值:" + localName);
			System.out.print("qName的属性值" + qName);
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			System.out.println("-------->characters() is invoked...");
			System.out.println("节点元素文本内容:" + new String(ch, start, length));
		}
	}
	/**
	 * 接下来需要实现如何封装SAX解析完毕的XML文档，都知道java是面向对象的编程的，那么这个时候可以把文档中的每个节点都看成一个对象，
	 * 包括节点里面的属性也是一样，那么在解析XML的时候直接使用javabean封装一下，思路就非常清晰了啊，但是现在还有一个问题：如何在
	 * SAXParser调用Parse()方法之后返回最终的结果集呢?就目前肯定不行，其一方法没有返回值，其二解析操作完全交给DefaultHandler去
	 * 做了，所以这种情况下肯定不能使用普通变量或者全局变量，因为使用了之后会随着当前操作类型的实例化生命州区而存在，并且DefaultHandler
	 * 在调用的时候又学要产生一个新的实例，这样前后就没有关联了。所以只能使用静态ArrayList完成了。
	 * 
	 */
	/**
	 * 具体操作如下:
	 * 1、前面说了构建节点对象和属性对象，具体代码如下:
	 */
	public class Node {
		private Long id;
		private String name;
		private String text;
		private List<Attribute> attributeList;
		private List<Node> nodeList;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public List<Attribute> getAttributeList() {
			return attributeList;
		}
		public void setAttributeList(List<Attribute> attributeList) {
			this.attributeList = attributeList;
		}
		public List<Node> getNodeList() {
			return this.nodeList;
		}
		public void setNodeList(List<Node> nodeList) {
			this.nodeList = nodeList;
		}
		
	}
	
	/**
	 * Xml属性对象
	 * @zhayw
	 */
	public class Attribute {
		private String name;
		private String value;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
	}
	public static List<Node> nodeList = null;
	public static Node node = null;
	
	public void parseXml03(){
	String xmlPath = "e:/user03.xml";
	
	try {
	//获取SAX分析器的工厂实例,专门负责创建SAXParser分析器
	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	//获取SAXParser分析器的实例
	SAXParser saxParser = saxParserFactory.newSAXParser();
	InputStream inputStream = new FileInputStream(new File(xmlPath));
	
	
	//解析xml文档						 XmlSAXHandler03
	saxParser.parse(inputStream, new XmlSAXHandler03());
	
	//迭代list
	if (SAXOperateXmlDemo2.nodeList.size() > 0) {
		for (Node node : SAXOperateXmlDemo2.nodeList) {
		System.out.println("---------------------------");
		System.out.println("[节点]" + node.getName() + ":" + node.getText());
		List<Attribute> attributeList = node.getAttributeList();
		if (attributeList !=null) {
			for (Attribute attribute : attributeList) {
			System.out.println("[属性]" + attribute.getName() + "" + attribute.getValue());
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
	/**
	 * 解析SAX的处理者03
	 * @zhaoyw
	 * 
	*/
	class XmlSAXHandler03 extends DefaultHandler {

		
		@Override
		public void startDocument() throws SAXException{
			SAXOperateXmlDemo2.nodeList = new ArrayList<Node>();
			
		}
		@Override
		public void endDocument() throws SAXException {
		
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			
			SAXOperateXmlDemo2.node = new Node();
			SAXOperateXmlDemo2.node.setId(null);
			SAXOperateXmlDemo2.node.setName(qName);
			//封装当前节点的属性
			List<Attribute> attributeList = new ArrayList<Attribute>();
			if (attributes.getLength()>0) {
				for (int i = 0; i < attributes.getLength(); i++) {
					Attribute attribute = new Attribute();
					attribute.setName(attributes.getQName(i));
					attribute.setValue(attributes.getValue(i));
					attributeList.add(attribute);
				}
			}
			SAXOperateXmlDemo2.node.setAttributeList(attributeList);
		}
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
		
		}
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
		
			if (SAXOperateXmlDemo2.node!=null) {
				SAXOperateXmlDemo2.node.setText(new String(ch, start, length));
				//因为startElement()在characters()的前面调用，所以必须放在后面才能把文本添加进去
				SAXOperateXmlDemo2.nodeList.add(SAXOperateXmlDemo2.node);
				SAXOperateXmlDemo2.node = null;
				//在这里之所以重新致为null是在解析标签的时候节点与节点之间的回车符、Tab符或者空格
				//及普通文本等这些字符串也算一个节点
				//如果不这样，那么解析的时候会把之前添加成功的节点内的文本给替换掉
				}
		}
		
	}
	public class User {		
		
		private Long id;
		private String name;
		private Long age;
		private String hobby;		
		private  List<String> pathList;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Long getAge() {
			return age;
		}
		public void setAge(Long age) {
			this.age = age;
		}
		public String getHobby() {
			return hobby;
		}
		public void setHobby(String hobby) {
			this.hobby = hobby;
		}
		public List<String> getPathList() {
			return pathList;
		}
		public void setPathList(List<String> pathList) {
			this.pathList = pathList;
		}
	}
	public static List<User> userList = null;
	public static User user = null;
	public void parseXml04() {

		String xmlPath = "e:/user03.xml";
		
		try {
		//获取SAX分析器的工厂实例,专门负责创建SAXParser分析器
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		//获取SAXParser分析器的实例
		SAXParser saxParser = saxParserFactory.newSAXParser();
		InputStream inputStream = new FileInputStream(new File(xmlPath));		
		
		//解析xml文档						 XmlSAXHandler03
		saxParser.parse(inputStream, new XmlSAXHandler04());
		
		//迭代list
		if (SAXOperateXmlDemo2.userList.size() > 0)
		{
			for (User user  : SAXOperateXmlDemo2.userList) {
			System.out.println("---------------------------");
			System.out.println("[id]" + user.getId());
			System.out.println("[姓名]" + user.getName());
			System.out.println("[年龄]" + user.getAge());
			System.out.println("[爱好]" + user.getHobby());
			List<String> pathList = user.getPathList();
			if (pathList.size()>0) {
				for (String logPaht: pathList)
				System.out.println("[path]" + logPaht);
			};
			
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
		private  List<String> pathList  ;
		@Override
		public void startDocument() throws SAXException{
			SAXOperateXmlDemo2.userList = new ArrayList<User>();
			
		}
		@Override
		public void endDocument() throws SAXException {
		
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			
			if(qName.equals("user")){
				SAXOperateXmlDemo2.user = new User();//每次解析到了user标签才会创建user对象实例
				//添加标签中的id属性
				if(attributes.getLength() > 0){
					SAXOperateXmlDemo2.user.setId(Long.valueOf(attributes.getValue("id")));
				}
			}
			if(qName.equals("paths")) {
				pathList = new ArrayList<String>();
				//System.out.println(attributes.getValue("qq"));
			}
			if (qName.equals("path")) {
				isLogPath = true;
				
			}
			this.currentQName = qName;
		}
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
		/**
		 * 需要说明的是，因为每一个非空标签都有characters(),那么无法知道user子标签循环完了
		 * 但是可以这样，如果不考虑子标签顺序可以判断是否解析到了最后一个子标签来判断或者直接在user标签的endElement中添加即可
		 */
			if (qName.equals("user")){
				SAXOperateXmlDemo2.userList.add(SAXOperateXmlDemo2.user);
				SAXOperateXmlDemo2.user = null;

			}
			if (qName.equals("paths"))
			{
				SAXOperateXmlDemo2.user.setPathList(pathList);
			}
			if (qName.equals("path")) {
				this.isLogPath = false;
			}
				
			this.currentQName = null;		
		}
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
		
			String content = new String(ch, start, length);
			//
			if (isLogPath){				
				pathList.add(content);		
				//System.out.print(content);
			}
			if (SAXOperateXmlDemo2.user !=null && currentQName != null){
				if(currentQName.equals("name")){
					SAXOperateXmlDemo2.user.setName(content);
				}else if(currentQName.equals("age")){
					SAXOperateXmlDemo2.user.setAge(Long.valueOf(content));
				}else if(currentQName.equals("hobby")){
					SAXOperateXmlDemo2.user.setHobby(content);
				}
				
			}
				
		}
	}
}
