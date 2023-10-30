/**
 * 
 */
/**
 * @author tmi
 *
 */
module app32_LookUp_Method_DI {
	requires spring.context;
	requires spring.beans;
	exports com.get_tt_right.beans;
    opens com.get_tt_right.beans to spring.core;

}