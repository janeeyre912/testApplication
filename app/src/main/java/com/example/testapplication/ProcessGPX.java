package com.example.testapplication;
import android.annotation.SuppressLint;
import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProcessGPX
{
    private static double[] altitude;
    private static double[] distance;
    private static double[] speed;
    private static String[] time;
    private static double topSpeed;
    private static double averageSpeed;
    private static int count = 0;
    private static String dataSet = null;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public ProcessGPX(Context context)
    {
        ProcessGPX.context = context;

        dataSet = getGPXData();
        altitude = calculateAltitude();
        distance = calculateDistance();
        speed = calculateSpeed();
        averageSpeed = calculateAverageSpeed();
        time = getTime();
    }


    public static String getGPXData()
    {
        final String FILENAME = "mock-data.gpx";
        StringBuilder data = new StringBuilder();

        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try
        {
            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(context.getAssets().open(FILENAME));

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            // get <trkpt>
            NodeList list = doc.getElementsByTagName("trkpt");

            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    // get trkpt's attributes
                    String lat = element.getAttribute("lat");
                    String lon = element.getAttribute("lon");

                    // get text
                    String altitude = element.getElementsByTagName("ele").item(0).getTextContent();
                    String time = element.getElementsByTagName("time").item(0).getTextContent();

                    data.append("POINT : ").append(node.getNodeName()).append(" ").append(count++).append("\n").append("Latitude : ").append(lat).append("\n").append("Longitude : ").append(lon).append("\n").append("Altitude : ").append(altitude).append("\n").append("Time : ").append(time).append("\n");
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return data.toString();
    }

public static String[] getTime() {
        String[] lines = dataSet.split("\\r?\\n");
        String[] time = new String[count];
        int c = 0;

        for (String line : lines) {
            if (line.contains("Time : ")) {
                time[c] = line.replace("Time : ", "");
                c++;
            }
        }

        return time;
    }

    private static double[] calculateAltitude()
    {
        String[] lines = dataSet.split("\\r?\\n");
        double[] altitudes = new double[count];
        int c = 0;

        for (String line : lines) {
            if (line.contains("Altitude : ")) {
                altitudes[c] = Double.parseDouble(line.replace("Altitude : ", ""));
                c++;
            }
        }

        return altitudes;
    }

    private static double correctAltitude()
    {
        return 0.0;
    }
    /**
     * Based on Haversine method.
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    private static double getDistance(double lat1, double lat2, double lon1, double lon2, double el1, double el2)
    {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    private static double[] calculateDistance()
    {
        String[] lines = dataSet.split("\\r?\\n");
        double[] distances = new double[count];
        double[] latitudes = new double[count];
        double[] longitudes = new double[count];
        int c1 = 0;
        int c2 = 0;

        for (String line : lines) {
            if (line.contains("Latitude : ")) {
                latitudes[c1] = Double.parseDouble(line.replace("Latitude : ", ""));
                c1++;
            }
            if (line.contains("Longitude : ")) {
                longitudes[c2] = Double.parseDouble(line.replace("Longitude : ", ""));
                c2++;
            }
        }

        distances[0] = 0.0;
        for(int i = 1; i < distances.length; i++)
        {
            distances[i] = getDistance(latitudes[i-1], latitudes[i], longitudes[i-1], longitudes[i], altitude[i-1], altitude[i]);
        }

        return distances;
    }

    private static double[] calculateSpeed()
    {
        DecimalFormat df = new DecimalFormat("#.##");

        String[] lines = dataSet.split("\\r?\\n");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS".replace("Z", ""), Locale.CANADA);
        double[] speeds = new double[count];
        Date[] times = new Date[count];
        int c = 0;

        for (String line : lines) {
            if (line.contains("Time : ")) {
                try {
                    times[c] = dateFormat.parse(line.replace("Time : ", ""));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c++;
            }
        }

        double maximum = 0;

        speeds[0] = 0.0;
        for(int i = 1; i < speeds.length; i++)
        {
            // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object
            assert times[i - 1] != null;
            long diff = times[i].getTime() - times[i-1].getTime();
            double diffSec = (double)(diff / (1000));

            double diffDist = distance[i];

            if(diffSec > 0) {
                speeds[i] = Double.parseDouble(df.format(diffDist/diffSec)); // speeds in m/s
            }
            else {
                speeds[i] = 0;
            }

            maximum = Math.max(speeds[i], maximum);
        }

        topSpeed = maximum;
        return speeds;
    }


    private static double calculateAverageSpeed()
    {
        double total = 0;

        for (double v : speed) {
            total = total + v;
        }

        return total / speed.length;
    }


    public static double[] getAltitude() {
        return altitude;
    }

    public static void setAltitude(double[] altitude) {
        ProcessGPX.altitude = altitude;
    }

    public static double[] getDistance() {
        return distance;
    }

    public static void setDistance(double[] distance) {
        ProcessGPX.distance = distance;
    }

    public static double[] getSpeed() {
        return speed;
    }

    public static void setSpeed(double[] speed) {
        ProcessGPX.speed = speed;
    }


    public static double getTopSpeed() {
        return topSpeed;
    }

    public static void setTopSpeed(double topSpeed) {
        ProcessGPX.topSpeed = topSpeed;
    }

    public static double getAverageSpeed() {
        return averageSpeed;
    }

    public static void setAverageSpeed(double averageSpeed) {
        ProcessGPX.averageSpeed = averageSpeed;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        ProcessGPX.count = count;
    }
}