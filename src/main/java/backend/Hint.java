package backend;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/*
 * @Author Ezzaamari Nassim
 * Simple HTTP Java client that will ask user for the word that he wants a hint for
 */

public class Hint {
    static HttpClient client = createHttpClient();

    /*
       Create a (an ?) HttpClient object that is going to be used for the sending and receiving operations
       when communicating with the Python server
       @return an HttpClient object
     */
    public static HttpClient createHttpClient()
    {
        return HttpClient.newHttpClient();
    }

    /*
      Create a HttpRequest object that contains the word which we need to get the hint from
      @param wordNeedingHint A string that corresponds to the word we're sending to the server
      @return This function returns a request object or null in the case the request couldn't be created
     */
    public static HttpRequest createHintRequest(String wordNeedingHint)
    {
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8000"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(wordNeedingHint, StandardCharsets.UTF_8))
                    .build();
        } catch (URISyntaxException e) {
            System.out.println("Couldn't create the request...");
        }
        return request;
    }

    /*
     This function is responsible of sending and receiving the request to the python server
     @param hintRequestToSend Is a request that contains the word in which we need the hint
     @return HttpResponse Which is the response that contains the information about the handling of the request
     by the Python server, if it's null then the Python server didn't answer at all, otherwise it either contain
     the error code or the hint itself
     */
    public static HttpResponse sendHintRequest(HttpRequest hintRequestToSend)
    {
        HttpResponse<String> response = null;
        try {
            response = client.send(hintRequestToSend, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            System.out.println("Unknown error occured during the sending of the request");
        } catch (InterruptedException e) {
            System.out.println("The sending request has been interrupted");
        }
        return  response;
    }

    /*
      Using a HttpResponse object, this function will handle the response accordingly, if an error
      has occured it will say which, otherwise it shall just return the hint itself
      @param response which is the response from the Python server (can be null in case of no answers)
      @return It either returns the hint, or null, if it returns null then it means an error has occured
     */
    public static String handleResponse(HttpResponse response)
    {
        if (response == null)
        {
            System.out.println("Response is null, server might not be on...");
            return null;
        }

        switch (response.statusCode())
        {
            case 404:
                System.out.println("The server couldn't find the word on its model database...");
                return null;
            case 200:
                return response.body().toString();
        }
        return null;
    }

    /*
      This simply wraps various functions in order to handle the whole hint request for the user
      @param wordNeedingHint A string that corresponds to the word we're sending to the server
      @return This function returns a SINGLE hint.
     */
    public static String getOneHint(String wordNeedingHint)
    {
        //it has been put in lowercase in order to avoid some issues, technically this could be done
        //within the server script as well
        wordNeedingHint = wordNeedingHint.toLowerCase();
        HttpRequest request = createHintRequest(wordNeedingHint);
        HttpResponse response = sendHintRequest(request);
        String hint = handleResponse(response);
        return hint;
    }


    public static void main(String args[]) {
        client = createHttpClient();
        while (true)
        {
            Scanner myObj = new Scanner(System.in);
            System.out.println("Please input the word from which you need to get a hint");
            String wordNeedingHint = myObj.nextLine();
            String hint = getOneHint(wordNeedingHint);
            if (hint != null)
            {
                System.out.println("Here's the hint : " + hint);
            }
            else
            {
                //The hint is null therefore... well what do you want this to do if it's null?
            }
        }
    }
}
