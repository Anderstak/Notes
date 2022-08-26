import model.Note;
import protocol.RespReader;
import protocol.RespWriter;
import protocol.model.RespArray;
import protocol.model.RespBulkString;
import protocol.model.RespObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private Socket socket = null;
    private RespWriter rw = null;
    private RespReader rr = null;

    Client(String host, int port) {
        try {
            // Создадим сокет для общения с сервером
            this.socket = new Socket(host, port);

            // Создадим для себя классы, чтобы писать и читать
            rw = new RespWriter(socket.getOutputStream());
            rr = new RespReader(socket.getInputStream());

        } catch (Exception e) {

        }
    }

    // Превратить RespObject в int
    static int toInt(RespObject a) {
        // На входе подается RespBulkString("34")
        String str = a.asString();
        // Теперь у меня строка "34"
        // Наконец - хочу вытащить само число
        int num = Integer.valueOf(str);
        // 34
        return num;
    }

    // Превратить int в RespBulkString
    static RespBulkString toBulkString(int a) {
        return new RespBulkString(String.valueOf(a).getBytes());
    }

    static Note toNote(RespObject x) {
        // Я знаю, что заметка представляется RespArray
        // который состоит из четырех полей RespBulkString в строгом порядке!

        /* ---Note---
            id -
            title -
            content -
            date -
         */

        // Чтобы создать заметку, нужно добавить конструктор... (иду в класс Note)
        List<RespObject> fields = ((RespArray) x).getObjects();
        int noteId = toInt(fields.get(0));
        String title = fields.get(1).asString();
        String content = fields.get(2).asString();
        int date = toInt(fields.get(3));

        return new Note(noteId, title, content, date);

    }

    Integer createNote() throws IOException {

        // Я здесь формирую мой запрос
        RespBulkString cmdName = new RespBulkString("create".getBytes());
        RespArray request = new RespArray(cmdName);
        // [ "create" ]

        this.rw.write(request);
        RespArray response = this.rr.readArray();

        // Хочу получить, что в моем ответе пришло (массив)
        List<RespObject> contents = response.getObjects();
        // например, [ "34" ]
        return toInt(contents.get(0));

    }

    Note editNote(int id, String title, String content) throws Exception {
        // Я здесь формирую мой запрос
        RespBulkString cmdName = new RespBulkString("edit".getBytes());
        RespBulkString noteId = toBulkString(id);
        RespBulkString titleString = new RespBulkString(title.getBytes());
        RespBulkString contentString = new RespBulkString(content.getBytes());
        RespArray request = new RespArray(cmdName, noteId, titleString, contentString);
        // [ "edit", "56", "New title", "New content" ]

        this.rw.write(request);
        RespArray response = this.rr.readArray();

        // Хочу получить, что в моем ответе пришло (массив)
        List<RespObject> contents = response.getObjects();

        // На этот запрос сервер сначала присылает статус его выполнения
        String status = contents.get(0).asString(); // success / error

        if (status.equals("success")) {
            // Заметка реально была удалена! Усё!
            // Далее должна идти удаленная заметка!
            // А я должен ее вернуть!
            return toNote(contents.get(1));
        } else if (status.equals("error")) {
            return null;
        } else {
            throw new Exception("Колючий ответ! Сервер, возможно, съежился.");
        }

    }

    Note viewNote(Integer id) throws Exception {
        // Я здесь формирую мой запрос
        RespBulkString cmdName = new RespBulkString("viewById".getBytes());
        RespBulkString noteId = toBulkString(id);
        RespArray request = new RespArray(cmdName, noteId);
        // [ "viewById", "56" ]

        this.rw.write(request);
        RespArray response = this.rr.readArray();

        // Хочу получить, что в моем ответе пришло (массив)
        List<RespObject> contents = response.getObjects();

        // На этот запрос сервер сначала присылает статус его выполнения
        String status = contents.get(0).asString(); // success / error

        if (status.equals("success")) {
            // Заметка реально была удалена! Усё!
            // Далее должна идти удаленная заметка!
            // А я должен ее вернуть!
            return toNote(contents.get(1));
        } else if (status.equals("error")) {
            return null;
        } else {
            throw new Exception("Колючий ответ! Сервер, возможно, съежился.");
        }

    }

    ArrayList<Note> viewNotes() throws Exception {

        // Я здесь формирую мой запрос
        RespBulkString cmdName = new RespBulkString("view".getBytes());
        RespArray request = new RespArray(cmdName);
        // [ "view" ]

        this.rw.write(request);
        RespArray response = this.rr.readArray();

        // Хочу получить, что в моем ответе пришло (массив Заметок)
        List<RespObject> contents = response.getObjects();

        /*
            [
                [   id -
                    title -
                    content -
                    date -
                ],
                [   id -
                    title -
                    content -
                    date -
                ], ...
            ]

         */

        ArrayList<Note> notes = new ArrayList<>();
        for (RespObject o : contents) {
            // пробегаюсь по всем заметкам
            // и создаю экземпляр класса Note
            notes.add(toNote(o));
        }

        return notes;

    }

    Note deleteNote(int id) throws Exception {
        // Я здесь формирую мой запрос
        RespBulkString cmdName = new RespBulkString("delete".getBytes());
        RespBulkString noteId = toBulkString(id);
        RespArray request = new RespArray(cmdName, noteId);
        // [ "delete", "56" ]

        this.rw.write(request);
        RespArray response = this.rr.readArray();

        // Хочу получить, что в моем ответе пришло (массив)
        List<RespObject> contents = response.getObjects();

        // На этот запрос сервер сначала присылает статус его выполнения
        String status = contents.get(0).asString(); // success / error

        if (status.equals("success")) {
            // Заметка реально была удалена! Усё!
            // Далее должна идти удаленная заметка!
            // А я должен ее вернуть!
            return toNote(contents.get(1));
        } else if (status.equals("error")) {
            return null;
        } else {
            throw new Exception("Колючий ответ! Сервер, возможно, съежился.");
        }

    }

    void close() {
        try {
            rw.close();
            rr.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Не получилось закрыть соединение.");
        }
    }

}