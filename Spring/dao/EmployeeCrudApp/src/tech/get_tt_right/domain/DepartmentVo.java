package tech.get_tt_right.domain;

import java.io.Serializable;

public class DepartmentVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String departmentId;
	private String departmentName;

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	@Override
	public String toString() {
		return "DepartmentVo [departmentId=" + departmentId + ", departmentName=" + departmentName + "]";
	}

}
