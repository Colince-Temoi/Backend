package tech.get_tt_right.SpringDataJpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import tech.get_tt_right.SpringDataJpa.entity.Product;

import java.util.List;

@SpringBootTest
public class PaginationAndSorting {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void pagination(){
        // Prepare page no and page size - In real-time pageSize this will come from the client
        int pageNo = 0;
        int pageSize = 5;

        // Pageable interface is used to implement pagination in Spring Data JPA.
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Product> pageResult = productRepository.findAll(pageable);

        List<Product> products = pageResult.getContent();

        products.forEach(System.out::println);

        // Total pages
        int totalPages = pageResult.getTotalPages();

        // Total elements
        long totalElements = pageResult.getTotalElements();

        // number of elements
        int numberOfElemenets = pageResult.getNumberOfElements();

        // size
        int size = pageResult.getSize();

        //last?
        boolean isLast = pageResult.isLast();

        // first?
        boolean isFirst = pageResult.isFirst();

        System.out.println("total pages -> "+totalPages);
        System.out.println("total elements -> "+totalElements);
        System.out.println("number of elements -> "+numberOfElemenets);
        System.out.println("size -> "+size);
        System.out.println("isLast? -> "+isLast);
        System.out.println("isFirst? -> "+isFirst);
    }

    @Test
    void sorting(){
        // Sort by price in ascending order
        String sortBy = "price";
//        String sortDir = "desc";
        List<Product> products = productRepository.findAll(Sort.by(sortBy).descending());
        products.forEach(System.out::println);
    }

    @Test
    void sortingByMultipleFields(){
        // Sort by name and description in ascending order
        String sortBy = "name";
        String sortByDesc = "description";
        String sortDir = "desc";

        Sort sortName = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Sort sortDesc = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortByDesc).ascending() : Sort.by(sortByDesc).descending();

        /* sorting on multiple columns or group by sort. */
        Sort groupSort = sortName.and(sortDesc);

        List<Product> products = productRepository.findAll(groupSort);
          products.forEach(System.out::println);
       }

         @Test
    void paginationAndSorting(){
        // Prepare page no and page size - In real-time pageSize this will come from the client
//        int pageNo = 0;
        int pageNo = 1;
        int pageSize = 5;

        // Sort by price in ascending order
        String sortBy = "price";
        String sortDir = "desc";
        Sort sort = Sort.by(sortBy).descending();

        // Pageable interface is used to implement pagination in Spring Data JPA.
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> page =  productRepository.findAll(pageable);
        List<Product> products = page.getContent();
        products.forEach(System.out::println);

             // Total pages
             int totalPages = page.getTotalPages();
             System.out.println("total pages -> "+totalPages);

         }
}
