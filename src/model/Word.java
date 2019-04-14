package model;

public class Word {
    private int id;
    private String name;
    private String hint;

    public Word(int id, String name, String hint) {
        this.id = id;
        this.name = name;
        this.hint = hint;
    }

    public Word(int id, String name) {
        this.id = id;
        this.name = name;
        this.hint = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHint() {
        return hint;
    }

    public void setId(int id) {
        this.id = id;
    }
}
