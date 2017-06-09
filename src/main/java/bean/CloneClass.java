package bean;

import java.util.ArrayList;
import java.util.List;

public class CloneClass {
	private int id;
	private int nlines;
	private int nfragments;
	private List<Clone> cloneList;

	public CloneClass(int id, int nlines, int nfragments) {
		this.id = id;
		this.nlines = nlines;
		this.nfragments = nfragments;
		this.cloneList = new ArrayList<Clone>(nfragments);
	}
	
	public void addClone(Clone clone) {
		this.cloneList.add(clone);
	}

	public int getId() {
		return this.id;
	}

	public int getNlines() {
		return this.nlines;
	}

	public int getNfragments() {
		return this.nfragments;
	}

	public List<Clone> getCloneList() {
		return this.cloneList;
	}

	public static void main(String[] args) {
	}

	public String toString() {
		return "CC-" + getId() + "(Fragments: " + getCloneList().size() + ")";
	}

	public void print() {
		System.out.println("<CLONE CLASS>");
		System.out.println("Clone Class ID" + getId());
		System.out.println("Number of Fragments" + getNfragments());
		System.out.println("Number of Cloned Lines" + getNlines());

		for (int i = 0; i < getCloneList().size(); i++) {
			((Clone) getCloneList().get(i)).print();
		}
		System.out.println("</CLONE CLASS>");
	}
}
