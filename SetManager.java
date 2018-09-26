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
            int addAt = AddAtIndex(s, c);

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

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Set<String> s = (HashSet)pair.getValue();
            if(s.size() < max) it.remove();
        }

        it = partitions.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int i = (int)pair.getKey();
            if(i == 0) return (Set<String>)pair.getValue();

        }

        if(partitions.size() > 1) Tiebreaker2(partitions);
        if(partitions.size() > 1) Tiebreaker3(partitions);

        while (partitions.size() > 1){
            partitions = Tiebreaker4(partitions);
            Tiebreaker3(partitions);
        }

        return partitions.entrySet().iterator().next().getValue();
    }

    private void Tiebreaker2(Map<Integer, Set<String>> partitions){
        Iterator it = partitions.entrySet().iterator();
        int min = wordLength;

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int charNum = NumOfChar((Integer)pair.getKey());
            if(charNum < min) min = charNum;
        }

        it = partitions.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int charNum = NumOfChar((Integer)pair.getKey());
            if(charNum > min) it.remove();
        }
    }

    private void Tiebreaker3(Map<Integer, Set<String>> partitions){
        Iterator it = partitions.entrySet().iterator();
        int rightMost = 0;

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int rightIndex = Rightmost((Integer)pair.getKey(), 0);
            if(rightIndex > rightMost) rightMost = rightIndex;
        }

        it = partitions.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int rightIndex = Rightmost((Integer)pair.getKey(), 0);
            if(rightIndex < rightMost) it.remove();
        }
    }

    private Map<Integer, Set<String>> Tiebreaker4(Map<Integer, Set<String>> partitions){
        Map<Integer, Set<String>> truncated = new HashMap<>();
        int rightmost = Rightmost(partitions.entrySet().iterator().next().getKey(), 0);
        Iterator it = partitions.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int truncatedKey = (int)pair.getKey() - (int)Math.pow(2,rightmost-1);
            truncated.put(truncatedKey,(Set<String>) pair.getValue());
        }

        return truncated;
    }

    private int Rightmost(int i, int count){
        count ++;

        if (i == 1) return count;
        if(i == 0) return 0;

        int rightmost = Rightmost(i / 2, count);

        if (rightmost > 0) return rightmost;
        if(i%2 > 0) return count;

        return 0;
    }

    private int NumOfChar(int i){
        if (i <= 1) {

            return i;
        }
        int remainder = i%2;

        return remainder + NumOfChar(i / 2);
    }

    private int AddAtIndex(String s, char c) {
        int addAt = 0;

        for (int i = 0; i < s.length(); i++){
            if(s.substring(i,i+1).equals(Character.toString(c))) addAt += (int)Math.pow(2,i);
        }

        return addAt;
    }
/*
    public static void main(String[] args){
        File file = new File("test.txt");
        SetManager sm = new SetManager(file, 7);
        sm.Partition('e');
        for(String s : sm.set) System.out.println(s);
    }
*/
}
