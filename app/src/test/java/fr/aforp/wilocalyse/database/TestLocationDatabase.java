package fr.aforp.wilocalyse.database;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import fr.aforp.wilocalyse.navigation.Position;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestLocationDatabase
{
    ArrayList<Location> locations = new ArrayList<>();

    @Before
    public void before() throws Exception
    {
        locations.add(new Location("A", "1", "Red", 50 ,60));
        locations.add(new Location("A", "1", "Blue", 50 ,60));
        locations.add(new Location("A", "22", "Green", 50 ,60));
        locations.add(new Location("B", "1", "Grey", 50 ,60));
        locations.add(new Location("B", "5", "Red", 50 ,60));
        locations.add(new Location("C", "1", "Red", 4 ,500));
    }

    @Test
    public void shallFindAllBuildings()
    {
        LocationDatabase sut = new LocationDatabase(locations);
        String[] expected = {"A", "B", "C"};
        assertArrayEquals(expected, sut.getBuildings().toArray());
    }

    @Test
    public void shallFindAllStages()
    {
        LocationDatabase sut = new LocationDatabase(locations);
        String[] expected = {"1", "22"};
        assertArrayEquals(expected, sut.getStages("A").toArray());
    }

    @Test
    public void shallReturnEmptyArrayForInvalidBuilding()
    {
        LocationDatabase sut = new LocationDatabase(locations);
        String[] expected = {};
        assertArrayEquals(expected, sut.getStages("NA").toArray());
    }

    @Test
    public void shallFindAllRooms()
    {
        LocationDatabase sut = new LocationDatabase(locations);
        String[] expected = {"Red", "Blue"};
        assertArrayEquals(expected, sut.getRooms("A", "1").toArray());
    }

    @Test
    public void shallReturnEmptyArrayForInvalidBuildingAndValidStage()
    {
        LocationDatabase sut = new LocationDatabase(locations);
        String[] expected = {};
        assertArrayEquals(expected, sut.getRooms("Invalid", "1").toArray());
    }

    @Test
    public void shallReturnEmptyArrayForValidBuildingAndInvalidStage()
    {
        LocationDatabase sut = new LocationDatabase(locations);
        String[] expected = {};
        assertArrayEquals(expected, sut.getRooms("A", "Invalid").toArray());
    }

    @Test
    public void shallFindPosition()
    {
        LocationDatabase sut = new LocationDatabase(locations);
        Position position = sut.getPosition("C", "1", "Red");
        assertEquals(4, position.x);
        assertEquals(500, position.y);
    }

    @Test
    public void shallReturnNullPositionForInvalidLocation()
    {
        LocationDatabase sut = new LocationDatabase(locations);
        Position expected = null;
        assertEquals(expected, sut.getPosition("Invalid", "1", "Red"));
    }
}
