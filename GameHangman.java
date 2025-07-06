package Hangman;

import java.io.*;
import java.util.*;

public class GameHangman  {
    private static final ArrayList<String> LIBRARY = new ArrayList<>();
    private static final String PATH = "src/Hangman/Words";
    private static final String STAR = "*";
    private static Set<String> usedLettersSet;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private String secretWord;
    private String mask;
    private static int countOfMistakes = 6;

    private GameHangman() {
        countOfMistakes = 6;
        usedLettersSet = new HashSet<>(33);
    }

    public static void main(String[] args) throws IOException {
        gameLoop();
    }

    private static void gameLoop() throws IOException {
        System.out.println("Я загадаю существительное в именительном падеже, а ты попробуешь его угадать, у тебя на это будет " + countOfMistakes + " попыток.");
        while (startGame()) {
            initLibrary(PATH);
            GameHangman game = new GameHangman();
            game.secretWord = game.chooseRandomWord();
            game.mask = game.maskWord(game.secretWord);

            while (checkAbilityToMove()) {
                String newLetter = enterLetter(game);
                if (game.validationEnterLetter(newLetter)) {
                    if (!game.checkLetterRepeat(newLetter)) {
                        if (game.checkContainsLetterInSecretWord(game.secretWord, newLetter)) {
                            game.wordContainsLetter(game, game.secretWord, game.mask, newLetter);
                            if (game.checkGameOver(game)) {
                                game.reportYouWin(game.secretWord);
                                break;
                            }
                        } else {
                            game.wordNotContainsLetter();
                            if (game.checkGameOver(game)) {
                                game.reportYouLoss(game.secretWord);
                            }
                        }
                    } else {
                        System.out.println("Ты уже вводил такую букву, введи другую");
                    }
                } else {
                    System.out.println("Не, ну ты вообще читаешь какие символы нужно вводить?! Попробуй заново, у тебя получится, я верю в тебя!");
                }
            }
        }
        System.out.println("Запусти меня заново как появится желание сыграть, я буду ждать!");
    }

    private static boolean startGame() {
        System.out.println("Поиграем? Нажми \"Enter\" для начала игры или \"Пробел + Enter\" для выхода");
        boolean isExit = true;
        while (isExit) {
            String answer = scanner.nextLine();
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

    private static void initLibrary(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while (reader.ready()) {
                LIBRARY.add(reader.readLine());
            }
        }
    }

    private String chooseRandomWord() {
        return LIBRARY.get(random.nextInt(LIBRARY.size())).toUpperCase();
    }

    private String maskWord(String word) {
        return STAR.repeat(word.length());
    }

    private static String enterLetter(GameHangman game) {
        System.out.println("Отгадай слово из " + game.secretWord.length() + " букв: " + game.mask + "\n" +
                "Ты уже использовал буквы: " + usedLettersSet + "\n" +
                "Введи 1 (одну) из 33 (тридцати трёх) букв русского языка, которая содержится в загаданном слове: ");
        return  scanner.nextLine().toUpperCase();
    }

    private boolean validationEnterLetter(String letter) {
        return letter.matches("[А-Я]{1}");
    }

    private boolean checkLetterRepeat(String letter) {
        if (!usedLettersSet.toString().contains(letter)) {
            usedLettersSet.add(letter.toUpperCase());
        }else {
            return true;
        }
        return false;
    }

    private boolean checkContainsLetterInSecretWord(String word, String letter) {
        return word.contains(letter);
    }

    private void wordContainsLetter(GameHangman game, String secretWord, String mask, String letter) {
        System.out.println("Есть такая буква в этом слове!");
        game.mask = game.openLetterInTheMask(secretWord, mask, letter);
    }

    private void wordNotContainsLetter() {
        countOfMistakes--;
        drawHangman(countOfMistakes);
    }

    private static boolean checkAbilityToMove() {
        return countOfMistakes > 0;
    }

    private String openLetterInTheMask(String secretWord, String mask, String newLetter) {
        char[] secretWordCharArray = secretWord.toCharArray();
        char[] maskCharArray = mask.toCharArray();
        char symbol = newLetter.charAt(0);
        for (int i = 0; i < secretWord.length(); i++) {
            if (maskCharArray[i] == STAR.charAt(0) && secretWordCharArray[i] == symbol) {
                maskCharArray[i] = symbol;
            }
        }
        return String.valueOf(maskCharArray);
    }

    private boolean checkGameOver(GameHangman game) {
        return !(game.mask.contains(STAR) && checkAbilityToMove());
    }

    private void reportYouWin(String word) {
        System.out.println("Ты выиграл, молодец! правильное слово \"" + word + "\" \n" +
                "(#^_^#)\n");
    }

    private void reportYouLoss(String secretWord) {
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
                System.out.println("Нет такой буквы в этом слове\n" +
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