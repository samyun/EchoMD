package echomd;

import java.util.Arrays;
import java.util.List;

/**
 * Util containing various text related utils.
 */
public final class echomdTextUtil {

    private echomdTextUtil() {
    }

    /**
     * List of drug names blacklisted for this app.
     */
    private static final List<String> NAME_BLACKLIST = Arrays.asList("drug", "drugs", 
    												"pill", "pills", "med", "meds", "medicine",
    												"tablet", "tablets", "dose", "doses", "lozenge",
    												"lozenges");

    /**
     * Text of complete help.
     */
    public static final String COMPLETE_HELP =


            "Here's some things you can say. Did I take my medicine, I have taken 1 pill of ibuprofin, "
                    + "and exit.";

    /**
     * Text of next help.
     */
    public static final String NEXT_HELP = "You can check if you took your medication, "
            + "tell me that you took medicine, or say help. What would you like?";

    /**
     * Cleans up the drug name, and sanitizes it against the blacklist.
     *

     * @param recognizedDrugName
     * @return
     */
    public static String getDrugName(String recognizedDrugName) {

        if (recognizedDrugName == null || recognizedDrugName.isEmpty()) {
            return null;
        }

        String cleanedName;
        if (recognizedDrugName.contains(" ")) {
            // the name should only contain a first name, so ignore the second part if any
            cleanedName = recognizedDrugName.substring(recognizedDrugName.indexOf(" "));
        } else {

            cleanedName = recognizedDrugName;
        }

        // if the name is on our blacklist, it must be mis-recognition
        if (NAME_BLACKLIST.contains(cleanedName)) {
            return null;
        }

        return cleanedName;
    }
}