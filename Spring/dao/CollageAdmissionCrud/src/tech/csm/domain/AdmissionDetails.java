package tech.csm.domain;

import java.io.Serializable;
import java.util.Date;

public class AdmissionDetails implements Serializable {

	private static final long serialVersionUID = 1L;
//	Primitive dependencies
	private Integer admissionId;
	private String candidateName;
	private String candidatePhone;
	private String candidateAddress;
	private Date admission_date;
//	Secondary dependencies
	private Collage collage;

//	Getters and setters
	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public String getCandidatePhone() {
		return candidatePhone;
	}

	public void setCandidatePhone(String candidatePhone) {
		this.candidatePhone = candidatePhone;
	}

	public String getCandidateAddress() {
		return candidateAddress;
	}

	public void setCandidateAddress(String candidateAddress) {
		this.candidateAddress = candidateAddress;
	}

	public Date getAdmission_date() {
		return admission_date;
	}

	public void setAdmission_date(Date admission_date) {
		this.admission_date = admission_date;
	}

	public Collage getCollage() {
		return collage;
	}

	public void setCollage(Collage collage) {
		this.collage = collage;
	}

//	toString
	@Override
	public String toString() {
		return "AdmissionDetails [admissionId=" + admissionId + ", candidateName=" + candidateName + ", candidatePhone="
				+ candidatePhone + ", candidateAddress=" + candidateAddress + ", admission_date=" + admission_date
				+ ", collage=" + collage + "]";
	}
	
	

}
