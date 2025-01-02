import java.io.*;
import org.json.JSONObject;
import java.nio.file.Files;
import java.util.*;

public class ShamirSecretSharing {

    public static int decodeValue(String value, int base) {
        return Integer.parseInt(value, base);
    }

    public static int lagrangeInterpolation(List<Point> points, int xValue) {
        double result = 0;

        for (int i = 0; i < points.size(); i++) {
            double term = points.get(i).y;
            for (int j = 0; j < points.size(); j++) {
                if (i != j) {
                    term *= (xValue - points.get(j).x) / (double) (points.get(i).x - points.get(j).x);
                }
            }
            result += term;
        }

        return (int) Math.round(result);
    }

    static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static int findConstantTerm(String inputFile) throws IOException {
        String content = new String(Files.readAllBytes(new File(inputFile).toPath()));
        JSONObject input = new JSONObject(content);

        // Extract the values for n and k
        int n = input.getJSONObject("keys").getInt("n");
        int k = input.getJSONObject("keys").getInt("k");

        if (n < k) {
            throw new IllegalArgumentException("Number of roots provided is less than required roots (k).");
        }
        List<Point> points = new ArrayList<>();
        for (String key : input.keySet()) {
            if (key.equals("keys")) continue;

            int x = Integer.parseInt(key);
            JSONObject valueObj = input.getJSONObject(key);
            int base = valueObj.getInt("base");
            String encodedY = valueObj.getString("value");

            // Decode y value using the base
            int y = decodeValue(encodedY, base);
            points.add(new Point(x, y));
        }
        List<Point> selectedPoints = points.subList(0, k);
        return lagrangeInterpolation(selectedPoints, 0);
    }

    public static void main(String[] args) {
        try {
            String testCase1 = "testcase1.json";
            String testCase2 = "testcase2.json";
            int res1 = findConstantTerm(testCase1);
            int res2 = findConstantTerm(testCase2);

            System.out.println("Constant term for Test Case 1: " + res1);
            System.out.println("Constant term for Test Case 2: " + res2);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
