package bean;

public class Clone {

	private int stratLine;
	private int endLine;
	private int numClonedLines;
	private int pcid;
	private int ccid;
	private String sourcePath;
	private long startCharPosition;
	private long endCharPosition;

	AnalyticsResult analysisResult;
	
	public AnalyticsResult getAnalysisResult() {
		return analysisResult;
	}

	public void setAnalysisResult(AnalyticsResult analysisResult) {
		this.analysisResult = analysisResult;
	}

	public int getccid() {
		return this.ccid;
	}

	public void setccid(int ccid) {
		this.ccid = ccid;
	}

	public Clone(int startLine, int endLine, int pcid, String sourcePath) {
		this.stratLine = startLine;
		this.endLine = endLine;
		this.pcid = pcid;
		this.sourcePath = sourcePath;
		this.numClonedLines = (endLine - startLine + 1);
		this.startCharPosition = 0L;
		this.endCharPosition = 0L;
	}

	public int getStratLine() {
		return this.stratLine;
	}

	public int getEndLine() {
		return this.endLine;
	}

	public int getNumClonedLines() {
		return this.numClonedLines;
	}

	public long getEndCharPosition() {
		return this.endCharPosition;
	}

	public void setEndCharPosition(long endCharPosition) {
		this.endCharPosition = endCharPosition;
	}

	public long getStartCharPosition() {
		return this.startCharPosition;
	}

	public void setStartCharPosition(long startCharPosition) {
		this.startCharPosition = startCharPosition;
	}

	public int getPcid() {
		return this.pcid;
	}

	public String getSourcePath() {
		return this.sourcePath;
	}

	public String toString() {
		return "PCID: " + getPcid();
	}

	public String getSource() {
		return "";
	}

	public void print() {
		System.out.println("< CLONE >");
		System.out.println("PCID = " + getPcid());
		System.out.println("Source = " + getSource());
		System.out.println("Source Path = " + getSourcePath());

		System.out.println("StartLine = " + getStratLine());
		System.out.print("EndLine = " + getEndLine());
		System.out.print("StartCharPosition = " + getStartCharPosition());
		System.out.print("EndCharPosition = " + this.endCharPosition);
		System.out.print("Clone class ID = " + getccid());
		System.out.println("</CLONE>");
	}

}
