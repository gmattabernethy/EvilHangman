import java.io.File;
import java.util.Set;

public class Game implements hangman.IEvilHangmanGame{
    private char[] guessedLetters;
    private SetManager sm;
    private int guesses;

    Game(int guesses){
        guessedLetters = new char[26];
        this.guesses = guesses;
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        if(!Character.isLetter(guess)){
            System.out.println("Invalid input");
            return null;
        }

        guess = Character.toLowerCase(guess);

        if(guessedLetters[guess-'a'] != 0 ) throw new GuessAlreadyMadeException();

        guesses--;
        guessedLetters[guess-'a'] = guess;

        return sm.Partition(guess);
    }

    @Override
    public void startGame(File dictionary, int wordLength) {
        sm = new SetManager(dictionary, wordLength);
    }

    public static void main(String[] args){
        Game game = new Game(26);
        File file = new File("dictionary.txt");
        game.startGame(file, 2);
        System.out.println(game.sm.set.size());
        try {
            game.makeGuess('A');
            System.out.println(game.guesses);
            game.makeGuess('e');
            System.out.println(game.guesses);
            game.makeGuess('o');
            System.out.println(game.guesses);
        }catch (GuessAlreadyMadeException ex){
            System.out.println("You already used that letter");
        }
        System.out.println(game.guessedLetters[0]);

        System.out.println(game.sm.set.size());

    }
}
