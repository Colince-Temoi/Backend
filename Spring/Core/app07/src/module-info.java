/**
 * 
 */
/**
 * @author tmi
 *
 */
module app07 {
	requires spring.context;
	exports com.get_tt_right.beans;
	exports com.get_tt_right.config;
	opens com.get_tt_right.config to spring.core;
}