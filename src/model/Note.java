package model;

import java.io.*;

public class Note implements java.io.Serializable {

    // глобальный (общий для всех заметок здесь)
    // номер последней созданной заметки
    private static int LAST_NOTE_ID = 0;

    // Уникальный номер заметки
    private int id;

    // Название заметки
    private String title;

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public int getCreationTime() {
        return creationTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        if (this.content==null) {
            return "";
        }
        return this.content;
    }

    public void setContent(String content) {
        if (content != null) {
            this.content = content;
        } else {
            this.content = "";
        }
    }

    // Содержание заметки
    private String content;

    // Дата изменения заметки
    // UNIX-временная метка (время в секундах с 1970 г.)
    private int creationTime;

    public Note() {
        LAST_NOTE_ID += 1;
        this.id = LAST_NOTE_ID;
        this.title = "New Note #" + LAST_NOTE_ID;
        this.content = "";
        this.creationTime = (int) (System.currentTimeMillis() / 1000);
    }
    public Note(int id, String title, String content, int date) {
        this.id=id;
        this.title=title;
        this.content=content;
        this.creationTime=date;
    }
    public void writeToFile(String filename) throws IOException {
        FileOutputStream fout = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(this);
    }

    public static Note readFromFile(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fin = new FileInputStream(filename);
        ObjectInputStream oos = new ObjectInputStream(fin);
        return (Note) oos.readObject();
    }


}
