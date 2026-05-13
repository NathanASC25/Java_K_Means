import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class KMeans {
    private static double euclidean_distance(double[] coord1, ArrayList<Double> coord2) {
        double x1 = coord1[0];
        double y1 = coord1[1];
        double x2 = coord2.get(0);
        double y2 = coord2.get(1);
        double result = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return result;
    }
    private static void closest_stationary_point(double[] singleCoord, ArrayList<ArrayList<Double>> pointArray, ArrayList<Double> squares, ArrayList<Double> xSums, ArrayList<Double> ySums, ArrayList<Integer> pointCount, ArrayList<Integer> closestClusters) {
        double shortestDistance = euclidean_distance(singleCoord, pointArray.get(0));
        // Index of closest cluster
        int closestCluster = 0;
        for (int i = 1; i < pointArray.size(); i++) {
            double newDistance = euclidean_distance(singleCoord, pointArray.get(i));
            if (newDistance < shortestDistance) {
                shortestDistance = newDistance;
                closestCluster = i;
            }
        }
        squares.add(Math.pow(shortestDistance, 2));
        double x = singleCoord[0];
        double y = singleCoord[1];
        double newXSum = xSums.get(closestCluster) + 1;
        double newYSum = ySums.get(closestCluster) + 1;
        xSums.set(closestCluster, newXSum);
        ySums.set(closestCluster, newYSum);
        int newCountValue = pointCount.get(closestCluster) + 1;
        pointCount.remove(closestCluster);
        pointCount.add(closestCluster, 0);
        pointCount.set(closestCluster, 0);
        pointCount.set(closestCluster, newCountValue);
        System.out.print(pointCount);
        closestClusters.add(closestCluster);
    }
    public static void main(String[] args) throws Exception {
        // Capture the distances squared of each point to the closest cluster
        ArrayList<Double> squares = new ArrayList<Double>();
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
        ArrayList<ArrayList<Double>> liz = new ArrayList<>();
        ArrayList<Double> xCoords = new ArrayList<Double>();
        ArrayList<Double> yCoords = new ArrayList<Double>();
        // Random centroids/later clusters
        ArrayList<ArrayList<Double>> pointArray = new ArrayList<>();
        int numIterations = 1;
        // Compares the last array with the current array of closest clusters
        ArrayList<Integer> tempArray = new ArrayList<Integer>();
        ArrayList<Double> xSums = new ArrayList<Double>();
        ArrayList<Double> ySums = new ArrayList<Double>();
        // Count the number of points that are closest to each cluster
        // Each point will have their closest cluster calculated by index
        ArrayList<Integer> closestClusters = new ArrayList<Integer>();
        while (scanCoords.hasNext()) {
            ArrayList<Double> lizGroup = new ArrayList<Double>();
            String num1 = scanCoords.next().substring(1, scanCoords.next().indexOf(",") - 1);
            String num2 = scanCoords.next().substring(scanCoords.next().indexOf(",") + 2, scanCoords.next().indexOf(")") - 2);
            double x = Double.parseDouble(num1);
            double y = Double.parseDouble(num2);
            lizGroup.add(x);
            lizGroup.add(y);
            liz.add(lizGroup);
        }
        for (int i = 0; i < liz.size(); i++) {
            xCoords.add(liz.get(i).get(0));
            yCoords.add(liz.get(i).get(1));
        }
        System.out.printf("\n");
        ArrayList<Integer> chosenPoints = new ArrayList<Integer>();
        System.out.printf("Enter the number of clusters: ");
        int numClusters = userInput.nextInt();
        System.out.printf("Number of Clusters: %d", numClusters);
        writeOutput.printf("Number of Clusters %d", numClusters);
        while (numClusters > 0) {
            double rand_num = Math.random() * liz.size();
            int rand_index = (int) rand_num;
            // Case handling for same elements
            if (chosenPoints.indexOf(rand_index) >= 0) {
                boolean notSame = false;
                while (notSame == false) {
                    rand_num = Math.random() * liz.size();
                    rand_index = (int) rand_num;
                    if (chosenPoints.indexOf(rand_index) >= 0) {
                        notSame = true;
                    }
                }
            }
            int r = rand_index;
            ArrayList<Double> collectedPoint = new ArrayList<Double>();
            collectedPoint.add(liz.get(r).get(0));
            collectedPoint.add(liz.get(r).get(1));
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
            System.out.print(pointArray.get(i) + "\n");
            writeOutput.print(pointArray.get(i) + "\n");
        }
        // Track changes in chosen coordinates
        int changes;
        double[] singleCoord;
        double sumOfSquares, xAvg, yAvg;
        while (endLoop == false) {
            squares.clear();
            changes = 0;
            for (int i = 0; i < liz.size(); i++) {
                // double[] singleCoord = {xCoords.get(i), yCoords.get(i)};
                singleCoord = new double[2];
                singleCoord[0] = xCoords.get(i);
                singleCoord[1] = yCoords.get(i);
                closest_stationary_point(singleCoord, pointArray, squares, xSums, ySums, pointCount, closestClusters);
                if (numIterations >= 2 && tempArray.get(i) != closestClusters.get(i)) {
                    changes += 1;
                }
            }
            sumOfSquares = 0.0;
            for (int i = 0; i < squares.size(); i++) {
                sumOfSquares += squares.get(i);
            }
            System.out.printf("\n\nSum of Squares: %f", sumOfSquares);
            System.out.printf("\nPoints: \n");
            writeOutput.printf("\n\nSum of Squares: %f", sumOfSquares);
            writeOutput.printf("\nPoints: \n");
            for (int i = 0; i < pointArray.size(); i++) {
                ArrayList<Double> newPoint = new ArrayList<Double>();
                xAvg = xSums.get(i) / pointCount.get(i);
                yAvg = ySums.get(i) / pointCount.get(i);
                newPoint.add(xAvg);
                newPoint.add(yAvg);
                pointArray.set(i, newPoint);
                System.out.print(pointArray.get(i) + "\n");
                writeOutput.print(pointArray.get(i) + "\n");
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
