/**
 *
 */
/**
 * @author tmi
 *
 */
module app26_autowiringAnnotaionsApproach {
	requires spring.context;
	requires spring.beans;
	exports com.get_tt_right.beans;
    opens com.get_tt_right.beans to spring.core;

}