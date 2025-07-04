package Hangman;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameHangman {
    private static final ArrayList<String> WORDS = new ArrayList<>();
    private static final String STAR = "*";
    static GameHangman game;
    public String secretWord;
    private String mask;
    private static int countOfMistakes = 6;
    private static StringBuilder unnecessaryLetters;

    public GameHangman(String path) throws IOException {
        countOfMistakes = 6;
        unnecessaryLetters = new StringBuilder();
        initLibrary(path);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Я загадаю существительное в именительном падеже, а ты попробуешь его угадать, у тебя на это будет " + countOfMistakes + " попыток.");
        while (startGame()) {
            game = new GameHangman("src/Hangman/Words");

            game.secretWord = game.chooseWord();
            game.mask = game.maskWord(game.secretWord);

            while (checkAbilityToMove()) {
                String letter = enterLetter();
                if (game.validationEnterLetter(letter)) {
                    if (!game.checkLetterRepeat(letter)) {
                        if (game.checkContainsLetterInTheWord(game.secretWord, letter)) {
                            game.theWordContainsTheLetter(game.secretWord, game.mask, letter);
                            if (!game.gameOver()) {
                                game.messageYouWin(game.secretWord);
                                break;
                            }
                        } else {
                            game.wordNotContainsTheLetter();
                            if (checkAbilityToMove()) {
                            } else {
                                game.messageYouLoss(game.secretWord);
                            }
                        }
                    }else {
                        if (unnecessaryLetters.toString().contains(letter)) {
                            System.out.println("Ты уже вводил такую букву, введи другую");
                            continue;
                        }
                        unnecessaryLetters.append(letter.toUpperCase()).append(" ");
                        checkAbilityToMove();
                    }
                } else System.out.println("Не, ну ты вообще читаешь какие символы нужно вводить?! Попробуй заново, у тебя получится, я верю в тебя!");
            }
        }
        System.out.println("Запусти меня заново как появится желание сыграть, я буду ждать!");
    }

    private static boolean startGame() {
        System.out.println("Поиграем? Нажми \"Enter\" для начала игры или \"Пробел + Enter\" для выхода");
        Scanner sc = new Scanner(System.in);
        boolean isExit = true;
        while (isExit) {
            String answer = sc.nextLine();
            if ((answer).equalsIgnoreCase("")) {
                isExit = false;
            } else if ((answer).equalsIgnoreCase(" ")) {
                break;
            } else {
                System.out.println("Промахнулся! Нажми на кнопку \"Enter\" для начала игры или \"Пробел + Enter\" если нет желания играть");
            }
        }
        return !isExit;
    }

    private int getRandom(int quantity) {
        Random random = new Random();
        return random.nextInt(quantity);
    }

    private static void initLibrary(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while (reader.ready()) {
                WORDS.add(reader.readLine());
            }
        }
    }

    private String chooseWord() {
        return WORDS.get(getRandom(WORDS.size())).toUpperCase();
    }

    private String maskWord(String word) {
        return STAR.repeat(word.length());
    }

    private static String enterLetter() {
        System.out.println("Отгадай слово из " + game.secretWord.length() + " букв: " + game.mask);
        System.out.println("Ты уже использовал буквы: " + unnecessaryLetters);
        System.out.println("Введи 1 (одну) из 33 (тридцати трёх) букв русского языка, которая содержится в загаданном слове: ");
        Scanner scanner = new Scanner(System.in);
        return  scanner.nextLine().toUpperCase();
    }

    private boolean validationEnterLetter(String letter) {
        return letter.matches("[А-Я]{1}");
    }

    private boolean checkLetterRepeat(String letter) {
        if (!unnecessaryLetters.toString().contains(letter)) {
            unnecessaryLetters.append(letter.toUpperCase()).append(" ");
        } else {
            return true;
        }
        return false;
    }

    private boolean checkContainsLetterInTheWord(String word, String letter) {
        return word.contains(letter);
    }

    private void theWordContainsTheLetter(String secretWord, String mask, String letter) {
        System.out.println("Есть такая буква в этом слове!");
        game.mask = game.openLetterInTheMask(secretWord, mask, letter);
    }

    private void wordNotContainsTheLetter() {
        countOfMistakes--;
        drawHangman(countOfMistakes);
    }

    private static boolean checkAbilityToMove() {
        return countOfMistakes > 0;
    }

    private String openLetterInTheMask(String word, String mask, String letter) {
        char[] wordCharArray = word.toCharArray();
        char[] maskCharArray = mask.toCharArray();
        char symbol = letter.charAt(0);
        for (int i = 0; i < word.length(); i++) {
            if (maskCharArray[i] == STAR.charAt(0) && wordCharArray[i] == symbol) {
                maskCharArray[i] = symbol;
            }
        }
        return String.valueOf(maskCharArray);
    }

    private boolean gameOver() {
        return (game.mask.contains(STAR) && checkAbilityToMove());
    }

    private void messageYouWin(String word) {
        System.out.println("Ты выиграл, молодец! правильное слово \"" + word + "\" \n" +
                "(#^_^#)\n");
    }

    private void messageYouLoss(String secretWord) {
        System.out.println("Ты проиграл, было загадано слово \"" + secretWord + "\"");
    }

    private static void drawHangman(int count) {
        switch (count) {
            case (5) :
                System.out.println("Нет такой буквы в этом слове, осталось " + countOfMistakes + " ошибок\n" +
                        "  ||=========\n" +
                                   "  ||        |\n" +
                                   "  ||        |  \n" +
                                   "  ||       (>L<)\n" +
                                   "  ||           \n" +
                                   "  ||           \n" +
                                   "  ||           \n" +
                                   "  ||           \n" +
                                   "  ||           \n" +
                                   "  ||           \n" +
                                   "__||_______________");
                break;
            case (4) :
                System.out.println("Нет такой буквы в этом слове, осталось " + countOfMistakes + " ошибок\n" +
                        "  ||=========\n" +
                        "  ||        |\n" +
                        "  ||        |  \n" +
                        "  ||       (>L<)\n" +
                        "  ||        | |  \n" +
                        "  ||        | | \n" +
                        "  ||        | |   \n" +
                        "  ||        |_|  \n" +
                        "  ||           \n" +
                        "  ||           \n" +
                        "__||_______________");
                break;
            case (3) :
                System.out.println("Нет такой буквы в этом слове, осталось " + countOfMistakes + " ошибок\n" +
                        "  ||=========\n" +
                        "  ||        |\n" +
                        "  ||        |  \n" +
                        "  ||       (>L<)\n" +
                        "  ||      //| |  \n" +
                        "  ||     // | | \n" +
                        "  ||        | |   \n" +
                        "  ||        |_|  \n" +
                        "  ||           \n" +
                        "  ||           \n" +
                        "__||_______________");
                break;
            case (2) :
                System.out.println("Нет такой буквы в этом слове, осталось " + countOfMistakes + " ошибок\n" +
                        "  ||=========\n" +
                        "  ||        |\n" +
                        "  ||        |  \n" +
                        "  ||       (>L<)\n" +
                        "  ||      //| |\\\\  \n" +
                        "  ||     // | | \\\\ \n" +
                        "  ||        | |   \n" +
                        "  ||        |_|  \n" +
                        "  ||           \n" +
                        "  ||           \n" +
                        "__||_______________");
                break;
            case (1) :
                System.out.println("Нет такой буквы в этом слове, осталось " + countOfMistakes + " ошибок\n" +
                        "  ||=========\n" +
                        "  ||        |\n" +
                        "  ||        |  \n" +
                        "  ||       (>L<)\n" +
                        "  ||      //| |\\\\  \n" +
                        "  ||     // | | \\\\ \n" +
                        "  ||        | |   \n" +
                        "  ||        |_|  \n" +
                        "  ||       //  \n" +
                        "  ||    __//     \n" +
                        "__||_______________");
                break;
            case (0) :
                System.out.println("Нет такой буквы в этом слове, осталось " + countOfMistakes + " ошибок\n" +
                        "  ||=========\n" +
                        "  ||        |\n" +
                        "  ||        |  \n" +
                        "  ||       (+L+)\n" +
                        "  ||      //| |\\\\  \n" +
                        "  ||     // | | \\\\ \n" +
                        "  ||        | |   \n" +
                        "  ||        |_|  \n" +
                        "  ||       // \\\\\n" +
                        "  ||    __//   \\\\__\n" +
                        "__||_______________");
        }
    }
}