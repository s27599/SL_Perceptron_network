import java.util.Arrays;

public class Perceptron {


    private double[] weights;
    private double threshold;
    private final double learningConst;
    private String name;

    public Perceptron(String name) {
        this.name = name;
//        this.weights = new double[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
        this.weights = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        this.threshold = 0.5;
        this.learningConst = 0.2;
    }

    public double testFinal(double[] inputs) {
        if (inputs.length != weights.length) {
            throw new IllegalArgumentException("you must have the same amount of inputs and weights");
        }
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }
        return sum - threshold;
    }


    public int test(double[] inputs) {
        if (inputs.length != weights.length) {
            throw new IllegalArgumentException("you must have the same amount of inputs and weights");
        }
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }
        if (sum >= threshold) {
            return 1;
        } else {
            return 0;
        }
    }

//    public int test(List<String[]> list) {
//        double counter = 0;
//        double correct = 0;
//
//        for (int i = 0; i < list.size(); i++) {
//
//int answCode = 0;
//            double[] values = new double[list.get(i).length - 1];
//            for (int j = 0; j < list.get(i).length - 1; j++) {
//                values[j] = Double.parseDouble(list.get(i)[j]);
//            }
//            int percAnsw = test(values);
//            counter++;
//            if (percAnsw == answCode) {
//                correct++;
//            }
//        }
//        return 0;
//
//    }

//    public void learn(List<String[]> list) {
//
//        for (int i = 0; i < list.size(); i++) {
//            String answ = list.get(i)[list.get(i).length - 1];
//            int answCode = answ.equals("Iris-setosa") ? 0 : 1;
//            double[] values = new double[list.get(i).length - 1];
//            for (int j = 0; j < list.get(i).length - 1; j++) {
//                values[j] = Double.parseDouble(list.get(i)[j]);
//            }
//            learn(values, answCode);
//
//        }
//    }


    public void learn(double[] inputs, int answ) {
        int rec = test(inputs);
        if (rec == answ) {
            return;
        }
        double[] newValues = new double[weights.length + 1];
        double[] tmpWeights = new double[weights.length + 1];
        double[] tmpInputs = new double[inputs.length + 1];
        addToArray(weights, tmpWeights, threshold);
        addToArray(inputs, tmpInputs, -1);
        for (int i = 0; i < tmpWeights.length; i++) {
            newValues[i] = tmpWeights[i] + (answ - rec) * learningConst * tmpInputs[i];
        }

        double[] newWeights = new double[weights.length];
        System.arraycopy(newValues, 0, newWeights, 0, weights.length);
        double newThreshold = newValues[tmpWeights.length - 1];

        weights = newWeights;
        threshold = newThreshold;
    }

    private void addToArray(double[] src, double[] des, double valToAdd) {
        System.arraycopy(src, 0, des, 0, src.length);
        des[des.length - 1] = valToAdd;
    }

    public void info() {
        System.out.println(Arrays.toString(weights) + " " + threshold);
    }


    private double roundToTwo(double num) {
        return Math.round(num * 100.0) / 100.0;
    }


    public String getName() {
        return name;
    }
}
