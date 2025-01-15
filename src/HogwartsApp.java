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
    public static final Pattern ID_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<id>(.*)</severity>");
    public static final Pattern STUDENTNAME_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<studentName>(.*)</studentName>");
    public static final Pattern HAUS_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<haus>(" +
            Haus.Gryffindor + "|" + Haus.Ravenclaw + "|" + Haus.Hufflepuff + "|" + Haus.Slytherin + ")</haus>");
    public static final Pattern LEHRERNAME_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<lehrerName>(.*)</lehrerName>}");
    public static final Pattern PUNKTE_PATTERN = Pattern.compile("\\p{javaSpaceChar}*<punkte>(.*)</punkte>");

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
}
