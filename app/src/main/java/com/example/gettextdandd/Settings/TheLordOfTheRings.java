package com.example.gettextdandd.Settings;

import com.example.gettextdandd.MainActivity;
import com.example.gettextdandd.R;

import java.util.Random;

public class TheLordOfTheRings implements SettingInterface{
    private String location, weather, biom;

    public TheLordOfTheRings(String biom, String location, String weather){
        this.biom = biom;
        this.location = location;
        this.weather = weather;
    }

    @Override
    public String GenerateFirstSentence() {
        Random random = new Random();
        int i = random.nextInt(5);

        String[] forms_of_team = MainActivity.getInstance().getResources().getStringArray(R.array.Forms_of_word_Team);

        String FirstSentence = "Ваша " + forms_of_team[i] +
                " пришла на локацию '" + biom + "'. " +
                " Она двигается по направлению к '" +
                location + "'. ";

        return FirstSentence;
    }

    @Override
    public String GenerateWeatherDescription() {
        if(weather.equals("случайная")){
            Random random = new Random();
            int i = random.nextInt(5);

            String[] weather_list = MainActivity.getInstance().getResources().getStringArray(R.array.Weather_TLR_TheWitcher);
            weather = weather_list[i];
        }
        String second_sentence = "На улице";

        switch (weather){
            case "солнце": case "туман": second_sentence += " стоит " + weather; break;
            case "дождь": case "снег": second_sentence += " идёт " + weather; break;
            case "облачно": second_sentence += " " + weather; break;
            case "неизвестная": return "";
        }

        return second_sentence + ".";
    }

    @Override
    public String GenerateThirdSentence() {
        return " Очутившись на месте, вы " +
                "первым делом осматриваетесь." +" Вы видите ";
    }

    @Override
    public String GenerateDescriptionLocationWithLSTM() {
        return null;
    }

    @Override
    public String GenerateNPCCharacters() {
        return null;
    }

    @Override
    public String GenerateDescription() {
        return GenerateFirstSentence() + GenerateWeatherDescription() + GenerateThirdSentence();
    }
}
