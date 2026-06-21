import java.io.*;
import java.util.*;
import static java.lang.Math.*;

public class KMeansGPT {

    // Capture the distances squared of each point to the closest cluster
    static ArrayList<Double> squares = new ArrayList<>();

    // Add the x and y of each point closest to each cluster
    static ArrayList<Double> xSums = new ArrayList<>();
    static ArrayList<Double> ySums = new ArrayList<>();

    // Count the number of points that are closest to each cluster
    static ArrayList<Integer> pointCount = new ArrayList<>();

    // Each point will have their closest cluster calculated by index
    static ArrayList<Integer> closestClusters = new ArrayList<>();

    public static double euclideanDistance(String[] coord1, Object[] coord2) {
        double x1 = Double.parseDouble(coord1[0]);
        double y1 = Double.parseDouble(coord1[1]);

        double x2 = Double.parseDouble(coord2[0].toString());
        double y2 = Double.parseDouble(coord2[1].toString());

        return sqrt(pow(x2 - x1, 2) + pow(y2 - y1, 2));
    }

    public static void closestStationaryPoint(
            String[] singleCoord,
            ArrayList<Object[]> points) {

        double shortestDistance =
                euclideanDistance(singleCoord, points.get(0));

        // Index of closest cluster
        int closestCluster = 0;

        for (int i = 1; i < points.size(); i++) {
            double newDistance =
                    euclideanDistance(singleCoord, points.get(i));

            if (newDistance < shortestDistance) {
                shortestDistance = newDistance;
                closestCluster = i;
            }
        }

        squares.add(pow(shortestDistance, 2));

        double x = Double.parseDouble(singleCoord[0]);
        double y = Double.parseDouble(singleCoord[1]);

        xSums.set(closestCluster,
                xSums.get(closestCluster) + x);

        ySums.set(closestCluster,
                ySums.get(closestCluster) + y);

        pointCount.set(closestCluster,
                pointCount.get(closestCluster) + 1);

        closestClusters.add(closestCluster);
    }

    public static void main(String[] args) throws Exception {

        BufferedReader directFile =
                new BufferedReader(new FileReader("coordinates.csv"));

        BufferedWriter resultsFile =
                new BufferedWriter(new FileWriter("model_results.csv"));

        boolean endLoop = false;

        ArrayList<String[]> liz = new ArrayList<>();
        ArrayList<String> xCoords = new ArrayList<>();
        ArrayList<String> yCoords = new ArrayList<>();

        // Random centroids / later clusters
        ArrayList<Object[]> pointArray = new ArrayList<>();

        int numIterations = 1;
        int numRepits = 0;

        // Compares the last array with the current array of closest clusters
        ArrayList<Integer> tempArray = new ArrayList<>();

        String line;

        while ((line = directFile.readLine()) != null) {
            liz.add(line.split(","));
        }

        for (int i = 0; i < liz.size(); i++) {

            xCoords.add(
                    liz.get(i)[0].substring(1));

            yCoords.add(
                    liz.get(i)[1]
                            .substring(0,
                                    liz.get(i)[1].length() - 2));
        }

        System.out.println();

        ArrayList<Integer> chosenPoints = new ArrayList<>();

        Scanner input = new Scanner(System.in);

        System.out.print("Enter the number of clusters: ");
        int numClusters = input.nextInt();

        System.out.println("Number of Clusters: " + numClusters);

        resultsFile.write(
                "Number of Clusters: " + numClusters);

        Random rand = new Random();

        while (numClusters > 0) {

            int randNum = rand.nextInt(liz.size());

            if (chosenPoints.contains(randNum)) {

                boolean notSame = false;

                while (!notSame) {

                    numRepits++;

                    randNum = rand.nextInt(liz.size());

                    if (!chosenPoints.contains(randNum)) {
                        notSame = true;
                    }
                }
            }

            int r = randNum;

            // Truncate string parentheses
            pointArray.add(new Object[]{
                    liz.get(r)[0]
                            .substring(1),
                    liz.get(r)[1]
                            .substring(0,
                                    liz.get(r)[1].length() - 2)
            });

            chosenPoints.add(r);

            numClusters--;
        }

        System.out.println(
                "Number of Repetitions Chosen: " + numRepits);

        for (int i = 0; i < pointArray.size(); i++) {
            xSums.add(0.0);
            ySums.add(0.0);
            pointCount.add(0);
        }

        System.out.println("\nPoints:\n");
        resultsFile.write("\nPoints:\n");

        for (Object[] i : pointArray) {

            System.out.println(
                    Arrays.toString(i) + "\n");

            resultsFile.write(
                    Arrays.toString(i) + "\n");
        }

        while (!endLoop) {

            squares.clear();

            int changes = 0;

            for (int i = 0; i < liz.size(); i++) {

                closestStationaryPoint(
                        new String[]{
                                xCoords.get(i),
                                yCoords.get(i)
                        },
                        pointArray);

                if (numIterations >= 2 &&
                        tempArray.get(i)
                                != closestClusters.get(i)) {

                    changes++;
                }
            }

            double num = 0;

            for (double d : squares) {
                num += d;
            }

            System.out.println(
                    "\n\nSum of Squares: " + num);

            System.out.println("\nPoints:\n");

            resultsFile.write(
                    "\n\nSum of Squares: " + num);

            resultsFile.write("\nPoints:\n");

            for (int i = 0; i < pointArray.size(); i++) {

                double xAvg =
                        xSums.get(i) / pointCount.get(i);

                double yAvg =
                        ySums.get(i) / pointCount.get(i);

                pointArray.set(i,
                        new Object[]{xAvg, yAvg});

                System.out.println(
                        Arrays.toString(pointArray.get(i))
                                + "\n");

                resultsFile.write(
                        Arrays.toString(pointArray.get(i))
                                + "\n");
            }

            System.out.println(
                    "Number of clusters changed: " + changes);

            System.out.println(
                    "\nThis is iteration: " + numIterations);

            resultsFile.write(
                    "Number of clusters changed: " + changes);

            resultsFile.write(
                    "\nThis is iteration: " + numIterations);

            if (numIterations >= 2 && changes == 0) {
                endLoop = true;
            }

            numIterations++;

            tempArray = new ArrayList<>(closestClusters);

            closestClusters.clear();

            // Reset sums and counts
            for (int i = 0; i < xSums.size(); i++) {
                xSums.set(i, 0.0);
                ySums.set(i, 0.0);
                pointCount.set(i, 0);
            }
        }

        System.out.println("\nDone!\n");

        resultsFile.write("\nDone!\n");

        directFile.close();
        resultsFile.close();
        input.close();
    }
}
