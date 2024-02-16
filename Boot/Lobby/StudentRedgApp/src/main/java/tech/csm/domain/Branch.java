package tech.csm.domain;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "t_branch_master")
public class Branch implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "branch_id")
	private Integer branchId;

	@Column(name = "branch_name")
	private String branchName;

	@OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
	private List<Student> students;

}
