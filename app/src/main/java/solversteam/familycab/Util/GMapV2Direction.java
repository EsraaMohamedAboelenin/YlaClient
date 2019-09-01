package solversteam.familycab.Util;

/**
 * Created by mosta on 5/8/2017.
 */

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import solversteam.familycab.Activities.RideNow_Activity;
import solversteam.familycab.Connection.Connection;
import solversteam.familycab.R;
import solversteam.familycab.Set_Map;


public class GMapV2Direction {
    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";
    private Boolean drawMarkers=false;
    Activity activity;
    private Document docm;

    public GMapV2Direction(Activity activity) {
        this.activity=activity;
    }
    public void setMarkers(Boolean drawMarkers)
    {
        this.drawMarkers=drawMarkers;
    }
    public void getDocument(final LatLng start, final LatLng end, final Boolean draw, final Set_Map set_map, final LinearLayout arrival_time, final TextView arrival_time_txt) {
        String url = "https://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode=driving"
                + "&key=AIzaSyDIWvq3r1zXF7biyZueBfsubuJbDlfrFCw"
        //AIzaSyCJSKodU2Jfk9dgB4wJFkbu4wQ-qJdvXl0
                + "&language=ar";
        Log.d("url", url);

        try {
            Connection connection=new Connection(activity,url,"Get",1);
            connection.reset();
            connection.Connect(new Connection.Result() {
                @Override
                public void data(String str) throws JSONException {
                    Log.d("checklogin",str);

                    try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                        InputSource is = new InputSource();
                        is.setCharacterStream(new StringReader(str));
                        docm = builder.parse(is);
                        Log.d("doc", docm.toString());

                        try
                        {
                            NodeList nl1;
                            nl1 = docm.getElementsByTagName("duration");

                            Node node1 = nl1.item(nl1.getLength() - 1);
                            NodeList nl2 = null;
                            nl2 = node1.getChildNodes();
                            Node node2 = nl2.item(getNodeIndex(nl2, "text"));
                            Log.d("DurationText", node2.getTextContent());
                            if(activity instanceof RideNow_Activity){
                         arrival_time.setVisibility(View.VISIBLE);
                         arrival_time_txt.setVisibility(View.VISIBLE);
                         arrival_time_txt.setText(node2.getTextContent());}


                        }
                        catch (Exception ex)
                        {
                            Log.d("Exception",ex.toString());

                        }
                        if(draw)
                        {
                            Log.d("getDirection", getDirection(docm)+"");
                           set_map.drawPrimaryLinePath(getDirection(docm));


                        }
                        else {

                            set_map.setmarker(end,2);
                        }

                        getDurationValue(docm);


                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }

                }});
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.toString());
        }
    }

    public String getDurationText() {
        try {
            NodeList nl1;
            nl1 = docm.getElementsByTagName("duration");

            Node node1 = nl1.item(nl1.getLength() - 1);
            NodeList nl2 = null;
            nl2 = node1.getChildNodes();
            Node node2 = nl2.item(getNodeIndex(nl2, "text"));
            Log.d("DurationText", node2.getTextContent());
            return node2.getTextContent();
        } catch (Exception e) {
            return "-1";
        }
    }
    private List<LatLng> decodePolyline(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();

        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p);
        }

        return poly;
    }
    public int getDurationValue(Document doc) {
        ArrayList<String>points=new ArrayList<>();
        ArrayList<LatLng> line=new ArrayList<>();
        try {
            NodeList nl1 = doc.getElementsByTagName("polyline");
            for(int i=0; i < nl1.getLength(); i++) {
            Node node1 = nl1.item(i);
            NodeList nl2 = node1.getChildNodes();
            Node node2 = nl2.item(getNodeIndex(nl2, "points"));
                points.add(node2.getTextContent());
                for(LatLng p : decodePolyline(node2.getTextContent())) {
                    line.add(p);
                }

            Log.i("DurationValue", points+"\n");
            }

            return Integer.parseInt("1");
        } catch (Exception e) {
            Log.d("excpt",e.toString());
            return -1;
        }
    }

    public String getDistanceText() {
        /*
         * while (en.hasMoreElements()) { type type = (type) en.nextElement();
         *
         * }
         */

        try {
            NodeList nl1;
            nl1 = docm.getElementsByTagName("distance");

            Node node1 = nl1.item(nl1.getLength() - 1);
            NodeList nl2 = null;
            nl2 = node1.getChildNodes();
            Node node2 = nl2.item(getNodeIndex(nl2, "text"));
            Log.d("DistanceText", node2.getTextContent());
            return node2.getTextContent();
        } catch (Exception e) {
            return "-1";
        }

        /*
         * NodeList nl1; if(doc.getElementsByTagName("distance")!=null){ nl1=
         * doc.getElementsByTagName("distance");
         *
         * Node node1 = nl1.item(nl1.getLength() - 1); NodeList nl2 = null; if
         * (node1.getChildNodes() != null) { nl2 = node1.getChildNodes(); Node
         * node2 = nl2.item(getNodeIndex(nl2, "value")); Log.d("DistanceText",
         * node2.getTextContent()); return node2.getTextContent(); } else return
         * "-1";} else return "-1";
         */
    }

    public int getDistanceValue(Document doc) {
        try {
            NodeList nl1 = doc.getElementsByTagName("distance");
            Node node1 = null;
            node1 = nl1.item(nl1.getLength() - 1);
            NodeList nl2 = node1.getChildNodes();
            Node node2 = nl2.item(getNodeIndex(nl2, "value"));
            Log.i("DistanceValue", node2.getTextContent());
            return Integer.parseInt(node2.getTextContent());
        } catch (Exception e) {
            return -1;
        }
        /*
         * NodeList nl1 = doc.getElementsByTagName("distance"); Node node1 =
         * null; if (nl1.getLength() > 0) node1 = nl1.item(nl1.getLength() - 1);
         * if (node1 != null) { NodeList nl2 = node1.getChildNodes(); Node node2
         * = nl2.item(getNodeIndex(nl2, "value")); Log.i("DistanceValue",
         * node2.getTextContent()); return
         * Integer.parseInt(node2.getTextContent()); } else return 0;
         */
    }

    public String getStartAddress(Document doc) {
        try {
            NodeList nl1 = doc.getElementsByTagName("start_address");
            Node node1 = nl1.item(0);
            Log.i("StartAddress", node1.getTextContent());
            return node1.getTextContent();
        } catch (Exception e) {
            return "-1";
        }

    }

    public String getEndAddress() {
        try {
            NodeList nl1 = docm.getElementsByTagName("end_address");
            Node node1 = nl1.item(0);
            Log.i("StartAddress", node1.getTextContent());
            return node1.getTextContent();
        } catch (Exception e) {
            return "-1";
        }
    }
    public String getCopyRights(Document doc) {
        try {
            NodeList nl1 = doc.getElementsByTagName("copyrights");
            Node node1 = nl1.item(0);
            Log.i("CopyRights", node1.getTextContent());
            return node1.getTextContent();
        } catch (Exception e) {
            return "-1";
        }

    }

    public ArrayList<LatLng> getDirection(Document doc) {
        NodeList nl1, nl2, nl3;
        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
        nl1 = doc.getElementsByTagName("step");
        if (nl1.getLength() > 0) {
            for (int i = 0; i < nl1.getLength(); i++) {
                Node node1 = nl1.item(i);
                nl2 = node1.getChildNodes();

                Node locationNode = nl2
                        .item(getNodeIndex(nl2, "start_location"));
                nl3 = locationNode.getChildNodes();
                Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                double lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));

                locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "points"));
                ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
                for (int j = 0; j < arr.size(); j++) {
                    listGeopoints.add(new LatLng(arr.get(j).latitude, arr
                            .get(j).longitude));
                }

                locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(getNodeIndex(nl3, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(getNodeIndex(nl3, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));
            }
        }

        return listGeopoints;
    }

    private int getNodeIndex(NodeList nl, String nodename) {
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals(nodename))
                return i;
        }
        return -1;
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }



}