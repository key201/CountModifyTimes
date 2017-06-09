package bean;

import java.util.Map;

//计算content内容的一个hash值
public class RevisePath {
	private String action;
	private String prop_mods;
	private String text_mods;
	private String kind;
	private String content;
	Map<String, String> mapReviseAct;

	public RevisePath() {

	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getProp_mods() {
		return prop_mods;
	}

	public void setProp_mods(String prop_mods) {
		this.prop_mods = prop_mods;
	}

	public String getText_mods() {
		return text_mods;
	}

	public void setText_mods(String text_mods) {
		this.text_mods = text_mods;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getMapReviseAct() {
		return mapReviseAct;
	}

	public void setMapReviseAct(Map<String, String> mapReviseAct) {
		this.mapReviseAct = mapReviseAct;
	}

}