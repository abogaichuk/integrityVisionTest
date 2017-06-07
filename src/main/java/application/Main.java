package application;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * @author abogaichuk
 */
public class Main {

    private static final String FILE_NAME = "words.txt";

    public static void main(String[] args) throws IOException {
        final List<String> words = Files.lines(Paths.get(FILE_NAME))
                .filter(Main::isNotEmpty)
                .peek(String::trim)
                .map(String::intern)
                .collect(toList());

        Set<String> concatenated = findConcatenated(words);
        System.out.println("concatenated size: " + concatenated.size());

        String max = getMax(concatenated);
        System.out.println("max: " + max);
        concatenated.remove(max);
        System.out.println("second max: " + getMax(concatenated));
    }

    private static Set<String> findConcatenated(final List<String> words) {
        return words.stream().parallel()
                .filter(word -> isConcatenated(word, getParts(word, words)))
                .collect(toSet());
    }

    private static String getMax(Set<String> concatenated) {
        return concatenated.stream()
                .max((s1, s2) -> Integer.compare(s1.length(), s2.length()))
                .orElse("");
    }

    private static Set<String> getParts(String word, List<String> words) {
        return words.stream()
                .filter(s -> word.contains(s) && !s.equals(word))
                .collect(toSet());
    }

    private static boolean isConcatenated(String word, Set<String> parts) {
        if (parts.size() < 2)
            return false;

        String s = word;
        for (String part : parts)
            s = s.replace(part, "");

        return s.isEmpty();
    }

    private static boolean isNotEmpty(String s) {
        return !s.isEmpty();
    }
}
