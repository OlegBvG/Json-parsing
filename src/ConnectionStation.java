import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ConnectionStation {

    private TreeSet<Station> stationsCon;

    public ConnectionStation()
    {
        stationsCon = new TreeSet<>();
    }

    public TreeSet<Station> getStations() {
        return stationsCon;
    }

    public void setStations(TreeSet<Station> stations) {
        this.stationsCon = stations;
    }

    public void addConnect(Station station, List<Station> stationsList) {
        stationsCon.addAll(stationsList.stream()
                .filter(s -> 0!=s.compareTo(station)).collect(Collectors.toList()));

    }

}
