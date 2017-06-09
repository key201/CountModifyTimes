package bean;

import java.util.ArrayList;
import java.util.List;

/*
<logentry revision="1736287">
<author>violetagg</author>
<date>2016-03-23T08:10:44.356352Z</date>
<paths>
<path action="M" prop-mods="true" text-mods="false" kind="dir">/tomcat/tc7.0.x/trunk</path>
<path action="M" prop-mods="false" text-mods="true" kind="file">/tomcat/tc7.0.x/trunk/webapps/docs/changelog.xml</path>
</paths>
<msg>
Merged revision 1736286 from tomcat/tc8.0.x/trunk: Fix typo
</msg>
</logentry>
 */
public class LogInformation {

	private List<RevisePath> reviseList;
	private String revision;
	private String commitAuthor;
	private String commitDate;
	private String commitMsg;	
	private List<String> revisedBugId;//一次提交可能修改多个bugs
	// private List<String> pathList;
	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getCommitAuthor() {
		return commitAuthor;
	}

	public void setCommitAuthor(String commitAuthor) {
		this.commitAuthor = commitAuthor;
	}

	public String getCommitDate() {
		return commitDate;
	}

	public void setCommitDate(String commitDate) {
		this.commitDate = commitDate;
	}

	public String getCommitMsg() {
		return commitMsg;
	}

	public void setCommitMsg(String commitMsg) {
		this.commitMsg = commitMsg;
	}
	
	public List<String> getRevisedBugId() {
		return revisedBugId;
	}

	public void setRevisedBugId(List<String> revisedBugId) {
		this.revisedBugId = revisedBugId;
	}
	/*
	 * public List<String> getPathList() { return pathList; } public void
	 * setPathList(List<String> pathList) { this.pathList = pathList; }
	 */
	public List<RevisePath> getReviseList() {
		return reviseList;
	}

	public void setReviseList(List<RevisePath> reviseList) {
		this.reviseList = reviseList;
	}
}
