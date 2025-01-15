import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



public class HogwartsApp {

    //todo for XML
    /**
     * A pattern to match and extract the value inside the <id> tag from an XML-like structure.
     * The expected format is:
     * <pre>
     *     <id>123</id>
     * </pre>
     * <p>
     * Regex Explanation:
     * <ul>
     *     <li>{@code \\p{javaSpaceChar}*} - Matches optional whitespace (spaces, tabs, etc.).</li>
     *     <li>{@code <id>} - Matches the literal opening tag {@code <id>}.</li>
     *     <li>{@code (.*)} - Captures any character (zero or more) inside the tag.</li>
     *     <li>{@code </id>} - Matches the literal closing tag {@code </id>}.</li>
     * </ul>
     * <p>
     * Usage: This pattern is used to parse and extract the value associated with the {@code <id>} tag
     * from an input string, typically in an XML or XML-like format.
     *
     * Example:
     * <pre>
     *     String input = "   <id>123</id>";
     *     Matcher matcher = ID_PATTERN.matcher(input);
     *     if (matcher.matches()) {
     *         String idValue = matcher.group(1); // Returns "123"
     *     }
     * </pre>
     */
    public static final Pattern ID_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<id>(.*)</severity>");

    /**
     * A pattern to match and extract the value inside the <studentName> tag from an XML-like structure.
     * The expected format is:
     * <pre>
     *     <studentName>John Doe</studentName>
     * </pre>
     * <p>
     * Regex Explanation:
     * <ul>
     *     <li>{@code \\p{javaSpaceChar}*} - Matches optional whitespace (spaces, tabs, etc.).</li>
     *     <li>{@code <studentName>} - Matches the literal opening tag {@code <studentName>}.</li>
     *     <li>{@code (.*)} - Captures any character (zero or more) inside the tag.</li>
     *     <li>{@code </studentName>} - Matches the literal closing tag {@code </studentName>}.</li>
     * </ul>
     * <p>
     * Usage: This pattern is used to parse and extract the student's name from an input string.
     *
     * Example:
     * <pre>
     *     String input = "   <studentName>Harry Potter</studentName>";
     *     Matcher matcher = STUDENTNAME_PATTERN.matcher(input);
     *     if (matcher.matches()) {
     *         String studentName = matcher.group(1); // Returns "Harry Potter"
     *     }
     * </pre>
     */
    public static final Pattern STUDENTNAME_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<studentName>(.*)</studentName>");

    /**
     * A pattern to match and extract the value of a Hogwarts house from the <haus> tag.
     * The expected format is:
     * <pre>
     *     <haus>Gryffindor</haus>
     * </pre>
     * Supported values: {@code Gryffindor}, {@code Ravenclaw}, {@code Hufflepuff}, {@code Slytherin}.
     * <p>
     * Regex Explanation:
     * <ul>
     *     <li>{@code \\p{javaSpaceChar}*} - Matches optional whitespace (spaces, tabs, etc.).</li>
     *     <li>{@code <haus>} - Matches the literal opening tag {@code <haus>}.</li>
     *     <li>{@code Gryffindor|Ravenclaw|Hufflepuff|Slytherin} - Matches one of the predefined house names.</li>
     *     <li>{@code </haus>} - Matches the literal closing tag {@code </haus>}.</li>
     * </ul>
     * <p>
     * Usage: This pattern ensures only valid Hogwarts house names are matched and extracted.
     *
     * Example:
     * <pre>
     *     String input = "   <haus>Ravenclaw</haus>";
     *     Matcher matcher = HAUS_PATTERN.matcher(input);
     *     if (matcher.matches()) {
     *         String house = matcher.group(1); // Returns "Ravenclaw"
     *     }
     * </pre>
     */

    public static final Pattern HAUS_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<haus>(" +
            Haus.Gryffindor + "|" + Haus.Ravenclaw + "|" + Haus.Hufflepuff + "|" + Haus.Slytherin + ")</haus>");

    /**
     * A pattern to match and extract the value inside the <lehrerName> tag from an XML-like structure.
     * The expected format is:
     * <pre>
     *     <lehrerName>Professor Snape</lehrerName>
     * </pre>
     * <p>
     * Regex Explanation:
     * <ul>
     *     <li>{@code \\p{javaSpaceChar}*} - Matches optional whitespace (spaces, tabs, etc.).</li>
     *     <li>{@code <lehrerName>} - Matches the literal opening tag {@code <lehrerName>}.</li>
     *     <li>{@code (.*)} - Captures any character (zero or more) inside the tag.</li>
     *     <li>{@code </lehrerName>} - Matches the literal closing tag {@code </lehrerName>}.</li>
     * </ul>
     * <p>
     * Usage: This pattern is used to extract teacher names from input strings.
     *
     * Example:
     * <pre>
     *     String input = "   <lehrerName>Professor McGonagall</lehrerName>";
     *     Matcher matcher = LEHRERNAME_PATTERN.matcher(input);
     *     if (matcher.matches()) {
     *         String teacherName = matcher.group(1); // Returns "Professor McGonagall"
     *     }
     * </pre>
     */
    public static final Pattern LEHRERNAME_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<lehrerName>(.*)</lehrerName>");

    /**
     * A pattern to match and extract the value inside the <punkte> tag from an XML-like structure.
     * The expected format is:
     * <pre>
     *     <punkte>50</punkte>
     * </pre>
     * <p>
     * Regex Explanation:
     * <ul>
     *     <li>{@code \\p{javaSpaceChar}*} - Matches optional whitespace (spaces, tabs, etc.).</li>
     *     <li>{@code <punkte>} - Matches the literal opening tag {@code <punkte>}.</li>
     *     <li>{@code (.*)} - Captures any character (zero or more) inside the tag.</li>
     *     <li>{@code </punkte>} - Matches the literal closing tag {@code </punkte>}.</li>
     * </ul>
     * <p>
     * Usage: This pattern is used to parse point values associated with students or houses.
     *
     * Example:
     * <pre>
     *     String input = "   <punkte>100</punkte>";
     *     Matcher matcher = PUNKTE_PATTERN.matcher(input);
     *     if (matcher.matches()) {
     *         String points = matcher.group(1); // Returns "100"
     *     }
     * </pre>
     */
    public static final Pattern PUNKTE_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<punkte>(.*)</punkte>");


    // TODO for .txt
    /**
     * Reads a file line by line, parses its content based on a predefined format, and constructs a list of {@link Entry} objects.
     * The file must have a specific format where each line contains data fields separated by the "&" delimiter.
     *
     * Example File Format:
     * <pre>
     * 1&Harry Potter&Gryffindor&Professor McGonagall&50
     * 2&Hermione Granger&Gryffindor&Professor McGonagall&60
     * 3&Draco Malfoy&Slytherin&Professor Snape&40
     * </pre>
     *
     * Each line corresponds to an {@link Entry} object with the following attributes:
     * <ul>
     *     <li>Field 1: Integer ID</li>
     *     <li>Field 2: Student Name (String)</li>
     *     <li>Field 3: Hogwarts House (Enum {@link Haus})</li>
     *     <li>Field 4: Teacher Name (String)</li>
     *     <li>Field 5: Points (Integer)</li>
     * </ul>
     *
     * @param path the path to the file to be read
     * @return a list of {@link Entry} objects populated with data from the file
     * @throws IOException if an I/O error occurs during file reading
     * @see Entry
     */
    public List<Entry> leseDatei(String path) throws IOException {
        Path filePath = Path.of(path);
        List<Entry> entries = new ArrayList<>();

        try(BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("&");
                Entry entry = new Entry(Integer.parseInt(fields[0]),fields[1], Haus.valueOf(fields[2]),fields[3],Integer.parseInt(fields[4]));

                entries.add(entry);
            }
            return entries;
        }
    }

    /**
     * Reads a JSON-like file line by line, parses its content, and constructs a list of {@link Entry} objects.
     * The file is expected to have a specific JSON-like structure where each object represents an {@link Entry}.
     *
     * Example JSON Format:
     * <pre>
     * [
     *     {
     *         "id": 1,
     *         "studentName": "Harry Potter",
     *         "haus": "Gryffindor",
     *         "lehrerName": "Professor McGonagall",
     *         "punkte": 50
     *     },
     *     {
     *         "id": 2,
     *         "studentName": "Hermione Granger",
     *         "haus": "Gryffindor",
     *         "lehrerName": "Professor McGonagall",
     *         "punkte": 60
     *     }
     * ]
     * </pre>
     *
     * Method Logic:
     * <ol>
     *     <li>Validates the file starts with a square bracket {@code [} to confirm a JSON array structure.</li>
     *     <li>Iterates through each JSON object in the array:
     *         <ul>
     *             <li>Extracts field names and values by splitting lines on the colon {@code :} character.</li>
     *             <li>Assigns the parsed values to the appropriate fields of an {@link Entry} object.</li>
     *         </ul>
     *     </li>
     *     <li>Validates the file ends with a closing square bracket {@code ]}.</li>
     * </ol>
     *
     * @param path the path to the JSON file to be read
     * @return a list of {@link Entry} objects populated with data from the JSON file
     * @throws IOException if an I/O error occurs or the file is not a valid JSON format
     * @see Entry
     */
    //TODO .json
    public List<Entry> leseJSON(String path) throws IOException {
        Path filePath = Path.of(path);
        List<Entry> entries = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(filePath)) {
            if (!reader.readLine().contains("[")) {
                throw new IOException("Invalid JSON file");
            }

            String nextLine = reader.readLine();

            while (!nextLine.contains("]")) {
                if (!nextLine.contains("{")) {
                    throw new IOException("Invalid JSON file");
                }

                Entry entry = new Entry(0,null,null,null,0);

                for (int i = 0; i < 5; i++) {
                    String line = reader.readLine();
                    String fieldName = line.split(":")[0].replace("\"", "").trim();

                    switch (fieldName) {
                        case "id": {
                            String s = line.split(":")[1].replace("\"", "").replace(",","").trim();
                            entry.setId(Integer.parseInt(s));
                            break;
                        }
                        case "studentName": {
                            String s = line.split(":")[1].replace("\"", "").replace(",","").trim();
                            entry.setStudentName(s);
                            break;
                        }
                        case "haus": {
                            String s = line.split(":")[1].replace("\"", "").replace(",","").trim();
                            entry.setHaus(Haus.valueOf(s));
                            break;
                        }
                        case "lehrerName": {
                            String s = line.split(":")[1].replace("\"", "").replace(",","").trim();
                            entry.setLehrerName(s);
                            break;
                        }
                        case "punkte": {
                            String s = line.split(":")[1].replace("\"", "").replace(",","").trim();
                            entry.setPunkte(Integer.parseInt(s));
                            break;
                        }
                    }
                }

                if (!reader.readLine().contains("}")) {
                    throw new IOException("Invalid JSON file");
                }

                entries.add(entry);
                nextLine = reader.readLine();
            }
        }
        return entries;
    }


    /**
     * Reads a CSV file line by line, parses its content, and constructs a list of {@link Entry} objects.
     * The file must have a specific structure where the first row is the header and subsequent rows contain values.
     *
     * Example CSV Format:
     * <pre>
     * id,studentName,haus,lehrerName,punkte
     * 1,Harry Potter,Gryffindor,Professor McGonagall,50
     * 2,Hermione Granger,Gryffindor,Professor McGonagall,60
     * 3,Draco Malfoy,Slytherin,Professor Snape,40
     * </pre>
     *
     * Method Logic:
     * <ol>
     *     <li>Reads the header row to determine the field names.</li>
     *     <li>Iterates through each subsequent row, splitting the values by the comma delimiter {@code ,}.</li>
     *     <li>Maps the field names from the header to the corresponding values in each row.</li>
     *     <li>Populates an {@link Entry} object and adds it to the result list.</li>
     * </ol>
     *
     * @param path the path to the CSV file to be read
     * @return a list of {@link Entry} objects populated with data from the CSV file
     * @throws IOException if an I/O error occurs during file reading
     * @see Entry
     */
    //TODO .csv
    public List<Entry> leseCSV(String path) throws IOException {
        Path filePath = Path.of(path);
        List<Entry> entries = new ArrayList<>();

        try(BufferedReader reader = Files.newBufferedReader(filePath)) {
            String header = reader.readLine();
            String[] fields = header.split(",");

            String line = reader.readLine();
            while(line != null) {
                String[] values = line.split(",");

                Entry entry = new Entry(0,null,null,null,0);

                for(int i = 0; i < fields.length; i++) {
                    String value = values[i].replace("\"", "");
                    switch (fields[i]) {
                        case "id": {
                            entry.setId(Integer.parseInt(value));
                            break;
                        }
                        case "studentName": {
                            entry.setStudentName(value);
                            break;
                        }
                        case "haus": {
                            entry.setHaus(Haus.valueOf(value));
                            break;
                        }
                        case "lehrerName": {
                            entry.setLehrerName(value);
                            break;
                        }
                        case "punkte": {
                            entry.setPunkte(Integer.parseInt(value));
                            break;
                        }
                    }
                }

                entries.add(entry);
                line = reader.readLine();
            }
        }
        return entries;
    }

    /**
     * Reads a TSV (Tab-Separated Values) file line by line, parses its content, and constructs a list of {@link Entry} objects.
     * The file must have a specific structure where the first row is the header and subsequent rows contain tab-separated values.
     *
     * Example TSV Format:
     * <pre>
     * id	studentName	haus	lehrerName	punkte
     * 1	Harry Potter	Gryffindor	Professor McGonagall	50
     * 2	Hermione Granger	Gryffindor	Professor McGonagall	60
     * 3	Draco Malfoy	Slytherin	Professor Snape	40
     * </pre>
     *
     * Method Logic:
     * <ol>
     *     <li>Reads the header row to determine the field names.</li>
     *     <li>Iterates through each subsequent row, splitting the values by the tab delimiter {@code \t}.</li>
     *     <li>Maps the field names from the header to the corresponding values in each row.</li>
     *     <li>Populates an {@link Entry} object and adds it to the result list.</li>
     * </ol>
     *
     * @param path the path to the TSV file to be read
     * @return a list of {@link Entry} objects populated with data from the TSV file
     * @throws IOException if an I/O error occurs during file reading
     * @see Entry
     */
    //todo for XML
    public List<Entry> leseTSV(String path) throws IOException {
        Path filePath = Path.of(path);
        List<Entry> entries = new ArrayList<>();

        try(BufferedReader reader = Files.newBufferedReader(filePath)) {
            String header = reader.readLine();
            String[] fields = header.split("\t");

            String line = reader.readLine();
            while(line != null) {
                String[] values = line.split("\t");

                Entry entry = new Entry(0,null,null,null,0);

                for(int i = 0; i < fields.length; i++) {
                    String value = values[i].replace("\"", "");
                    switch (fields[i]) {
                        case "id": {
                            entry.setId(Integer.parseInt(value));
                            break;
                        }
                        case "studentName": {
                            entry.setStudentName(value);
                            break;
                        }
                        case "haus": {
                            entry.setHaus(Haus.valueOf(value));
                            break;
                        }
                        case "lehrerName": {
                            entry.setLehrerName(value);
                            break;
                        }
                        case "punkte": {
                            entry.setPunkte(Integer.parseInt(value));
                            break;
                        }
                    }
                }

                entries.add(entry);
                line = reader.readLine();
            }
        }
        return entries;
    }

    public List<Entry> leseXML(String path) throws IOException {
        Path filePath = Path.of(path);
        List<Entry> entries = new ArrayList<>();

        try(BufferedReader reader = Files.newBufferedReader(filePath)) {
            if (!reader.readLine().equals("<entries>")) {
                throw new IOException("Invalid XML file");
            }

            String line = reader.readLine();

            while (!line.equals("</entries>")) {
                Entry entry = new Entry(0,null,null,null,0);

                if (!line.contains("<entry>")) {
                    throw new IOException("Invalid XML file");
                }

                String nextLine = reader.readLine();
                while (!nextLine.contains("</entry>")) {
                    Matcher idMatcher = ID_PATTERN.matcher(nextLine);
                    if (idMatcher.matches()) {
                        entry.setId(Integer.parseInt(idMatcher.group(1)));
                    }

                    Matcher studentNameMatcher = STUDENTNAME_PATTERN.matcher(nextLine);
                    if (studentNameMatcher.matches()) {
                        entry.setStudentName(studentNameMatcher.group(1));
                    }

                    Matcher hausMatcher = HAUS_PATTERN.matcher(nextLine);
                    if (hausMatcher.matches()) {
                        entry.setHaus(Haus.valueOf(hausMatcher.group(1)));
                    }

                    Matcher lehrerNameMatcher = LEHRERNAME_PATTERN.matcher(nextLine);
                    if (lehrerNameMatcher.matches()) {
                        entry.setLehrerName(lehrerNameMatcher.group(1));
                    }

                    Matcher punkteMatcher = PUNKTE_PATTERN.matcher(nextLine);
                    if (punkteMatcher.matches()) {
                        entry.setPunkte(Integer.parseInt(punkteMatcher.group(1)));
                    }

                    nextLine = reader.readLine();
                }

                entries.add(entry);
                line = reader.readLine();
            }
        }
        return entries;
    }

    public void studierendeGrossbuchstaben(char g, List<Entry> entries) {
        Set<String> studierende = entries.stream().
                map(entry -> entry.getStudentName()).
                filter(studentName -> studentName.startsWith(String.valueOf(g))).
                collect(Collectors.toSet());

        for(String studentName : studierende) {
            System.out.println(studentName);
        }
    }

    public void griffindorStudierende(List<Entry> entries) {
        Set<String> studierende = entries.stream().
                filter(entry -> entry.getHaus().equals(Haus.valueOf("Gryffindor"))).
                map(entry -> entry.getStudentName()).
                collect(Collectors.toSet());

        for(String studentName : studierende) {
            System.out.println(studentName);
        }
    }


    //todo save in .txt
    /**
     * Writes the total points for each Hogwarts house to a file, sorted in descending order of points.
     * The output file will contain one line per house, formatted as {@code <Haus>#<Punkte>}.
     *
     * Example Output:
     * <pre>
     * Gryffindor#150
     * Ravenclaw#100
     * Hufflepuff#90
     * Slytherin#80
     * </pre>
     *
     * Method Logic:
     * <ol>
     *     <li>Initializes a map to calculate total points for each house, setting initial points to zero.</li>
     *     <li>Iterates through the provided {@link Entry} list, adding points to the corresponding house in the map.</li>
     *     <li>Sorts the map entries by points in descending order.</li>
     *     <li>Writes the sorted results to a file, with each line containing a house name and its total points, separated by {@code #}.</li>
     * </ol>
     *
     * @param path    the path to the file where the results will be written
     * @param entries the list of {@link Entry} objects containing the data to be processed
     * @throws IOException if an I/O error occurs during file writing
     * @see Entry
     * @see Haus
     */
    public void schreibeDatei(String path, List<Entry> entries) throws IOException {
        Path filePath = Path.of(path);
        Map<Haus, Integer> hausPunkte = new HashMap<>();

        for(Haus haus: Haus.values()) {
            hausPunkte.put(haus, 0);
        }

        for(Entry entry : entries) {
            hausPunkte.put(entry.getHaus(), hausPunkte.get(entry.getHaus()) + entry.getPunkte());
        }

        List<Map.Entry<Haus, Integer>> sortiert = hausPunkte.entrySet().
                stream().
                sorted((a,b) -> a.getValue() > b.getValue() ? -1 : 1).
                collect(Collectors.toList());

        try(FileWriter writer = new FileWriter(filePath.toFile())) {
            for(Map.Entry<Haus, Integer> entry: sortiert) {
                writer.write(entry.getKey() + "#" + entry.getValue() + "\n");
            }
        }
    }

    //todo save in .csv
    /**
     * Writes the total points for each Hogwarts house to a CSV file.
     * The output file will contain two columns: "Haus" and "Punkte".
     *
     * Example CSV Output:
     * <pre>
     * Haus,Punkte
     * Gryffindor,150
     * Ravenclaw,100
     * Hufflepuff,90
     * Slytherin,80
     * </pre>
     *
     * Method Logic:
     * <ol>
     *     <li>Initializes a map to calculate total points for each house.</li>
     *     <li>Iterates through the list of {@link Entry} objects to update house points.</li>
     *     <li>Sorts the map entries by points in descending order.</li>
     *     <li>Writes the sorted data to a CSV file with a header row.</li>
     * </ol>
     *
     * @param path    the path to the CSV file to be written
     * @param entries the list of {@link Entry} objects containing the data to be processed
     * @throws IOException if an I/O error occurs during file writing
     * @see Entry
     */
    public void schreibeCSV(String path, List<Entry> entries) throws IOException {
        Path filePath = Path.of(path);
        Map<Haus, Integer> hausPunkte = new HashMap<>();

        // Initialize points for each house
        for (Haus haus : Haus.values()) {
            hausPunkte.put(haus, 0);
        }

        // Aggregate points for each house
        for (Entry entry : entries) {
            hausPunkte.put(entry.getHaus(), hausPunkte.get(entry.getHaus()) + entry.getPunkte());
        }

        // Sort the houses by total points in descending order
        List<Map.Entry<Haus, Integer>> sortedEntries = hausPunkte.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toList());

        // Write the sorted data to a CSV file
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            // Write the header row
            writer.write("Haus,Punkte\n");

            // Write each house and its total points
            for (Map.Entry<Haus, Integer> entry : sortedEntries) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        }
    }

}
