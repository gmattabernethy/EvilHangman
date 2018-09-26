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
        HashMap<Integer, Set<String>> partitions = new HashMap<>();

        for(String s: set){
            int addAt = wordLength;

            if(s.contains(Character.toString(c))){
                addAt = AddAtIndex(s, c);
            }

            if(partitions.get(addAt) == null) partitions.put(addAt, new HashSet<>());
            partitions.get(addAt).add(s);
        }

        int max = 0;
        Iterator it = partitions.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Set<String> s = (HashSet)pair.getValue();
            if(s.size() > max) max = s.size();
        }

        set = EvilAlgorithm(partitions, max);

        return set;
    }

    private Set<String> EvilAlgorithm(Map<Integer, Set<String>> partitions, int max){
        Iterator it = partitions.entrySet().iterator();
        int min = wordLength;

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Set<String> s = (HashSet)pair.getValue();
            int charNum = CharNum((Integer)pair.getKey());
            if(charNum < min) min = charNum;
            if(s.size() < max) it.remove();
        }

        if(partitions.size() == 1) return partitions.entrySet().iterator().next().getValue();

        it = partitions.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Set<String> s = (HashSet)pair.getValue();
            int charNum = CharNum((Integer)pair.getKey());
            if(s.size() > min) it.remove();
        }

        if(partitions.size() == 1) return partitions.entrySet().iterator().next().getValue();

        return null;
    }

    private int CharNum(int i){
        if (i <= 1) {

            return i;
        }
        int remainder = i%2;

        return remainder + CharNum(i / 2);
    }

    private int AddAtIndex(String s, char c) {
        int addAt = 0;

        for (int i = 0; i < s.length(); i++){
            if(s.substring(i,i+1).equals(Character.toString(c))) addAt += (int)Math.pow(2,i);
        }

        return addAt;
    }

    public static void main(String[] args){
        File file = new File("test.txt");
        SetManager sm = new SetManager(file, 4);
        //sm.Partition('y');
        System.out.println(sm.AddAtIndex("crap", 'a'));
        System.out.println(sm.CharNum(sm.AddAtIndex("crap", 'a')));
    }

}
