import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ConnectionStation {

    private TreeSet<Station> stationsCon;
/*    public ConnectionStation(TreeSet stations)
    {
        this.stations = stations;
    }
*/

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

    public void addConnect(Station station, List<Station> stations_) {
        stationsCon.addAll(stations_.stream()
                .filter(s -> !s.equals(station)).collect(Collectors.toList()));
    }

}
