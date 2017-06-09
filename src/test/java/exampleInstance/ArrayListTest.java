package exampleInstance;


import java.util.List;
import java.util.Map;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ArrayListTest {
	
//集合Set的使用	
public static void testSet()
{
	Set set = new HashSet();
	String s1 = new String("Hello");
	String s2=s1;
	String s3 = new String("world");
	
	set.add(s1);
	set.add(s2);
	set.add(s3);
	
	System.out.println("集合中对象的数目:"+set.size());//打印集合中对象的数目
	
	//Set的add()方法是如何判断对象是否已经存放在集合中？
	String newStr = "hel";
	boolean isExists=false;
	
	Iterator<String> it = set.iterator();
	while(it.hasNext())
	{
		String oldStr = (String)it.next();
		if(newStr.equals(oldStr))
		{
			isExists=true;
		}
		System.out.println(oldStr);
	}
	System.out.println(isExists);
}

//
public static void testList()
{
	List<String> listTest = new ArrayList<String>();
	listTest.add("a");
	listTest.add("c");
	listTest.add("d");
	listTest.add("c");
	
	System.out.println(listTest.size());
	
	for(Iterator<String> iter = listTest.iterator();iter.hasNext();)
	{
		System.out.println(iter.next());
	}	
}

public static void testListZa()
{
	List<Object> listObj = new ArrayList<Object>();
	listObj.add("add");
	listObj.add("sub");
	listObj.add("test");


	for(Iterator<Object> iterator=listObj.iterator();iterator.hasNext();)
	{
		String strText = (String)iterator.next();
		System.out.println(strText);
	}

	Map<String, String> map0 = new HashMap<String,String>();
	map0.put("name", "zhang");
	map0.put("island", "dao");
	map0.put("metrics","guige");
	map0.put("breaks","daduan");
	map0.put("isomorphic", "tongxingde");

	String strName = map0.get("name");
	String strIslang = map0.get("island");
	System.out.println(strName+strIslang);


	List<Map<String, Object>> listObjs = new ArrayList<Map<String,Object>>();
	Map<String, Object> map1 = new HashMap<String, Object>();
	map1.put("tackled","解决");
	map1.put("nutshell","总而言之");
	map1.put("discourse","论述");
	listObjs.add(map1);
	
	Map<String, Object> map2 = new HashMap<String, Object>();
	map2.put("explicity", "明白的明确的");
	map2.put("island", "岛屿");
	map2.put("vary form", "不同");
	map2.put("analyse","分析研究");
	listObjs.add(map2);
	
	Map<String, Object> map3 = new HashMap<String, Object>();
	map3.put("artifacts", "人工环境");
	map3.put("paradigms", "范例样例模范");
	listObjs.add(map3);
	
	Map<String, Object> map4 = new HashMap<String, Object>();
	map4.put("match mate marry matched", "匹配");
	map4.put("regular", "有规律的规则不变的");
	map4.put("assign", "分派，分配");
	listObjs.add(map3);
	
	Map<String, Object> map5 = new HashMap<String, Object>();
	map5.put("isomorphic", "同形的，同构的");//iso
	map5.put("analysis", "分析，分解");
	listObjs.add(map5);
	
	for(Map<String, Object> mapObject : listObjs) {
		for(Map.Entry<String, Object>entry : mapObject.entrySet()){
			System.out.println(entry.getKey()+entry.getValue());
		}
	}
}

public static void testTraverList()
{
	ArrayList<String> arrL = new ArrayList<String>();  
    ArrayList<String> arrLTmp1 = new ArrayList<String>();  
    ArrayList<String> arrLTmp2 = new ArrayList<String>();  
    ArrayList<String> arrLTmp3 = new ArrayList<String>();  
    ArrayList<String> arrLTmp4 = new ArrayList<String>();  
    
	for (int i=0;i<10;i++){  
	    arrL.add("第"+i+"个");  
	}  
	
	long t1 = System.nanoTime();  
	//方法1  
	Iterator<String> it = arrL.iterator();  
	while(it.hasNext()){  
	    arrLTmp1.add(it.next());  
	}  
	long t2 = System.nanoTime();  
	//方法2  
	for(Iterator<String> it2 = arrL.iterator();it2.hasNext();){  
	    arrLTmp2.add(it2.next());  
	}  
	long t3 = System.nanoTime();  
	//方法3  
	for (String vv :arrL){  
	    arrLTmp3.add(vv);  
	}  
	long t4 = System.nanoTime();  
	//方法4  
	for(int i=0;i<arrL.size();i++){  
	    arrLTmp4.add(arrL.get(i));  
	}  
	long t5 = System.nanoTime();  
	System.out.println("第一种方法耗时：" + (t2-t1)/1000 + "微秒");  
	System.out.println("第二种方法耗时：" + (t3-t2)/1000 + "微秒");  
	System.out.println("第三种方法耗时：" + (t4-t3)/1000 + "微秒");  
	System.out.println("第四种方法耗时：" + (t5-t4)/1000 + "微秒");  
	      
}

public static void PatterRegular() {
	List<String> lsBugId = new ArrayList<String>();
	Pattern p = Pattern.compile("[0-9]{5}");
	Matcher m = p.matcher("18462 Tomcat7 Catalin");
	while(m.find()) {
		String bugid = m.group() + m.start() + (m.end() -1 );
		lsBugId.add(bugid);
		System.out.println("Match" + m.group() + " at position " + m.start() + "-" + (m.end()-1));
		}
}

public static void TestMapOnly() {
	Map<String, String > mapOnly = new HashMap<String, String>();
	mapOnly.put("111", "zhen111");
	mapOnly.put("222", "zhen222");
	mapOnly.put("333", "zhen333");
	mapOnly.put("111", "zhen111");
	for(String id :mapOnly.keySet()) {
		String strText = String.format("%s---->%s", id, mapOnly.get(id));
		System.out.println(strText);
	}
	System.out.println(mapOnly.size());
}

public static void main(String[] args) {
	
	//集合的测试 一、判断一个字符串str不为空的方法有：
	//  1. str!=null;
	//  2. "".equals(str);
	//  3. str.length()!=0;
	testSet();
	testListZa();	
	//正则表达式的测试
	PatterRegular();
	TestMapOnly();
	}
}
	

