import com.google.gson.annotations.Expose;
import java.util.*;

public class StationIndex
{

    @Expose(serialize = true, deserialize = true)
    HashMap<String, Line> number2line;
    @Expose(serialize = true, deserialize = true)
    TreeSet<Station> stations;
    @Expose(serialize = true, deserialize = false)
    transient TreeMap<Station, ConnectionStation> connections;

    public StationIndex()
    {
        number2line = new HashMap<>();
        stations = new TreeSet<>();
        connections = new TreeMap<>();
    }

    public void addStation(Station station)
    {
        stations.add(station);
    }

    public void addLine(Line line)
    {
        number2line.put(line.getNumber(), line);
    }

    public void addConnection(List<Station> stations)
    {
        for(Station station : stations)
        {

            if(!connections.containsKey(station)) {
                connections.put(station, new ConnectionStation());
            }

            ConnectionStation connectedStations = connections.get(station);

            connectedStations.addConnect(station, stations);

        }
    }

    public Line getLine(String number)
    {
        return number2line.get(number);
    }

    public Station getStation(String name)
    {
        for(Station station : stations)
        {
            if(station.getName().equalsIgnoreCase(name)) {
                return station;
            }
        }
        return null;
    }

    public Station getStation(String name, String lineNumber)
    {
        Station query = new Station(name, getLine(lineNumber));
        Station station = stations.ceiling(query);
        return station.equals(query) ? station : null;
    }

    public ConnectionStation getConnectedStations(Station station)
    {
        if(connections.containsKey(station)) {
            return connections.get(station);
        }
        return new ConnectionStation() {
        };
    }
}


