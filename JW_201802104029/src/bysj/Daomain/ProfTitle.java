package bysj.Daomain;

import java.io.Serializable;

public final class ProfTitle  implements Comparable<ProfTitle>,Serializable{
	private Integer id;
	private String description;
	private String no;
	private String remarks;
	public ProfTitle(Integer id, String description, String no, String remarks) {
		super();
		this.id = id;
		this.description = description;
		this.no = no;
		this.remarks = remarks;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public int compareTo(ProfTitle o) {
		// TODO Auto-generated method stub
		return this.id - o.id;
	}
}
