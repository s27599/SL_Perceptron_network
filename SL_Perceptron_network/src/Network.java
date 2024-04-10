import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Network {

    Map<String, Perceptron> perceptrons;


    public Network() {
        perceptrons = new HashMap<>();
    }

    public void test() {
        String enTestString = "Higgs was born in the Elswick district of Newcastle upon Tyne, England, to Thomas Ware Higgs (1898–1962) and his wife Gertrude Maude née Coghill (1895–1969).[19][20][21][22][23] His father worked as a sound engineer for the BBC, and as a result of childhood asthma, together with the family moving around because of his father's job and later World War II, Higgs missed some early schooling and was taught at home.[24] When his father relocated to Bedford, Higgs stayed behind in Bristol with his mother, and was largely raised there. He attended Cotham Grammar School in Bristol from 1941 to 1946,[19][25] where he was inspired by the work of one of the school's alumni, Paul Dirac, a founder of the field of quantum mechanics.[22]";
        String slTestString = "Bol autorom teoretického modelu známeho ako Higgsov mechanizmus, vďaka ktorému v roku 1964[3] predpovedal existenciu častice dnes nazývanej Higgsov bozón. Článok, v ktorom predpokladal existenciu subatomárnej častice, zaslal časopisu Physical Letters, no editori z CERN-u ho odmietli s tým, že „je bez zjavného významu pre fyziku“. Na čo Higgs článok doplnil o nový odsek a poslal ho ďalšiemu časopisu Physical Review Letters, ktorý ho vydal na jeseň 1964.[4][5] 4. júla 2012 bola existencia tejto častice predbežne dokázaná dvomi nezávislými vedeckými tímami (ATLAS a CMS) pracujúcimi na Veľkom hadrónovom urýchlovači v Európskej organizácii pre jadrový výskum.[6] Dnes je Higgsov bozón súčasťou štandardného modelu.";
        String czTestString = "Peter Ware Higgs, CH (29. května 1929 Newcastle upon Tyne, Spojené království[1] – 8. dubna 2024 Edinburgh, Spojené království[2][3]) byl britský teoretický fyzik, který v roce 1964[4] předpověděl existenci částice nazvané později Higgsův boson, za což mu byla udělena Nobelova cena za rok 2013.[5] Existence této částice by měla vysvětlit původ hmotnosti ostatních elementárních částic.[4] Existence Higgsova bosonu byla potvrzena experimenty CMS a ATLAS na urychlovači LHC v CERNu v Ženevě 4. července 2012.";


        double english = perceptrons.get("english").testFinal(processMapToDouble(countLetters(enTestString.toCharArray())));
        System.out.println("english" + english);
        double czech = perceptrons.get("czech").testFinal(processMapToDouble(countLetters(enTestString.toCharArray())));
        System.out.println("czech" + czech);
        double slovak = perceptrons.get("slovak").testFinal(processMapToDouble(countLetters(enTestString.toCharArray())));
        System.out.println("slovak" + slovak);

        double english1 = perceptrons.get("english").testFinal(processMapToDouble(countLetters(slTestString.toCharArray())));
        System.out.println("\nenglish" + english1);
        double czech1 = perceptrons.get("czech").testFinal(processMapToDouble(countLetters(slTestString.toCharArray())));
        System.out.println("czech" + czech1);
        double slovak1 = perceptrons.get("slovak").testFinal(processMapToDouble(countLetters(slTestString.toCharArray())));
        System.out.println("slovak" + slovak1);

        double english2 = perceptrons.get("english").testFinal(processMapToDouble(countLetters(czTestString.toCharArray())));
        System.out.println("\nenglish" + english2);
        double czech2 = perceptrons.get("czech").testFinal(processMapToDouble(countLetters(czTestString.toCharArray())));
        System.out.println("czech" + czech2);
        double slovak2 = perceptrons.get("slovak").testFinal(processMapToDouble(countLetters(czTestString.toCharArray())));
        System.out.println("slovak" + slovak2);
    }


    public void learn(String path) {
        try {
            for (final File fileEntry : Objects.requireNonNull(new File(path).listFiles())) {
                if (fileEntry.isDirectory()) {
//                    String dirName = file.getParent().getFileName().toString();
                    String dirName = fileEntry.getName();
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


                    try (
                            FileChannel inputChannel = FileChannel.open(file, StandardOpenOption.READ, StandardOpenOption.CREATE);
                    ) {
                        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
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
