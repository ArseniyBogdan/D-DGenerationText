package com.example.gettextdandd.Settings;

import android.content.res.Resources;
import android.util.Log;

import com.example.gettextdandd.LSTM_ModelManager;
import com.example.gettextdandd.MainActivity;
import com.example.gettextdandd.R;

import java.util.Random;

public class TheLordOfTheRings implements SettingInterface{
    private String location, weather, biom, Key_object, NPC;
    private Resources res = MainActivity.getInstance().getResources();

    private String NameOfDictionary = "word_dict_Tolkin_en.json";


    private final LSTM_ModelManager lstm_modelManager = new LSTM_ModelManager(NameOfDictionary);

    public TheLordOfTheRings(String biom, String location, String weather, String Key_object, String NPC){
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
                location + "', с целью найти " + Key_object + ".";

        return FirstSentence;
    }

    public String GenerateNPCSentence(){
        String NPCSentence = "";
        int count = RandomOf4();
        switch (count){
            case 1: NPCSentence+= "Вы натыкаетесь на " + NPC + ", ощутив ваше присутствие" +  NPC + ", оно оживилось."; break;
            case 2: NPCSentence+= "Пока вы шли, ваш взгляд упал на " + NPC + ", в нём есть что-то особенное, и вы можете это узнать."; break;
            case 3: NPCSentence+= "Через некоторое время беспамятной ходьбы, впереди себя вы замечаете" + NPC + "."; break;
            case 4: NPCSentence+= "Вы не заметили как перед вами нечто живое - " + NPC + "."; break;
        }
        return NPCSentence;
    }

    public String GenerateKey_ObjectDescription(){
        String Key_ObjectSentence = "";
        int count = RandomOf4();
        switch (count){
            case 1: Key_ObjectSentence+= "Вы появились в этом странном месте, с целью найти" + Key_object + "..."; break;
            case 2: Key_ObjectSentence+= "После пробуждения, вас не покидает мысль, что " + Key_object + " вам нужен."; break;
            case 3: Key_ObjectSentence+= "Вас послали в этот мир, для того чтобы вы заполучили этот" + Key_object + "."; break;
            case 4: Key_ObjectSentence+= "После пробуждения вы осознали свою цель, найти" + Key_object + "."; break;
        }
        return Key_ObjectSentence;
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
            case "Подземелье": return GenerateDungeonNPC();
            case "Горы": return GenerateMountainsNPC();
            case "Крепость": return GenerateCastleNPC();
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
        String TavernNPCDescription = "";
        if((RandomOf4()-1)/2 == 0){
            TavernNPCDescription = "В таверне вы замечаете множество различных лючностей, " +
                    "начиная от обычных пьяниц, девушек, разносящих горячительные " +
                    "напитки, заканчивая хозяином таверны и странником, сидящем в дальнем углу. Он как-то странно поглядывает на вас";
        }
        else{
            TavernNPCDescription = "Если вы устали после долгого пути, то вы можете снять себе комнату в этой таверне," +
                    " но будьте бдительны, тут не каждый является тем, за кого себя выдаёт. Вы можете поспрашивать" +
                    " окружающих о чём-то, что вас интересует. Не забудьте потратить немного монет, чтобы получить достойный ответ.";
        }
        return TavernNPCDescription;
    }

    public String GenerateDungeonNPC(){
        String DungeonDesciptionSentence = "";
        int index = Random();
        if(index<=30){
            DungeonDesciptionSentence += "На удивление здесь темно и пусто, что же может пойти не так?";
        }
        else if(index <= 60){
            DungeonDesciptionSentence += GenerateGoblinDescription();
        }
        else if(index <= 80){
            DungeonDesciptionSentence += GenerateSpidersDescription();
        }
        else if(index <= 95){
            DungeonDesciptionSentence += GenerateOlogHiesDescription();
        }


        return DungeonDesciptionSentence;
    }

    public String GenerateOlogHiesDescription(){
        String OlogHiesDescription = "";

        if((RandomOf4()-1) == 0){
            OlogHiesDescription = " Вы слышите низкий и долгий вздох. Очень долгий вздох... " +
                    "Похоже за сводом пешеры есть что-то большое и вполне себе существущее. Кто-то из " +
                    "вас осмеливается заглянуть за угол и вернувшись сообщил, что там Олог-Хай. Похоже вам " +
                    "сегодня очень сильно не повезло. \n Справка: Олог-хаи - Порода особых троллей, выведенная Сауроном";
        }
        else {
            if ((RandomOf4() - 1) == 0) {
                OlogHiesDescription = "Идя в темноте, вы натыкаетесь на что-то большое, похожее на стену, " +
                        "но через пару секунд, вы понимаете что это совсем не стена. Это ОГРОМНЫЙ олог-хай, " +
                        "способный одним уларом сделать из вас лепёшку, вам почезло, если бы он " +
                        "бодрствовал, то вы бы сейчас вероятно были мертвы";
            } else {
                OlogHiesDescription = "Идя в темноте, вы натыкаетесь на что-то большое, похожее на стену, " +
                        "но через пару секунд, вы понимаете что это совсем не стена. Это ОГРОМНЫЙ олог-хай " +
                        "и похоже вы его разбудили, советую вам поскорее бежать из этого места";

            }
        }

        return OlogHiesDescription;
    }

    public String GenerateGoblinDescription(){
        String GoblinDescription = "";
        if((RandomOf4()-1) == 0){
            GoblinDescription += "Идя по подземелью уже довольно долгое время,за которое " +
                    "ничего не произошло, вы совсем потеряли бдительность. Как внезапно из-за " +
                    "какой-то щели одного из вас хватают за ногу и валят на пол, ещё через " +
                    "секунду рядом с ним опускается меч, от которого он чудом успеевает " +
                    "отвернуться, вам надо действовать быстро.";
        }
        else{
            GoblinDescription += "Последние два дня вы мало отдыхали и вот, наконец " +
                    "подвернулась минутка присесть на холодный пол подземелья, как не успев " +
                    "коснуться пола ни одно из ваших колен, вы слышите шорох," +
                    " исходящий из противоположного угла пещеры, что там такое?";
        }
        return GoblinDescription;
    }

    public String GenerateSpidersDescription(){
        String SpiderDescription = "";

        if((RandomOf4() - 1) == 0){
            SpiderDescription = "Вы двигаетесь вдоль каменистого свода подземелья осматривая его " +
                    "в тусклом огне факелов. В скором времени кто-то из вас наступает во что-то липучее. " +
                    "Он пытается поднять ногу, и понимает что сапог застрял в этом нечто. Это оказалась паутина." +
                    " Вы понимаете, что сейчас сюда сбежится стая пауков, с которой вам надо будет сражаться";
        }
        else{
            SpiderDescription = "В очередной раз, решив отдохнуть, вы собираетесь в путь, как вы чувствуете, " +
                    "что по потолку что-то бегает, много маленьких ножек. Через мгновенье одного из ваших друзей " +
                    "утаскивают в темноту, вы даже не успеваете понять что произошло. " +
                    "Теперь нало его как-то найти, до того как его не съели.";
        }


        return SpiderDescription;
    }

    public String GenerateCastleNPC(){
        return "Крепость - сердце города, самое защищённое его место. " +
                "В крепости живут самые высшие вельможи, от которых вы можете получить много информации, " +
                "если вы им понравитесь или за мешок монет. В том числе, тут одни из самых лучших харчёвен " +
                "города, в которых вы можете перекусить так, как в последний раз. Вы видите несколько мужчин, " +
                "похоже что они тоже поглядывают на вас.";
    }

    public String GenerateMountainsNPC(){
        String MountainsNPCDescription = "";
        int index = Random();

        if(index <= 50){
            MountainsNPCDescription += "Вы передвигаетесь по одному из опаснейших место средиземья, " +
                    "но похоже вам улыбается удача и вы никого враждебного не встречаете.";
        }
        else{
            MountainsNPCDescription += GenerateVargsDescription();
        }
//        else if(index<=80){
//            MountainsNPCDescription += GenerateVyvernDescription();
//        }
//        else if(index<=97){
//            MountainsNPCDescription += GenerateNazgulsDescription();
//        }
//        else{
//            MountainsNPCDescription += GenerateDragonDescription();
//        }

        return MountainsNPCDescription;
    }

    public String GenerateVargsDescription(){
        String VargsDescription = "";
        if((RandomOf4()-1)/2 == 0){
            VargsDescription += " Через завывания ветра, вы слышите эхо, " +
                    "похожее на вой большой собаки, возможно это волки, они не так страшны, " +
                    "а вот если это варги, то это уже другое дело...";
        }
        else{
            VargsDescription += " Вы уже довольно долго шли по снежной пустыне, и вы уже начали думать, " +
                    "что вам слышиться вой, но совсем скоро вы убедились в том, что это не ваше воображение";
        }
        return VargsDescription;
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
        String FirstMessage = GenerateFirstSentence() + GenerateWeatherDescription() + GenerateThirdSentence() + " You see";

        String DesriptionLocationSentence = lstm_modelManager.GenerateLocationDescriptionForLTR(FirstMessage);

        Log.d("My_log", DesriptionLocationSentence);

        return GenerateKey_ObjectDescription() + GenerateFirstSentence() + GenerateWeatherDescription()
                 + GenerateThirdSentence() + DesriptionLocationSentence + GenerateNPCSentence() + GenerateNPCCharacters();
    }
}
