package com.example.testapplication;

import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/test.gpx";

        TextView textInfo = findViewById((R.id.info));
        StringBuilder info = new StringBuilder();

        File splitFile = new File(path);
        info.append(splitFile.getPath()).append("\n\n");

        List<Split> gpxList = decodeSplit(splitFile);

        for(int i = 0; i < gpxList.size(); i++){
           info.append(gpxList.get(i).getSplitInfo()).append("\n");
        }


        Log.i("MainActivity", "info" + info );

//        textInfo.setText(info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }



    private List<Split> decodeSplit(File file){
        List<Split> list = new ArrayList<>();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            FileInputStream fileInputStream = new FileInputStream(file);
            Document document = documentBuilder.parse(fileInputStream);
            Element elementRoot = document.getDocumentElement();

            NodeList nodelist_split = elementRoot.getElementsByTagName("split");

            for (int i = 0; i < nodelist_split.getLength(); i++) {

                Node node = nodelist_split.item(i);
                NodeList childNotes = node.getChildNodes();

                String newAvgSpeed = searchNodeListForTarget(childNotes, "avgSpeed");
                String newDistance = searchNodeListForTarget(childNotes, "distance");
                String startTime = searchNodeListForTarget(childNotes, "startTime");
                String endTime = searchNodeListForTarget(childNotes, "endTime");

                Double newAvgSpeedD = Double.parseDouble(newAvgSpeed);
                Double newDistanceD = Double.parseDouble(newDistance);

                Split newSplit = new Split(newAvgSpeedD, newDistanceD, startTime, endTime);
                list.add(newSplit);

            }
            fileInputStream.close();
        } catch (ParserConfigurationException e) {
            Log.e("testApplication", "parse fileInputStream error", e);
        } catch (SAXException e) {
            Log.e("testApplication", "SAX error", e);
        } catch (FileNotFoundException e) {
            Log.e("testApplication", "file not found", e);
        } catch (IOException e) {
            Log.e("testApplication", "failed IO operation", e);
        }

        return list;

    }

    String searchNodeListForTarget(NodeList src, String target){

        for(int i = 0; i < src.getLength(); i++){
            Node n = src.item(i);
            if (n.getNodeName().equals(target)){
                return n.getFirstChild().getNodeValue();
            }
        }
        return "";
    }
}