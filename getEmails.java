package com.company;
import java.net.*;
import java.io.*;
import java.util.*;

public class getEmails {

  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(System.in);
    String input = scan.nextLine();
    getHTML(input);
  }

  public static void getHTML(String stringURL) throws Exception{
    URL address;
    address = new URL(stringURL);
    // Read the HTML from the given URL
    BufferedReader in = new BufferedReader(new InputStreamReader(address.openStream()));
    String inputLine;
    // Write each line of HTML to the output file output.txt
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
    while ((inputLine = in.readLine()) != null) {
      writer.write(inputLine);
    }
    in.close();
    writer.close();
    findEmails(stringURL);
  }

  public static void findEmails(String stringURL) throws Exception{
    String file = "output.txt";
    String line = null;
    // Read from the downloaded HTML
    FileReader fileReader = new FileReader(file);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    // Go through each line of the downloaded HTML
    while ((line = bufferedReader.readLine()) != null) {
      for (int i = 0; i < line.length(); i++) {
        // Check for references to other pages by looking for any HTML href's
        if (line.length() > (i+3) && line.substring(i, i+4).equals("href")) {
          String newURL = stringURL;
          int newPos = i + 5;
          // Read the address information
          while (line.length() > (newPos+1) && !line.substring(newPos, newPos+1).equals("\"")) {
            // Make a new URL based on the given href information
            newURL += line.substring(newPos, newPos+1);
            newPos++;
          }
          // Call the getHTML function on the newly found address
          getHTML(newURL);
        }
        // Check for emails addresses listed based on presence of @ symbol
        if (line.substring(i,i+1).equals("@")) {
          printEmail(line, i);
        }
      }
    }
    bufferedReader.close();
  }

  public static void printEmail(String line, int position) {
    String email = "@";
    int pos = position;
    // Find the part of the email address that comes after the @
    // i.e. hotmail.com, gmail.com, husky.neu.edu
    while (line.substring(pos, pos+1).matches("[a-zA-Z]") ||
            line.substring(pos, pos+1).equals(".")) {
      email += line.substring(pos, pos+1);
      pos++;
    }
    pos = position;
    // Find the part of the email address that comes before the @
    while (line.substring(pos-1, pos).matches("[a-zA-Z]")) {
      email = line.substring(pos-1, pos) + email;
      pos--;
    }
    System.out.println(email);
  }
}
