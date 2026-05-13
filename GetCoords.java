import java.io.File;
import java.io.PrintWriter;
public class GetCoords {
    public static void main(String[] args) throws Exception{
        File coordsFile = new File("./TextFiles/coordinates.csv");
        PrintWriter coordsWrite = new PrintWriter(coordsFile);
        for (int i = 0; i < 36000; i++) {
            /** For x and y respectively, Gen1 creates a random integer
             *  and Gen2 is a float multiplier
             */
            double xGen1 = Math.random() * -15 + 65;
            double xGen2 = Math.random();
            double yGen1 = Math.random() * -15 + 65;
            double yGen2 = Math.random();
            double x = xGen1 * xGen2;
            double y = yGen1 * yGen2;
            coordsWrite.printf("(%f,%f)\n", x, y);
        }
        coordsWrite.close();
    }
}
