package com.example.gettextdandd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gettextdandd.Settings.Fallout;
import com.example.gettextdandd.Settings.TheLordOfTheRings;
import com.example.gettextdandd.Settings.TheWitcher;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance;
    private String[] location_list, weather_list, number_of_players_list, setting_list,
            bioms_TLR, bioms_TheWitcher, bioms_Fallout;
    private TheWitcher theWitcher;
    private TheLordOfTheRings theLordOfTheRings;
    private Fallout fallout;
    private ImageView ic_settings;
    private TextView location_description_text;
    private Button button_generate;
    private Resources res;

    private final String LAST_CONFIGURATION_OF_SETTING = "Last_Setting", LAST_CONFIGURATION_OF_BIOM = "Last_Biom",
            LAST_CONFIGURATION_OF_LOCATION = "Last_Location", LAST_CONFIGURATION_OF_WEATHER = "Last_Weather",
            LAST_CONFIGURATION_OF_KEY_PERSON = "Last_Key_Person", LAST_CONFIGURATION_OF_KEY_OBJECT = "Last_Key_Object";

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        instance = this;

        res = getApplicationContext().getResources();

        prefs = getSharedPreferences("pref", MODE_PRIVATE);

        bioms_TLR = res.getStringArray(R.array.Bioms_TLR);
        bioms_TheWitcher = res.getStringArray(R.array.Bioms_TheWitcher);
        bioms_Fallout = res.getStringArray(R.array.Bioms_Fallout);

        number_of_players_list = res.getStringArray(R.array.Number_of_Players);
        setting_list = res.getStringArray(R.array.Setting);

        if(!prefs.getBoolean("firstRun", false)) {
            // пропишем стартовые настройки генерации
            String[] Erebor = res.getStringArray(R.array.Erebor);
            prefs.edit().putString(LAST_CONFIGURATION_OF_SETTING, setting_list[0]).apply();
            prefs.edit().putString(LAST_CONFIGURATION_OF_BIOM, bioms_TLR[0]).apply();
            prefs.edit().putString(LAST_CONFIGURATION_OF_LOCATION, Erebor[0]).apply();
            prefs.edit().putString(LAST_CONFIGURATION_OF_WEATHER, "Неизвестная").apply();
            prefs.edit().putString(LAST_CONFIGURATION_OF_KEY_PERSON, "Неизвестный").apply();
            prefs.edit().putString(LAST_CONFIGURATION_OF_KEY_OBJECT, "Неизвестный предмет").apply();
            prefs.edit().putBoolean("firstRun", true).apply();
        }

        ic_settings = findViewById(R.id.ic_settings);
        location_description_text = findViewById(R.id.location_description);
        button_generate = findViewById(R.id.button_generate);
        button_generate.setOnClickListener(ButtonGenerateClickListener);
        ic_settings.setOnClickListener(OnIconClickListener);

    }

    private final View.OnClickListener OnIconClickListener = view -> {
        AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View window_for_creation = inflater.inflate(R.layout.window_with_options, null);
        a_builder.setView(window_for_creation);

        AutoCompleteTextView Setting = window_for_creation.findViewById(R.id.Setting_spinner);
        AutoCompleteTextView biom = window_for_creation.findViewById(R.id.Biom_spinner);
        AutoCompleteTextView location = window_for_creation.findViewById(R.id.Location_spinner);
        AutoCompleteTextView weather = window_for_creation.findViewById(R.id.Weather_spinner);

        TextInputEditText Key_Person = window_for_creation.findViewById(R.id.Key_person_TIE);
        TextInputEditText Key_Object = window_for_creation.findViewById(R.id.Key_object_TIE);

        String Setting_Text = prefs.getString(LAST_CONFIGURATION_OF_SETTING, "");

        String[] Settings_list = res.getStringArray(R.array.Setting);

        ArrayAdapter arrayAdapter_Settings = new ArrayAdapter(getApplicationContext(), R.layout.list_item, Settings_list); // каждый раз создаём новые адаптеры, т.к. иначе ломается выпадающий список
        Setting.setText(Setting_Text); // устанавливаем первую отметку, которая находится в буфере
        Setting.setAdapter(arrayAdapter_Settings); //обновляем адаптер

        biom.setText(prefs.getString(LAST_CONFIGURATION_OF_BIOM, ""));
        location.setText(prefs.getString(LAST_CONFIGURATION_OF_LOCATION, ""));
        weather.setText(prefs.getString(LAST_CONFIGURATION_OF_WEATHER, ""));
        Key_Person.setText(prefs.getString(LAST_CONFIGURATION_OF_KEY_PERSON, ""));
        Key_Object.setText(prefs.getString(LAST_CONFIGURATION_OF_KEY_OBJECT, ""));

        SwitchAdapters("Last", window_for_creation);

        a_builder.setCancelable(true)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    prefs.edit().putString(LAST_CONFIGURATION_OF_SETTING, Setting.getText().toString()).apply();
                    prefs.edit().putString(LAST_CONFIGURATION_OF_BIOM, biom.getText().toString()).apply();
                    prefs.edit().putString(LAST_CONFIGURATION_OF_LOCATION, location.getText().toString()).apply();
                    prefs.edit().putString(LAST_CONFIGURATION_OF_WEATHER, weather.getText().toString()).apply();
                    prefs.edit().putString(LAST_CONFIGURATION_OF_KEY_PERSON, Objects.requireNonNull(Key_Person.getText()).toString()).apply();
                    prefs.edit().putString(LAST_CONFIGURATION_OF_KEY_OBJECT, Objects.requireNonNull(Key_Object.getText()).toString()).apply();
                })
                .setNegativeButton("Отменить", (dialog, which) -> {
                    dialog.cancel();
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Параметры генерации текста");
        alert.show();

        Setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SwitchAdapters(Setting.getText().toString(), window_for_creation);
                location.clearListSelection();
                weather.clearListSelection();
            }
        });
        biom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setAdapterLocations(biom.getText().toString(), location);
            }
        });
    };

    public void SwitchAdapters(String Setting, View dialog_window){
        switch (Setting){
            case "Приключения в Средиземье":
                String[] bioms_list = res.getStringArray(R.array.Bioms_TLR);
                String[] weather_list = res.getStringArray(R.array.Weather_TLR_TheWitcher);

                setAdapters(dialog_window, bioms_list, weather_list);
                break;
            case "Fallout":
                String[] bioms_list2 = res.getStringArray(R.array.Bioms_Fallout);
                String[] weather_list2 = res.getStringArray(R.array.Weather_Fallout);

                setAdapters(dialog_window, bioms_list2, weather_list2);
                break;
            case "Ведьмак":
                String[] bioms_list3 = res.getStringArray(R.array.Bioms_TheWitcher);
                String[] weather_list3 = res.getStringArray(R.array.Weather_TLR_TheWitcher);

                setAdapters(dialog_window, bioms_list3, weather_list3);
                break;
            case "Last":
                AutoCompleteTextView biom_spinner = dialog_window.findViewById(R.id.Biom_spinner);
                AutoCompleteTextView location_spinner = dialog_window.findViewById(R.id.Location_spinner);
                AutoCompleteTextView weather = dialog_window.findViewById(R.id.Weather_spinner);


                String biom = prefs.getString(LAST_CONFIGURATION_OF_BIOM, "");
                biom_spinner.setText(biom);

                setAdapterLocations(biom, location_spinner);
                Setting = prefs.getString(LAST_CONFIGURATION_OF_SETTING, "");

                ArrayAdapter arrayAdapter_weather, arrayAdapter_bioms;
                String[] weather_list4, biom_list4;
                if(!Setting.equals("Fallout")){
                    weather_list4 = res.getStringArray(R.array.Weather_TLR_TheWitcher);
                    if(Setting.equals("Ведьмак")){
                        biom_list4 = res.getStringArray(R.array.Bioms_TheWitcher);
                    }
                    else {
                        biom_list4 = res.getStringArray(R.array.Bioms_TLR);
                    }
                }
                else{
                    biom_list4 = res.getStringArray(R.array.Bioms_Fallout);
                    weather_list4 = res.getStringArray(R.array.Weather_Fallout);
                }

                arrayAdapter_weather = new ArrayAdapter(getApplicationContext(),
                        R.layout.list_item, weather_list4); // каждый раз создаём новые адаптеры, т.к. иначе ломается выпадающий список
                arrayAdapter_bioms = new ArrayAdapter(getApplicationContext(),
                        R.layout.list_item, biom_list4);
                weather.setText(prefs.getString(LAST_CONFIGURATION_OF_WEATHER, "")); // устанавливаем первую отметку, которая находится в буфере

                biom_spinner.setAdapter(arrayAdapter_bioms);
                weather.setAdapter(arrayAdapter_weather);
                break;
        }
    }

    public void setAdapters(View dialog_window, String[] bioms_list, String[] weather_list){
        AutoCompleteTextView biom = dialog_window.findViewById(R.id.Biom_spinner);
        AutoCompleteTextView location = dialog_window.findViewById(R.id.Location_spinner);
        AutoCompleteTextView weather = dialog_window.findViewById(R.id.Weather_spinner);

        ArrayAdapter arrayAdapter_bioms = new ArrayAdapter(getApplicationContext(), R.layout.list_item, bioms_list); // каждый раз создаём новые адаптеры, т.к. иначе ломается выпадающий список
        biom.setText(bioms_list[0]); // устанавливаем первую отметку, которая находится в буфере
        biom.setAdapter(arrayAdapter_bioms); //обновляем адаптер
        setAdapterLocations(biom.getText().toString(), location);

        biom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setAdapterLocations(biom.getText().toString(), location);
            }
        });

        ArrayAdapter arrayAdapter_weather = new ArrayAdapter(getApplicationContext(), R.layout.list_item, weather_list); // каждый раз создаём новые адаптеры, т.к. иначе ломается выпадающий список
        weather.setText(weather_list[0]); // устанавливаем первую отметку, которая находится в буфере
        weather.setAdapter(arrayAdapter_weather); //обновляем адаптер
    }

    public void setAdapterLocations(String biom, AutoCompleteTextView location){

        switch(biom){
            case "Эребор": location_list = res.getStringArray(R.array.Erebor); break;
            case "Пещеры Мориа": location_list = res.getStringArray(R.array.Moria); break;
            case "Шир": location_list = res.getStringArray(R.array.Shir); break;
            case "Гондор": location_list = res.getStringArray(R.array.Gondor); break;
            case "Минас Итиль": location_list = res.getStringArray(R.array.Minas_Itil); break;
            case "Гундабад": location_list = res.getStringArray(R.array.Gyndabad); break;
            case "Мордор": location_list = res.getStringArray(R.array.Mordor); break;
            case "Княжество Туссент": location_list = res.getStringArray(R.array.Principality_of_Tussent); break;
            case "Каэр Морхен": location_list = res.getStringArray(R.array.Kaer_Morhen); break;
            case "Велен": location_list = res.getStringArray(R.array.Velen); break;
            case "Острова Скеллиге": location_list = res.getStringArray(R.array.Islands_Skellige); break;
            case "Белый сад": location_list = res.getStringArray(R.array.White_Garden); break;
            case "Новиград": location_list = res.getStringArray(R.array.Novigrad); break;
            case "Содружество": location_list = res.getStringArray(R.array.Commonwealth); break;
            case "Far Harbor": location_list = res.getStringArray(R.array.Far_Harbor); break;
            case "Nuka-World": location_list = res.getStringArray(R.array.Nuka_World); break;
            case "Big Boston": location_list = res.getStringArray(R.array.Big_Boston); break;
            case "Пустошь Мохаве": location_list = res.getStringArray(R.array.Mojave_Wasteland); break;
            case "Лагерь Легиона Цезаря": location_list = res.getStringArray(R.array.Сaesars_legion_camp); break;
        }

        ArrayAdapter arrayAdapter_locations = new ArrayAdapter(getApplicationContext(), R.layout.list_item, location_list); // каждый раз создаём новые адаптеры, т.к. иначе ломается выпадающий список
        location.setText(location_list[0]); // устанавливаем первую отметку, которая находится в буфере
        location.setAdapter(arrayAdapter_locations); //обновляем адаптер
    }

    public final View.OnClickListener ButtonGenerateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String Setting = prefs.getString(LAST_CONFIGURATION_OF_SETTING, "");
            String biom = prefs.getString(LAST_CONFIGURATION_OF_BIOM, "");
            String location = prefs.getString(LAST_CONFIGURATION_OF_LOCATION, "");
            String weather = prefs.getString(LAST_CONFIGURATION_OF_WEATHER, "");

            switch(Setting){
                case "Ведьмак":
                    theWitcher = new TheWitcher(biom, location, weather);
                    location_description_text.setText(theWitcher.GenerateDescription()); break;
                case "Fallout":
                    fallout = new Fallout(biom, location, weather);
                    location_description_text.setText(fallout.GenerateDescription()); break;
                case "Приключения в Средиземье":
                    theLordOfTheRings = new TheLordOfTheRings(biom, location, weather);
                    location_description_text.setText(theLordOfTheRings.GenerateDescription()); break;
            }
        }
    };

    public static MainActivity getInstance() {
        return instance;
    }
}