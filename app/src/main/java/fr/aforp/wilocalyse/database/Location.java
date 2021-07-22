package fr.aforp.wilocalyse.database;

import fr.aforp.wilocalyse.navigation.Position;

public class Location
{
    public String building;
    public String stage;
    public String room;
    public Position position;

    Location(String building, String stage, String room, int x, int y)
    {
        this.building = building;
        this.stage = stage;
        this.room = room;
        this.position = new Position(x, y);
    }

    Location(String building, String stage, String room, Position position)
    {
        this.building = building;
        this.stage = stage;
        this.room = room;
        this.position = new Position(position);
    }

    @Override
    public String toString() {
        return "Location{" +
                "building='" + building + '\'' +
                ", stage='" + stage + '\'' +
                ", room='" + room + '\'' +
                ", position=" + position +
                '}';
    }
}
