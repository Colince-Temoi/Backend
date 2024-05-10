package tech.csm.domain;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString @NoArgsConstructor @AllArgsConstructor
public class Job {
    private String jobId;
    private String jobTitle;
    private int minSalary;
    private int maxSalary;
}
