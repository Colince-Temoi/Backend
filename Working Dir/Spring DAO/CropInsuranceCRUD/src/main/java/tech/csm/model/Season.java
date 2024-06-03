package tech.csm.model;

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
@Table(name="t_season")
public class Season implements Serializable {
	@Id
	@Column(name = "season_id")
	private Integer seasonId;
	@Column(name = "name")
	private String seasonName;
}
