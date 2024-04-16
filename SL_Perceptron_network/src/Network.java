import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Network {

    Map<String, Perceptron> perceptrons;


    public Network() {
        perceptrons = new HashMap<>();
    }

    public String test(String TestString) {
        Map<String, Double> resMap = new HashMap<>();
        for (String curr : perceptrons.keySet()) {
            resMap.put(curr, perceptrons.get(curr).testFinal(processMapToDouble(countLetters(TestString.toCharArray()))));
        }
        return Collections.max(resMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    }


    public void learn(String path) {
        try {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            for (final File fileEntry : Objects.requireNonNull(new File(path).listFiles())) {
                if (fileEntry.isDirectory()) {
                    String dirName = fileEntry.getName();
                    if (dirName.charAt(0) != '#')
                        if (!perceptrons.containsKey(dirName)) {
                            perceptrons.put(dirName, new Perceptron(dirName));
                        }
                }
            }

            Files.walkFileTree(Path.of(path), new FileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String dirName = file.getParent().getFileName().toString();

                    if (perceptrons.containsKey(dirName))
                        try (
                                FileChannel inputChannel = FileChannel.open(file, StandardOpenOption.READ, StandardOpenOption.CREATE);
                        ) {
                            inputChannel.read(buffer);
                            buffer.flip();
                            CharBuffer decoded = StandardCharsets.UTF_8.decode(buffer);
                            Map<Character, Double> letters = countLetters(decoded.array());
                            double[] primitiveValues = processMapToDouble(letters);

                            perceptrons.get(dirName).learn(primitiveValues, 1);
                            for (Map.Entry<String, Perceptron> entry : perceptrons.entrySet()) {
                                if (!entry.getKey().equals(dirName)) {
                                    entry.getValue().learn(primitiveValues, 0);
                                }
                            }
                            buffer.clear();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<Character, Double> countLetters(char[] chars) {
        Map<Character, Double> letters = new HashMap<>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (char letter : alphabet) {
            letters.put(letter, 0.0);
        }

        int size = 0;

        for (char c : chars) {
            c = Character.toLowerCase(c);
            if (letters.containsKey(c)) {
                letters.put(c, letters.get(c) + 1);
                size++;
            }
        }
        for (Map.Entry<Character, Double> entry : letters.entrySet()) {
            letters.put(entry.getKey(), entry.getValue() / size);
        }


        return letters;
    }

    private double[] processMapToDouble(Map<Character, Double> letters) {
        Collection<Double> collection = letters.values();
        Double[] values = collection.toArray(new Double[0]);
        return Arrays.stream(values).mapToDouble(Double::doubleValue).toArray();

    }


}
