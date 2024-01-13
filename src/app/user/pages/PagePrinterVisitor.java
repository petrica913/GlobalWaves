package app.user.pages;

public class PagePrinterVisitor implements PageVisitor{
    private StringBuilder result = new StringBuilder();

    @Override
    public void visit(HomePage homePage) {
        result.append(homePage.display());
    }

    @Override
    public void visit(LikedContentPage likedContentPage) {
        result.append(likedContentPage.display());
    }

    @Override
    public void visit(HostPage hostPage) {
        result.append(hostPage.display());
    }

    @Override
    public void visit(ArtistPage artistPage) {
        result.append(artistPage.display());
    }

    public String getResult() {
        return result.toString();
    }
}
