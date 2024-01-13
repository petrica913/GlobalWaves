package app.user.pages;

public class PagePrinterVisitor implements PageVisitor {
    private StringBuilder result = new StringBuilder();

    /**
     * @param homePage for the homepage
     */
    @Override
    public void visit(final HomePage homePage) {
        result.append(homePage.display());
    }

    /**
     * @param likedContentPage for the likedContentPage
     */
    @Override
    public void visit(final LikedContentPage likedContentPage) {
        result.append(likedContentPage.display());
    }

    /**
     * @param hostPage for the hostPage
     */
    @Override
    public void visit(final HostPage hostPage) {
        result.append(hostPage.display());
    }

    /**
     * @param artistPage for the artistPage
     */
    @Override
    public void visit(final ArtistPage artistPage) {
        result.append(artistPage.display());
    }

    /**
     * @return the result of visiting
     */
    public String getResult() {
        return result.toString();
    }
}
