package com.get_tt_right.accounts.constants;
/**
 * This class contains all the constants used in the Accounts module.
 * Every constant in this class is public, static, and final so that no one can change it.
 * At the same time we can use the constants of this class in any other class without creating an object of this class.
 * With the private constructor, we are restricting the instantiation of this class. No one can create an object of this class.
 * The reason is I only want to maintain constants in this class and don't want someone polluting it with some methods or any other business logic.
 */
public final class AccountsConstants {

    private AccountsConstants() {
        // restrict instantiation
    }

    public static final String  SAVINGS = "Savings";
    public static final String  ADDRESS = "123 Main Street, New York";
    public static final String  STATUS_201 = "201";
    public static final String  MESSAGE_201 = "Account created successfully";
    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Request processed successfully";
    public static final String  STATUS_417 = "417";
    public static final String  MESSAGE_417_UPDATE= "Update operation failed. Please try again or contact Dev team";
    public static final String  MESSAGE_417_DELETE= "Delete operation failed. Please try again or contact Dev team";
     public static final String  STATUS_500 = "500";
     public static final String  MESSAGE_500 = "An error occurred. Please try again or contact Dev team";

}