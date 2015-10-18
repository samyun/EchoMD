package echomd.storage;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.InputSource;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Contains player and score data to represent a score keeper game.
 */
public class echomdGameData {
	// names of the drugs
    private List<String> names;
    // number of doses left in the perscription period
    private Map<String, Long> numDoses;
    // last date taken
    private Map<String, Calendar> lastDates;
    // total number of doses taken for the day
    private Map<String, Long> dayTotals;
    // frequency to take doses
    private Map<String, Long> frequency;
    // total number of doses in a perscription period supply
    private Map<String, Long> supply;
    // number of refills allowed
    private Map<String, Long> numRefills;

    public echomdGameData() {
        // public no-arg constructor required for DynamoDBMapper marshalling
    }

    /**
     * Creates a new instance of {@link echomdGameData} with initialized and populated data from FHIR
     * 
     * @return
     */
    public static echomdGameData newInstance() {
        echomdGameData newInstance = new echomdGameData();
        
        ArrayList<String> names = new ArrayList<String>();

        TreeMap<String, Long> frequency = new TreeMap<String, Long>(String.CASE_INSENSITIVE_ORDER);
        TreeMap<String, Long> supply = new TreeMap<String, Long>(String.CASE_INSENSITIVE_ORDER);
        TreeMap<String, Long> numRefills = new TreeMap<String, Long>(String.CASE_INSENSITIVE_ORDER);
        TreeMap<String, Long> numDoses = new TreeMap<String, Long>(String.CASE_INSENSITIVE_ORDER);
        TreeMap<String, Calendar> lastDates = new TreeMap<String, Calendar>();
        TreeMap<String, Long> dayTotals = new TreeMap<String, Long>(String.CASE_INSENSITIVE_ORDER);
        
        fhirQuery(names, frequency, supply, numRefills, numDoses, lastDates, dayTotals);
        
        newInstance.setNames(names);
        newInstance.setFrequency(frequency);
        newInstance.setSupply(supply);
        newInstance.setNumRefills(numRefills);
        
        newInstance.setNumDoses(numDoses);
        newInstance.setLastDates(lastDates);
        newInstance.setDayTotals(dayTotals);
        return newInstance;
    }

    public void resyncFHIR() {
        ArrayList<String> names = new ArrayList<String>();
        TreeMap<String, Long> frequency = new TreeMap<String, Long>(String.CASE_INSENSITIVE_ORDER);
        TreeMap<String, Long> supply = new TreeMap<String, Long>(String.CASE_INSENSITIVE_ORDER);
        TreeMap<String, Long> numRefills = new TreeMap<String, Long>(String.CASE_INSENSITIVE_ORDER);
        TreeMap<String, Long> numDoses = new TreeMap<String, Long>(String.CASE_INSENSITIVE_ORDER);
        TreeMap<String, Calendar> lastDates = new TreeMap<String, Calendar>(String.CASE_INSENSITIVE_ORDER);
        TreeMap<String, Long> dayTotals = new TreeMap<String, Long>(String.CASE_INSENSITIVE_ORDER);
        
        fhirQuery(names, frequency, supply, numRefills, numDoses, lastDates, dayTotals);
        
        this.setNames(names);
        this.setFrequency(frequency);
        this.setSupply(supply);
        this.setNumRefills(numRefills);
        
        this.setNumDoses(numDoses);
        this.setLastDates(lastDates);
        this.setDayTotals(dayTotals);
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public Map<String, Long> getNumDoses() {
        return numDoses;
    }

    public void setNumDoses(Map<String, Long> numDoses) {
        this.numDoses = numDoses;
    }

    public Map<String, Calendar> getLastDates() {
        return lastDates;
    }

    public void setLastDates(Map<String, Calendar> lastDates) {

        this.lastDates = lastDates;
    }

    public Map<String, Long> getDayTotals() {
        return dayTotals;
    }

    public void setDayTotals(Map<String, Long> dayTotals) {
        this.dayTotals = dayTotals;
    }

    public Map<String, Long> getFrequency() {
        return frequency;
    }

    public void setFrequency(Map<String, Long> frequency) {
        this.frequency = frequency;
    }

    public Map<String, Long> getSupply() {
        return supply;
    }

    public void setSupply(Map<String, Long> supply) {
        this.supply = supply;
    }

    public Map<String, Long> getNumRefills() {
        return numRefills;
    }

    public void setNumRefills(Map<String, Long> numRefills) {
    	this.numRefills = numRefills;
    }
    
    public static void fhirQuery(ArrayList<String> names, TreeMap<String, Long> frequency, TreeMap<String, Long> supply, 
    		TreeMap<String, Long> numRefills, TreeMap<String, Long> numDoses, TreeMap<String, Calendar> lastDates, 
    		TreeMap<String, Long> dayTotals)
    {
		/*)
		String drugName = "lipitor";
		names.add(drugName);
		frequency.put(drugName, 1L);
		supply.put(drugName, 30L);
		numRefills.put(drugName, 12L);
		numDoses.put(drugName, 30L);
		lastDates.put(drugName, null);
		dayTotals.put(drugName, 0L);
		
	}*/
	String[] nameTrolls = {"allegra", "cocaine", "advil", "zocor"};
	
	for (int i = 0; i<4;i++)
	{
		String drugName = nameTrolls[i];
		names.add(drugName);
		frequency.put(drugName, 1L);
		supply.put(drugName, 30L);
		numRefills.put(drugName, 2L);
		numDoses.put(drugName, 30L);
		lastDates.put(drugName, null);
		dayTotals.put(drugName, 0L);
	}
    	/*
		String drugName;
    	String[] splitName = {};

    	//grab the xml and start parsing        
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	DocumentBuilder db = null;
    	String url = "https://open-ic.epic.com/FHIR/api/FHIR/DSTU2/MedicationPrescription?patient=TSvxrNacr7Cv7KQXd2Y8lFXnKQyRbVPmWyfDobtXFBOsB&status=active";
    	String output;
    	try {
    		output = httpGet(url);
    		db = dbf.newDocumentBuilder();
    		InputSource is = new InputSource(new StringReader(output));
    		Document doc = db.parse(is);
    		NodeList med = doc.getElementsByTagName("medication");
    		NodeList dose = doc.getElementsByTagName("dosageInstruction");
    		NodeList dispense = doc.getElementsByTagName("dispense");

    		for (int i = 0; i < med.getLength(); i++) {
    			splitName =  med.item(i).getFirstChild().getAttributes().item(0).getTextContent().split("\\p{Punct}");
    			//drugName = splitName[1].replaceAll("[()]", "").toLowerCase();
				drugName = nameTrolls[i];
				names.add(drugName);
    			frequency.put(drugName, Long.parseLong(dose.item(i).getChildNodes().item(4).getFirstChild().getFirstChild().getAttributes().item(0).getTextContent(),10));
    			supply.put(drugName, Long.parseLong(dispense.item(i).getChildNodes().item(2).getFirstChild().getAttributes().item(0).getTextContent(),10));
    			numRefills.put(drugName, Long.parseLong(dispense.item(i).getFirstChild().getAttributes().item(0).getTextContent(),10));
    			numDoses.put(drugName, supply.get(drugName));
    			lastDates.put(drugName, null);
    			dayTotals.put(drugName, 0L);
				
				
    		}

    	} catch (Exception e) {
    		e.printStackTrace();;
    	}*/
    }

    private static String httpGet(String urlStr) throws IOException {
    	URL url = new URL(urlStr);
    	HttpURLConnection conn =
    			(HttpURLConnection) url.openConnection();

    	if (conn.getResponseCode() != 200) {
    		throw new IOException(conn.getResponseMessage());
    	}

    	// Buffer the result into a string
    	BufferedReader rd = new BufferedReader(
    			new InputStreamReader(conn.getInputStream()));
    	StringBuilder sb = new StringBuilder();
    	String line;
    	while ((line = rd.readLine()) != null) {
    		sb.append(line);
    	}
    	rd.close();

    	conn.disconnect();
    	return sb.toString();
    }
}
