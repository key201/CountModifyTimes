package contain.bug.clonecode.extract;

import java.io.File;
import java.io.IOException;
import java.util.List;

import bean.Clone;
import bean.FileNameManage;

public class GenerateCloneCode {
	
	//新文件的存放路径
	String driver="e:/";
	//源代码的存放路径
	String origDriver = "C:/";
	public void GenerateCodeFile(Clone cloneCode, String cloneId){
		/*
		String savePath = "";
		String detectPath = cloneCode.getSourcePath();
		String [] fullpath = detectPath.split("/");
		String baseDirName = detectPath.substring(FileNameManage.getCloneDetectPath().length());
		String orifileFullpath = origDriver  +fullpath[4] +"/" +"tags"+ "/" + fullpath[5] + "/" +baseDirName;

		int startNumber = cloneCode.getStratLine();
		int endNumber   = cloneCode.getEndLine();
		
		List<String> listText = FileOperate.ReadSelectedLine(orifileFullpath,  startNumber,  endNumber) ;
		savePath = this.driver + fullpath[4] +"/"+cloneId+"/";
		
		
		//以克隆群为单位提取克隆代码片段文件
		String saveFilePath = savePath + fullpath[fullpath.length-1];
		mkdir(savePath);
		FileOperate.writeTextFile(saveFilePath, listText);*/
		
		return ;
		}
	public void mkdir(String path) {  
	    File fd = null;  
	    try {  
	        fd = new File(path);  
	        if (!fd.exists()) {  
	            fd.mkdirs();  
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        fd = null;  
	    }  
	}  
	public static void main(String [] args)  throws IOException {
//		Clone cloneCode = new Clone();
//		
//		GenerateCloneCode file = new GenerateCloneCode();
//		file.GenerateCodeFile(cloneCode, cloneId);
	}
}