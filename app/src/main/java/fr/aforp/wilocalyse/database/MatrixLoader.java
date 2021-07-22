package fr.aforp.wilocalyse.database;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MatrixLoader
{
    private String TAG = "MatrixLoader";

    public int[][] load(Context context)
    {
        int[][] matrix = null;
        try
        {
            //matrix = new int[299][212];
            matrix = new int[2115][1020];
            InputStream input = context.getAssets().open("plan-masse-chemin-matrice-1020x2115.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            int i = 0;
            while (line != null)
            {
                for (int j = 0; j < line.length(); j++)
                {
                    matrix[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
                }
                i++;
                line = reader.readLine();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return matrix;
    }
}
