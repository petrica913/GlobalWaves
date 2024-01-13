package app.user.pages;

public interface PageVisitor {
    /**
     * @param homePage for the homepage
     */
    void visit(HomePage homePage);
    /**
     * @param likedContentPage for the likedContentPage
     */
    void visit(LikedContentPage likedContentPage);
    /**
     * @param hostPage for the hostPage
     */
    void visit(HostPage hostPage);
    /**
     * @param artistPage for the artistPage
     */
    void visit(ArtistPage artistPage);
}
