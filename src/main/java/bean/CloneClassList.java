package bean;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class CloneClassList {
	  //systeminfo
	  private String system;//已有
	  private String granularity;//yiyou
	  private String threshold;//exist
	  private int minLine;//exist
	  private int maxLines;//exist
	  //cloneinfo
	  private int npcs;//exist
	  private int npairs;//exit

	  private int nclones;//263
	  private int nfragments;//在xml中没有有可能指的是nlines

	  //classinfo
	  private int nclasses;//exist
	  
	  //runinfo
	  private long ncompares;//exit
	  private long cputime;//exit
	  
	  private List<CloneClass> cloneClassList;
	  public static Document document;
	  Map<Integer, List<Integer>> hmByGID = new HashMap();
	  
	  Map<Integer, Integer> hmByPCID = new HashMap();
	  
	  public CloneClassList()
	  {
	    this.cloneClassList = new ArrayList<CloneClass>();
	  }
	  
	  public void initialize(File f)
	  {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setValidating(false);
	    DocumentBuilder builder = null;
	    try {
	      builder = factory.newDocumentBuilder();
	    }
	    catch (ParserConfigurationException e) {
	      e.printStackTrace();
	    }
	    try {
	      document = builder.parse(f);
	    }
	    catch (SAXException e) {
	      e.printStackTrace();
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    }
	    document.getDocumentElement().normalize();
	    readSysteminfo(document);
	    readClassinfo(document);
	    readRuninfo(document);
	    readClonesClass(document);
	  }
	  
	  private void readSysteminfo(Document d) {
	    NodeList nodeList = d.getElementsByTagName("systeminfo");
	    NamedNodeMap attributeNodes = nodeList.item(0).getAttributes();
	    setSystem(attributeNodes.getNamedItem("system").getNodeValue());
	    setGranularity(attributeNodes.getNamedItem("granularity").getNodeValue());
	    setThreshold(attributeNodes.getNamedItem("threshold").getNodeValue());
	    setMinLine(Integer.parseInt(attributeNodes.getNamedItem("minlines").getNodeValue()));
	    setMaxLines(Integer.parseInt(attributeNodes.getNamedItem("maxlines").getNodeValue()));
	  }
	  
	  private void readClassinfo(Document d)
	  {
//	    <classinfo nclasses="263"/>
	    NodeList nodeList = d.getElementsByTagName("classinfo");
	    NamedNodeMap attributeNodes = nodeList.item(0).getAttributes();
	    setNclasses(Integer.parseInt(attributeNodes.getNamedItem("nclasses").getNodeValue()));

//	    <cloneinfo npcs="15188" npairs="663"/>
	    nodeList = d.getElementsByTagName("cloneinfo");
		attributeNodes = nodeList.item(0).getAttributes();
	    setNpairs(Integer.parseInt(attributeNodes.getNamedItem("npairs").getNodeValue()));
	    attributeNodes.getNamedItem("npcs").getNodeValue();
//<class classid="1" nclones="3" nlines="127" similarity="92">
//	    for(int i=0; i<attributeNodes.getLength();i++)
//	    {
	    	
//	    	System.out.println(attributeNodes.item(i).getNodeValue());
//	    }
//	    setNclones(Integer.parseInt(attributeNodes.getNamedItem("nclones").getNodeValue()));
//	    setNfragments(Integer.parseInt(attributeNodes.getNamedItem("nfragments").getNodeValue()));
//	    setNpairs(Integer.parseInt(attributeNodes.getNamedItem("npairs").getNodeValue()));
//	    setNclasses(Integer.parseInt(attributeNodes.getNamedItem("nclasses").getNodeValue()));
	  }
	  
	  private void readRuninfo(Document d)
	  {
	    NodeList nodeList = d.getElementsByTagName("runinfo");
	    NamedNodeMap attributeNodes = nodeList.item(0).getAttributes();
	    setNcompares(Long.parseLong(attributeNodes.getNamedItem("ncompares").getNodeValue()));
	    setCputime(Long.parseLong(attributeNodes.getNamedItem("cputime").getNodeValue()));
	  }
	  
	  private void readClonesClass(Document d)
	  {
	    int numOfClones = 0;
	    CloneClass cloneClass = null;
	    
	    NodeList nodeList = d.getElementsByTagName("class");
	    
        int nclassCount = nodeList.getLength();//263
	    for (int i = 0; i < nodeList.getLength(); i++) {
	      List<Integer> a = new ArrayList();
	      NamedNodeMap attributeNodes = nodeList.item(i).getAttributes();
	      int id = Integer.parseInt(attributeNodes.getNamedItem("classid").getNodeValue());
	      int nlines = Integer.parseInt(attributeNodes.getNamedItem("nlines").getNodeValue());
//	      int nfragments = Integer.parseInt(attributeNodes.getNamedItem("nfragments").getNodeValue());
	      
	      nodeList.item(i).normalize();
	      NodeList secondaryNodeList = nodeList.item(i).getChildNodes();
	      int nfragments = secondaryNodeList.getLength();//获取克隆片段数
	     
	      cloneClass = new CloneClass(id, nlines, nfragments);
		  
	      for (int j = 0; j < secondaryNodeList.getLength(); j++) {
	        if (secondaryNodeList.item(j).getNodeType() == 1)
	        {
	          NamedNodeMap secondaryAttributeNodes = secondaryNodeList.item(j).getAttributes();
	          String file = secondaryAttributeNodes.getNamedItem("file").getNodeValue();
	          int startLine = Integer.parseInt(secondaryAttributeNodes.getNamedItem("startline").getNodeValue());
	          int endLine = Integer.parseInt(secondaryAttributeNodes.getNamedItem("endline").getNodeValue());
	          int pcid = Integer.parseInt(secondaryAttributeNodes.getNamedItem("pcid").getNodeValue());
	          cloneClass.addClone(new Clone(startLine, endLine, pcid, file));
	          
	          a.add(Integer.valueOf(pcid));
	          this.hmByPCID.put(Integer.valueOf(pcid), Integer.valueOf(id));
	          numOfClones++;
	        }
	      }
	      
	      this.hmByGID.put(Integer.valueOf(id), a);
	      if (cloneClass != null) {
	        this.cloneClassList.add(cloneClass);
	        cloneClass = null;
	      }
	    }
	  }
	  
	  public List<CloneClass> getList()
	  {
	    return this.cloneClassList;
	  }
	  
	  public String getSystem() {
	    return this.system;
	  }
	  
	  public void setSystem(String system) {
	    this.system = system;
	  }
	  
	  public String getGranularity() {
	    return this.granularity;
	  }
	  
	  public void setGranularity(String granularity) {
	    this.granularity = granularity;
	  }
	  
	  public String getThreshold() {
	    return this.threshold;
	  }
	  
	  public void setThreshold(String threshold) {
	    this.threshold = threshold;
	  }
	  
	  public int getMinLine() {
	    return this.minLine;
	  }
	  
	  public void setMinLine(int minLine) {
	    this.minLine = minLine;
	  }
	  
	  public int getMaxLines() {
	    return this.maxLines;
	  }
	  
	  public void setMaxLines(int maxLines) {
	    this.maxLines = maxLines;
	  }
	  
	  public int getNpcs() {
	    return this.npcs;
	  }
	  
	  public void setNpcs(int npcs) {
	    this.npcs = npcs;
	  }
	  
	  public int getNclones() {
	    return this.nclones;
	  }
	  
	  public void setNclones(int nclones) {
	    this.nclones = nclones;
	  }
	  
	  public int getNfragments() {
	    return this.nfragments;
	  }
	  
	  public void setNfragments(int nfragments) {
	    this.nfragments = nfragments;
	  }
	  
	  public int getNpairs() {
	    return this.npairs;
	  }
	  
	  public void setNpairs(int npairs) {
	    this.npairs = npairs;
	  }
	  
	  public int getNclasses() {
	    return this.nclasses;
	  }
	  
	  public void setNclasses(int nclasses) {
	    this.nclasses = nclasses;
	  }
	  
	  public long getNcompares() {
	    return this.ncompares;
	  }
	  
	  public void setNcompares(long ncompares) {
	    this.ncompares = ncompares;
	  }
	  
	  public long getCputime() {
	    return this.cputime;
	  }
	  
	  public void setCputime(long cputime) {
	    this.cputime = cputime;
	  }
	  
	  public String toString() {
	    return "" + this.system + "( Threshold " + this.threshold + " Clone Classes:" + this.nclasses;
	  }
	  
	  public Map<Integer, List<Integer>> getHmByGID()
	  {
	    Set set = this.hmByGID.entrySet();
	    Iterator i = set.iterator();
	    Map.Entry me;
	    while (i.hasNext()) {
	      me = (Map.Entry)i.next();
	    }
	    
	    return this.hmByGID;
	  }
	  
	  public Map<Integer, Integer> getHmByPcid()
	  {
	    return this.hmByPCID;
	  }
	  
	  public void print() {
	    System.out.println("System = " + getSystem());
	    System.out.println("Threshold = " + getThreshold());
	    System.out.println("Granularity = " + getGranularity());
	    System.out.println("Num of Clone classes = " + getNclasses());
	    System.out.println("Num of Fragments = " + getNfragments());
	    System.out.println("Num of Clones = " + getNclones());
	    
	    for (int i = 0; i < getList().size(); i++) {
	      ((CloneClass)getList().get(i)).print();
	    }
	  }
	  public static List<CloneClass> getListCloneClass(String cloneClassXml) {

		  File file = new File(cloneClassXml);
		  CloneClassList ccList = new CloneClassList();
		  ccList.initialize(file);
		  ArrayList<CloneClass> lsCloneClass = (ArrayList<CloneClass>)ccList.getList();
		  
		  return lsCloneClass;
	  }
	  public static void transferNicad2Fclones(String nicadXml, String fclonesXml) {
		  nicadXml = "E:/tmp/tom7clones/TOMCAT_7_0_11_functions-clones/TOMCAT_7_0_11_functions-clones-0.3-classes.xml";
		  
		  List<CloneClass> lsCloneClass = getListCloneClass(nicadXml);
		  CloneClass cloneClass;
		  for (int i=0; i < lsCloneClass.size(); i++) {
			  cloneClass = (CloneClass)lsCloneClass.get(i);
			  List<Clone> listClone = cloneClass.getCloneList();
			  for (Clone clone : listClone) {
				  System.out.println(clone.getSourcePath());
			  }			  
		  }	
	  }
	  public static void main(String [] args) {
		  
		  String cloneClassXml = "E:/tmp/tom7clones/TOMCAT_7_0_11_functions-clones/TOMCAT_7_0_11_functions-clones-0.3-classes.xml";
		  
		  List<CloneClass> lsCloneClass = getListCloneClass(cloneClassXml);
		  CloneClass cloneClass;
		  for (int i=0; i < lsCloneClass.size(); i++) {
			  cloneClass = (CloneClass)lsCloneClass.get(i);
			  List<Clone> listClone = cloneClass.getCloneList();
			  for (Clone clone : listClone) {
				  System.out.println(clone.getSourcePath());
			  }			  
		  }
		  //ccList.print();
	  }	  
}
