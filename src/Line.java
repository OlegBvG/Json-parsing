import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import static java.lang.CharSequence.compare;

public class Line implements Comparable<Line>
{
    @Expose
    private String number;
    @Expose
    private String name;
    private List<Station> stations;

    public Line(String number, String name)
    {
        this.number = number;
        this.name = name;
        stations = new ArrayList<>();
    }

    public String getNumber()
    {
        return number;
    }

    public String getName()
    {
        return name;
    }

    public void addStation(Station station)
    {
        stations.add(station);
    }

    public List<Station> getStations()
    {
        return stations;
    }

    public int compareTo(Line line)
    {
        return compare(number, line.getNumber());
    }

    @Override
    public boolean equals(Object obj)
    {
        return compareTo((Line) obj) == 0;
    }
}