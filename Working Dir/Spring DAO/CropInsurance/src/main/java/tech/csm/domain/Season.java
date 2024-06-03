package tech.csm.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Entity
@Getter
@Setter
@ToString
@Table(name = "t_season")
public class Season implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "season_id")
	private Integer seasonId;
	@Column(name = "season_name")
	private String seasonName;

}
