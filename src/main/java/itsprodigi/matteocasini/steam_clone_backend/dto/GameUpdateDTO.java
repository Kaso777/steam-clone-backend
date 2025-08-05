package itsprodigi.matteocasini.steam_clone_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class GameUpdateDTO {

    private String title;
    private BigDecimal price;
    private LocalDate releaseDate;
    private String developer;
    private String publisher;
    private List<String> tagNames;

    public GameUpdateDTO() {}

    public GameUpdateDTO(String title, BigDecimal price, LocalDate releaseDate, String developer, String publisher, List<String> tagNames) {
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
        this.developer = developer;
        this.publisher = publisher;
        this.tagNames = tagNames;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getDeveloper() { return developer; }
    public void setDeveloper(String developer) { this.developer = developer; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public List<String> getTagNames() { return tagNames; }
    public void setTagNames(List<String> tagNames) { this.tagNames = tagNames; }

    @Override
    public String toString() {
        return "GameUpdateDTO{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", releaseDate=" + releaseDate +
                ", developer='" + developer + '\'' +
                ", publisher='" + publisher + '\'' +
                ", tagNames=" + tagNames +
                '}';
    }
}