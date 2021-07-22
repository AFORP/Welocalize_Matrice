package fr.aforp.wilocalyse.algorithms;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import fr.aforp.wilocalyse.R;
import fr.aforp.wilocalyse.algorithms.GFG;
import fr.aforp.wilocalyse.navigation.Indicator;

public class PathFinder {

    ArrayList<HashMap<String, String>> roadStep = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashMap;
    Indicator indicator = new Indicator();

    public PathFinder() {

    }

    public ArrayList<HashMap<String, String>> getRoadStep() {
        return roadStep;
    }

    // Calculer un chemin avec matrice, point de depart et point d'arrivee
    public ArrayList findPath(int[][] mat, int Xstart, int Ystart, int Xend, int Yend) {
        roadStep.clear();
        ArrayList<Integer[]> road = new ArrayList<Integer[]>();

        GFG.Point src = new GFG.Point(Xstart,Ystart);
        GFG.Point dest = new GFG.Point(Xend,Yend);
        int i = GFG.BFS(mat,src,dest);
        Log.i("path", "" + i);
        mat[src.x][src.y] = 1;
        mat[dest.x][dest.y] = 1;
        int x = src.x;
        int y = src.y;
        while(!GFG.pathQ.isEmpty()) {
            GFG.Point p = GFG.pathQ.peek();
            Integer[] point = {p.x, p.y};
            road.add(point);
            if(p.x == x-1 && p.y == y+1) {
                hashMap = new HashMap<String, String>();
                Indicator.Indication ind = indicator.getTurnRight();
                hashMap.put("titre", ind.text);
                hashMap.put("img", ind.iconPath);
                roadStep.add(hashMap);
                Log.i("mouvement", "déplacez vous en arriere droit(diag)  vers "+ p.x+"col "+ p.y);
            }
            else if(p.x==x+1 && p.y==y+1) {
                hashMap = new HashMap<String, String>();
                hashMap.put("titre", "Déplacez vous en avant droit(diag) vers "+ p.x+"col "+ p.y);
                hashMap.put("img", String.valueOf(R.drawable.arrow_rigth));
                roadStep.add(hashMap);
                Log.i("mouvement", "déplacez vous en avant droit(diag) vers " + p.x + "col " + p.y);
            }
            else if(p.x==x-1 && p.y==y-1) {
                hashMap = new HashMap<String, String>();
                hashMap.put("titre", "Déplacez vous en avant arrière gauche(diag) vers "+ p.x+"col "+ p.y);
                hashMap.put("img", String.valueOf(R.drawable.arrow_rigth));
                roadStep.add(hashMap);
                Log.i("mouvement", "déplacez vous en  arriere guache(diag) vers " + p.x + "col " + p.y);
            }
            else if(p.x==x+1 && p.y==y-1) {
                hashMap = new HashMap<String, String>();
                hashMap.put("titre", "Déplacez vous en avant gauche (diag) vers "+ p.x+"col "+ p.y);
                hashMap.put("img", String.valueOf(R.drawable.arrow_rigth));
                roadStep.add(hashMap);
                Log.i("mouvement", "déplacez vous en avant guache(diag) vers" + p.x + "col " + p.y);
            }
            else if(p.x==x-1 && p.y==y) {
                hashMap = new HashMap<String, String>();
                hashMap.put("titre", "Déplacez vous en arriere vers "+ p.x+"col "+ p.y);
                hashMap.put("img", String.valueOf(R.drawable.arrow_rigth));
                roadStep.add(hashMap);
                Log.i("mouvement", "déplacez vous en arriere vers " + p.x + "col " + p.y);
            }
            else if(p.x==x+1 && p.y==y) {
                hashMap = new HashMap<String, String>();
                hashMap.put("titre", "Déplacez vous en avant vers "+ p.x+"col "+ p.y);
                hashMap.put("img", String.valueOf(R.drawable.arrow_rigth));
                roadStep.add(hashMap);
                Log.i("mouvement", "déplacez vous en avant vers " + p.x + "col " + p.y);
            }
            else if(p.x==x && p.y==y+1) {
                hashMap = new HashMap<String, String>();
                hashMap.put("titre", "Déplacez vous à droite vers "+ p.x+"col "+ p.y);
                hashMap.put("img", String.valueOf(R.drawable.arrow_rigth));
                roadStep.add(hashMap);
                Log.i("mouvement", "déplacez vous à droite vers " + p.x + "col " + p.y);
            }
            else if(p.x==x && p.y==y-1) {
                hashMap = new HashMap<String, String>();
                hashMap.put("titre", "Déplacez vous à gauche vers "+ p.x+"col "+ p.y);
                hashMap.put("img", String.valueOf(R.drawable.arrow_rigth));
                roadStep.add(hashMap);
                Log.i("mouvement", "déplacez vous à guache vers" + p.x + "col " + p.y);
            }
            x=p.x;
            y=p.y;
            GFG.pathQ.remove();
        }
        if (i != -1) {
            Log.i("path", "Shortest Path is " + i);
        }
        else {
            Log.i("path", "Shortest Path doesn't exist");
        }

        return road;
    }

}
