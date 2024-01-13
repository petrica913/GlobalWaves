package app.user.pages;

public interface PageVisitor {
    void visit(HomePage homePage);
    void visit(LikedContentPage likedContentPage);
    void visit(HostPage hostPage);
    void visit(ArtistPage artistPage);
}
