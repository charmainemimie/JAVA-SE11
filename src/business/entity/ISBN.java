package business.entity;

public class ISBN {
    private final String isbn;

    public ISBN(String inputISBN){
        this.isbn = validate(inputISBN);
    }

    private String validate(String inputISBN){
        if (inputISBN.isEmpty()) return "";
        return inputISBN;
    }
}
