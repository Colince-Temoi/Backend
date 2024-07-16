package tech.csm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product1 {
    private int id;
    private String title;
    private double price;
    private String category;
    private String description;
}