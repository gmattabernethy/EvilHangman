package hangman;

import java.io.File;
import java.io.InvalidObjectException;
import java.util.Scanner;
import java.util.*;

public class Game implements hangman.IEvilHangmanGame{
    private List<Character> guessedLetters;
    private SetManager sm;
    private int guesses;
    private String word;

    public Game(){}

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        if(!Character.isLetter(guess)){
            return null;
        }
        guess = Character.toLowerCase(guess);

        if(guessedLetters.contains(guess) ) throw new GuessAlreadyMadeException();

        guesses--;
        guessedLetters.add(guess);
        Collections.sort(guessedLetters);

        return sm.Partition(guess);
    }

    private int updateWord(Set<String> set, char ch){
        StringBuilder builder = new StringBuilder();
        Iterator<String> it = set.iterator();
        String s = it.next();
        int count = 0;
        for (int i = 0; i < s.length(); i++){
            if(s.substring(i,i+1).equals(Character.toString(ch))){
                builder.append(ch);
                count++;
            }
            else builder.append(word.substring(i,i+1));
        }

        this.word = builder.toString();
        return count;
    }

    @Override
    public void startGame(File dictionary, int wordLength) {
        guessedLetters = new ArrayList<Character>();

        word = "";
        for(int i = 0; i < wordLength; i++) word+="-";

        sm = new SetManager(dictionary, wordLength);
    }

    public static void main(String[] args){
        int wordLength = Integer.parseInt(args[1]);
        Game game = new Game();
        game.guesses = Integer.parseInt(args[2]);
        File file = new File(args[0]);
        game.startGame(file, wordLength);

        try {
            Scanner in = new Scanner(System.in);
            char ch;
            Set<String> set = new HashSet<String>();

            while (game.guesses > 0){
                System.out.println("You have " + game.guesses + " guesses left");
                System.out.print("Used letters: ");
                for(char c : game.guessedLetters) System.out.print(c + " ");
                System.out.println();
                System.out.println("Word: " + game.word);
                System.out.print("Enter guess: " );

                try {
                    String str = in.nextLine();
                    if(str.length() != 1) throw new InvalidObjectException(str);
                    ch = str.charAt(0);
                    set = game.makeGuess(ch);
                    if(set == null) throw new InvalidObjectException(str);
                    int numOfLetter = game.updateWord(set, ch);
                    if(numOfLetter > 0){
                        System.out.println("Yes, there is " + numOfLetter + " " + ch + "\n");
                        game.guesses++;    
                    }
                    else System.out.println("Sorry, there are no " + ch + "'s\n");
                }catch (GuessAlreadyMadeException ex){
                    System.out.println("You already used that letter");
                }catch (InvalidObjectException ex) {
                    System.out.println("Invalid input");
                }

                if(set != null && set.size() == 1 && game.word.equals(set.iterator().next().toString())){
                    System.out.println("You win!\nThe word was: " + set.iterator().next().toString());
                    break;
                }
            }

            if(set.size() > 1){
                System.out.println("You lose!\nThe word was: " + set.iterator().next().toString());
            }

            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
