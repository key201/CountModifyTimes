package bean;


/**
 * 本类主要是用来存放运行代码的路径信息的
 * @author zhaoyw
 *bugsFileName:此文件是从网上dump下来的bugs,然后保存到本地磁盘中
 *commlogXmlFile:使用svn命令：svn log --verbose --xml >1.xml获取开发者修复bug的日志
 *cloneClassXml:对项tomcat7使用nicad进行克隆检测，产生的结果
 *cloneDetectPath：在进克隆代码与bugid进行映射时需要比较修改日志中的文件路径与克隆代码路径是否
 *相同，所以需要把前缀去除掉,只比较有用的部分。
 *commitPath:修改日志中的前缀也需要去除的为了映射的需要
 *
 */
public class FileNameManage {	
	
	private static String bugsFileName ;
	private static String commlogXmlFile ;
	//software_functions-clones-0.3-classes.xml
	//private static String cloneClassXml  = "e:/tmp/TOMCAT_7_0_11_functions-clones-0.3-classes.xml";
	private static String cloneClassXml ;
	//使用克隆检测工具检测结果的路径
	//private static String cloneDetectPath = "/home/jie/codeclones/tomcat7x/TOMCAT_7_0_11/";
	private static String cloneDetectPath;
	private static String commitPath;
//	private static String computePath ;
/*
	private static String bugsFileName   ="e:/tmp/Tomcat7bugs_Info.txt" ;
	private static String commlogXmlFile = "e:/tmp/Tomcat7reviselog.xml";
	//software_functions-clones-0.3-classes.xml
	//private static String cloneClassXml  = "e:/tmp/TOMCAT_7_0_11_functions-clones-0.3-classes.xml";
	private static String cloneClassXml  = "e:/tmp/software_functions-clones-0.3-classes.xml";
	//使用克隆检测工具检测结果的路径
	//private static String cloneDetectPath = "/home/jie/codeclones/tomcat7x/TOMCAT_7_0_11/";
	private static String cloneDetectPath = "/home/zhaoyw/software/tomcat/trunk/";
	private static String commitPath = "/tomcat/tc7.0.x/trunk/";*/
	private static String computePath = "F:/workplace/detected_src/lunwenPro/tomcat7x";
	
	public static void setBugsFileName(String bugsFileName) {
		FileNameManage.bugsFileName = bugsFileName;
	}

	public static void setCommlogXmlFile(String commlogXmlFile) {
		FileNameManage.commlogXmlFile = commlogXmlFile;
	}

	public static void setCloneClassXml(String cloneClassXml) {
		FileNameManage.cloneClassXml = cloneClassXml;
	}
	public static String getComputePath() {
		return computePath;
	}

	public static void setComputePath(String computePath) {
		FileNameManage.computePath = computePath;
	}

	public static String getBugsFileName() {
		return bugsFileName;
	}
	
	public static String getCommlogXmlFile() {
		return commlogXmlFile;
	}
	
	public static String getCloneClassXml() {
		return cloneClassXml;
	}

	public static String getCloneDetectPath() {
		return cloneDetectPath;
	}

	public static void setCloneDetectPath(String cloneDetectPath) {
		FileNameManage.cloneDetectPath = cloneDetectPath;
	}

	public static String getCommitPath() {
		return commitPath;
	}

	public static void setCommitPath(String commitPath) {
		FileNameManage.commitPath = commitPath;
	}
}
