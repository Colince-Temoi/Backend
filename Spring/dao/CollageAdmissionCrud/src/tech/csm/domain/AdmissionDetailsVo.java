package tech.csm.domain;

import java.io.Serializable;

public class AdmissionDetailsVo implements Serializable {

	private static final long serialVersionUID = 1L;
//	Primitive dependencies
	private String admissionId;
	private String candidateName;
	private String candidatePhone;
	private String candidateAddress;
	private String admission_date;
//	Secondary dependencies
	private CollageVo collageVo;

//	Getters and Setters
	public String getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(String admissionId) {
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

	public String getAdmission_date() {
		return admission_date;
	}

	public void setAdmission_date(String admission_date) {
		this.admission_date = admission_date;
	}

	public CollageVo getCollageVo() {
		return collageVo;
	}

	public void setCollageVo(CollageVo collageVo) {
		this.collageVo = collageVo;
	}

//	toString
	@Override
	public String toString() {
		return "AdmissionDetailsVo [admissionId=" + admissionId + ", candidateName=" + candidateName
				+ ", candidatePhone=" + candidatePhone + ", candidateAddress=" + candidateAddress + ", admission_date="
				+ admission_date + ", collageVo=" + collageVo + "]";
	}
	
	

}
