package echomd.storage;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.amazon.speech.speechlet.Session;

/**
 * Represents a echomd instance.
 */
public final class echomdGame {
    private Session session;
    private echomdGameData gameData;

    private echomdGame() {
    }

    /**
     * Creates a new instance of {@link echomdGame} with the provided {@link Session} and
     * {@link echomdGameData}.
     * <p>
     * To create a new instance of {@link echomdGameData}, see
     * {@link echomdGameData#newInstance()}
     * 
     * @param session
     * @param gameData
     * @return
     * @see echomdGameData#newInstance()
     */
    public static echomdGame newInstance(Session session, echomdGameData gameData) {
        echomdGame game = new echomdGame();
        game.setSession(session);
        game.setGameData(gameData);
        return game;
    }

    protected void setSession(Session session) {
        this.session = session;
    }

    protected Session getSession() {
        return session;
    }

    protected echomdGameData getGameData() {
        return gameData;
    }

    protected void setGameData(echomdGameData gameData) {
        this.gameData = gameData;
    }

    /**
     * Returns the list containing the names of medications
     * 
     * @return 
     */
    public List<String> getNames() {
        return gameData.getNames();
    }

    /**
     * Returns true if the doctor has drug entries, false otherwise.
     * 
     * @return true if the doctor has drug entries, false otherwise
     */
    public boolean hasDrugEntries() {
        return !gameData.getNames().isEmpty();
    }

    /**
     * Returns the number of drugs in the system.
     * 
     * @return the number of drugs in the system.
     */
    public int getNumberOfDrugsInSystem() {
        return gameData.getNames().size();
    }

    /**
     * Add a drug to the doctor.
     * 
     * @param drugName
     *            Name of the drug
     */
    public void addDrugToSystem(String drugName) {
        gameData.getNames().add(drugName);
    }

    /**
     * Returns true if the drug exists in the doctor, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @return true if the drug exists in the doctor, false otherwise
     */
    public boolean hasDrug(String drugName) {
        List<String> list = gameData.getNames();
		for (String s : list)
		{
			if (s.equalsIgnoreCase(drugName))
				return true;
		}
		return false;
    }

    /**
     * Returns true if the doctor has dose left in period entries, false otherwise.
     * 
     * @return true if the doctor has dose left in period entries, false otherwise
     */
    public boolean hasNumDosesEntries() {
        return !gameData.getNumDoses().isEmpty();
    }

    /**
     * Returns the number of doses left in the perscription period for a drug
     * 
     * @param drugName
     *            drug name
     * @return number of doses left in the perscription period
     */
    public long getDosesLeftInPeriodForDrug(String drugName) {
		Map<String, Long> map = gameData.getNumDoses();
		for (Map.Entry<String, Long> pair : map.entrySet())
		{
			if (pair.getKey().equalsIgnoreCase(drugName))
				return pair.getValue();
		}
		return 0;
    }

    /**
     * Returns true if the numDoses entry for a drug exists in the doctor, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @return true if the drug exists in the doctor, false otherwise
     */
    public boolean hasNumDosesForDrug(String drugName) {
		Map<String, Long> map = gameData.getNumDoses();
		for (Map.Entry<String, Long> pair : map.entrySet())
		{
			if (pair.getKey().equalsIgnoreCase(drugName))
				return true;
		}
		return false;
    }

    /**
     * Adds the doses passed to it to the current doses for a drug. Returns true if the drug
     * existed, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @param dose
     *            dose to be added
     * @return true if the drug existed, false otherwise.
     */
    private boolean addDosesLeftInPeriodForDrug(String drugName, long dose) {

        long currentDose = 0L;
        if (hasDrug(drugName) && hasNumDosesForDrug(drugName)) {
        	currentDose = getDosesLeftInPeriodForDrug(drugName);
			gameData.getNumDoses().remove(drugName.toLowerCase());
			gameData.getNumDoses().put(drugName.toLowerCase(), Long.valueOf(currentDose + dose));
			return true;
        }
        else{
        	return false;
        }
    }

    /**
     * Remove the doses passed to it from the current doses for a drug. Returns true if the drug
     * existed, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @param dose
     *            dose to be removed
     * @return true if the drug existed, false otherwise.
     */
    public boolean removeDosesLeftInPeriodForDrug(String drugName, long dose) {

        long currentDose = 0L;
        if (hasDrug(drugName) && hasNumDosesForDrug(drugName)) {
        	currentDose = getDosesLeftInPeriodForDrug(drugName);
			gameData.getNumDoses().remove(drugName.toLowerCase());
            gameData.getNumDoses().put(drugName.toLowerCase(), Long.valueOf(currentDose - dose));
            return true;
        }
        else{
        	return false;
        }
    }
    
    public boolean resetDosesLeftInPeriodForDrug(String drugName) {
    	if (hasDrug(drugName) && hasNumDosesForDrug(drugName)) {
			gameData.getNumDoses().remove(drugName.toLowerCase());
            gameData.getNumDoses().put(drugName.toLowerCase(), Long.valueOf(getSupplyForDrug(drugName)));
            return true;
        }
        else{
        	return false;
        }
    }

    /**
     * Returns true if the doctor has last dates taken entries, false otherwise.
     * 
     * @return true if the doctor has last dates taken entries, false otherwise
     */
    public boolean hasLastDatesEntries() {
        return !gameData.getLastDates().isEmpty();
    }

    /**
     * Returns true if the lastDates entry for a drug exists in the doctor, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @return true if the drug exists in the doctor, false otherwise
     */
    public boolean hasLastDatesForDrug(String drugName) {
        return (getLastDatesForDrug(drugName) !=null);
    }

    /**
     * Returns the last date the drug was taken
     * 
     * @param drugName
     *            drug name
     * @return last date drug was taken
     */
    public Calendar getLastDatesForDrug(String drugName) {		
		Map<String, Calendar> map = gameData.getLastDates();
		for (Map.Entry<String, Calendar> pair : map.entrySet())
		{
			if (pair.getKey().equalsIgnoreCase(drugName))
				return pair.getValue();
		}
		return null;
    }

    /**
     * Sets the last date drug was taken to now. Returns true if the drug
     * existed, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @return true if the drug existed, false otherwise.
     */
    public boolean setLastDateToToday(String drugName) {
    	if (hasDrug(drugName)) {
			gameData.getLastDates().remove(drugName.toLowerCase());
            gameData.getLastDates().put(drugName.toLowerCase(), Calendar.getInstance());
            return true;
        }
        else{
        	return false;
        }
    }

    /**
     * Compares last date a drug was used to today. Returns true if used today,
     * false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @return true if the drug was used today, false otherwise.
     */
    public boolean checkIfDrugUsedToday(String drugName) {

    	if (hasDrug(drugName) && hasLastDatesForDrug(drugName)) {
            Calendar lastUsed = getLastDatesForDrug(drugName);
            
            if (lastUsed == null)
            	return false;
            
            Calendar today = Calendar.getInstance();
            
            if (lastUsed.YEAR == today.YEAR && lastUsed.DAY_OF_YEAR == today.DAY_OF_YEAR)
            	return true;
            
            return false;
        }
        else{
        	return false;
        }
    }
    
    /**
     * If the drug was last taken prior to today, resync FHIR and reset
     * dayTotals.
     */
    public void resyncIfNewDay()
    {
    	boolean newDay = false;
    	List<String> names = gameData.getNames();
    	if (names.size() < 1)
		{
    		newDay = true;
		}
    	else
    	{
        	for (String s : names)
        	{
        		if (!checkIfDrugUsedToday(s))
        		{
        			newDay = true;
        			break;
        		}
        	}
    	}

    	
    	if (newDay)
    	{
    		resyncFHIR();
    	}
    }
	
	/**
	 * Resyncs with the FHIR service.
	 */
	public void resyncFHIR()
	{
		gameData.resyncFHIR();
	}

    /**
     * Returns true if the doctor has day total entries, false otherwise.
     * 
     * @return true if the doctor has day total entries, false otherwise
     */
    public boolean hasDayTotalEntries() {
        return !gameData.getDayTotals().isEmpty();
    }

    /**
     * Returns the number of doses taken in the day for a drug
     * 
     * @param drugName
     *            drug name
     * @return number of doses left in the perscription period
     */
    public long getDosesTakenTodayForDrug(String drugName) {
		Map<String, Long> map = gameData.getDayTotals();
		for (Map.Entry<String, Long> pair : map.entrySet())
		{
			if (pair.getKey().equalsIgnoreCase(drugName))
				return pair.getValue();
		}
		return 0;
    }

    /**
     * Returns true if the dayTotals entry for a drug exists in the doctor, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @return true if the drug exists in the doctor, false otherwise
     */
    public boolean hasDayTotalsForDrug(String drugName) {
		Map<String, Long> map = gameData.getDayTotals();
		for (Map.Entry<String, Long> pair : map.entrySet())
		{
			if (pair.getKey().equalsIgnoreCase(drugName))
				return true;
		}
		return false;
    }

    /**
     * Adds the doses passed to it to the current doses taken today for a drug. Returns true if the drug
     * existed, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @param dose
     *            dose to be added
     * @return true if the drug existed, false otherwise.
     */
    public boolean addDosesTakenTodayForDrug(String drugName, long dose) {

        long currentDose = 0L;
        if (hasDrug(drugName) && hasDayTotalsForDrug(drugName)) {
        	currentDose = getDosesTakenTodayForDrug(drugName);
			gameData.getDayTotals().remove(drugName.toLowerCase());
            gameData.getDayTotals().put(drugName.toLowerCase(), Long.valueOf(currentDose + dose));
            return true;
        }
        else{
        	return false;
        }
    }

    /**
     * Resets current doses taken today for a drug to 0. Returns true if the drug
     * existed, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @return true if the drug existed, false otherwise.
     */
    public boolean resetDosesTakenTodayForDrug(String drugName) {

        long currentDose = 0L;
        if (hasDrug(drugName) && hasDayTotalsForDrug(drugName)) {
			gameData.getDayTotals().remove(drugName.toLowerCase());
        	gameData.getDayTotals().put(drugName.toLowerCase(), null);
        	return true;
        }
        else{
        	return false;
        }
    }

    /**
     * Remove the doses passed to it from the current doses taken today for a drug. Returns true if the drug
     * existed, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @param dose
     *            dose to be removed
     * @return true if the drug existed, false otherwise.
     */
    private boolean removeDosesTakenTodayForDrug(String drugName, long dose) {

        long currentDose = 0L;
        if (hasDrug(drugName) && hasDayTotalsForDrug(drugName)) {
        	currentDose = getDosesTakenTodayForDrug(drugName);
			gameData.getDayTotals().remove(drugName.toLowerCase());
            gameData.getDayTotals().put(drugName.toLowerCase(), Long.valueOf(currentDose - dose));
            return true;
        }
        else{
        	return false;
        }
    }

    /**
     * Returns true if the doctor has frequency entries, false otherwise.
     * 
     * @return true if the doctor has frequency entries, false otherwise
     */
    public boolean hasFrequencyEntries() {
        return !gameData.getFrequency().isEmpty();
    }

    /**
     * Returns the frequency to take a drug
     * 
     * @param drugName
     *            drug name
     * @return frequency to take the drug
     */
    public long getFrequencyForDrug(String drugName) {
		Map<String, Long> map = gameData.getFrequency();
		for (Map.Entry<String, Long> pair : map.entrySet())
		{
			if (pair.getKey().equalsIgnoreCase(drugName))
				return pair.getValue();
		}
		return 0;
    }

    /**
     * Returns true if the frequency entry for a drug exists in the doctor, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @return true if the drug exists in the doctor, false otherwise
     */
    public boolean hasFrequencyForDrug(String drugName) {
		Map<String, Long> map = gameData.getFrequency();
		for (Map.Entry<String, Long> pair : map.entrySet())
		{
			if (pair.getKey().equalsIgnoreCase(drugName))
				return true;
		}
		return false;
    }

    /**
     * Change the dosage frequency for a drug. Returns true if the drug
     * existed, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @param frequency
     *            frequency to be changed to
     * @return true if the drug existed, false otherwise.
     */
    public boolean changeFrequencyOfDrug(String drugName, long frequency) {

        if (hasDrug(drugName) && hasFrequencyForDrug(drugName)) {
			gameData.getFrequency().remove(drugName.toLowerCase());
        	gameData.getFrequency().put(drugName.toLowerCase(), Long.valueOf(frequency));
        	return true;
        }
        else{
        	return false;
        }
    }

    /**
     * Returns true if the doctor has amount of supply entries, false otherwise.
     * 
     * @return true if the doctor has amount of supply entries, false otherwise
     */
    public boolean hasSupplyEntries() {
        return !gameData.getSupply().isEmpty();
    }

    /**
     * Returns the amount supplied for a drug
     * 
     * @param drugName
     *            drug name
     * @return amount supplied for a drug
     */
    public long getSupplyForDrug(String drugName) {
		Map<String, Long> map = gameData.getSupply();
		for (Map.Entry<String, Long> pair : map.entrySet())
		{
			if (pair.getKey().equalsIgnoreCase(drugName))
				return pair.getValue();
		}
		return 0;
    }

    /**
     * Returns true if the amount supplied for a drug exists in the doctor, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @return true if the drug exists in the doctor, false otherwise
     */
    public boolean hasSupplyForDrug(String drugName) {
		Map<String, Long> map = gameData.getSupply();
		for (Map.Entry<String, Long> pair : map.entrySet())
		{
			if (pair.getKey().equalsIgnoreCase(drugName))
				return true;
		}
		return false;
    }

    /**
     * Change the supply for a drug. Returns true if the drug
     * existed, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @param supply
     *            supply to be changed to
     * @return true if the drug existed, false otherwise.
     */
    public boolean changeSupplyOfDrug(String drugName, long supply) {

        if (hasDrug(drugName) && hasSupplyForDrug(drugName)) {
			gameData.getSupply().remove(drugName.toLowerCase());
        	gameData.getSupply().put(drugName.toLowerCase(), Long.valueOf(supply));
        	return true;
        }
        else{
        	return false;
        }
    }

    /**
     * Returns true if the doctor has amount of refill entries, false otherwise.
     * 
     * @return true if the doctor has amount of refill entries, false otherwise
     */
    public boolean hasNumRefillsEntries() {
        return !gameData.getNumRefills().isEmpty();
    }

    /**
     * Returns the number of refills for a drug
     * 
     * @param drugName
     *            drug name
     * @return number of refills for a drug
     */
    public long getNumRefillsForDrug(String drugName) {
		Map<String, Long> map = gameData.getNumRefills();
		for (Map.Entry<String, Long> pair : map.entrySet())
		{
			if (pair.getKey().equalsIgnoreCase(drugName))
				return pair.getValue();
		}
		return 0;
    }

    /**
     * Returns true if the number of refills for a drug exists in the doctor, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @return true if the drug exists in the doctor, false otherwise
     */
    public boolean hasNumRefillsForDrug(String drugName) {
		Map<String, Long> map = gameData.getNumRefills();
		for (Map.Entry<String, Long> pair : map.entrySet())
		{
			if (pair.getKey().equalsIgnoreCase(drugName))
				return true;
		}
		return false;
    }

    /**
     * Change the refill for a drug. Returns true if the drug
     * existed, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @param refill
     *            refill to be changed to
     * @return true if the drug existed, false otherwise.
     */
    public boolean changeNumRefillsOfDrug(String drugName, long refill) {

        if (hasDrug(drugName) && hasNumRefillsForDrug(drugName)) {
			gameData.getNumRefills().remove(drugName.toLowerCase());
        	gameData.getNumRefills().put(drugName.toLowerCase(), Long.valueOf(refill));
        	return true;
        }
        else{
        	return false;
        }
    }

    /**
     * Remove the specificed amount of refills from the total for a drug. Returns true if the drug
     * existed, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @param refill
     *            refills to remove
     * @return true if the drug existed, false otherwise.
     */
    public boolean removeNumRefillsOfDrug(String drugName, long refill) {

        if (hasDrug(drugName) && hasNumRefillsForDrug(drugName)) {
        	long currentRefills = getNumRefillsForDrug(drugName);
			gameData.getNumRefills().remove(drugName.toLowerCase());
        	gameData.getNumRefills().put(drugName.toLowerCase(), Long.valueOf(currentRefills - refill));
        	return true;
        }
        else{
        	return false;
        }
    }
    
    /**
     * Remove the 1 refill from the total for a drug. Returns true if the drug
     * existed, false otherwise.
     * 
     * @param drugName
     *            Name of the drug
     * @return true if the drug existed, false otherwise.
     */
    public boolean removeOneNumRefillsOfDrug(String drugName){
    	return removeNumRefillsOfDrug(drugName, 1);
    }
}
