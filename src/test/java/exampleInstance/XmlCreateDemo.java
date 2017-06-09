package exampleInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

//E:\tmp\exercise
//说明：“\p{Space}”匹配空白字符，前面加个反斜线字符，是为了转义。转义后，
//加了个中括号，是分了分组，不加中括号也没事，只是给清楚点，后面的加号是“一次或多次”的意思。
public class XmlCreateDemo {

	public static void main(String[] args) throws IOException {
		readFileByLine();
		TestReadSelectedLine();
	}

	public static void readFileByLine() {
	try {
			Scanner in = new Scanner(new File("C:/temp/Tomcat7bugs_Info.txt"));

			while (in.hasNextLine()) {

				String str = in.nextLine();
				// System.out.println(str);
				splitt(str);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	//按照空格分隔字符串
	public static String[] splitt(String str) {

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
    public static void readFileByRandomAccess(String fileName) {
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
    
    //  读取文件指定行。 
    static List<String> readAppointedLineNumber(File sourceFile, int startNumber, int endNumber) throws IOException {
	 List<String> fileList = new ArrayList<String>();
     FileReader in = new FileReader(sourceFile);
     LineNumberReader reader = new LineNumberReader(in);
     //reader.setLineNumber(lineNumber);
     String fileText = reader.readLine();
     long totalNum = getTotalLines(sourceFile);
     
     if (endNumber < 0 || endNumber > totalNum ) {
         System.out.println("不在文件的行数范围之内。");
     }
     if (startNumber < 0 || startNumber > totalNum ) {
         System.out.println("不在文件的行数范围之内。");
     }
     reader.setLineNumber(startNumber);
     while(reader.getLineNumber()<=endNumber){
     
	     //System.out.println("当前行号为:" + reader.getLineNumber());
	     int currNum = reader.getLineNumber();
	     fileText = reader.readLine();
	     System.out.println("第"+ currNum + "行的内容:" + fileText);
	     fileList.add(fileText);
     }
     reader.close();
     in.close();     
     return fileList;
 }

 // 文件内容的总行数。 
 static int getTotalLines(File file) throws IOException {
     FileReader in = new FileReader(file);
     LineNumberReader reader = new LineNumberReader(in);
     String s = reader.readLine();
     int lines = 0;
     while (s != null) {
         lines++;
         s = reader.readLine();
     }
     reader.close();
     in.close();
     return lines;
 }
  
 public static void TestReadSelectedLine() throws IOException {
      
     // 读取文件 
     File sourceFile = new File("E:/tmp/exercise/Tomcat7bugs_Info.txt");
     // 获取文件的内容的总行数 
     int totalNo = getTotalLines(sourceFile);
     System.out.println("There are "+totalNo+ " lines in the text!");
      
     // 指定读取的行号 
     int lineNumber = 2;      
     // 读取指定的行 
     List<String> lsfileContent = readAppointedLineNumber(sourceFile, 2, 5);   
     for(String fileContent: lsfileContent) {
    	 System.out.println(fileContent);
     }      
 } 
public static void TestWriteXml() {
	
}

	
	
	
}
