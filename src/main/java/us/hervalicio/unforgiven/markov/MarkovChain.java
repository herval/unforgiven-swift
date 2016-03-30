package us.hervalicio.unforgiven.markov;

import java.util.*;

/**
 * Created by herval on 3/30/16.
 */
public class MarkovChain {
    private final Random rnd = new Random();
    private final Map<String, List<String>> chains = new HashMap<>();
    private final List<String> openers = new ArrayList<>();

    public void load(List<String> sequence) {
        String head = sequence.get(0);
        if(!openers.contains(head)) {
            openers.add(head);
        }

        load(head, drop(sequence, 1));
    }

    private void load(String head, List<String> tail) {
        if(tail.size() <= 1) {
            return;
        }

        String next = tail.get(0);
        put(head, next);

        load(next, drop(tail, 1));
    }

    private List<String> drop(List<String> list, int q) {
        return list.subList(q, list.size());
    }

    private void put(String key, String value) {
        List<String> nextForHead = chains.getOrDefault(key, new ArrayList<>());
        nextForHead.add(value);
        chains.put(key, nextForHead);
    }

    private String random(List<String> list) {
        if(list.isEmpty()) {
            return null;
        }

        if(list.size() == 1) {
            return list.get(0);
        }
        return list.get(rnd.nextInt(list.size() - 1));
    }

    public String[] take(int maxWords) {
        String head = random(openers);

        List<String> words = new ArrayList<>();

        String prev = head;
        for (int i = 0; i < maxWords; i++) {
            words.add(prev);
            List<String> next = chains.getOrDefault(prev, new ArrayList<>());
            String word = random(next);
            if(word != null) {
                prev = word;
            } else {
                break;
            }
        }

        return words.toArray(new String[0]);
    }
}
