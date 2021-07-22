package fr.aforp.wilocalyse.navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import fr.aforp.wilocalyse.R;

public class PathDrawer {
    int mapX;
    int mapY;
    Paint pBlue = new Paint();
    Paint pStroke = new Paint();

    //Constructeur
    public PathDrawer() {

    }

    //Dessiner un chemin a partir de la route et du canvas de la carte
    public void drawOnMap(ArrayList<Integer[]> road, Canvas MapCanvas,int Xstart, int Ystart, int Xend, int Yend) {
        //Initialisation des pinceaux
        pBlue.setColor(Color.parseColor("#4887f5"));
        pBlue.setStyle(Paint.Style.STROKE);
        pBlue.setStrokeJoin(Paint.Join.ROUND);
        pBlue.setStrokeCap(Paint.Cap.ROUND);
        pBlue.setShadowLayer(0.3f, 0.2f, 0.2f, Color.parseColor("#0e3675"));

        for(int i = 0;i<road.size();i ++) {
            mapY = road.get(i)[0];
            mapX = road.get(i)[1];
            MapCanvas.drawCircle(mapX, mapY, 1, pBlue);
        }
    }
}
