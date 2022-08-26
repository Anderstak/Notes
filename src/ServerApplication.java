import model.Note;
import protocol.RespReader;
import protocol.RespWriter;
import protocol.model.RespArray;
import protocol.model.RespBulkString;
import protocol.model.RespObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerApplication {

    static NoteKeeper noteKeeper = null;

    static RespArray noteToRespArray(Note n) {
        RespBulkString noteId = Client.toBulkString(n.getId());
        RespBulkString title = new RespBulkString(n.getTitle().getBytes());
        RespBulkString content = new RespBulkString(n.getContent().getBytes());
        RespBulkString time = Client.toBulkString(n.getCreationTime());


        return new RespArray(noteId, title, content, time);
    }

    // метод, который обрабатывает сообщение
    static RespArray processMessage(RespArray request) throws Exception {

        /* Правильное сообщение имеет следующую структуру
            название операции "create/edit/view/delete/viewById"
                "create": доп. параметры не требуются
                возвращается ["номер созданного сообщения"]
                "edit": далее идентификатор заметки, затем название, затем содержание
                возвращается Заметка
                "view": доп. параметры не требуются
                возвращается массив [Заметок]
                "viewById": далее идентификатор заметки
                возвращается Заметка
                "delete": далее идентификатор заметки
                возвращается Заметка
            Заметка - это массив из четырех значений:
                идентификатор
                название
                содержание
                дата (секунды с 1970 года)
         */

        // Хочу представить сообщение как ArrayList
        ArrayList<RespObject> r = new ArrayList<>();
        r.addAll(request.getObjects());

        // Название полученного сообщения (метода)
        String name = r.get(0).asString();

        System.out.println("Я получил сообщение, у которого смог распознать название " + name);

        if (name.equals("create")) {
            Integer noteId = noteKeeper.create();
            // я отправил [ RespBulkString "noteId" ]
            return new RespArray(new RespBulkString(String.valueOf(noteId).getBytes()));
        }

        if (name.equals("edit")) {

            System.out.println(r.get(0).asString());
            System.out.println(r.get(1).asString());
            System.out.println(r.get(2).asString());
            System.out.println(r.get(3).asString());

            try {
                Integer noteId = Client.toInt(r.get(1));
                String title = r.get(2).asString();
                String content = r.get(3).asString();

                // Попытался обновить заметку
                noteKeeper.edit(noteId, title, content);

                // Получаю заметку обратно обновленную
                Note note = noteKeeper.view(noteId);

                // Превратил заметку в RespArray
                RespArray n = noteToRespArray(note);

                // Составляю сообщение с ответом
                RespBulkString status = new RespBulkString("success".getBytes());

                return new RespArray(status, n);

            } catch (Exception e) {

                e.printStackTrace();

                // Составляю сообщение с ответом
                RespBulkString status = new RespBulkString("error".getBytes());
                RespBulkString message = new RespBulkString(e.getMessage().getBytes());
                return new RespArray(status, message);

            }

            // TODO: реализовать метод
        }

        if (name.equals("view")) {
            // TODO: реализовать метод

            ArrayList<Note> notes = noteKeeper.view();

            // Получить RespArray со всеми заметками
            ArrayList<RespArray> noteArrays = new ArrayList<>();
            for (int i = 0; i < notes.size(); i++) {
                noteArrays.add(noteToRespArray(notes.get(i)));
            }
            RespArray respNotes = new RespArray(noteArrays.toArray(new RespArray[0]));
            return respNotes;

        }

        if (name.equals("viewById")) {
            try {
                Integer noteId = Client.toInt(r.get(1));


                // Получаю заметку обратно обновленную
                Note note = noteKeeper.view(noteId);

                // Превратил заметку в RespArray
                RespArray n = noteToRespArray(note);

                // Составляю сообщение с ответом
                RespBulkString status = new RespBulkString("success".getBytes());

                return new RespArray(status, n);

            } catch (Exception e) {
                e.printStackTrace();
                // Составляю сообщение с ответом
                RespBulkString status = new RespBulkString("error".getBytes());
                RespBulkString message = new RespBulkString(e.getMessage().getBytes());
                return new RespArray(status, message);

            }
        }

        if (name.equals("delete")) {
            try {
                Integer noteId = Client.toInt(r.get(1));

                // Получаю заметку обратно обновленную
                Note note = noteKeeper.delete(noteId);

                // Превратил заметку в RespArray
                RespArray n = noteToRespArray(note);

                // Составляю сообщение с ответом
                RespBulkString status = new RespBulkString("success".getBytes());

                return new RespArray(status, n);

            } catch (Exception e) {

                // Составляю сообщение с ответом
                RespBulkString status = new RespBulkString("error".getBytes());
                RespBulkString message = new RespBulkString(e.getMessage().getBytes());
                return new RespArray(status, message);

            }
        }

        throw new Exception("Сообщение повреждено или метод не поддерживается!");

    }

    static Socket newlyCreatedSocket = null;

    public static void main(String args[]) {

        /*
         listener - слушатель
         Попробуем создать серверный сокет
        */

        ServerSocket listener = null;

        // Если порт занят, возникнет исключение
        try {
            listener = new ServerSocket(30256);
        } catch (IOException e) {
            // Мы обработаем это исключение.
            // Завершаем программу, поскольку не получилось открыть порт.
            System.out.println(e);
            System.exit(1);
        }

        try {
            System.out.println("Сервер готов принимать ваши запросы! Шлите их ему!");

            // Программа ждет на главном потоке запроса на наш порт через сокет
            // accept - блокирует наш поток. Это как Scanner.
            // пока не придут данные, программа дальше не пойдет

            // Создадим экземпляр NoteKeeper, чтобы работать с заметками
            noteKeeper = new NoteKeeper();

            while (true) {

                newlyCreatedSocket = listener.accept();

                // Чтобы мы могли общаться сразу с несколькими пользователями,
                // отошлем отдельный сокет на отдельный поток

                Thread t = new Thread(new Runnable() { public void run() {

                    Socket socket = newlyCreatedSocket;

                    System.out.println("Я поймал запрос от пользователя! Я так счастлив!");

                    try {
                        // Open input and output streams
                        RespWriter rw = new RespWriter(socket.getOutputStream());
                        RespReader rr = new RespReader(socket.getInputStream());

                        // Прочитаем то, что нам послал клиент

                        while (true) {
                            RespArray request = rr.readArray();
                            RespArray response = processMessage(request);
                            System.out.println(response.getObjects().get(0).asString());
                            rw.write(response);
                        }

                    } catch (Exception e) {

                    }

                }});

                t.run();

            }

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        System.out.println("Server stopped!");

    }

}