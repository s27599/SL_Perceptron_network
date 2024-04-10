public class Main {
    public static void main(String[] args) {

Network network = new Network();
        for (int i = 0; i < 100; i++) {
            network.learn("data");
        }

        network.test();
    }
}