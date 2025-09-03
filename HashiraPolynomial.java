import java.math.BigInteger;
import java.util.*;

public class HashiraPolynomial {
    public static void main(String[] args) {
        // ---------- Array of Testcases ----------
        String[] testcases = {
                // Testcase 1
                "{\n" +
                        "    \"keys\": {\n" +
                        "        \"n\": 4,\n" +
                        "        \"k\": 3\n" +
                        "    },\n" +
                        "    \"1\": { \"base\": \"10\", \"value\": \"4\" },\n" +
                        "    \"2\": { \"base\": \"2\", \"value\": \"111\" },\n" +
                        "    \"3\": { \"base\": \"10\", \"value\": \"12\" },\n" +
                        "    \"6\": { \"base\": \"4\", \"value\": \"213\" }\n" +
                        "}",

                // Testcase 2
                "{\n" +
                        "  \"keys\": {\n" +
                        "      \"n\": 10,\n" +
                        "      \"k\": 7\n" +
                        "  },\n" +
                        "  \"1\": { \"base\": \"6\", \"value\": \"13444211440455345511\" },\n" +
                        "  \"2\": { \"base\": \"15\", \"value\": \"aed7015a346d635\" },\n" +
                        "  \"3\": { \"base\": \"15\", \"value\": \"6aeeb69631c227c\" },\n" +
                        "  \"4\": { \"base\": \"16\", \"value\": \"e1b5e05623d881f\" },\n" +
                        "  \"5\": { \"base\": \"8\", \"value\": \"316034514573652620673\" },\n" +
                        "  \"6\": { \"base\": \"3\", \"value\": \"2122212201122002221120200210011020220200\" },\n" +
                        "  \"7\": { \"base\": \"3\", \"value\": \"20120221122211000100210021102001201112121\" },\n" +
                        "  \"8\": { \"base\": \"6\", \"value\": \"20220554335330240002224253\" },\n" +
                        "  \"9\": { \"base\": \"12\", \"value\": \"45153788322a1255483\" },\n" +
                        "  \"10\": { \"base\": \"7\", \"value\": \"1101613130313526312514143\" }\n" +
                        "}"
        };

        // ---------- Process Each Testcase ----------
        for (int t = 0; t < testcases.length; t++) {
            System.out.println("========== Testcase " + (t + 1) + " ==========");
            solveTestcase(testcases[t]);
            System.out.println();
        }
    }

    // Method to process one testcase
    private static void solveTestcase(String jsonInput) {
        // Extract n and k
        int n = Integer.parseInt(jsonInput.split("\"n\":")[1].split(",")[0].trim());
        int k = Integer.parseInt(jsonInput.split("\"k\":")[1].split("[,}]")[0].trim());

        List<BigInteger> roots = new ArrayList<>();

        // Extract all base-value pairs
        String[] chunks = jsonInput.split("\\}");
        for (String chunk : chunks) {
            if (chunk.contains("\"base\"")) {
                String baseStr = chunk.split("\"base\":")[1].split(",")[0]
                        .replaceAll("[^0-9]", "");
                String valStr = chunk.split("\"value\":")[1].split("\"")[1];

                int base = Integer.parseInt(baseStr);
                BigInteger root = new BigInteger(valStr, base);
                roots.add(root);
            }
        }

        // Polynomial degree = k - 1
        int degree = k - 1;
        roots = roots.subList(0, degree);

        // Start polynomial: coeff = [1] (means polynomial = 1)
        List<BigInteger> coeff = new ArrayList<>();
        coeff.add(BigInteger.ONE);

        // Multiply (x - r) for each root
        for (BigInteger r : roots) {
            List<BigInteger> newCoeff = new ArrayList<>(Collections.nCopies(coeff.size() + 1, BigInteger.ZERO));
            for (int i = 0; i < coeff.size(); i++) {
                // term * x
                newCoeff.set(i + 1, newCoeff.get(i + 1).add(coeff.get(i)));
                // term * (-r)
                newCoeff.set(i, newCoeff.get(i).add(coeff.get(i).negate().multiply(r)));
            }
            coeff = newCoeff;
        }

        // Print polynomial
        System.out.print("Polynomial: ");
        for (int i = coeff.size() - 1; i >= 0; i--) {
            BigInteger c = coeff.get(i);
            if (c.equals(BigInteger.ZERO)) continue;
            if (i == coeff.size() - 1) {
                System.out.print(c + "x^" + i);
            } else {
                if (c.compareTo(BigInteger.ZERO) >= 0) System.out.print(" + " + c + "x^" + i);
                else System.out.print(" - " + c.abs() + "x^" + i);
            }
        }
        System.out.println();

        System.out.println("Coefficients (constant to highest degree): " + coeff);
    }
}
