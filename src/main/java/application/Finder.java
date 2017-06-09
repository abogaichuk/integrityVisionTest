package application;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * @author abogaichuk
 */
public class Finder {

    private static final String FILE_NAME = "words.txt";

    public static void main(String[] args) throws IOException {
        new Finder().process();
    }

    public void process() throws IOException {
        final List<String> words = Files.lines(Paths.get(FILE_NAME))
                .filter(this::isNotEmpty)
                .peek(String::trim)
                .map(String::intern)
                .collect(toList());

        long start = new Date().getTime();
        Set<String> concatenated = findConcatenated(words);
        System.out.println("execute time: " + (new Date().getTime() - start));
        System.out.println("concatenated size: " + concatenated.size());

        String max = getMax(concatenated);
        System.out.println("max: " + max);
        concatenated.remove(max);
        System.out.println("second max: " + getMax(concatenated));
    }

    private Set<String> findConcatenated(final List<String> words) {
        return words.stream().parallel()
                .filter(word -> isConcatenated(word, getParts(word, words)))
                .collect(toSet());
    }

    private String getMax(Set<String> words) {
        return words.stream()
                .max((s1, s2) -> Integer.compare(s1.length(), s2.length()))
                .orElse("");
    }

    private List<String> getParts(String word, List<String> words) {
        return words.stream()
                .filter(s -> word.contains(s) && !s.equals(word))
                .collect(toList());
    }

    private boolean isConcatenated(String word, List<String> parts) {
        if (parts.size() < 2)
            return false;
        if (word.isEmpty())
            return true;

        for (String part : parts) {
            if (word.startsWith(part)) {
                boolean result = isConcatenated(word.substring(part.length()), parts);
                if (result)
                    return true;
            }
        }
        return false;
    }

    private boolean isNotEmpty(String s) {
        return !s.isEmpty();
    }
}
