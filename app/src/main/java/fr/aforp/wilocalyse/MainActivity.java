package fr.aforp.wilocalyse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import fr.aforp.wilocalyse.navigation.MapActivity;

// TODO supprimer ces activités student/teacher/visitor !!!

public class MainActivity extends AppCompatActivity {

    private CardView imgBtTeacher;
    private CardView imgBtStudent;
    private CardView imgBtSpeaker;
    private CardView imgBtVisitor;
    private ImageView imgBtInfos;
    private TextView imgBtLinkAforp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.imgBtTeacher = (CardView) findViewById(R.id.imgBtEmployee);
        this.imgBtStudent = (CardView) findViewById(R.id.imgBtStudent);
        this.imgBtSpeaker = (CardView) findViewById(R.id.imgBtSpeaker);
        this.imgBtVisitor = (CardView) findViewById(R.id.imgBtVisitor);
        this.imgBtLinkAforp = (TextView) findViewById(R.id.btLinkAforp);
        this.imgBtInfos = (ImageView) findViewById(R.id.btInfos);

        imgBtTeacher.setOnClickListener(view -> {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        });

        imgBtStudent.setOnClickListener(view -> {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        });

        imgBtSpeaker.setOnClickListener(view -> {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        });

        imgBtVisitor.setOnClickListener(view -> {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        });

        imgBtLinkAforp.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.aforp.fr/"));
            startActivity(intent);
        });

        imgBtInfos.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            View dialog = LayoutInflater.from(getBaseContext()).inflate(R.layout.popup_aforp_informations, null, false);
            builder.setView(dialog);
            final AlertDialog alertDialog = builder.create();
            Button buttonClose = dialog.findViewById(R.id.button_close);
            buttonClose.setOnClickListener(v -> {
                alertDialog.dismiss();
            });
            alertDialog.show();
        });
    }
}