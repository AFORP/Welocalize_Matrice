package fr.aforp.wilocalyse.navigation;

import fr.aforp.wilocalyse.R;

public class Indicator
{
    public class Indication
    {
        public String iconPath;
        public String text;

        Indication(int iconId, String text)
        {
            this.iconPath = String.valueOf(iconId);
            this.text = text;
        }
    }

    public Indication getGoAhead()
    {
        return new Indication(R.drawable.arrow_up, "Aller tout droit");
    }

    public Indication getGoBack()
    {
        return new Indication(R.drawable.arrow_up, "Revener en arrière");
    }

    public Indication getTurnRight()
    {
        return new Indication(R.drawable.arrow_rigth, "Tourner à droite");
    }

    public Indication getTurnLeft()
    {
        return new Indication(R.drawable.arrow_left, "Tourner à droite");
    }

    public Indication getStairs()
    {
        return new Indication(R.drawable.stairs, "Prendre les escaliers");
    }

    // etc.
}
