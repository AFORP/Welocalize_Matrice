package fr.aforp.wilocalyse.database;

import java.util.ArrayList;

import fr.aforp.wilocalyse.navigation.Position;

public class LocationDatabase
{
    private ArrayList<Location> locations;

    public LocationDatabase(ArrayList<Location> locations)
    {
        this.locations = locations;
    }

    public ArrayList<String> getBuildings()
    {
        ArrayList<String> buildings = new ArrayList<>();
        for (Location location : locations)
        {
            if (location.building.equals("A") || location.building.equals("E")) {
                if (!buildings.contains(location.building)) {
                    buildings.add(location.building);
                }
            }
        }
        return buildings;
    }

    public ArrayList<String> getStages(String building)
    {
        ArrayList<String> stages = new ArrayList<>();
        for (Location location : locations)
        {
            if (location.building.equals(building) && location.stage.equals("RDC")) {
                if (location.building.equals(building) && !stages.contains(location.stage))
                {
                    stages.add(location.stage);
                }
            }
        }
        return stages;
    }

    public ArrayList<String> getRooms(String building, String stage)
    {
        ArrayList<String> rooms = new ArrayList<>();
        for (Location location : locations)
        {
            if (location.building.equals(building) && location.stage.equals(stage) && !rooms.contains(location.room))
            {
                rooms.add(location.room);
            }
        }
        return rooms;
    }

    public Position getPosition(String building, String stage, String room)
    {
        for (Location location : locations)
        {
            if (location.building.equals(building) && location.stage.equals(stage) && location.room.equals(room))
            {
                return location.position;
            }
        }
        return null;
    }
}
