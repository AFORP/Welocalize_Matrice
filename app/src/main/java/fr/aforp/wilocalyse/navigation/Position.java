package fr.aforp.wilocalyse.navigation;

public class Position
{
    public int x;
    public int y;

    public Position()
    {
        this.x = 0;
        this.y = 0;
    }

    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Position(Position position)
    {
        this.x = position.x;
        this.y = position.y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
