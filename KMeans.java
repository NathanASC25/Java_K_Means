import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Arrays;
import java.math.BigDecimal;

// Wrapper Class Rules (Double)
// 1. Call .doubleValue() method when performing calculations
// 2. Otherwise assign and cast with Double type
public class KMeans {
    private static Double euclidean_distance(Double[] coord1, Double[] coord2) {
        Double x1 = coord1[0];
        Double y1 = coord1[1];
        Double x2 = coord2[0];
        Double y2 = coord2[1];
        Double result = Math.sqrt(Math.pow(x2.doubleValue() - x1.doubleValue(), 2) + Math.pow(y2.doubleValue() - y1.doubleValue(), 2));
        return result;
    }
    private static void closest_stationary_point(Double[] singleCoord, ArrayList<Double[]> pointArray, ArrayList<Double> squares, ArrayList<Double> xSums, ArrayList<Double> ySums, ArrayList<Integer> pointCount, ArrayList<Integer> closestClusters) {
        Double shortestDistance = euclidean_distance(singleCoord, pointArray.get(0));
        // Index of closest cluster
        int closestCluster = 0;
        // New Distance declaration
        Double newDistance;
        for (int i = 1; i < pointArray.size(); i++) {
            newDistance = euclidean_distance(singleCoord, pointArray.get(i));
            if (newDistance.doubleValue() < shortestDistance.doubleValue()) {
                shortestDistance = newDistance;
                closestCluster = i;
            }
        }
        squares.add(Math.pow(shortestDistance.doubleValue(), 2));
        Double x = singleCoord[0];
        Double y = singleCoord[1];
        Double newXSum = xSums.get(closestCluster).doubleValue() + 1;
        Double newYSum = ySums.get(closestCluster).doubleValue() + 1;
        xSums.set(closestCluster, newXSum);
        ySums.set(closestCluster, newYSum);
        int newCountValue = pointCount.get(closestCluster) + 1;
        pointCount.set(closestCluster, newCountValue);
        System.out.print(pointCount);
        closestClusters.add(closestCluster);
    }
    public static void main(String[] args) throws IOException {
        // Capture the distances squared of each point to the closest cluster
        ArrayList<Double> squares = new ArrayList<>();
        File resultsFile = new File("TextFiles/model_results.csv");
        File directFile = new File("TextFiles/coordinates.csv");
        Scanner scanCoords = new Scanner(directFile);
        Scanner userInput = new Scanner(System.in);
        PrintWriter writeOutput = new PrintWriter(resultsFile);
       // while (scanCoords.hasNextLine()) {
           // double x = scanCoords.nextFloat();
           // double y = scanCoords.nextFloat();
           // System.out.printf("X-Value: %f, Y-Value: %f\n", x, y);
           // System.out.print(scanCoords.next() + "\n");
         //  String num1 = scanCoords.next().substring(1, scanCoords.next().indexOf(",") - 1);
         //  String num2 = scanCoords.next().substring(scanCoords.next().indexOf(",") + 2, scanCoords.next().indexOf(")") - 2);
         //  double x = Double.parseDouble(num1);
         //  double y = Double.parseDouble(num2);
         //  System.out.printf("X-Value: %f, Y-Value: %f\n", x, y);
        //}
        boolean endLoop = false;
        ArrayList<Double[]> list = new ArrayList<>();
        ArrayList<Double> xCoords = new ArrayList<>();
        ArrayList<Double> yCoords = new ArrayList<>();
        // Random centroids/later clusters
        ArrayList<Double[]> pointArray = new ArrayList<>();
        int numIterations = 1;
        // Compares the last array with the current array of closest clusters
        ArrayList<Integer> tempArray = new ArrayList<>();
        ArrayList<Double> xSums = new ArrayList<>();
        ArrayList<Double> ySums = new ArrayList<>();
        // Count the number of points that are closest to each cluster
        // Each point will have their closest cluster calculated by index
        ArrayList<Integer> closestClusters = new ArrayList<>();
        // A Point - Set of X and Y Coordinates
        Double[] arrGroup = new Double[2];
        Double x, y;
        String num1, num2;
        while (scanCoords.hasNext()) {
            num1 = scanCoords.next().substring(1, scanCoords.next().indexOf(",") - 1);
            num2 = scanCoords.next().substring(scanCoords.next().indexOf(",") + 2, scanCoords.next().indexOf(")") - 2);
            x = Double.parseDouble(num1);
            y = Double.parseDouble(num2);
            arrGroup[0] = x;
            arrGroup[1] = y;
            list.add(arrGroup);
        } scanCoords.close();
        for (int i = 0; i < list.size(); i++) {
            xCoords.add(list.get(i)[0]);
            yCoords.add(list.get(i)[1]);
        }
        System.out.printf("\n");
        ArrayList<Integer> chosenPoints = new ArrayList<>();
        System.out.printf("Enter the number of clusters: ");
        int numClusters = userInput.nextInt(); userInput.close();
        System.out.printf("Number of Clusters: %d", numClusters);
        writeOutput.printf("Number of Clusters %d", numClusters);
        Double rand_num; int rand_index, r; boolean notSame;
        Double[] collectedPoint = new Double[2];
        while (numClusters > 0) {
            rand_num = Math.random() * list.size();
            rand_index = (int) rand_num.doubleValue();
            // Case handling for same elements
            if (chosenPoints.indexOf(rand_index) >= 0) {
                notSame = false;
                while (notSame == false) {
                    rand_num = Math.random() * list.size();
                    rand_index = (int) rand_num.doubleValue();
                    if (chosenPoints.indexOf(rand_index) >= 0) {
                        notSame = true;
                    }
                }
            }
            r = rand_index;
            collectedPoint[0] = list.get(r)[0];
            collectedPoint[1] = list.get(r)[1];
            pointArray.add(collectedPoint);
            chosenPoints.add(r);
            numClusters -= 1;
        }
        ArrayList<Integer> pointCount = new ArrayList<>();
        for (int i = 0; i < pointArray.size(); i++) {
            xSums.add(0.0);
            ySums.add(0.0);
            pointCount.add(0);
        }
        System.out.printf("\nPoints: \n");
        writeOutput.printf("\nPoints: \n");
        for (int i = 0; i < pointArray.size(); i++) {
            // System.out.print(Arrays.toString(pointArray.get(i).doubleValue()) + "\n");
            System.out.printf("[%f, %f]\n", pointArray.get(i)[0].doubleValue(), pointArray.get(i)[1].doubleValue());
            writeOutput.printf("[%f, %f]\n", pointArray.get(i)[0].doubleValue(), pointArray.get(i)[1].doubleValue());
        }
        // Track changes in chosen coordinates
        int changes;
        Double[] singleCoord = new Double[2];
        BigDecimal sumOfSquares;
        Double xAvg, yAvg;
        while (endLoop == false) {
            squares.clear();
            changes = 0;
            for (int i = 0; i < list.size(); i++) {
                // double[] singleCoord = {xCoords.get(i), yCoords.get(i)};
                singleCoord[0] = xCoords.get(i);
                singleCoord[1] = yCoords.get(i);
                closest_stationary_point(singleCoord, pointArray, squares, xSums, ySums, pointCount, closestClusters);
                if (numIterations >= 2 && tempArray.get(i) != closestClusters.get(i)) {
                    changes += 1;
                }
            }
            sumOfSquares = BigDecimal.valueOf(0.0);
            for (int i = 0; i < squares.size(); i++) {
                sumOfSquares.add(BigDecimal.valueOf(squares.get(i).doubleValue()));
            }
            System.out.printf("\n\nSum of Squares: %f", sumOfSquares);
            System.out.printf("\nPoints: \n");
            writeOutput.printf("\n\nSum of Squares: %f", sumOfSquares);
            writeOutput.printf("\nPoints: \n");
            // Calculate new cluster point based on averages of x and y coordinates
            Double[] newPoint = new Double[2];
            for (int i = 0; i < pointArray.size(); i++) {
                xAvg = xSums.get(i).doubleValue() / pointCount.get(i);
                yAvg = ySums.get(i).doubleValue() / pointCount.get(i);
                newPoint[0] = xAvg;
                newPoint[1] = yAvg;
                pointArray.set(i, newPoint);
                System.out.printf("[%f, %f]\n", pointArray.get(i)[0].doubleValue(), pointArray.get(i)[1].doubleValue());
                writeOutput.printf("[%f, %f]\n", pointArray.get(i)[0].doubleValue(), pointArray.get(i)[1].doubleValue());
            }
            System.out.printf("Number of clusters changed: %d", changes);
            System.out.printf("\nThis is iteration: %d", numIterations);
            if (numIterations >= 2 && changes == 0) {
                endLoop = true;
            }
            numIterations += 1;
            //for (int i = 0; i < closestClusters.size(); i++) {
            //    tempArray.add(closestClusters.get(i));
            //}
            tempArray = closestClusters;
            System.out.println("\nClosest Clusters: " + closestClusters);
            closestClusters.clear();
        }
        System.out.printf("\nDone!\n");
        writeOutput.printf("\nDone!\n");
        scanCoords.close();
        userInput.close();
        writeOutput.close();
    }
}
