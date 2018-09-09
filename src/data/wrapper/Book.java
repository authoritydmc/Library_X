package data.wrapper;


public class Book {
    private String department;
    private String Subejct;
    private String year;
    private Integer quantity;
    private String id;
    private String title;
    private String author;
    private String publisher;
    private Boolean isAvail;
    private String Description;

    public String getDescription() {
        return Description;
    }

    public Book(String id, String title, String author, String publisher, Boolean isAvail, String Description, String dept, String Subject, String year, Integer quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isAvail = isAvail;
        this.Description = Description;
        this.department = dept;
        this.Subejct = Subject;
        this.year = year;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDepartment() {
        return department;
    }

    public String getSubejct() {
        return Subejct;
    }

    public String getYear() {
        return year;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Boolean getAvailability() {
        return isAvail;
    }

    public void setIsAvail(Boolean isAvail) {
        this.isAvail = isAvail;
    }


}
