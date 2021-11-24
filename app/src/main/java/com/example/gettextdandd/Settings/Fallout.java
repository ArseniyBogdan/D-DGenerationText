package com.example.gettextdandd.Settings;

import android.content.res.Resources;
import android.util.Log;

import com.example.gettextdandd.LSTM_ModelManager;
import com.example.gettextdandd.MainActivity;
import com.example.gettextdandd.R;

import java.util.HashMap;
import java.util.Random;

public class Fallout implements SettingInterface{
    private String location, weather, biom, Key_object, NPC;

    private String NameOfDictionary = "word_dict_Anklav_rus.json";

    private final LSTM_ModelManager lstm_modelManager = new LSTM_ModelManager(NameOfDictionary);

    Resources res = MainActivity.getInstance().getResources();

    public Fallout(String biom, String location, String weather, String Key_object, String NPC){
        this.biom = biom;
        this.location = location;
        this.weather = weather;
        this.Key_object = Key_object;
        this.NPC = NPC;
    }

    @Override
    public String GenerateFirstSentence() {
        Random random = new Random();
        int i = random.nextInt(4);

        String[] forms_of_team = MainActivity.getInstance().getResources().getStringArray(R.array.Forms_of_word_Team);

        String FirstSentence = "Ваша " + forms_of_team[i] +
                " пришла на локацию '" + biom + "'. " +
                " Она двигается по направлению к '" +
                location + "', с целью найти " + Key_object  + ".";

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
            case "солнце": case "кислотный туман":
                second_sentence += " стоит " + weather;
                if(weather.equals("кислотный туман")){
                    String[] warnings_list = MainActivity.getInstance().getResources().getStringArray(R.array.Warning_Forms);
                    second_sentence += warnings_list[RandomOf4()-1];
                }
                break;
            case "радиоактивный снег": case "радиоактивный дождь":
                String[] warning_list = MainActivity.getInstance().getResources().getStringArray(R.array.Warning_Forms_Radiation);
                second_sentence += " идёт " + weather;
                second_sentence += " хоть вы и выросли в радиоактивном мире, " + warning_list[RandomOf4()-1];
            break;
            case "облачно": second_sentence += " " + weather; break;
            case "неизвестная": return "";
        }

        return second_sentence + ".";
    }

    public int RandomOf4(){
        Random random = new Random();
        return random.nextInt(4) + 1;
    }

    @Override
    public String GenerateThirdSentence() {
        return " Очутившись на месте, вы " +
                "первым делом осматриваетесь. ";
    }

    @Override
    public String GenerateDescriptionLocationWithLSTM() {
        return null;
    }

    public String GenerateNPCSentence(){
        String NPCSentence = "";
        int count = RandomOf4();
        switch (count){
            case 1: NPCSentence+= "Вы натыкаетесь на " + NPC + ", ощутив ваше присутствие " +  NPC + ", оно оживилось."; break;
            case 2: NPCSentence+= "Пока вы шли, ваш взгляд упал на " + NPC + ", в нём есть что-то особенное, и вы можете это узнать."; break;
            case 3: NPCSentence+= "Через некоторое время беспамятной ходьбы, впереди себя вы замечаете " + NPC + "."; break;
            case 4: NPCSentence+= "Вы не заметили как перед вами нечто живое - " + NPC + "."; break;
        }
        return NPCSentence;
    }

    public String GenerateKey_ObjectDescription(){
        String Key_ObjectSentence = "";
        int count = RandomOf4();
        switch (count){
            case 1: Key_ObjectSentence+= "Вы появились в этом странном месте, с целью найти " + Key_object + "..."; break;
            case 2: Key_ObjectSentence+= "После пробуждения, вас не покидает мысль, что " + Key_object + " вам нужен."; break;
            case 3: Key_ObjectSentence+= "Вас послали в этот мир, для того чтобы вы заполучили " + Key_object + "."; break;
            case 4: Key_ObjectSentence+= "После пробуждения вы осознали свою цель, найти " + Key_object + "."; break;
        }
        return Key_ObjectSentence;
    }

    @Override
    public String GenerateNPCCharacters() {
        switch(location){
            case "Топи": return GenerateSwampNPC();
            case "Город": return res.getString(R.string.CityNPCDescription_Fallout);
            case "Лес": return GenerateForestNPC();
            case "Дорога": return GenerateRoadNPC();
            case "Городские развалины": return GenerateCityRuinsNPC();
            case "Пустошь": return GenerateWastelandsNPC();
            case "Лагерь Цезаря": return res.getString(R.string.LegionCaesarsNPC);
        }
        return null;
    }

    public int Random(){
        Random random = new Random();
        return random.nextInt(101);
    }

    public String GenerateSwampNPC(){
        String SwampSentence = "";
        int index = Random();
        int count;
        SwampSentence += res.getString(R.string.Start_SwampSentence);

        if(index <= 40){
            SwampSentence += " небольшую группу детёнышей болотника." +
                    " Они не представляют вам опасности, но возможно рядом есть их мама, " +
                    "поэтому будьте осторожны.";
        }
        else if(index <= 75 ){
            count = RandomOf4();
            if(count>1){
                SwampSentence +=" " + count + " болотников, они могут донести неприятностей, " +
                        "это зависит от вашей экипировки.";
            }
            else{
                SwampSentence +=" " + 1 + " болотника, который и не подозревает что вы рядом.";
            }
        }
        else if(index <= 95){
            count = RandomOf4()/2;
            if (count == 1){
                SwampSentence +=" " + 1 + " болотника-охотника, кажется он вас заметил," +
                        " вы понимаете, что с ним будет трудно тягаться.";
            }
            else{
                SwampSentence +=" " + 2 + " болотников-охотников, один такой может расправится " +
                        "с целым карованом, а на что способны двое таких? Скоро вы узнаете...";
            }
        }
        else{
            count = RandomOf4()/2;
            if(count == 1){
                SwampSentence += " Мать болотников, она ещё вас не заметила, вам лучше бежать пока не поздно";
            }
            else{
                SwampSentence += " Мать болотников, она вас заметила, похоже вам сейчас не поздоровится";
            }
        }

        return SwampSentence;
    }

    public String GenerateForestNPC(){
        String ForestSentence = "";
        int index = Random();
        int count;
        if(index<=30){
            ForestSentence += " Похоже вы одни.";
        }
        else if(index<=50){
            ForestSentence += res.getString(R.string.GnuseDescription);
        }
        else if(index<=70){
            ForestSentence += " Вы слышите низкое жужжание. Где-то вдалеке летит что-то большое. Вскоре вы замечаете";
            count = RandomOf4();
            if(count == 1){
                ForestSentence += " Вы видите одного дутня. Лучше расправиться с ними сейчас, " +
                        "пока он далеко и не заметил вас. Если ужалит, то будет очень больно";
            }
            else{
                ForestSentence += " Вы видите группу из " + count + " дутней. Лучше расправиться " +
                    "с ними сейчас, пока они далеко и не замтеили вас. Если ужалят, то будет очень больно";
            }
        }
        else if(index <= 95){
            ForestSentence += GenerateRaidersDescription();
        }
        else{
            ForestSentence += GenerateRadScorpionDescription();
        }

        return ForestSentence;
    }

    public String GenerateRoadNPC(){
        String RoadSentence = "";
        int index = Random();
        if(index<=33){
            RoadSentence += " Похоже вы одни.";
        }
        else if(index<=58){
            RoadSentence += GenerateRaidersDescription();
        }
        else if(index<=68){
            RoadSentence += res.getString(R.string.GnuseDescription);
        }
        else if(index <= 73){
            RoadSentence += GenerateSintsDescription();
        }
        else if (index<=88){
            RoadSentence += res.getString(R.string.MoleRatDescription);
        }
        else if(index<=98){
            RoadSentence += res.getString(R.string.DeathClawDescription);
        }
        return RoadSentence;
    }

    public String GenerateCityRuinsNPC(){
        String CityRuinsSentence = "";
        int index = Random();
        if(index <= 15){
            CityRuinsSentence += GenerateRaidersDescription();
        }
        else if(index <= 25){
            CityRuinsSentence += res.getString(R.string.MoleRatDescription);
        }
        else if(index <= 30){
            CityRuinsSentence += GenerateSintsDescription();
        }
        else if(index <= 45){
            CityRuinsSentence += res.getString(R.string.SuperMutantDescription);
        }
        else if(index <= 75){
            CityRuinsSentence += res.getString(R.string.GulsDescription);
        }
        else if(index <= 78){
            CityRuinsSentence += res.getString(R.string.DeathClawDescription);
        }
        else{
            CityRuinsSentence += " Похоже вы одни.";
        }

        return CityRuinsSentence;
    }

    public String GenerateRaidersDescription(){
        String Raiders_Sentence = "";

        Raiders_Sentence += "Вы замечаете группу рейдеров, они выглядят ";
        int count = RandomOf4();
        switch(count){
            case 1:
                Raiders_Sentence += "уставшими.";
                int chance = Random();
                if(chance <= 10){
                    Raiders_Sentence+= " Большинство из них похожи на новобранцев, " +
                            "возможно, их можно будет направить на правильную дорогу";
                }
                else{
                    Raiders_Sentence+= " Среди них одни пьяницы и наркоманы.";
                }
                break;
            case 2: Raiders_Sentence+="сонными."; break;
            case 3: Raiders_Sentence+="как с похмелья."; break;
            case 4: Raiders_Sentence+="бодрыми."; break;
        }

        return Raiders_Sentence;
    }

    public String GenerateSintsDescription(){
        String SintsDesription = "";
        SintsDesription += "Вы замечаете группу синтов, они хорошо вооружены. ";
        if(RandomOf4()%2==1){
            SintsDesription += "Среди них есть синт-охотник. ";
        }
        SintsDesription += "Сложно сказать, что они тут делают, но на глаза им лучше не попадаться.";
        return SintsDesription;
    }

    public String GenerateRadScorpionDescription(){
        String RadScorpionDescription = "";

        int count = RandomOf4()%2;
        int count2 = RandomOf4()%2;
        if(count == 0){
            RadScorpionDescription += "Вы замечаете " + 1 + " радскорпиона. ";
            if(count2 == 0){
                RadScorpionDescription += "Он греется на солнце, похоже он сыт.";
            }
            else{
                RadScorpionDescription += "Он проголодался и ищет чем бы подкрепиться.";
            }
        }
        else{
            RadScorpionDescription += "Вы замечаете " + 2 + " радскорпионов. ";
            if(count2 == 0){
                RadScorpionDescription += "Они греются на солнце, похоже они сыты.";
            }
            else{
                RadScorpionDescription += "Они проголодались и ищут чем бы подкрепиться.";
            }
        }

        return RadScorpionDescription;
    }

    public String GenerateWastelandsNPC(){
        String WasteLandsNPC = "";
        int index = Random();
        if(index <= 65){
            WasteLandsNPC += res.getString(R.string.Wasteland_is_wasteland);
        }
        else if(index<=75){
            WasteLandsNPC += GenerateRaidersDescription();
        }
        else if(index<=85){
            WasteLandsNPC += res.getString(R.string.MoleRatDescription);
        }
        else{
            WasteLandsNPC += res.getString(R.string.Caravan);
        }
        return WasteLandsNPC;
    }

    @Override
    public String GenerateDescription() {
        String FirstMessage = GenerateFirstSentence() + GenerateWeatherDescription() + GenerateThirdSentence() + " Вы видите";


        String DesriptionLocationSentence = lstm_modelManager.GenerateLocationDescriptionForFallout(FirstMessage);

        Log.d("My_log", DesriptionLocationSentence);

        return GenerateKey_ObjectDescription() + GenerateFirstSentence() + GenerateWeatherDescription()
                + GenerateThirdSentence() + DesriptionLocationSentence + GenerateNPCSentence() + GenerateNPCCharacters();
    }
}
