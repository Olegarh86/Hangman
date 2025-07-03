package Hangman;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameHangman {
    private final ArrayList<String> WORDS = new ArrayList<>();
    static GameHangman game;
    public String word = null;
    private String mask = null;
    private static int count = 6;
    private static StringBuilder sb =new StringBuilder();

    public GameHangman() {
        count = 6;
        sb.delete(0, sb.length());
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Я загадаю существительное в именительном падеже, а ты попробуешь его угадать, у тебя на это будет 6 попыток. \n" +
                "Поиграем? Нажми \"Enter\" для начала игры или \"Пробел + Enter\" для выхода");
        while (startGame()) {
            game = new GameHangman();
            game.word = game.chooseWord("src/Hangman/Words").toUpperCase();
            game.mask = game.maskWord(game.word);

            while (count > 0) {
                String letter = move();
                if (game.validation(letter)) {
                    if (game.checkLetter(game.word, letter)) {
                        System.out.println("Есть такая буква в этом слове!");
                        sb.append(letter.toUpperCase()).append(" ");
                        game.mask = game.openLetter(game.word, game.mask, letter);
                        if (!game.mask.contains("*")) {
                            System.out.println("Ты выиграл, молодец! правильное слово \"" + game.word + "\" \n" +
                                    "(#^_^#)\n" +
                                    "Сыграем ещё раз?\n" +
                                    "Нажми на кнопку \"Enter\" для начала игры или \"Пробел + Enter\" если нет желания играть");
                            break;
                        }
                    } else {
                        if (sb.toString().contains(letter)) {
                            System.out.println("Была уже такая буква, введи другую");
                            continue;
                        }
                        sb.append(letter.toUpperCase()).append(" ");
                        countOfMoves();
                    }
                } else System.out.println("Не, ну ты вообще читаешь какие символы нужно вводить?! Пробуй заново, у тебя не получилось");
            }
        }
        System.out.println("Запусти меня заново как появится желание сыграть, я буду ждать!");
    }

    private static boolean startGame() throws IOException {
        Scanner sc = new Scanner(System.in);
        boolean result = false;
        boolean isExit = false;
        while (!isExit) {
            String answer = sc.nextLine();
            if ((answer).equalsIgnoreCase("")) {
                result = true;
                isExit = true;
            } else if ((answer).equalsIgnoreCase(" ")) {
                break;
            } else {
                System.out.println("Промахнулся! Нажми на кнопку \"Enter\" для начала игры или \"Пробел + Enter\" если нет желания играть");
            }
        }
        return result;
    }

    private int getRandom(int quantity) {
        Random random = new Random();
        return random.nextInt(quantity);
    }

    private String chooseWord(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while (reader.ready()) {
                WORDS.add(reader.readLine());
            }
        }
        return WORDS.get(getRandom(WORDS.size()));
    }

    private String maskWord(String word) {
        int length = word.length();
        return "*".repeat(length);
    }

    private static String move() {
        System.out.println("Отгадай слово из " + game.word.length() + " букв: " + game.mask);
        System.out.println("Ты уже использовал буквы: " + sb);
        System.out.println("Введи 1 (одну) из 33 (тридцати трёх) букв русского языка, которая содержится в загаданном слове: ");
        Scanner scanner = new Scanner(System.in);
        return  scanner.nextLine().toUpperCase();
    }

    private boolean checkLetter(String word, String letter) {
        if (sb.toString().contains(letter)) {
            return false;
        }
        return word.contains(letter);
    }

    private static void countOfMoves() throws IOException {
        count--;
        if (count > 0) {
            System.out.println("Нет такой буквы в этом слове, осталось " + count + " ошибок");
            drawHangman(count);
        } else {
            System.out.println("Нет такой буквы в этом слове. Вы не угадали слово \"" + game.word + "\"");
            drawHangman(count);
            System.out.println("Сыграем ещё раз?\n" +
                    "Нажми на кнопку \"Enter\" для начала игры или \"Пробел + Enter\" если нет желания играть");
            count--;
        }
    }

    private String openLetter(String word, String mask, String letter) {
        char[] charsWord = word.toCharArray();
        char[] charsMask = mask.toCharArray();
        char star = '*';
        char symbol = letter.charAt(0);
        for (int i = 0; i < word.length(); i++) {
            if (charsMask[i] == star && charsWord[i] == symbol) {
                charsMask[i] = symbol;
            }
        }
        return String.valueOf(charsMask);
    }

    private boolean validation(String letter) {
        if (letter != null) {
            if (letter.matches("[А-Я]{1}")) {
                return true;
            }
        }
        return false;
    }
    private static void drawHangman(int count) {
        switch (count) {
            case (5) :
                System.out.println("  ||=========\n" +
                                   "  ||        |\n" +
                                   "  ||        |  \n" +
                                   "  ||       (>L<)\n" +
                                   "  ||           \n" +
                                   "  ||           \n" +
                                   "  ||           \n" +
                                   "  ||           \n" +
                                   "  ||           \n" +
                                   "  ||           \n" +
                                   "  ||           \n" +
                                   "__||_______________");
                break;
            case (4) :
                System.out.println("  ||=========\n" +
                        "  ||        |\n" +
                        "  ||        |  \n" +
                        "  ||       (>L<)\n" +
                        "  ||        | |  \n" +
                        "  ||        | | \n" +
                        "  ||        | |   \n" +
                        "  ||        |_|  \n" +
                        "  ||           \n" +
                        "  ||           \n" +
                        "  ||           \n" +
                        "__||_______________");
                break;
            case (3) :
                System.out.println("  ||=========\n" +
                        "  ||        |\n" +
                        "  ||        |  \n" +
                        "  ||       (>L<)\n" +
                        "  ||      //| |  \n" +
                        "  ||     // | | \n" +
                        "  ||        | |   \n" +
                        "  ||        |_|  \n" +
                        "  ||           \n" +
                        "  ||           \n" +
                        "  ||           \n" +
                        "__||_______________");
                break;
            case (2) :
                System.out.println("  ||=========\n" +
                        "  ||        |\n" +
                        "  ||        |  \n" +
                        "  ||       (>L<)\n" +
                        "  ||      //| |\\\\  \n" +
                        "  ||     // | | \\\\ \n" +
                        "  ||        | |   \n" +
                        "  ||        |_|  \n" +
                        "  ||           \n" +
                        "  ||           \n" +
                        "  ||           \n" +
                        "__||_______________");
                break;
            case (1) :
                System.out.println("  ||=========\n" +
                        "  ||        |\n" +
                        "  ||        |  \n" +
                        "  ||       (>L<)\n" +
                        "  ||      //| |\\\\  \n" +
                        "  ||     // | | \\\\ \n" +
                        "  ||        | |   \n" +
                        "  ||        |_|  \n" +
                        "  ||       //  \n" +
                        "  ||    __//     \n" +
                        "  ||           \n" +
                        "__||_______________");
                break;
            case (0) :
                System.out.println("  ||=========\n" +
                        "  ||        |\n" +
                        "  ||        |  \n" +
                        "  ||       (+L+)\n" +
                        "  ||      //| |\\\\  \n" +
                        "  ||     // | | \\\\ \n" +
                        "  ||        | |   \n" +
                        "  ||        |_|  \n" +
                        "  ||       // \\\\\n" +
                        "  ||    __//   \\\\__\n" +
                        "  ||            \n" +
                        "__||_______________");
        }
    }
}