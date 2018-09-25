import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

public class SetManager {
    public Set<String> set;
    private int wordLength;

    public SetManager(File dictionary, int wordLength){
        this.wordLength = wordLength;
        set = new HashSet<>();

        try {
            Scanner in = new Scanner(dictionary);
            String str;

            while(in.hasNext()) {
                str = in.next();
                boolean word = true;

                for (int i = 0; i < str.length(); i++){
                    if(!Character.isLetter(str.charAt(i))) word = false;

                }

                if(word && str.length() == wordLength) set.add(str);
            }

            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Set<String> Partition(char c){
        ArrayList<Set<String>> partitions = new ArrayList<>();
        int sizeOfPartitions = (int)Math.pow(2, wordLength);
        for (int i = 0; i < sizeOfPartitions; i++) partitions.add(new HashSet<>());

        for(String s: set){
            int addAt = wordLength;

            if(s.contains(Character.toString(c))){
                addAt = AddAtIndex(s, c);
            }

            partitions.get(addAt).add(s);
        }

        int max = 1;

        for (Set<String> s: partitions){
            if(s.size() > max) max = s.size();
        }

        for (Iterator<Set<String>> it = partitions.listIterator(); it.hasNext(); ) {
            Set<String> s = it.next();
            if (s.size() != max) {
                it.remove();
            }
        }

        //TODO implement EVIL ALGORITHM tie breaker

        return set;
    }

    private int AddAtIndex(String s, char c) {
        int i = s.indexOf(c);
        int addAt = i;
        int count = 1;

        while (i >= 0){
            i = s.indexOf(c, i+1);
            addAt+= wordLength+i-count;
        }

        return addAt;
    }

    public static void main(String[] args){
        File file = new File("test.txt");
        SetManager sm = new SetManager(file, 5);
        sm.Partition('y');
    }

}
