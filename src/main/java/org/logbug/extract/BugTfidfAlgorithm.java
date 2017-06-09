package org.logbug.extract;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
 
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import bean.BugInformation;
import contain.bug.clonecode.extract.ParserBugInfo;
public class BugTfidfAlgorithm {
	
	//保存所有的bug信息
	private static Map<String, BugInformation> mapBugsList ;
	/**
	 * 文件名保存在list
	 */
	private static List<String> fileList = new ArrayList<String>(); 
	/**
	 * 所有文件tf结果.key:文件名,value:该文件tf
	 */
	private static Map<String, Map<String, Double>> allTfMap = new TreeMap<String, Map<String, Double>>();  
 
	/**
	 * 所有文件分词结果.key:文件名,value:该文件分词统计
	 */
    private static Map<String, Map<String, Integer>> allSegsMap = new TreeMap<String, Map<String, Integer>>(); 
 
    /**
	 * 所有文件分词的idf结果.key:文件名,value:词w在整个文档集合中的逆向文档频率idf (Inverse Document Frequency)，即文档总数n与词w所出现文件数docs(w, D)比值的对数
	 */
    private static Map<String, Double> idfMap = new TreeMap<String, Double>();  
 
    /**
     * 统计包含单词的文档数  key:单词  value:包含该词的文档数
     */
    private static Map<String, Integer> containWordOfAllDocNumberMap=new TreeMap<String, Integer>();
 
    /**
     * 统计单词的TF-IDF
     * key:文件名 value:该文件tf-idf
     */
    private static Map<String, Map<String, Double>> tfIdfMap = new TreeMap<String, Map<String, Double>>();  
 
 
	public static Map<String, BugInformation> getMapBugsList() {
		return mapBugsList;
	}

	public static void setMapBugsList(Map<String, BugInformation> mapBugsList) {
		BugTfidfAlgorithm.mapBugsList = mapBugsList;
	}

	/**
	 * 
	* @Title: readDirs
	* @Description: 递归获取文件
	* @param @param filepath
	* @param @return List<String>
	* @param @throws FileNotFoundException
	* @param @throws IOException    
	* @return List<String>   
	* @throws
	 */
    private static List<String> readDirs(String filepath) throws FileNotFoundException, IOException {  
    	try {  
            File file = new File(filepath);  
            if (!file.isDirectory()) {  
                System.out.println("输入的参数应该为[文件夹名]");  
                System.out.println("filepath: " + file.getAbsolutePath());  
            } else if (file.isDirectory()) {  
                String[] filelist = file.list();  
                for (int i = 0; i < filelist.length; i++) {  
                    File readfile = new File(filepath + File.separator + filelist[i]);  
                    if (!readfile.isDirectory()) {  
                        fileList.add(readfile.getAbsolutePath());  
                    } else if (readfile.isDirectory()) {  
                        readDirs(filepath + File.separator + filelist[i]);  
                    }  
                }  
            }  
 
        } catch (FileNotFoundException e) {  
            e.printStackTrace();
        }  
        return fileList;  
    }
 
    /**
     * 
    * @Title: readFile
    * @Description: 读取文件转化成string
    * @param @param file
    * @param @return String
    * @param @throws FileNotFoundException
    * @param @throws IOException    
    * @return String   
    * @throws
     */
    private static String readFile(String file) throws FileNotFoundException, IOException {  
        StringBuffer sb = new StringBuffer();  
        InputStreamReader is = new InputStreamReader(new FileInputStream(file), "UTF-8");  
        BufferedReader br = new BufferedReader(is);  
        String line = br.readLine();  
        while (line != null) {  
            sb.append(line).append("\r\n");  
            line = br.readLine();  
        }  
        br.close();  
        return sb.toString();  
    }  
 
 
    /**
     * 
    * @Title: segString
    * @Description: 用ik进行字符串分词,统计各个词出现的次数
    * @param @param content
    * @param @return  Map<String, Integer>  
    * @return Map<String,Integer>   
    * @throws
     */
    private static Map<String, Integer> segString(String content){
        // 分词
        Reader input = new StringReader(content);
        // 智能分词关闭（对分词的精度影响很大）
        IKSegmenter iks = new IKSegmenter(input, true);
        Lexeme lexeme = null;
        Map<String, Integer> words = new TreeMap<String, Integer>();
        try {
            while ((lexeme = iks.next()) != null) {
                if (words.containsKey(lexeme.getLexemeText())) {
                    words.put(lexeme.getLexemeText(), words.get(lexeme.getLexemeText()) + 1);
                } else {
                    words.put(lexeme.getLexemeText(), 1);
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        return words;
    }
 
    /**
     * 
    * @Title: segStr
    * @Description: 返回LinkedHashMap的分词
    * @param @param content
    * @param @return    
    * @return Map<String,Integer>   
    * @throws
     */
    public static Map<String, Integer> segStr(String content){
        // 分词
        Reader input = new StringReader(content);
        // 智能分词关闭（对分词的精度影响很大）
        IKSegmenter iks = new IKSegmenter(input, true);
        Lexeme lexeme = null;
        Map<String, Integer> words = new LinkedHashMap<String, Integer>();
        try {
            while ((lexeme = iks.next()) != null) {
                if (words.containsKey(lexeme.getLexemeText())) {
                    words.put(lexeme.getLexemeText(), words.get(lexeme.getLexemeText()) + 1);
                } else {
                    words.put(lexeme.getLexemeText(), 1);
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        return words;
    }
 
    public static Map<String, Integer> getMostFrequentWords(int num,Map<String, Integer> words){
 
        Map<String, Integer> keywords = new LinkedHashMap<String, Integer>();
        int count=0;
        // 词频统计
        List<Map.Entry<String, Integer>> info = new ArrayList<Map.Entry<String, Integer>>(words.entrySet());
        Collections.sort(info, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
                return obj2.getValue() - obj1.getValue();
            }
        });
 
        // 高频词输出
        for (int j = 0; j < info.size(); j++) {
            // 词-->频
            if(info.get(j).getKey().length()>1){
                if(num>count){
                    keywords.put(info.get(j).getKey(), info.get(j).getValue());
                    count++;
                }else{
                    break;
                }
            }
        }
        return keywords;
    }
 
    /**
     * 
    * @Title: tf
    * @Description: 分词结果转化为tf,公式为:tf(w,d) = count(w, d) / size(d)
    * 即词w在文档d中出现次数count(w, d)和文档d中总词数size(d)的比值
    * @param @param segWordsResult
    * @param @return    
    * @return TreeMap<String,Double>   
    * @throws
     */
    private static TreeMap<String, Double> tf(Map<String, Integer> segWordsResult) { 
 
        TreeMap<String, Double> tf = new TreeMap<String, Double>();// 正规化  
        if(segWordsResult==null || segWordsResult.size()==0){
    		return tf;
    	}
        Double size=Double.valueOf(segWordsResult.size());
        Set<String> keys=segWordsResult.keySet();
        for(String key: keys){
        	Integer value=segWordsResult.get(key);
        	tf.put(key, Double.valueOf(value)/size);
        }
        return tf;  
    }  
 
    /**
     * 
    * @Title: allTf
    * @Description: 得到所有bug的summary信息
    * @param @param log full path
    * @param @return Map<String, Map<String, Double>>
    * @return Map<String,Map<String,Double>>   
    * @throws
     */
    public static Map<String, Map<String, Double>> allTf(String filePath){
		
		ParserBugInfo rFile = new ParserBugInfo();
		// <bugid,bug_Description>
		mapBugsList = rFile.getMapBugsList(filePath);	//mapBugsList是类的静态成员变量，保存所有的bug信息	
		
		for(String bugid : mapBugsList.keySet())
		{
			BugInformation bugInfo = mapBugsList.get(bugid);
			System.out.println(bugid);
			String content= bugInfo.getBugSummary();
			Map<String, Integer> segs=segString(content);
			allSegsMap.put(bugid, segs);
			allTfMap.put(bugid, tf(segs));
		}
    	return allTfMap;
    }
 
    /**
     * 
    * @Title: wordSegCount
    * @Description: 返回分词结果,以LinkedHashMap保存
    * @param @param dir
    * @param @return    
    * @return Map<String,Map<String,Integer>>   
    * @throws
     */
    public static Map<String, Map<String, Integer>> wordSegCount(String dir){
    	try{
    		fileList=readDirs(dir);
    		for(String filePath : fileList){
    			String content=readFile(filePath);
    			Map<String, Integer> segs=segStr(content);
  			    allSegsMap.put(filePath, segs);
    		}
    	}catch(FileNotFoundException ffe){
    		ffe.printStackTrace();
    	}catch(IOException io){
    		io.printStackTrace();
    	}
    	return allSegsMap;
    }
 
 
    /**
     * 
    * @Title: containWordOfAllDocNumber
    * @Description: 统计包含单词的文档数  key:单词  value:包含该词的文档数
    * @param @param allSegsMap
    * @param @return    
    * @return Map<String,Integer>   
    * @throws
     */
    private static Map<String, Integer> containWordOfAllDocNumber(Map<String, Map<String, Integer>> allSegsMap){
    	if(allSegsMap==null || allSegsMap.size()==0){
    		return containWordOfAllDocNumberMap;
    	}
 
    	Set<String> fileList=allSegsMap.keySet();
    	for(String filePath: fileList){
    		Map<String, Integer> fileSegs=allSegsMap.get(filePath);
    		//获取该文件分词为空或为0,进行下一个文件
    		if(fileSegs==null || fileSegs.size()==0){
    			continue;
    		}
    		//统计每个分词的idf
    		Set<String> segs=fileSegs.keySet();
    		for(String seg : segs){
    			if (containWordOfAllDocNumberMap.containsKey(seg)) {
    				containWordOfAllDocNumberMap.put(seg, containWordOfAllDocNumberMap.get(seg) + 1);
                } else {
                	containWordOfAllDocNumberMap.put(seg, 1);
                }
    		}
 
    	}
    	return containWordOfAllDocNumberMap;
    }
 
    /**
     * 
    * @Title: idf
    * @Description: idf = log(n / docs(w, D)) 
    * @param @param containWordOfAllDocNumberMap
    * @param @return Map<String, Double> 
    * @return Map<String,Double>   
    * @throws
     */
    public static Map<String, Double> idf(Map<String, Map<String, Integer>> allSegsMap){
    	if(allSegsMap==null || allSegsMap.size()==0){
    		return idfMap;
    	}
    	containWordOfAllDocNumberMap=containWordOfAllDocNumber(allSegsMap);
    	Set<String> words=containWordOfAllDocNumberMap.keySet();
    	Double wordSize=Double.valueOf(containWordOfAllDocNumberMap.size());
    	for(String word: words){
    		Double number=Double.valueOf(containWordOfAllDocNumberMap.get(word));
    		idfMap.put(word, Math.log(wordSize/(number+1.0d)));
    	}
    	return idfMap;
    }
 
    /**
     * 
    * @Title: tfIdf
    * @Description: tf-idf
    * @param @param tf,idf
    * @return Map<String, Map<String, Double>>   
    * @throws
     */
    public static Map<String, Map<String, Double>> tfIdf(Map<String, Map<String, Double>> allTfMap,Map<String, Double> idf){
 
    	Set<String> fileList=allTfMap.keySet();
     	for(String filePath : fileList){
    		Map<String, Double> tfMap=allTfMap.get(filePath);
    		Map<String, Double> docTfIdf=new TreeMap<String,Double>();
    		Set<String> words=tfMap.keySet();
    		for(String word: words){
    			Double tfValue=Double.valueOf(tfMap.get(word));
        		Double idfValue=idf.get(word);
        		docTfIdf.put(word, tfValue*idfValue);
    		}
    		tfIdfMap.put(filePath, docTfIdf);
    	}
    	return tfIdfMap;
    }
 
    
    /***只需要计算TF的值就够用的,因为bug描述和日志描述的文本量太小**/
    public static Map<String, Map<String, Double>> getBugTfidfMap(String fileName){    	
		
		//String fileName = "e:/tmp/Tomcat7bugs_Info.txt";
    	
    	System.out.println("tf--------------------------------------");
    	Map<String, Map<String, Double>> allTfMap=BugTfidfAlgorithm.allTf(fileName);
//    	Set<String> revisionList=allTfMap.keySet();
//      	for(String bugid : revisionList){
//      		System.out.println("filePath:" + bugid);
//     		Map<String, Double> tfMap=allTfMap.get(bugid);
//     		Set<String> words=tfMap.keySet();
//     		for(String word: words){
//     			System.out.println("bugid:"+bugid+"     word:"+word+"      tf:"+tfMap.get(word));
//     		}
//     	}
      	return allTfMap;
      	/*
      	System.out.println("idf--------------------------------------");
    	Map<String, Double> idfMap=BugTfidfAlgorithm.idf(allSegsMap);
    	Set<String> words=idfMap.keySet();
      	for(String word : words){
     		System.out.println("word:"+word+"     tf:"+idfMap.get(word));
     	}
 
      	System.out.println("tf-idf--------------------------------------");
      	Map<String, Map<String, Double>> tfIdfMap=BugTfidfAlgorithm.tfIdf(allTfMap, idfMap);
      	Set<String> bugs=tfIdfMap.keySet();
      	for(String bugid : bugs){
      		Map<String, Double> tfIdf=tfIdfMap.get(bugid);
    		Set<String> segs=tfIdf.keySet();
    		for(String word: segs){
    			System.out.println("bugid:"+bugid+"     word:"+word+"        tf-idf:"+tfIdf.get(word));
    		}
      	}*/
      	//return tfIdfMap;
    }
    
    //23:42
    public static void main(String[] args){    	
		
		String fileName = "e:/tmp/lianxi.txt";
    	
    	System.out.println("tf--------------------------------------");
    	Map<String, Map<String, Double>> allTfMap=BugTfidfAlgorithm.allTf(fileName);
    	Set<String> revisionList=allTfMap.keySet();
      	for(String bugid : revisionList){
      		System.out.println("filePath:" + bugid);
     		Map<String, Double> tfMap=allTfMap.get(bugid);
     		Set<String> words=tfMap.keySet();
     		for(String word: words){
     			System.out.println("bugid:"+bugid+"     word:"+word+"      tf:"+tfMap.get(word));
     		}
     	}
 
      	System.out.println("idf--------------------------------------");
    	Map<String, Double> idfMap=BugTfidfAlgorithm.idf(allSegsMap);
    	Set<String> words=idfMap.keySet();
      	for(String word : words){
     		System.out.println("word:"+word+"     tf:"+idfMap.get(word));
     	}
 
      	System.out.println("tf-idf--------------------------------------");
      	Map<String, Map<String, Double>> tfIdfMap=BugTfidfAlgorithm.tfIdf(allTfMap, idfMap);
      	Set<String> bugs=tfIdfMap.keySet();
      	for(String bugid : bugs){
      		Map<String, Double> tfIdf=tfIdfMap.get(bugid);
    		Set<String> segs=tfIdf.keySet();
    		for(String word: segs){
    			System.out.println("bugid:"+bugid+"     word:"+word+"        tf-idf:"+tfIdf.get(word));
    		}
      	}
    }

}
