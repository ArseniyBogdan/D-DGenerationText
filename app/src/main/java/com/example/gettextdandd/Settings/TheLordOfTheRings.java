package com.example.gettextdandd.Settings;

import android.content.res.Resources;

import com.example.gettextdandd.MainActivity;
import com.example.gettextdandd.R;

import java.util.Random;

public class TheLordOfTheRings implements SettingInterface{
    private String location, weather, biom;
    private Resources res = MainActivity.getInstance().getResources();

    public TheLordOfTheRings(String biom, String location, String weather){
        this.biom = biom;
        this.location = location;
        this.weather = weather;
    }

    @Override
    public String GenerateFirstSentence() {
        Random random = new Random();
        int i = random.nextInt(4);

        String[] forms_of_team = MainActivity.getInstance().getResources().getStringArray(R.array.Forms_of_word_Team);

        String FirstSentence = "Ваша " + forms_of_team[i] +
                " пришла на локацию '" + biom + "'. " +
                " Она двигается по направлению к '" +
                location + "'. ";

        return FirstSentence;
    }

    public int RandomOf4(){
        Random random = new Random();
        return random.nextInt(4) + 1;
    }

    public int Random(){
        Random random = new Random();
        return random.nextInt(101);
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
    public String GenerateNPCCharacters() {
        switch(location){
            case "Мёртвы топи": return GenerateSwampNPC();
            case "Город": return res.getString(R.string.CityNPCDescription_TLR);
            case "Лес": return GenerateForestNPC();
            case "Таверна": return GenerateTavernNPC();
//            case "Подземелье": return GenerateDungeonNPC();
//            case "Горы": return GenerateMountainsNPC();
//            case "Крепость": return GenerateMountainsNPC();
        }
        return null;
    }

    public String GenerateSwampNPC(){
        String SwampSentence = "";
        if ((RandomOf4()-1)/2 == 0){
            SwampSentence += " Мёртвы топи, вы не найдёте места более жуткого чем это во всём Средиземье.";
        }
        else{
            SwampSentence += " Мёртвые Болота. Когда-то здесь была равнина, на " +
                    "которой произошла древняя битва людей и эльфов с армиями Саурона.";
        }
        SwampSentence += " Вы вряд ли кого живого встретите здесь, а вот мертвецов тут навалом." +
                " \n *Мимо вас пролетел один из огоньков мертвецов*";

        return SwampSentence;
    }

    public String GenerateForestNPC(){
        String ForestSentence = "";
        int index = Random();
        int count;
        if(index <= 30){
            ForestSentence += " Вы не замечаете ничего того, что могло бы вам навредить";
        }
        else if(index <= 50){
            if((RandomOf4()-1)/2 == 0){
                ForestSentence += " Вы замечаете между деревьями какое-то движение, " +
                        "скорее всего мимо вас движется небольшой отряд орков," +
                        " раз уж вы не почувствовали как дрожит земля" +
                        "вас идёт большой отряд орков. Их можно застать врасплох, " +
                        "но нужно выбирать быстрее, они скоро скроются из вида";
            }
            else{
                ForestSentence += " Вы начинаете чувствовать как дрожит земля, прямо " +
                        "на вас движется большой отряд орков с большой скоростью, вам вряд ли " +
                        "получится справиться с ними. Принимать рещение нужно прямо сейчас, иначе " +
                        "они размажут вас по земле и глазом не моргнув";
            }
        }
        else if(index <= 70){
            ForestSentence += " Также вы замечаете молодую девушку, идущую среди деревьев. " +
                    "Что она делает в этой чаще одна? Может ей нужна ваша помощь?";
        }
        else {
            if((RandomOf4()-1) / 2 == 0){
                ForestSentence += " Потратив 17 часов ходьбы по лесу, вы пониаметеЮ что " +
                        "окончательно заблудились. Сходить с тропинки было не лучшей идеей. " +
                        "Теперь нужно как-то вернуться на прежний маршрут.";
            }
            else{
                ForestSentence += " идя по этому лесу вы натыкаетесь на одно очень" +
                        " странное строение. Возможно, там есть то, что вам нужно?";
            }
        }
        return ForestSentence;
    }

    public String GenerateTavernNPC(){
        String TavernNPCDescription = "В таверне вы замечаете множество различных лючностей, " +
                "начиная от обычных пьяниц, девушек, разносящих горячительные " +
                "напитки, заканчивая хозяином таверны и странником, сидящем в дальнем углу.";
        TavernNPCDescription = "";
        // хозяин таверны, пьяницы, молчаливый странниц,
        return null;
    }

    @Override
    public String GenerateThirdSentence() {
        return " Очутившись на месте, вы " +
                "первым делом осматриваетесь.";
    }

    @Override
    public String GenerateDescriptionLocationWithLSTM() {
        return null;
    }


    @Override
    public String GenerateDescription() {
        return GenerateFirstSentence() + GenerateWeatherDescription() + GenerateThirdSentence();
    }
}
