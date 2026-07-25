package com.get_tt_right.customer.query;

import lombok.Value;

/**
 * VERB+NOUN+Query
 * In this class I don't want to maintain any fields. I want to keep it empty. Reason: Technically, we are not going to query any DB to fetch the results. We simply just want to know the overall status of the orchestration saga pattern flow.
 */
@Value
public class FindUpdateMobileSagaQuery {

}