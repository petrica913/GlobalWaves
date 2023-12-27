package app.user.pages;

public interface Page {
    /**
     * @return the content of a page
     */
    String display();

    /**
     * @return page type
     */
    String type();
}
