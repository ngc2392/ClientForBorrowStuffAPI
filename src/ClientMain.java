//https://mas.lvc.edu/svn/cds362-s18/lmp004
//https://mas.lvc.edu/svn/cds362-s18/examples

import java.util.Scanner;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class ClientMain {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Scanner userInput = new Scanner(System.in);

        boolean keepGoing = true;

        String n;
        while(keepGoing) {

            System.out.println("Press 1 to do a GET, press 2 to do a POST, press 3 to do a PUT, press 4 to do a DELETE");

             n = userInput.nextLine();

            if(n.equalsIgnoreCase("1")) {

                System.out.println("We will do a GET request");

                System.out.println("type 'all' to get all items, type 'id' to get an item by id");

                n = userInput.nextLine();

                if(n.equalsIgnoreCase("all")) {

                    HttpGet httpGet = new HttpGet("http://localhost:8080/BorrowStuffWebApplication_war_exploded/items");

                    HttpResponse rawResponse = httpClient.execute(httpGet);

                    //convert the entity from the response into a json object
                    HttpEntity jsonResponse = rawResponse.getEntity();
                    String content = EntityUtils.toString(jsonResponse);
                    System.out.println(content);


                } else {

                    System.out.println("type an id number");

                    String id = userInput.nextLine();

                    System.out.println("http://localhost:8080/BorrowStuffWebApplication_war_exploded/items/"+id);

                    HttpGet httpGet = new HttpGet("http://localhost:8080/BorrowStuffWebApplication_war_exploded/items/"+id);

                    HttpResponse rawResponse = httpClient.execute(httpGet);

                    //convert the entity from the response into a json object
                    HttpEntity jsonResponse = rawResponse.getEntity();
                    String content = EntityUtils.toString(jsonResponse);
                    System.out.println(content);

                }

            } else if(n.equalsIgnoreCase("2")) {
                //do a POST
                System.out.println("Enter the name of the item");
                String name = userInput.nextLine();

                System.out.println("Enter the category of the item");
                String category = userInput.nextLine();

                System.out.println("Enter the description  of the item");

                String description = "";
                description = userInput.nextLine();


                System.out.println("Enter the beginning duration of the item (April 15 2018)");

                String durationBeginning = "";
                durationBeginning = userInput.nextLine();

                System.out.println("Enter the end duration of the item (May 15 2018)");

                String durationEnd = "";
                if(userInput.hasNext()){
                    durationEnd = userInput.nextLine();
                }

                System.out.println("Enter the zipcode of the location of the item");
                String zipCode = userInput.nextLine();

                System.out.println("Enter the id of who it is owned by");
                Integer ownedBy = Integer.valueOf(userInput.nextLine()); //need to convert this to int

                System.out.println("Enter the id of who it is borrowed by (press enter if no one has borrowed it");
                Integer borrowedBy = Integer.valueOf(userInput.nextLine()); //need to convert this to int

                JsonObject item1 = new JsonObject();

                item1.addProperty("id", 1);
                item1.addProperty("name", name);
                item1.addProperty("category", category);
                item1.addProperty("description", description);
                item1.addProperty("duration_beginning", durationBeginning);
                item1.addProperty("duration_end", durationEnd);
                item1.addProperty("belongs_to", ownedBy);
                item1.addProperty("borrowed_by", borrowedBy);
                item1.addProperty("zipcode",zipCode);

                String json = gson.toJson(item1);

                System.out.println("The object that will be sent to the server is " + json);

                HttpPost httpPost = new HttpPost("http://localhost:8080/BorrowStuffWebApplication_war_exploded/items");

                for(Header a : httpPost.getAllHeaders()) {
                    System.out.println(a.toString());
                }

                StringEntity entity = new StringEntity(item1.toString());
                entity.setContentType("Application/json");
                httpPost.setEntity(entity);

                HttpResponse rawResponse = httpClient.execute(httpPost);

                //convert the entity from the response into a json object
                HttpEntity jsonResponse = rawResponse.getEntity();
                String content = EntityUtils.toString(jsonResponse);
                System.out.println(content);

            } else if(n.equalsIgnoreCase("3")) {
                System.out.println("You will do a DELETE request");

                // do a delete
                System.out.println("Enter the id of the item you want to delete");
                String idOfItemToDelete = userInput.next();
                System.out.println("You are going to delete the item with id " + idOfItemToDelete);

                HttpDelete httpDelete = new HttpDelete("http://localhost:8080/BorrowStuffWebApplication_war_exploded/items/"+idOfItemToDelete);

                HttpResponse responseBody = httpClient.execute(httpDelete);
                System.out.println(responseBody);

            }

            System.out.println("Do you want to make another request");

            n = userInput.nextLine();

            if(!(n.equalsIgnoreCase("yes") || n.equalsIgnoreCase("y"))) {
                keepGoing = false;
            } else {
                keepGoing = true;
            }

        }

    }

}
