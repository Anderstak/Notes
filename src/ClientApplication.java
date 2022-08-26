import model.Note;
import protocol.model.RespArray;
import protocol.model.RespBulkString;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ClientApplication {

    static Client client;

    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE

    // Bold
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

    // Underline
    public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    public static final String RED_UNDERLINED = "\033[4;31m";    // RED
    public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

    // Background
    public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    public static final String RED_BACKGROUND = "\033[41m";    // RED
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

    // High Intensity
    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

    // High Intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
    public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
    public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
    public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE

    static void printNote(Note n) {
        System.out.println(n.getTitle() + " / " + n.getCreationTime());
        if (n.getContent().equals("")) {
            System.out.println("Заметка обезъежена = пустая, короче.");
        } else {
            System.out.println(n.getContent());
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Божественные Ежовые Заметки v1.0");
        System.out.println("Напишите /help для справки.");

        ClientApplication.client = new Client("localhost", 30256);

        // Подсказка дня
        System.out.println();
        System.out.println("Ежевичная подсказка дня от Ежа:");
        int randomNumber = (new Random()).nextInt(10);
        if (randomNumber % 10 == 0) {
            System.out.println("Если Ваше приложение не работает, проверьте, подключен ли компьютер к сети.");
        } else if (randomNumber % 10 == 1) {
            System.out.println("Если у Вас плохой интернет, закройте окно, чтобы он не выветривался.");
        } else if (randomNumber % 10 == 2) {
            System.out.println("Если у Вас нету тещи, то Вам её не потерять...");
        } else {
            System.out.println("Заведите Ежа, что б все работало без каких-то еживых багов!");
        }
        System.out.println();

        // Читаем команды: слушаем и повинуемся
        Scanner in = new Scanner(System.in);
        while (true) {
            String command = in.nextLine();

            if (command.startsWith("/help")) {
                //System.out.println("Здесь будет справка.");

                String CMD = GREEN_BACKGROUND_BRIGHT + WHITE_BOLD_BRIGHT;
                String CMDD = GREEN_BACKGROUND_BRIGHT + WHITE_BRIGHT;
                System.out.println(CMD + "/create" + RESET);
                System.out.println(CMDD + "Создать новую заметку");
                System.out.println(CMD + "/edit номер_заметки");
                System.out.println(CMDD + "Редактировать содержимое заметки");

            }

            if (command.startsWith("/create")) {
                try {
                    Integer id = client.createNote();
                    System.out.println("Была создана новая заметка с номером " + id);
                } catch (Exception e) {
                    System.out.println("Ошибка! Ежидзе ушел в гости Совунье. И погряз в настойках.");
                }
            }

            if (command.startsWith("/edit")) {

                try {
                    System.out.println("Введите номер заметки для редактирования:");
                    int noteId = in.nextInt();
                    in.nextLine();


                    Note currentNote = client.viewNote(noteId);
                    if (currentNote != null) {
                        printNote(currentNote);

                        System.out.println("Введите новый заголовок (или enter, чтобы пропустить):");
                        String title = in.nextLine();

                        System.out.println("Введите новый текст заметки:");
                        System.out.println("Введите END, чтобы прекратить ввод.");
                        String text = "";

                        String buffer = in.nextLine();
                        while (!buffer.equals("END")) {
                            text += buffer;
                            buffer = in.nextLine();
                        }

                        Note updateNote = client.editNote(noteId,
                                title.equals("") ? currentNote.getTitle() : title, text);

                        if (updateNote != null) {
                            System.out.println("Еж обновил заметку за номером #" + noteId);
                        }

                    } else {
                        System.out.println("Еж не нашел заметки за номером #" + noteId);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Еж не смог справиться с возложенной миссией");
                }


            }

            if (command.equals("/view")) {
                try {
                    System.out.println("Введите номер заметки для просмотра:");
                    int noteId = in.nextInt();

                    Note currentNote = client.viewNote(noteId);
                    if (currentNote != null) {
                        printNote(currentNote);
                    } else {
                        System.out.println("Еж не нашел заметки за номером #" + noteId);
                    }

                } catch (Exception e) {
                    System.out.println("Еж не смог справиться с возложенной миссией");
                }
            }

            if (command.startsWith("/viewAll")) {
                try {

                    ArrayList<Note> notes = client.viewNotes();
                    for (int i = 0; i < notes.size(); i++) {
                        System.out.println(GREEN_BOLD + notes.get(i).getId() + " | " + notes.get(i).getTitle());
                        String text = notes.get(i).getContent();
                        System.out.println(text.substring(0, Math.min(text.length(), 50)));
                    }

                } catch (Exception e) {
                    System.out.println("Еж не смог справиться с возложенной миссией");
                }
            }

            if (command.startsWith("/delete")) {
                try {
                    System.out.println("Введите номер заметки для удаления:");
                    int noteId = in.nextInt();

                    Note currentNote = client.deleteNote(noteId);
                    if (currentNote != null) {
                        printNote(currentNote);
                        System.out.println("Еж удалил заметку за номером #" + noteId);
                    } else {
                        System.out.println("Еж не нашел заметки за номером #" + noteId);
                    }

                } catch (Exception e) {
                    System.out.println("Еж не смог справиться с возложенной миссией");
                }
            }

            if (command.startsWith("/exit")) {

                String[] questions = {"Вы уверены, что хотите выйти из программы?",
                        "Вы знаете, что из-за Вас будет плакать один Öзж.",
                        "Вам никогда не казалось, что Вы дурно воспитаны?",
                        "Вы платите налоги? Если нет, придется! Растраивайтесь."};

                for (int i = 0; i < questions.length; i++) {
                    System.out.println(questions[i]);
                    in.nextLine();
                }

                System.out.println("Adios. Я уЕЖаю от Вас...");
                break;
            }

        }

    }
}
