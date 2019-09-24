import com.google.gson.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;


public class Parsing {
    public static void parsingHTML(String inHtml) throws IOException {


        Connection connect = Jsoup.connect(inHtml);
        connect.maxBodySize(0);
        connect.timeout(0);
        Document document = connect.get();

        StationIndex stationIndex = new StationIndex();

        String tempNumberLine = "";
        String numberLine = "";
        String nameLine = "";
        String nameStation = "";
        String nameStationTransition = "";


        Elements allElements = document.getElementsByTag("table").eq(3).select("tr:gt(0)");
        Elements monorail = document.getElementsByTag("table").eq(4).select("tr:gt(1)");
        Elements centralRing = document.getElementsByTag("table").eq(5).select("tr:gt(1)");
        allElements.addAll(monorail);
        allElements.addAll(centralRing);

        int countIn = 0;
        int countOut = 0;

        for (Element element : allElements) {

            numberLine = element.select("td:eq(0)").select("span:eq(0)").text();
            nameLine = element.select("td:eq(0)").select("span:eq(1)").attr("title");

            if (element.select("td:eq(1)").select("span:eq(0)").hasText()) {
                nameStation = element.select("td:eq(1)").select("span:eq(0)").text();
            } else {
                nameStation = element.select("td:eq(1)").select("a:eq(0)").text();
            }


            if (numberLine != tempNumberLine) {
                stationIndex.addLine(new Line(numberLine, nameLine));
                tempNumberLine = numberLine;
            }

            stationIndex.addStation(new Station(nameStation, stationIndex.getLine(numberLine)));

            countIn++;

        }

        for (Element element : allElements) {

            if (element.select("td:eq(1)").select("span:eq(0)").hasText()) {
                nameStation = element.select("td:eq(1)").select("span:eq(0)").text();
            } else {
                nameStation = element.select("td:eq(1)").select("a:eq(0)").text();
            }

            for (int i = 0; element.select("td:eq(3)").select("span:eq(" + i++ + ")").hasText(); ) {

                nameStationTransition = element.select("td:eq(3)").select("span:eq(" + i++ + ")").attr("title");

                for (Station s : stationIndex.stations) {
                    if (nameStationTransition.toUpperCase().contains(s.toString().toUpperCase()))
                        nameStationTransition = s.toString();

                }

                stationIndex.addConnection(new ArrayList<>(Arrays.asList(
                        stationIndex.getStation(nameStation), stationIndex.getStation(nameStationTransition))));
                System.out.println(nameStationTransition + " --*");
            }
        }

        System.out.println("\nЛинии Московского метро:");
        Set<Map.Entry<String, Line>> set = stationIndex.number2line.entrySet();
        for (Map.Entry<String, Line> l : set) {
            System.out.print(l.getKey() + ": ");
            System.out.println(l.getValue().getName());
        }

        System.out.println("\nСтанции Московского метро:");
        int countLine = 0;
        String numLine = "";
        for (Station s : stationIndex.stations) {
            if (countLine == 0) numLine = s.getLine().getNumber();
            countLine++;
            if (!numLine.equals(s.getLine().getNumber())) {
                System.out.println(numLine + " линия => " + countLine + " станций;");
                countLine = 0;
            }
            System.out.println(s.getLine().getNumber() + " => линия: " + s.getLine().getName() + " => станция: " + s.getName());
            countOut++;
        }
        System.out.println(numLine + " линия => " + countLine + " станций;");

        System.out.println();
        System.out.println("Количество станций при парсинге  = " + countIn);
        System.out.println("Количество станций прогруженных = " + countOut);


        System.out.println("\nПереходы Московского метро:");
        Set<Map.Entry<Station, ConnectionStation>> setTree = stationIndex.connections.entrySet();

        for (Map.Entry<Station, ConnectionStation> sT : setTree) {
            System.out.print(sT.getKey() + " => " + sT.getValue().getStations() + "\n");
        }

        String toJson = "..\\JsonParsing\\Data\\stationIndex.json";
        parsingJson(stationIndex, toJson);


    }

    public static void parsingJson(StationIndex stationIndex, String fileJson) throws IOException {


//        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(fileJson)) {
            gson.toJson(stationIndex, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gsonIn = new Gson();

        StationIndex stInd = null;
        try (Reader reader = new FileReader(fileJson)) {
            stInd = gsonIn.fromJson(reader, StationIndex.class);

        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("\nСтанции Московского метро из JSON - файла:");
        int countLine = 0;
        String numLine = "";
        int countOut = 0;
        for (Station s : stInd.stations) {
            if (countLine == 0) numLine = s.getLine().getNumber();
            countLine++;
            if (!numLine.equals(s.getLine().getNumber())) {
                System.out.println(numLine + " линия => " + countLine + " станций;");
                countLine = 0;
            }
            System.out.println(s.getLine().getNumber() + " => линия: " + s.getLine().getName() + " => станция: " + s.getName());
            countOut++;
        }
        System.out.println(numLine + " линия => " + countLine + " станций;");

        System.out.println();
        System.out.println("Количество станций из JSON - файла = " + countOut);

    }

}
