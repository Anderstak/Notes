import java.io.IOException;
import java.util.*;

import model.Note;

public class NoteKeeper {
    private Map<Integer, Note> notes = new HashMap();

    NoteKeeper() {
    }

    Integer create() {
        Note N = new Note();
        this.notes.put(N.getId(), N);
        return N.getId();
    }

    void edit(Integer id, String title, String text) throws Exception {
        if (this.notes.get(id) != null) {
            this.notes.get(id).setTitle(title);
            this.notes.get(id).setContent(text);
        } else {
            throw new Exception("Фырк! Нет такой заметки :( ");
        }
    }

    Note view(Integer id) throws Exception {
        if (this.notes.get(id) != null) {
            return this.notes.get(id);
        } else {
            throw new Exception("Фырк! Нет такой заметки :( ");
        }
    }

    Note delete(Integer id) throws Exception {
        if (this.notes.get(id) != null) {
            Note t = this.notes.get(id);
            this.notes.remove(id);
            return t;
        } else {
            throw new Exception("Фырк! Нет такой заметки :( ");
        }
    }

    ArrayList<Note> view() {
        return new ArrayList<Note>(this.notes.values());
    }
//    Set<Note> view() {
//        Set<Note> x = new HashSet<>();
//        for (Integer key: this.notes.keySet()){
//            x.add(this.notes.get(key));
//        }
//        return x;
//    }

    // Загрузить заметки из файла локального
    void readFromFile() {
        // TODO:
    }

    // Записать все мои заметки в файл
    void writeToFile(){
        // TODO:
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        NoteKeeper k = new NoteKeeper();

        Note n = new Note();
        n.setTitle("Love");
        n.setContent("Love2!");
        n.writeToFile(String.valueOf(n.getId()));

        Note y = Note.readFromFile(String.valueOf(n.getId()));
        System.out.println(y.getTitle());

        try {

//            Note none = new Note();
//            none.setTitle("Love");
//            none.setContent("Love2!");
//            none.writeToFile(String.valueOf(n.getId()));
//
//            Note yew = Note.readFromFile(String.valueOf(n.getId()));
//            System.out.println(y.getTitle());
//
//
//            Integer noteId = k.create();
//            k.edit(noteId, "love", "care");
//            System.out.println(k.view(noteId).getTitle());
//            k.edit(noteId, "love wins", "care is required!");
//            System.out.println(k.view(noteId).getTitle());
//            k.delete(noteId);
//            System.out.println(k.view(noteId).getTitle());



        } catch (Exception e) {
            System.out.println("Фырк! Нет такой заметки :( ");
        }

    }
}
