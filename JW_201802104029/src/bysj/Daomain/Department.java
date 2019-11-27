package bysj.Daomain;

import java.io.Serializable;

public final class Department implements Comparable<Department>,Serializable{
	private Integer id;
	private String description;
	private String no;
	private String remarks;
	private School school;

	public Department(Integer id, String description, String no,
			String remarks, School school) {
		this(description, no, remarks, school);
		this.id = id;
	}

	public Department(String description, String no,
					  String remarks, School school) {
		super();
		this.description = description;
		this.no = no;
		this.remarks = remarks;
		this.school = school;
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

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	@Override
	public int compareTo(Department o) {
		// TODO Auto-generated method stub
		return this.id-o.id;
	}
	
	
}
