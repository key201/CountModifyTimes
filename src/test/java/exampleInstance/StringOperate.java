package exampleInstance;


import java.util.List;
import java.util.Map;
import java.util.regex.*;

import bean.FileNameManage;
import contain.bug.clonecode.extract.FileOperate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class StringOperate {
	
	public static void subStringTest() {
	//str＝str.substring(int beginIndex);截取掉str从首字母起长度为beginIndex的字符串，将剩余字符串赋值给str；	
		//新文件的存放路径
		String driver="e:/";
		//源代码的存放路径
		String origDriver = "C:/";
		String savePath = "";
		String detectPath = "/home/jie/codeclones/tomcat7x/TOMCAT_7_0_11/java/org/apache/coyote/http11/Http11Processor.java";
		String baseDirName = detectPath.substring(FileNameManage.getCloneDetectPath().length());
		//结果为:java/org/apache/coyote/http11/Http11Processor.java
		System.out.println(baseDirName);
		
		String [] fullPath = detectPath.split("/");
	}
	
	
public static void main(String[] args) {
	
	subStringTest() ;


}
}
	

