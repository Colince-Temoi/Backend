package com.eazybytes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eazybytes.model.Notice;

@Repository
public interface NoticeRepository extends CrudRepository<Notice, Long> {
	/* This method is used to find all the active notices
	 *  We are using JPQL to fetch the records from the Notice table where the current date is between the notice begin date and notice end date
	 *  Which means I don't want do display the notices which are already expired
	 *  If the beginning date and end date are in between current date then only I want to display the notices.
	 *  That why the method name is findAllActiveNotices
	 *  */
	@Query(value = "from Notice n where CURDATE() BETWEEN n.noticBegDt AND n.noticEndDt")
	List<Notice> findAllActiveNotices();

}