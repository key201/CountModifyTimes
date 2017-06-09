package contain.bug.clonecode.extract;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import bean.AnalyticsResult;
import bean.BugInformation;
import bean.LogInformation;

//此类就行用来往磁盘上写文件用的:文件类型为:xml、txt等类型的文件
public class FileOperate {

	String fileFullName;
	
	public FileOperate(String fileFullName) {
		this.fileFullName = fileFullName;
	}
	
	public void readFileByLine() {
		try {

			Scanner in = new Scanner(new File(fileFullName));

			while (in.hasNextLine()) {

				String str = in.nextLine();
				// System.out.println(str);
				splitt(str);
			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

	// 按照空格分隔字符串
	public String[] splitt(String str) {

		String strr = str.trim();

		String[] abc = strr.split("[\\p{Space}]+");

		for (int i = 0; i < abc.length; i++) {
			System.out.println(abc[i]);
		}
		return abc;
	}

	/**
	 * 随机读取文件内容
	 */
	public void readFileByRandomAccess(String fileName) {
		RandomAccessFile randomFile = null;
		try {
			System.out.println("随机读取一段文件内容：");
			// 打开一个随机访问文件流，按只读方式
			randomFile = new RandomAccessFile(fileName, "r");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 读文件的起始位置
			int beginIndex = (fileLength > 4) ? 4 : 0;
			// 将读文件的开始位置移到beginIndex位置。
			randomFile.seek(beginIndex);
			byte[] bytes = new byte[10];
			int byteread = 0;
			// 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
			// 将一次读取的字节数赋给byteread
			while ((byteread = randomFile.read(bytes)) != -1) {
				System.out.write(bytes, 0, byteread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	// 读取文件指定行。
	public static List<String>  readAppointedLineNumber(File sourceFile, int startNumber, int endNumber) {
		List<String> fileList = new ArrayList<String>();
		FileReader in = null;
		LineNumberReader reader = null;

		try {
		 in = new FileReader(sourceFile);
		 reader = new LineNumberReader(in);
		 // reader.setLineNumber(lineNumber);
		String fileText = reader.readLine();
		
		long totalNum = getTotalLines(sourceFile);
		
		if (endNumber < 0 || endNumber > totalNum) {
			System.out.println("不在文件的行数范围之内。");
		}
		if (startNumber < 0 || startNumber > totalNum) {
			System.out.println("不在文件的行数范围之内。");
		}
		while ( reader.getLineNumber() < startNumber-1 ){
			fileText = reader.readLine();
		}
		reader.setLineNumber(startNumber);
		while (reader.getLineNumber() <= endNumber) {
//			System.out.println("当前行号为:" + reader.getLineNumber());
//			int currNum = reader.getLineNumber();
			fileText = reader.readLine();
//			System.out.println("第" + currNum + "行的内容:" + fileText);
			
			fileList.add(fileText);
		}
			}catch (FileNotFoundException e) {
				 e.printStackTrace();
			 }catch (IOException e) {
			  e.printStackTrace();
			 }finally{
			  try {
				  reader.close();
				  in.close();
			  } catch (IOException e) {
			 	 e.printStackTrace();
			  }
		}	
		
		return fileList;
	}

	// 文件内容的总行数。
	public static int getTotalLines(File file) {
		
		
		int lines = 0;
		FileReader in = null;
		LineNumberReader reader = null ;
		try {
			in = new FileReader(file);
			reader= new LineNumberReader(in);
			String s = reader.readLine();
	
			while (s != null) {
				lines++;
				s = reader.readLine();
			}	

			}catch (FileNotFoundException e) {
				 e.printStackTrace();
			 }catch (IOException e) {
			  e.printStackTrace();
			 }finally{
			  try {
				  reader.close();
				  in.close();
			  } catch (IOException e) {
			 	 e.printStackTrace();
			  }
			}	
		
		return lines;
	}
	
	//读取文件的指定的行
	public static List<String>  ReadSelectedLine(String fileFullPath, int startNumber, int endNumber)  {
		// 读取文件
		File sourceFile = new File(fileFullPath);
		// 获取文件的内容的总行数
		int totalNo = getTotalLines(sourceFile);
		//System.out.println("There are " + totalNo + " lines in the text!");

		// 指定读取的行号
		//int lineNumber = 2;
		// 读取指定的行
		List<String> lsfileContent = readAppointedLineNumber(sourceFile, startNumber, endNumber);
//		for (String fileContent : lsfileContent) {
//			System.out.println(fileContent);
//		}
		return lsfileContent;
	}
	
public static void writeTextFile(String fileName, List<String> listFileText) {
 
		File file = new File(fileName);
		//boolean fileExist = file.exists();
		boolean fileExist = false;
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			 fw = new FileWriter(file, fileExist);
			 writer = new BufferedWriter(fw);
			 //writer.write("vision    bugId       bugSummary						                                                    				logSummary\r\n\r\n");
			 
			 for(String fileContent : listFileText)
			 {				
//				System.out.println(fileContent);
				writer.write(fileContent);
				 writer.newLine();//换行
				 writer.flush();
			 } 

		}catch (FileNotFoundException e) {
				 e.printStackTrace();
			 }catch (IOException e) {
			  e.printStackTrace();
			 }finally{
			  try {
			 	 writer.close();
			 	 fw.close();
			  } catch (IOException e) {
			 	 e.printStackTrace();
			  }
			 }
			//BugInformation bugInformation = mapBugsList.get(keyBugid);
	}

	public static void main(String[] args) throws IOException {
		//readFileByLine();
		//TestReadSelectedLine();
		FileOperate.ReadSelectedLine("E:/tmp/result.txt", 10, 12);
	}
}
