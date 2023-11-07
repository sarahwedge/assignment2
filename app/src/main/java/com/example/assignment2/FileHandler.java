package com.example.assignment2;
import java.io.BufferedReader;
import java.io.File;  // Import the File class
import java.io.FileInputStream;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner; // Import the Scanner class to read text files

public class FileHandler {
    public static String readFile(String filePath) throws IOException {
//        StringBuilder content = new StringBuilder();
//
//        // Create a FileInputStream and a BufferedReader to read the file
//        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
//             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assets.open(filePath)))) {
//
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                content.append(line).append('\n'); // Read each line and append it to the content
//            }
//        }
//        return content.toString();
        return filePath;
    }
}

