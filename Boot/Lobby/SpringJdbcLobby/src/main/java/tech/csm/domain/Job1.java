package tech.csm.domain;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @AllArgsConstructor @Setter @Getter
public class Job1 {
    private String jobId;
    private String jobTitle;
    private Double minSalary;
    private Double maxSalary;
}

