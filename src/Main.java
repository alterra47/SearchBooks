import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    private static HttpURLConnection fetchAPIresponse(String url) {
        try{
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            return con;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String readAPIresponse(HttpURLConnection con) {
        try{
            StringBuilder response = new StringBuilder();
            Scanner scanner = new Scanner(con.getInputStream());
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            return response.toString();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    private static JSONArray getBookData(String query) {
        query = query.replaceAll(" ", "+");
        String urlString = "https://www.dbooks.org/api/search/"+query;
        try{
            HttpURLConnection connection = fetchAPIresponse(urlString);
            if(connection.getResponseCode() !=200) {
                System.out.println("Error in connection: "+connection.getResponseCode());
                return null;
            }
            else{
                String booksJSON = readAPIresponse(connection);
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(booksJSON);
                return  (JSONArray) jsonObject.get("books");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static void displayBooksdata(JSONArray books) {
        for(int i = 0; i < books.size(); i++) {
            JSONObject book = (JSONObject) books.get(i);
            System.out.println("Title: "+book.get("title"));
            System.out.println("Authors: "+book.get("authors"));
        }
    }
    public static void main(String[] args) {
        try{
            String query=new String();
            Scanner scanner = new Scanner(System.in);
            do{
                System.out.println("Search books: (Enter No to exit)");
                query=scanner.nextLine();
                if(query.equals("No"))break;

                //get books data
                JSONArray bookData = (JSONArray) getBookData(query);
                displayBooksdata(bookData);
            }
            while(!query.equals("No"));
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}