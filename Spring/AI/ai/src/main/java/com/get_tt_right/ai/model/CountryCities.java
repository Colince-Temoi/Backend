package com.get_tt_right.ai.model;

import java.util.List;

/** Inside this record, the class I'm going to define a couple of fields. the first one is country name, which is going to be represented using string, whereas the list of cities they are going to be represented by using list of string.
 * Now my Pojo object is ready.
 */
public record CountryCities(String country, List<String> cities) {
}
