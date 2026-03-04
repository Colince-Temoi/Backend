package com.get_tt_right.loans.constants;
// This class is used to define the constants that are used across the application. This is a good practice to define the constants in a separate class so that we can use them across the application. This will help us to avoid hardcoding the values in the code. If we need to change the value, we can change it in this class, and it will reflect across the application.
public final class LoansConstants {

    private LoansConstants() {
        // restrict instantiation
    }

    public static final String  HOME_LOAN = "Home Loan";
    public static final int  NEW_LOAN_LIMIT = 1_00_000; // This format where we are using underscore to separate the digits is called Numeric Literals with Underscores in Java. It's a feature that was introduced in Java 7. It is used to improve the readability of the code. But behind the scenes when my JVM runs this code, it will remove the underscores and treat it as a normal number.
    public static final String  STATUS_201 = "201";
    public static final String  MESSAGE_201 = "Loan created successfully";
    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Request processed successfully";
    public static final String  STATUS_417 = "417";
    public static final String  MESSAGE_417_UPDATE= "Update operation failed. Please try again or contact Dev team";
    public static final String  MESSAGE_417_DELETE= "Delete operation failed. Please try again or contact Dev team";
    // public static final String  STATUS_500 = "500";
    // public static final String  MESSAGE_500 = "An error occurred. Please try again or contact Dev team";

}
