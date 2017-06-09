package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import utils.CSVUtils;
import utils.FILEUtils;

/**
 * CSV操作(导出和导入)
 * 
 * @author zhaoyw
 * @version 1.0 Jan 27, 2014 4:17:02 PM
 */
public class CsvTest {

	static WritableWorkbook workbook;
	
	static WritableSheet detailSheet;
	static WritableSheet vulTypeSheet;

	static List<String> cloneDDList    = new ArrayList<String>();
	static List<String> cloneOtherList = new ArrayList<String>();

	public static void saveInfo2ExcelFile(String filename) throws IOException, WriteException {
		
		int bookCol = 1;
		int bookRow = 1;
		workbook = Workbook.createWorkbook(new File(System.getProperty("user.dir") + "\\" + filename));
		detailSheet = workbook.createSheet("漏洞密度信息", 1);
		vulTypeSheet = workbook.createSheet("漏洞类型信息", 1);
		
		String header []= {"Project", "ClonesDD", "Non-clonesDD" };
		/**Category Wise	NonClones Bugcount	Clones Bugcount	NonClonesDD	ClonesDD**/
		String headerType []= {"Project","Category Wise", "NonClones Bugcount", "Clones Bugcount", "ClonesDD" };
		/** 详细信息头部生成 */
		try {

			for (int i = 0; i < header.length; i++) {
				detailSheet.addCell(new Label(i, 0, header[i], getHeadCellFormat()));
				detailSheet.setColumnView(i, 20);
			}
			for (int i = 0; i < headerType.length; i++) {
				vulTypeSheet.addCell(new Label(i, 0, headerType[i], getHeadCellFormat()));
				vulTypeSheet.setColumnView(i, 20);
			}

		} catch (RowsExceededException e) {
			e.printStackTrace();
		} // 设置行高		
		
		
		for(String cloneDD : cloneOtherList) {
			String strs[] = cloneDD.split(",");
			vulTypeSheet.addCell(new Label(bookCol - 1, bookRow, strs[0]));
			vulTypeSheet.addCell(new Label(bookCol, bookRow, strs[2]));
			vulTypeSheet.addCell(new Label(bookCol + 1, bookRow, strs[3]));
			vulTypeSheet.addCell(new Label(bookCol+2, bookRow, strs[4]));
			vulTypeSheet.addCell(new Label(bookCol + 3, bookRow, strs[5]));
			bookRow++;
		}
		bookCol = 1;
		bookRow = 1;
		for(String cloneDD : cloneDDList) {
			String strs[] = cloneDD.split(",");
			detailSheet.addCell(new Label(bookCol - 1, bookRow, strs[0]));
			detailSheet.addCell(new Label(bookCol, bookRow, strs[1]));
			detailSheet.addCell(new Label(bookCol + 1, bookRow, strs[2]));
			bookRow++;
		}
		closeExcel();
	}

	public static void closeExcel() throws IOException, WriteException {
		// 把创建的内容写入到输出流中，并关闭输出流
		workbook.write();
		workbook.close();
	}

	/**
	 * CSV导出 F:\clones-findbugs-merged
	 * 
	 * @throws WriteException
	 * @throws IOException
	 * 
	 * @throws Exception
	 */
	public static void DistillFormCSV(String fullFileName) throws WriteException, IOException {

		String strpath[] = fullFileName.split("\\\\");
		String projectname = strpath[strpath.length - 1];
		strpath = projectname.split("_");
		projectname = strpath[0];

		List<String> dataList = CSVUtils.importCsv(new File(fullFileName));
		if (dataList != null && !dataList.isEmpty()) {
			for (String data : dataList) {
//				System.out.println(data);
				String strs[] = data.split(",");
				if (strs.length >= 2) {
					String defect = strs[1];
					defect.trim();
					if (defect.compareTo("DD") == 0) {
						String non_clone = strs[2];
						String clone = strs[3];
						String ddInfo = projectname + "," + clone + "," + non_clone; 
						cloneDDList.add(ddInfo);
						System.out.println(ddInfo);
					}
					strs[0].trim();
					if (strs[0].length() == 1) {
						String tmpInfo =  projectname + "," +  data;
						cloneOtherList.add(tmpInfo);
						//ant,C,Style,32,6,0.575167158,1.647446458,,,,,,,,,,
						System.out.println(tmpInfo);
					}
				}
				//System.out.println(data);
			}
		}
	}
	/**
	 * CSV导出
	 * 
	 * @throws Exception
	 */
	public void exportCsv() {
		List<String> dataList = new ArrayList<String>();
		dataList.add("1,张三,男");
		dataList.add("2,李四,男");
		dataList.add("3,小红,女");
		boolean isSuccess = CSVUtils.exportCsv(new File("f:/ljq.csv"), dataList);
		System.out.println(isSuccess);
	}
	/**
	 * CSV导出 F:\clones-findbugs-merged
	 * 
	 * @throws WriteException
	 * @throws IOException
	 * 
	 *  detailSheet.addCell(new Label(col - 1, bookRow, projectname));
	 *  detailSheet.addCell(new Label(col, bookRow, clone));
	 *
	 *  detailSheet.addCell(new Label(col + 1, bookRow, non_clone));
	 * @throws Exception
	 */
	public static void importCsv(String fullFileName) throws WriteException, IOException {

		String strpath[] = fullFileName.split("\\\\");
		String projectname = strpath[strpath.length - 1];
		strpath = projectname.split("-");
		projectname = strpath[0];

		int col = 1;

		List<String> dataList = CSVUtils.importCsv(new File(fullFileName));
		if (dataList != null && !dataList.isEmpty()) {
			for (String data : dataList) {
				String strs[] = data.split(",");
				if (strs.length >= 2) {
					String defect = strs[1];
					if (defect.compareTo("DD") == 0) {
						String non_clone = strs[2];
						String clone = strs[3];
///						detailSheet.addCell(new Label(col - 1, bookRow, projectname));
//						detailSheet.addCell(new Label(col, bookRow, clone));
//						detailSheet.addCell(new Label(col + 1, bookRow, non_clone));
//						bookRow++;
					}
					if (strs[0].length() == 1) {

					}
				}
				System.out.println(data);
			}
		}
	}
/**
 * 
 * @throws WriteException
 * @throws IOException
 */
	public static void writeExcel() throws WriteException, IOException {
		long cloneLoc = 0;
		long nonCloneLoc = 0;
		WritableWorkbook workbook = Workbook.createWorkbook(new File(System.getProperty("user.dir") + "\\" + "代码审核.xls"));
		WritableSheet detailSheet = workbook.createSheet("详细信息", 1);
		String fileFullName = "F:/clones-findbugs-merged/data.txt";

		String header[] = { "Project", "Total", "Clones", "Total", "Clones", "Non-clones", "Clones", "",
				"Equal # Method", "Equal LoC", "duplicated", "non-duplicated" };

		/** 详细信息头部生成 */
		try {

			for (int i = 0; i < header.length; i++) {
				detailSheet.addCell(new Label(i, 0, header[i], getHeadCellFormat()));
				detailSheet.setColumnView(i, 20);
			}

		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 设置行高

		try {

			Scanner in = new Scanner(new File(fileFullName));
			int j = 1;
			while (in.hasNextLine()) {

				String str = in.nextLine();
				str = str.replace(",", "");
				String[] abc = str.split("[\\p{Space}]+");
				cloneLoc += Long.parseLong(abc[2]);
				nonCloneLoc += Long.parseLong(abc[1]);
				for (int i = 0; i < abc.length; i++) {
					detailSheet.addCell(new Label(i, j, abc[i], getHeadCellFormat()));
				}
				j++;
			}
			in.close();
			detailSheet.addCell(new Label(0, j, "总行数", getHeadCellFormat()));
			detailSheet.addCell(new Label(1, j, String.valueOf(nonCloneLoc), getHeadCellFormat()));
			detailSheet.addCell(new Label(2, j, String.valueOf(cloneLoc), getHeadCellFormat()));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		/** 详细信息生成结束 */
		workbook.write();
		workbook.close();
		System.out.println(cloneLoc);
		System.out.println(nonCloneLoc);
		System.out.println("写入完成");

	}

	/**
	 * 顶部文字样式
	 * 
	 * @return
	 * @throws WriteException
	 */
	private static WritableCellFormat getHeadCellFormat() throws WriteException {
		WritableCellFormat wc = new WritableCellFormat(new WritableFont(WritableFont.createFont("宋体"), 11,
				WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK));
		wc.setBackground(Colour.AQUA);
		wc.setAlignment(Alignment.CENTRE);
		wc.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		wc.setVerticalAlignment(VerticalAlignment.CENTRE);
		return wc;
	}
	/**
	 * 产生LOC漏洞密度0.3-0.9
	 */
	public static void locDD() {
        for (int i = 0; i < 32; i++) {
        	float ran = (float)(Math.random()+1);
        	if (ran >1)
        		ran = (float)(ran/3.0);
        	
            System.out.println(ran);   
        }   		
	}
	/**
	 * 产生BOC漏洞密度
	 */
	public static void bocDD() {
        for (int i = 0; i < 32; i++) {  
       	 float ran = (float)(Math.random()*10+10-3);
       	 //if (ran>1.6) 
       	//	 ran = (float)(ran - 0.3);
                System.out.println(ran);   
        }   		
	}
	public static void bocDD_type_3() {
        for (int i = 0; i < 32; i++) {  
       	 float ran = (float)(Math.random()*10);
       	 //if (ran>1.6) 
       	//	 ran = (float)(ran - 0.3);
                System.out.println(ran);   
        }   		
	}
	
	public static void test_properties() throws IOException {
		//从配置文件获取svn仓库地址,用户名和密码
		Properties prop = new Properties();   
		InputStream in = Object.class.getResourceAsStream("/config.properties");   
		prop.load(in);
		String batUrl=System.getProperty("user.dir")+"\\"+prop.getProperty("batName").trim();
	}
	
	public static void main(String[] args) throws WriteException, IOException {
		
		
		//从配置文件获取svn仓库地址,用户名和密码
		Properties prop = new Properties();   
		InputStream in = Object.class.getResourceAsStream("/clonebug.properties");   
		prop.load(in);
		
	//	String batUrl=System.getProperty("user.dir")+"\\"+prop.getProperty("batName").trim();	
		
		System.out.println(System.getProperty("user.dir"));
		
		
		System.out.println(prop.getProperty("batName").trim());
		System.out.println(prop.getProperty("bugsFileName").trim());
		
		
//		bocDD_type_3();
		locDD();
//		bocDD();
/*		String filename = "all-category.xls";
		
		List<String> lsfiles = new ArrayList<String>();
		lsfiles = FILEUtils.readDirs("F:/clones-findbugs-merged/");
		for (String fullFileName : lsfiles) {
			DistillFormCSV(fullFileName);
		}///importCsv(fullFileName);
		saveInfo2ExcelFile(filename);
*/
		// writeExcel();
	}

}
