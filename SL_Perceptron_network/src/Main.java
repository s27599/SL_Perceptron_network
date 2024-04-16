import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final String enTestString = "Higgs was born in the Elswick district of Newcastle upon Tyne, England, to Thomas Ware Higgs (1898–1962) and his wife Gertrude Maude née Coghill (1895–1969).[19][20][21][22][23] His father worked as a sound engineer for the BBC, and as a result of childhood asthma, together with the family moving around because of his father's job and later World War II, Higgs missed some early schooling and was taught at home.[24] When his father relocated to Bedford, Higgs stayed behind in Bristol with his mother, and was largely raised there. He attended Cotham Grammar School in Bristol from 1941 to 1946,[19][25] where he was inspired by the work of one of the school's alumni, Paul Dirac, a founder of the field of quantum mechanics.[22]";
        final String czTestString = "V obecné teorii relativity (OTR) je gravitace vysvětlena zakřivením časoprostoru. Toto zakřivení vzniká přítomností hmoty a energie a projevuje se např. tím, že součet úhlů v trojúhelníku nemusí být 180°, nebo tím, že lokálně nejrovnější čáry – geodetiky nejsou na rozdíl od přímek vždy „rovné“. Pohyb těles v gravitačním poli probíhá po geodetikách tak, že jejich sklon k časové ose udává rychlost tělesa. Geodetiky bývají často v populární literatuře označovány za nejkratší spojnice, což však není pravda vždy. Pro slabá gravitační pole dává OTR stejné předpovědi jako Newtonova teorie gravitace. Schopnost gravitačního působení lze v Newtonově gravitační teorii určovat nejen gravitační silou.";
        final String grTestString = "Die Ölnachfrage hängt primär vom Wachstum des Bruttosozialprodukts, von strukturellen Veränderungen der Wirtschaft und vom technischen Fortschritt sowie der Entwicklung des Ölpreises ab. Die Nachfrage von erschöpfbaren Gütern kann unter bestimmten Bedingungen verschwinden, so durch technologischen Fortschritt oder die Einführung eines Substitutes. Auch der Fall der Enteignung der Ressource wird als Verschwinden der Nachfrage modelliert.[9] In unterschiedlichen makroökonomischen Publikationen wurde gezeigt, dass je nach Einschätzung der Eintrittswahrscheinlichkeit eines dieser oben genannten Szenarien, der Produzent bzw. Förderer eine Beschleunigung der Produktion bzw. Exploration versucht.[";

        Network network = new Network();
        for (int i = 0; i < 100; i++) {
            network.learn("data");
        }

//        System.out.println(network.test(enTestString));
//        System.out.println(network.test(czTestString));
//        System.out.println(network.test(grTestString));
        Scanner scanner = new Scanner(System.in);
        String answ = "";
        do {
            System.out.println("1 - przykładowe dane");
            System.out.println("2 - własne dane");
            System.out.println("q - wyjście");
            answ = scanner.nextLine();


            switch (answ) {
                case "1":
                    System.out.println(network.test(enTestString));
                    System.out.println(network.test(czTestString));
                    System.out.println(network.test(grTestString));
                    break;
                case "2":
                    System.out.println("podaj tekst: ");
                    System.out.println(network.test(scanner.nextLine()));

            }


        } while (!answ.equals("q"));


    }
}