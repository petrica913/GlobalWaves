package app.user.Collections;

import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private String date;
    private static final int DAY1 = 1;
    private static final int DAY28 = 28;
    private static final int DAY31 = 31;
    private static final int MONTH1 = 1;
    private static final int MONTH2 = 2;
    private static final int MONTH12 = 12;
    private static final int YEAR1900 = 1900;
    private static final int YEAR2023 = 2023;

    public Event(final String name, final String description, final String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    /**
     * @return a boolean if the date is valid
     */
    public boolean isValidDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);

        try {
            Date parsedDate = dateFormat.parse(this.date);
            return isValidDay(parsedDate) && isValidMonth(parsedDate) && isValidYear(parsedDate);
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * @param dateNum for date
     * @return if the day is valid or not
     */
    private boolean isValidDay(final Date dateNum) {
        int day = Integer.parseInt(new SimpleDateFormat("dd").format(dateNum));
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(dateNum));

        return day >= DAY1 && day <= DAY31 && !(month == MONTH2 && day > DAY28);
    }

    /**
     * @param dateNum for date
     * @return if the month is valid or not
     */
    private boolean isValidMonth(final Date dateNum) {
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(dateNum));
        return month >= MONTH1 && month <= MONTH12;
    }

    /**
     * @param dateNum for date
     * @return if the year is valid or not
     */
    private boolean isValidYear(final Date dateNum) {
        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(dateNum));
        return year >= YEAR1900 && year <= YEAR2023;
    }

}
