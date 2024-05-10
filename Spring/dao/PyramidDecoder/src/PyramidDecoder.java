import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class PyramidDecoder {
    public static String decode(String messageFile) {
        // Initialize an empty HashMap to store words and their corresponding positions
        HashMap<Integer, String> wordMap = new HashMap<>();
        // Variable to track the number of lines in the input file
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(messageFile))) {
            String line;
            // Read each line of the file
            while ((line = br.readLine()) != null) {
                // Split each line into parts using whitespace as separator
                String[] parts = line.trim().split("\\s+");
                if (parts.length > 0) {
                    // Store the word and its position in the HashMap
                    wordMap.put(Integer.parseInt(parts[0]), parts[1]);
                    // Increment the counter for non-empty lines
                    count++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Build the decoded message
        StringBuilder decodedMessage = new StringBuilder();
        // Initialize variables for navigating the pyramid
        int curLine = 1; // Current line number
        int inc = 2;     // Increment for moving to the next line

        // Traverse the pyramid and extract words according to the pattern
        while (curLine <= count) {
            decodedMessage.append(wordMap.get(curLine)).append(" ");
            curLine += inc; // Move to the next line
            inc++;         // Increase the increment for the next iteration
        }

        return decodedMessage.toString().trim();
    }

    public static void main(String[] args) {
        String messageFilePath = "coding_qual_input (1).txt";
        String decodedMessage = decode(messageFilePath);
        System.out.println(decodedMessage);
    }
}
