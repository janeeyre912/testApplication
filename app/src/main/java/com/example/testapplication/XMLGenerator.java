package com.example.testapplication;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XMLGenerator {


    /**
     * Code inspired by: https://www.jitendrazaa.com/blog/java/create-xml-file-using-jaxp-and-transformation-apis/
     */


    private String fileOutput = "Data.xml";
    private Context context;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public XMLGenerator(Context context, int splitInterval){
        this.context = context;

        generateXML(splitInterval);
    }


    public XMLGenerator(Context context) {
        this.context = context;

        generateXML();
    }

    public void generateXML(){
        try {
            double[] altitude = ProcessGPX.getAltitude();
            double[] distance = ProcessGPX.getDistance();
            double[] speed = ProcessGPX.getSpeed();
            double[] movingAverage = ProcessGPX.getSpeed();
            double topSpeedValue = ProcessGPX.getTopSpeed();
            double averageSpeedValue = ProcessGPX.getAverageSpeed();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element waypoint = doc.createElement("Waypoints");

            for(int i = 2; i < ProcessGPX.getCount(); i++){
                Element point = doc.createElement("Point");

                point.setAttribute("ID", String.valueOf(i));
                point.setAttribute("Altitude", String.valueOf(altitude[i]));
                point.setAttribute("Distance", String.valueOf(distance[i]));
                point.setAttribute("Speed", String.valueOf(speed[i]));
                point.setAttribute("MovingAverage", String.valueOf(movingAverage[i]));

                waypoint.appendChild(point);
            }

            waypoint.setAttribute("topSpeed", String.valueOf(topSpeedValue));
            waypoint.setAttribute("averageSpeed", String.valueOf(averageSpeedValue));

            doc.appendChild(waypoint);

            //Call the saveXML method to save the data to the file.
            saveXML(doc);

            readFile();

        } catch (ParserConfigurationException | TransformerConfigurationException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**generates xml for split information as user required split interval
     *
     * @param splitInterval int time interval in minutes
     *
     */
    public void generateXML(int splitInterval){
        try {
            double[] altitude = ProcessGPX.getAltitude();
            double[] distance = ProcessGPX.getDistance();
            double[] speed = ProcessGPX.getSpeed();
            String[] time = ProcessGPX.getTime();
            double topSpeedValue = ProcessGPX.getTopSpeed();
            double averageSpeedValue = ProcessGPX.getAverageSpeed();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element splitseg = doc.createElement("splitseg");


            int interval = splitInterval * 60 / 5;
            double distanceSplit = 0;
            double avgSpeedSplit = 0;
            String startTimeSplit = "";
            String endTimeSplit = "";

                int i = 1;
                int count = 1;
                while(count <= interval && i < ProcessGPX.getCount()){

                    distanceSplit += distance[i];
                    avgSpeedSplit += speed[i];

                    if(count == 1){
                        startTimeSplit = time[i];
                    }
                    if (count == interval || i == ProcessGPX.getCount() - 1){
                        endTimeSplit = time[i];
                        distanceSplit = distanceSplit / 1000; //(m -> km)
                        avgSpeedSplit = avgSpeedSplit / count;
                        avgSpeedSplit = avgSpeedSplit * 3.6; // m/s -> km/h
                        Element split = doc.createElement("split");
                        splitseg.appendChild(split);

                        split.setAttribute("ID", String.valueOf(i));
                        Element avgSpeed = doc.createElement("avgSpeed");
                        split.appendChild(avgSpeed);
                        avgSpeed.setTextContent(df.format(avgSpeedSplit));
                        Element distance1 = doc.createElement("distance");
                        split.appendChild(distance1);
                        distance1.setTextContent(df.format(distanceSplit));
                        Element startTime = doc.createElement("startTime");
                        split.appendChild(startTime);
                        startTime.setTextContent(String.valueOf(startTimeSplit));
                        Element endTime = doc.createElement("endTime");
                        split.appendChild(endTime);
                        endTime.setTextContent(String.valueOf(endTimeSplit));
                        count = 0;
                        distanceSplit = 0;
                        avgSpeedSplit = 0;
                        startTimeSplit = "";
                        endTimeSplit = "";
                    }

                    if (speed[i] != 0){
                        count++;
                    }
                    i++;
                }


            splitseg.setAttribute("topSpeed", String.valueOf(topSpeedValue));
            splitseg.setAttribute("averageSpeed", String.valueOf(averageSpeedValue));

            doc.appendChild(splitseg);

            //Call the saveXML method to save the data to the file.
            saveXML(doc);

            readFile();

        } catch (ParserConfigurationException | TransformerConfigurationException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveXML(Document doc) throws TransformerException, IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        //Use indents and indent amount of 4 to make it look more clean
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        Source source = new DOMSource(doc);
        Result output = new StreamResult(new FileOutputStream(context.getFilesDir() + "/" + fileOutput));

        transformer.transform(source, output);

        Log.i("XML", "XML File Created");
    }


    private void readFile() throws IOException {
        FileInputStream is;
        BufferedReader reader;
        final File file = new File(context.getFilesDir() + "/" + fileOutput);

        if (file.exists()) {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while(line != null){
                Log.d("XML", line);
                line = reader.readLine();
            }
        }
    }

}