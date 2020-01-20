import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Iterator;
import java.util.List;
//https://textapis.p.mashape.com/sentiment/?text=Same+system+-+same+problem+-+this+is+clearly+a+design+fault+Mine+was+fixed+under+by+samsung+inside+the+first+12+months+-+but+again+I+have+an+unusable+sound+system+I+bought+a+Samsung+TV+-+Samsung+blueray+sound+system+-+TV+failed+repaired+under+warranty+sound+system+failed+That+is+a+100%+failure+rate+Will+not+touch+Samsung+again

public class ApiHandler {

    private final static String API_KEY = "hpz5Cdfz83msheUadP45La7TeFYhp1KG4KPjsnikCL4QzxzWfX";
    private static final int TOTAL_LENGTH_RESULTS = 20;
    private List<String> searchResults;


    public ApiHandler(List<String> searchResults){
        this.searchResults = searchResults;
    }

    public void sendRequestForSentimentAnalysis() {

        Iterator<String> iterator = searchResults.iterator();

        for (int i = 0; i < TOTAL_LENGTH_RESULTS; i++){
            if (iterator.hasNext()){
                String text = iterator.next();
                System.out.println(text);
                text = text.replace("%", "");
                text = text.replace("\"", "");
                text = text.replace("(", "");
                text = text.replace(")", "");
                text = text.replace("'", "");
                text = text.replace(" ", "+");
                text = text.replace(".", "");

                JsonNode jsonNode = null;
                try {
                    jsonNode = sendRequest(text);
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
                System.out.println(jsonNode);
            }
        }



    }

    public JsonNode sendRequest(String text) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("https://textapis.p.mashape.com/sentiment/?text=" + text)
                .header("X-Mashape-Key", API_KEY)
                .header("Accept", "application/json")
                .asJson();

        return response.getBody();
    }
}
