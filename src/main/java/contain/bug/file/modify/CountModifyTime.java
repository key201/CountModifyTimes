package contain.bug.file.modify;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collection;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;

import com.zouhao.util.SVNUtil;

import bean.AnalyticsResult;
import bean.Clone;
import bean.FileNameManage;
import bean.LogInformation;
import contain.bug.clonecode.extract.BugClone2XmlFileMain;
import contain.bug.clonecode.extract.ReviseBugCloneCodeList;

public class CountModifyTime {
	
	private static String batUrl;//批处理文件路径

	private static void setConfig(){
		try {   
			//从配置文件获取svn仓库地址,用户名和密码
			Properties prop = new Properties();   
			InputStream in = Object.class.getResourceAsStream("/config.properties");   
			prop.load(in);
			batUrl=System.getProperty("user.dir")+"\\"+prop.getProperty("batName").trim();
		} catch (IOException e) {   
			e.printStackTrace();   
		}
	}

	/**
	 * 顶部文字样式
	 * @return
	 * @throws WriteException
	 */
	private static WritableCellFormat getHeadCellFormat() throws WriteException{
		WritableCellFormat wc = new WritableCellFormat(new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.BOLD,
				false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK));
		wc.setBackground(Colour.AQUA);
		wc.setAlignment(Alignment.CENTRE);
		wc.setBorder(Border.ALL, BorderLineStyle.THIN,Colour.BLACK);
		wc.setVerticalAlignment(VerticalAlignment.CENTRE);
		return wc;
	}
	private static WritableCellFormat getFirstCellFormat() throws WriteException{
		WritableCellFormat wc = new WritableCellFormat(new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD,
				false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK));
		wc.setBackground(Colour.WHITE);
		return wc;
	}

	private static WritableCellFormat getSecondCellFormat() throws WriteException{
		WritableCellFormat wc = new WritableCellFormat(new WritableFont(WritableFont.createFont("宋体"),11,WritableFont.NO_BOLD,
				false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK));
		wc.setBackground(Colour.LIGHT_GREEN);
		return wc;
	}
	
	public static int getCount(String modifyText) {
		String [] text = modifyText.split(" ");
		String number = text[2];
		text = number.split(",");
		int count = Integer.parseInt(text[1]);
		return count;
	}	
	
	public static int computeModifyCount(String cmdExe) throws SVNException, IOException, RowsExceededException, WriteException{
		//svn diff -r 1556808 trunk/test/org/apache/catalina/startup/TestHostConfigAutomaticDeployment.java
		int modifyCount = 0;
		/**计算修改代码行数*/
		StringBuffer exeCmdReturnText = new StringBuffer();
		List<String> listModify = new ArrayList<String>();
		//Process process = Runtime.getRuntime().exec(batUrl+" "+prevStartVision+" "+current+" "+rootPath+" "+rootPath.substring(0,2));
		Process process = Runtime.getRuntime().exec(cmdExe);
		BufferedReader buffeReader = new BufferedReader( new InputStreamReader(process.getInputStream()));
		String readText = "";
		while((readText=buffeReader.readLine())!=null) {
			System.out.println(readText);
			exeCmdReturnText.append(readText);
			if(readText.contains("@@")){
				//统计修改次数
				listModify.add(readText);	
				modifyCount += getCount(readText);
			}
		}
		readText = exeCmdReturnText.toString();
		process.destroy();
		return modifyCount;
		/**计算修改代码行数结束*/
	}
	public static String getComandLine(AnalyticsResult analyResult ){
		
  		Clone bugClone = analyResult.getCloneCode();
  		String exePath = FileNameManage.getComputePath();
  		String [] sourcePath = exePath.split("/");
  		String sourceFullPath = exePath;
  		String fullPathLog = bugClone.getSourcePath();
  		exePath = fullPathLog.substring(fullPathLog.indexOf("trunk"));
  		String revision = analyResult.getLogInfomation().getRevision();
  		System.out.println(exePath);
  		String cmdExe = batUrl+" "+revision + " "+ exePath + " " +sourceFullPath +" " + sourcePath[0];
  		System.out.println(cmdExe);
  		return cmdExe;
	}
	
	public static void main(String arge[]) throws SVNException, IOException, RowsExceededException, WriteException{
		
		setConfig();
		WritableWorkbook workbook = Workbook.createWorkbook(new File(System.getProperty("user.dir")+"\\"+"代码审核.xls"));
		WritableSheet detailSheet = workbook.createSheet("详细信息",1);
		
		/**详细信息头部生成*/
		detailSheet.setRowView(0, 600); //设置行高
		detailSheet.addCell(new Label(0,0,"SVN信息",getHeadCellFormat()));
		detailSheet.addCell(new Label(1,0,"修改代码行数",getHeadCellFormat()));
		detailSheet.addCell(new Label(2,0,"代码质量基数（每次提交基数加1）",getHeadCellFormat()));
		detailSheet.addCell(new Label(3,0,"代码质量评分（合格1；不合格0）",getHeadCellFormat()));
		detailSheet.addCell(new Label(4,0,"注释质量基数（每次提交基数加1）",getHeadCellFormat()));
		detailSheet.addCell(new Label(5,0,"注释质量评分（合格1；不合格0）",getHeadCellFormat()));
		detailSheet.addCell(new Label(6,0,"备注",getHeadCellFormat()));
		
		/**设置第一列的宽度*/
		detailSheet.setColumnView(0,40);
		detailSheet.setColumnView(1,20);
		detailSheet.setColumnView(2,30);
		detailSheet.setColumnView(3,30);
		detailSheet.setColumnView(4,30);
		detailSheet.setColumnView(5,30);
		detailSheet.setColumnView(6,50);
		
		/**详细信息头部生成结束*/
		int i=0;
		boolean first=true;
		BugClone2XmlFileMain.initSettings();
  		String bugsFileName   = FileNameManage.getBugsFileName();
		String commlogXmlFile = FileNameManage.getCommlogXmlFile();
		String cloneClassXml  = FileNameManage.getCloneClassXml();
		
		String cmdExe  = null;
	
		ReviseBugCloneCodeList bugCloneList = new ReviseBugCloneCodeList(bugsFileName,commlogXmlFile,cloneClassXml);	
		Map<String, List<AnalyticsResult>> analyticsClassMap;
		
		analyticsClassMap = bugCloneList.getBugCloneListByBugId();
		for(String cloneClassId : analyticsClassMap.keySet()){
			//保存克隆类的id
		    List<AnalyticsResult>analyResultList=analyticsClassMap.get(cloneClassId);
			for(AnalyticsResult analyResult :analyResultList) {
				int updateRow =0;
				cmdExe = getComandLine(analyResult);
				updateRow = computeModifyCount(cmdExe);
				LogInformation logInfo = analyResult.getLogInfomation();
				
				WritableCellFormat cellFormat=first?getFirstCellFormat():getSecondCellFormat();
	
				detailSheet.addCell(new Label(0,++i,"Revision: " +logInfo.getRevision(),cellFormat));
				autoComplateCellFormat(detailSheet,i,cellFormat);
				detailSheet.addCell(new Label(0,++i,"Author: "+ logInfo.getCommitAuthor() ,cellFormat));
				autoComplateCellFormat(detailSheet,i,cellFormat);
				detailSheet.addCell(new Label(0,++i,"Date: " + logInfo.getCommitDate(),cellFormat));
				autoComplateCellFormat(detailSheet,i,cellFormat);
				detailSheet.addCell(new Label(0,++i,"Message",cellFormat));
	
				/**计算修改代码行数结束*/	
				detailSheet.addCell(new Number(1,i,updateRow,cellFormat));
				detailSheet.addCell(new Number(2,i,1,cellFormat));
				detailSheet.addCell(new Number(3,i,1,cellFormat));
				detailSheet.addCell(new Number(4,i,1,cellFormat));
				detailSheet.addCell(new Number(5,i,1,cellFormat));
				detailSheet.addCell(new Label(6,i,"",cellFormat));
	
	
				detailSheet.addCell(new Label(0,++i,"ddd",cellFormat));
				autoComplateCellFormat(detailSheet,i,cellFormat);
				detailSheet.addCell(new Label(0,++i,"----",cellFormat));
				autoComplateCellFormat(detailSheet,i,cellFormat);
				detailSheet.addCell(new Label(0,++i,"",cellFormat));
				autoComplateCellFormat(detailSheet,i,cellFormat);
				first=first?false:true;
			}
		}
		/**详细信息生成结束*/		
		workbook.write();
		workbook.close();
		
		System.out.println("修改次数统计完成！");
	}
	
	private static void autoComplateCellFormat(WritableSheet sheet,int i,WritableCellFormat format) throws RowsExceededException, WriteException{
		for(int k=1;k<=6;k++){
			sheet.addCell(new Label(k,i,"",format));
		}
	}
}
