package fr.aforp.wilocalyse.navigation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import java.util.ArrayList;
import fr.aforp.wilocalyse.R;
import fr.aforp.wilocalyse.database.LocationDatabase;

// TODO enlever les éléments start* quand la géolocalisation wifi sera opérationnelle

public class NavigationBox extends DialogFragment
{
    public interface NavigationFragmentListener
    {
        void onReturnValue(Position start, Position end, String startTitle, String destinationTitle);
    }

    private static final String TAG = "NavigationBox";
    private final LocationDatabase locationDatabase;
    private LocationData startLocationData;
    private LocationData endLocationData;
    private Spinner spinnerStartBuilding;
    private Spinner spinnerStartStage;
    private Spinner spinnerStartRoom;
    private Spinner spinnerEndBuilding;
    private Spinner spinnerEndStage;
    private Spinner spinnerEndRoom;

    public NavigationBox(LocationDatabase locationDatabase)
    {
        this.locationDatabase = locationDatabase;
        ArrayList<String> startBuildings = locationDatabase.getBuildings();
        ArrayList<String> startStages = locationDatabase.getStages(startBuildings.get(0));
        ArrayList<String> startRooms = locationDatabase.getRooms(startBuildings.get(0), startStages.get(0));
        startLocationData = new LocationData(startBuildings, startStages, startRooms);
        ArrayList<String> endBuildings = new ArrayList<String>(startBuildings.size());
        ArrayList<String> endStages = new ArrayList<String>(startStages.size());
        ArrayList<String> endRooms = new ArrayList<String>(startRooms.size());
        for (String item : startBuildings) // deep copy
        {
            endBuildings.add(new String(item));
        }
        for (String item : startStages)
        {
            endStages.add(new String(item));
        }
        for (String item : startRooms)
        {
            endRooms.add(new String(item));
        }
        endLocationData = new LocationData(endBuildings, endStages, endRooms);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.popup_route_configurator, null, false);
        initSpinners(dialog);
        builder.setView(dialog);
        final AlertDialog alertDialog = builder.create();
        Button buttonEnter = dialog.findViewById(R.id.btEnter);
        buttonEnter.setOnClickListener(v -> {
            NavigationFragmentListener activity = (NavigationFragmentListener) getActivity();
            activity.onReturnValue(getPosition(startLocationData), getPosition(endLocationData), startLocationData.rooms.get(startLocationData.currentRoomIndex),  endLocationData.rooms.get(endLocationData.currentRoomIndex));
            dismiss();
        });
        Button buttonClose = dialog.findViewById(R.id.btClose);
        buttonClose.setOnClickListener(v -> {
            dismiss();
        });
        return alertDialog;
    }

    private void initSpinners(View view)
    {
        spinnerStartBuilding = view.findViewById(R.id.spinnerBatDep);
        spinnerStartStage = view.findViewById(R.id.spinnerEtageDep);
        spinnerStartRoom = view.findViewById(R.id.spinnerSalleDep);
        spinnerEndBuilding = view.findViewById(R.id.spinnerBatArr);
        spinnerEndStage = view.findViewById(R.id.spinnerEtageArr);
        spinnerEndRoom = view.findViewById(R.id.spinnerSalleArr);
        setSpinners(startLocationData, spinnerStartBuilding, spinnerStartStage, spinnerStartRoom);
        setSpinners(endLocationData, spinnerEndBuilding, spinnerEndStage, spinnerEndRoom);
    }

    private void setSpinners(LocationData data, Spinner spinnerBuilding, Spinner spinnerStage, Spinner spinnerRoom)
    {
        setSpinnerAdapter(spinnerBuilding, data.buildings);
        setSpinnerAdapter(spinnerStage, data.stages);
        setSpinnerAdapter(spinnerRoom, data.rooms);
        setSpinnerListeners(data, spinnerBuilding, spinnerStage, spinnerRoom);
    }

    private void setSpinnerAdapter(Spinner spinner, ArrayList<String> list)
    {
        spinner.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_spinner_item, list));
    }

    private void setSpinnerListeners(LocationData data, Spinner spinnerBuilding, Spinner spinnerStage, Spinner spinnerRoom)
    {
        spinnerBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                data.currentBuildingIndex = position;
                String building = data.buildings.get(position);
                data.stages = locationDatabase.getStages(building);
                setSpinnerAdapter(spinnerStage, data.stages);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                data.currentBuildingIndex = 0;
                Log.e(TAG, "Buildings list view is empty");
            }
        });
        spinnerStage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                String building = data.buildings.get(data.currentBuildingIndex);
                String stage = data.stages.get(position);
                data.rooms = locationDatabase.getRooms(building, stage);
                setSpinnerAdapter(spinnerRoom, data.rooms);
                data.currentStageIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                data.currentBuildingIndex = 0;
                Log.e(TAG, "Stages list view is empty");
            }
        });
        spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                data.currentRoomIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                data.currentRoomIndex = 0;
                Log.e(TAG, "Room list view is empty");
            }
        });
    }

    private Position getPosition(LocationData data)
    {
        String building = data.buildings.get(data.currentBuildingIndex);
        String stage = data.stages.get(data.currentStageIndex);
        String room = data.rooms.get(data.currentRoomIndex);
        return locationDatabase.getPosition(building, stage, room);
    }

    private class LocationData
    {
        private ArrayList<String> buildings;
        private ArrayList<String> stages;
        private ArrayList<String> rooms;
        private int currentBuildingIndex = 0;
        private int currentStageIndex = 0;
        private int currentRoomIndex = 0;

        LocationData(ArrayList<String> buildings, ArrayList<String> stages, ArrayList<String> rooms)
        {
            this.buildings = buildings;
            this.stages = stages;
            this.rooms = rooms;
        }
    }
}

//public class NavigationBox extends DialogFragment
//{
//    private static final String TAG = "NavigationBox";
//    private Spinner spinnerStartBuilding;
//    private Spinner spinnerStartStage;
//    private Spinner spinnerStartRoom;
//    private Spinner spinnerEndBuilding;
//    private Spinner spinnerEndStage;
//    private Spinner spinnerEndRoom;
//
//    private final LocationDatabase locationDatabase;
//    private ArrayList<String> buildings;
//    private ArrayList<String> stages;
//    private ArrayList<String> rooms;
//    private int currentBuildingIndex = 0;
//    private int currentStageIndex = 0;
//    private int currentRoomIndex = 0;
//
//    public NavigationBox(LocationDatabase locationDatabase)
//    {
//        this.locationDatabase = locationDatabase;
//        buildings = locationDatabase.getBuildings();
//        stages = locationDatabase.getStages(buildings.get(0));
//        rooms = locationDatabase.getRooms(buildings.get(0), stages.get(0));
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState)
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.popup_route_configurator, null, false);
//        initSpinners(dialog);
//        builder.setView(dialog);
//        final AlertDialog alertDialog = builder.create();
//        Button buttonEnter = dialog.findViewById(R.id.btEnter);
//        buttonEnter.setOnClickListener(v -> {
//            dismiss();
//        });
//        return alertDialog;
//    }
//
//    private void initSpinners(View view)
//    {
//        spinnerStartBuilding = view.findViewById(R.id.spinnerBatDep);
//        spinnerStartStage = view.findViewById(R.id.spinnerEtageDep);
//        spinnerStartRoom = view.findViewById(R.id.spinnerSalleDep);
//        spinnerEndBuilding = view.findViewById(R.id.spinnerBatArr);
//        spinnerEndStage = view.findViewById(R.id.spinnerEtageArr);
//        spinnerEndRoom = view.findViewById(R.id.spinnerSalleArr);
//        setSpinnerAdapter(spinnerStartBuilding, buildings);
//        setSpinnerAdapter(spinnerStartStage, stages);
//        setSpinnerAdapter(spinnerStartRoom, rooms);
//
//    }
//
//    private void setSpinnerAdapter(Spinner spinner, ArrayList<String> list)
//    {
//        spinner.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_spinner_item, list));
//    }
//    private void setSpinnerListeners()
//    {
//        spinnerStartBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
//            {
//                currentBuildingIndex = position;
//                String building = buildings.get(position);
//                stages = locationDatabase.getStages(building);
//                setSpinnerAdapter(spinnerStartStage, stages);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent)
//            {
//                currentBuildingIndex = 0;
//                Log.e(TAG, "Buildings list view is empty");
//            }
//        });
//        spinnerStartStage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
//            {
//                String building = buildings.get(currentBuildingIndex);
//                String stage = stages.get(position);
//                rooms = locationDatabase.getRooms(building, stage);
//                setSpinnerAdapter(spinnerStartRoom, rooms);
//                currentStageIndex = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent)
//            {
//                currentBuildingIndex = 0;
//                Log.e(TAG, "Stages list view is empty");
//            }
//        });
//        spinnerStartRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
//            {
//                currentRoomIndex = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent)
//            {
//                currentRoomIndex = 0;
//                Log.e(TAG, "Room list view is empty");
//            }
//        });
//    }
//}