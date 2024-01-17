class Leap {
    boolean isLeapYear(int year) {
        System.out.println("Printing to stdout");
        System.err.println("Printing to stderr");

        return year % 400 == 0 || year % 100 != 0 && year % 4 == 0;
    }
}
