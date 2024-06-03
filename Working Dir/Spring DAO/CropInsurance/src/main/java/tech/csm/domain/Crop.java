package tech.csm.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "t_crops")
public class Crop implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "crop_id")
	private Integer cropId;
	@Column(name = "crop_name")
	private String cropName;
	
	@ManyToOne
	@JoinColumn(name = "season_id")
	private Season season;
	
	
}
