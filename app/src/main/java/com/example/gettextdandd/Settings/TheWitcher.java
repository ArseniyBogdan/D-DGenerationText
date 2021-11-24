package com.example.gettextdandd.Settings;

import android.content.res.Resources;
import android.util.Log;

import com.example.gettextdandd.LSTM_ModelManager;
import com.example.gettextdandd.MainActivity;
import com.example.gettextdandd.R;

import java.util.Random;

public class TheWitcher implements SettingInterface{
    private final String biom;
    private final String location;
    private final String Key_object;
    private final String NPC;

    private String NameOfDictionary = "word_dict_Sapkovsky.json";

    private final LSTM_ModelManager lstm_modelManager = new LSTM_ModelManager(NameOfDictionary);

    private String weather;
    private final Resources res = MainActivity.getInstance().getResources();

    public TheWitcher(String biom, String location, String weather, String Key_object, String NPC){
        this.biom = biom;
        this.location = location;
        this.weather = weather;
        this.Key_object = Key_object;
        this.NPC = NPC;
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

    public String GenerateThirdSentence(){
        return " Очутившись на месте, вы " +
                "первым делом осматриваетесь. ";
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

    @Override
    public String GenerateDescriptionLocationWithLSTM() {
        return null;
    }

    @Override
    public String GenerateNPCCharacters() {
        switch(location){
            case "Топи": return GenerateSwampNPC();
            case "Город": return res.getString(R.string.CityNPCDescription_TheWitcher);
            case "Лес": return GenerateForestNPC();
            case "Корчма": return GenerateTavernNPC();
            case "Подземелье": return GenerateDungeonNPC();
            case "Горы": return GenerateMountainsNPC();
        }
        return null;
    }

    public String GenerateSwampNPC(){
        String SwampSentence = "";
        int index = Random();
        int count;
        SwampSentence += res.getString(R.string.Start_SwampSentence) + " ";

        if(index <= 40){
            SwampSentence += "небольшую группу утоплцев, снующую из стороны в сторону. Главное держаться поближе к суше";
        }
        else if(index <= 70 ){
            count = RandomOf4();
            if(count>1){
                SwampSentence += count + " гнильцов, по одному они не так опасны, но когда их стая, " +
                        "они могут доставить множество проблем, не говоря про то, что они взрываются при смерти.";
            }
            else{
                SwampSentence += 1 + " гнильца, он не так страшен, как кажется на первый взгляд. Самое главное - вовремя отбежать. :)";
            }
        }
        else if(index <= 90){
            count = RandomOf4()/2;
            if (count == 1){
                SwampSentence += 1 + " гуля, помимо того, что он силён, так он ещё может регенерировать.";
            }
            else{
                SwampSentence += 2 + " гулей, их будет слонжно одолеть, лучше всего фокусироваться " +
                        "на одном, а потом переходить на другого, иначе вы рискуете быть съеденным";
            }
        }
        else{
            SwampSentence += GenerateFogmanDescription();
        }

        return SwampSentence;
    }

    public String GenerateFogmanDescription(){
        return "... Туман? Какой-то он странный, как будто надвигается на " +
                "вас и окружает белой стеной. Вам становится не по себе.";
    }

    public String GenerateForestNPC(){
        String ForestSentence = "";
        int index = Random();
        if(index<=30){
            ForestSentence += " Похоже вы одни.";
        }
        else if(index<=50){
            ForestSentence += GenerateDogDescription();
        }
        else if(index<=70){
            ForestSentence += GenerateWolfDescription();
        }
        else if(index <= 95){
            ForestSentence += GenerateBrigandDescription();
        }
        else{
            ForestSentence += GenerateDemonDescription();
        }

        return ForestSentence;
    }

    public String GenerateDogDescription(){
        String DogDescription = "Вы видите стаю собак. ";
        int count = (RandomOf4()-1)/2;
       if(count == 0){
           DogDescription += "'Собака – вроде как лучший друг человека… " +
                   "Но в эти паршивые времена даже друзьям нельзя доверять.' " +
                   "– Альфред Панкрац, нищий из Новиграда.";
       }
       else{
           DogDescription += "Некоторые говорят, что собака – зеркало человека. " +
                   "Если к собаке относиться хорошо, она отблагодарит верностью и доверием," +
                   " а если плохо, отплатит так же: на крик ответит рычанием, на пинок – укусом. " +
                   "А поскольку злых ладей ныне более чем достаточно, злых собак тоже хватает." +
                   " Особенно опасны одичавшие псины, которые уже совсем потеряли уважение к человеку, " +
                   "зато распробовали кровь и падаль.";
        }
        return DogDescription;
    }

    public String GenerateWolfDescription(){
        String WolfDescription = "Вы видите стаю волков. ";
        int count = (RandomOf4()-1)/2;
        if (count == 0){
            WolfDescription += "«„Спокойно... я знаю, как приручить волка” — последние слова ловчего Дунбара».";
        }
        else{
            WolfDescription += "'Мой знакомый обыкновенно говорил, что со всеми этими грифонами " +
                    "и василисками старых добрых волков уже и бояться нечего... " +
                    "А потом волки сожрали у него половину отары' \n " +
                    "— Ингвар, пастух";
        }
        return  WolfDescription;
    }

    public String GenerateBrigandDescription(){
        String BrigandDescription = "Вы видите группу разбойников. ";
        int count = (RandomOf4()-1)/2;
        if (count == 0){
            BrigandDescription += " Они слабо вооружены, видно только недавно начали " +
                    "грабить ближайшие деревни. Вы можете преподнести им урок";
        }
        else {
            BrigandDescription += " Они хорошо вооружены и ведут за собой какого-то купца. " +
                    "Возможно если вы освободите его, то он щедро вас вознаградит.";
        }
        return BrigandDescription;
    }

    public String GenerateTavernNPC(){
        String Tavern_sentence = "";
        int count = (RandomOf4()-1)/2;
        if(count == 0){
            Tavern_sentence += "Войдя в корчму, вы видите владельца корчмы, разливающего горячительные " +
                    "напитки уже поддатым посетителям. Все посматривают на вас с опаской, как-будто чувствуют, " +
                    "что от вас исходит некая сила. Но это не останавливает нескольких " +
                    "пьяниц от колких высказываний в вашу сторону.";
        }
        else{
            Tavern_sentence += "Войдя в корчму, вы никого не замечаете. Она пуста. " +
                    "Довольно редкое явление в наше время. Здесь явно что-то произошло.";
        }

        return Tavern_sentence;
    }

    public String GenerateDemonDescription(){
        return  " Вы видите беса. Это не самое лучше существо, " +
                "с которым можно играть в прятки, потому что он чует вас за километр";
    }

    public String GenerateDungeonNPC(){
        String Dungeon_sentence = "Вокруг вас сгущается тьма. Вы чувствуете пристальный взгляд на себе. ";
        int index = Random();
        if(index <= 30){
            Dungeon_sentence += "Вы выжидаете ещё немного, но похоже вам показалось.";
        }
        else if(index <= 50){
            Dungeon_sentence += "В скором времени из темноты появляются виновники вашего беспокойства.";
            Dungeon_sentence += GenerateNakerDescription();
        }
        else if(index <= 70){
            Dungeon_sentence += "В скором времени из темноты появляются виновники вашего беспокойства.";
            Dungeon_sentence += GenerateFogmanDescription();
        }
        else if(index <= 90){
            Dungeon_sentence += "В скором времени из темноты появляются виновники вашего беспокойства.";
            Dungeon_sentence += GenerateRockTroll();
        }
        else{
            Dungeon_sentence += "В скором времени из темноты появляются виновники вашего беспокойства.";
            Dungeon_sentence += GenerateLycanthropeDescription();
        }
        return Dungeon_sentence;
    }

    public String GenerateLycanthropeDescription(){
        String LycantropeDescription = "";
        if ((RandomOf4()-1)/2 == 0){
            LycantropeDescription += "«Говорят, ликантропия заразна, достаточно одного укуса, " +
                    "чтобы стать оборотнем. Ведьмаки знают, что это неправда. Оборотнем от " +
                    "одного укуса можно стать лишь в результате сильного проклятия».\n" +
                    "— Описание Волколака в «Гвинте»";
        }
        else{
            LycantropeDescription += "е так страшен волк, как его малюют. " +
                    "Зато волколак куда страшней! - Эльза Вильге, лучница";
        }

        return  LycantropeDescription;
    }

    public String GenerateNakerDescription(){
        String NakerDescription = "Это довольно большая группа накеров, готовая разодрать вас в клочья.";
        if((RandomOf4()-1)/2 == 0){
            NakerDescription += "'Будьте внимательны, милсдари, под мостом завелись накеры. " +
                    "Если поедете группой, не задерживаясь, то бояться нечего. " +
                    "Но ежели у вас там ось в телеге полетит, ежели вы там застрянете… " +
                    "Закройте глаза и молитесь Мелитэле.'\n" +
                    "— Курт Хаммербах, городской стражник из Венгерберга.";
        }
        else{
            NakerDescription += "«Мелкие, быстрые, гнусные».\n" +
                    "— Описание Накера в «Гвинте»";
        }

        return NakerDescription;
    }

    public String GenerateRockTroll(){
        String RockTrollDescription = "Похоже что это скальный тролль.";
        if((RandomOf4()-1)/2 == 0){
            RockTrollDescription += "«Существует множество видов троллей. " +
                    "Однако до сих пор не удалось найти ни одного, который " +
                    "не был бы туп, как ведро гвоздей».\n" +
                    "— «Кровная вражда: Ведьмак. Истории»";
        }
        else{
            RockTrollDescription += "Он медленно подходит к вам и говорит:'Скажи загадку. Только простую. А если нет, бум по башке.'";
        }

        return RockTrollDescription;
    }

    public String GenerateMountainsNPC(){
        String MountainDescription = "";
        int index = Random();
        if(index <= 30){
            MountainDescription = "двигаясь по горным хребтам, вы не замечаете ничего опасного на своём пути. Похоже вам сегодня очень везёт.";
        }
        else if(index <= 50){
            MountainDescription = GenerateGarpianDescription();
        }
        else if(index <= 70){
            MountainDescription += "Вы замечаете большой камень на верщине холма, и камень тоже замечает вас.";
            MountainDescription = GenerateRockTroll();
        }
        else if(index<=90){
            MountainDescription += GenerateVasiliskDesription();
        }
        else{
            MountainDescription += GenerateGryphonDescription();
        }

        return MountainDescription;
    }

    public String GenerateGarpianDescription(){
        String GarpianDescription = "Ваш слух, натренированный годами, улавливает лёгкий шелест " +
                "крыльев. С каждой секундой этот звук усиливается, похоже что-то летит прямо на вас. ";
        GarpianDescription += "В последнюю секунду вы разворачиваетесь и видите над собой пикирующую стаю гарпий.\n";
        if((RandomOf4()-1)/2 == 0){
            GarpianDescription += " «Спрячьте украшения, сударыня. Не то гарпии слетятся».\n" +
                    "— Описание Гарпии в «Гвинте»";
        }
        else{
            GarpianDescription += "На самом деле, чудовища не собирают в логове сокровищ. " +
                    "Ну, за исключением гарпий. Эти, да. Они блестяшки любят.\n" +
                    "— Лето, ведьмак Школы Змеи";
        }
        return GarpianDescription;
    }

    public String GenerateVasiliskDesription(){
        String VasiliskDescription = "Ваш слух, натренированный годами, улавливает лёгкий шелест " +
                "крыльев. С каждой секундой этот звук усиливается, похоже что-то летит прямо на вас. ";
        VasiliskDescription += "Вы навсегда запомнили звук, издаваемый этими крыльями, это василиск";
        if((RandomOf4()-1) == 0){
            VasiliskDescription += "Здесь похоронен благородный рыцарь Родерик, " +
                    "который пал в схватке с василиском. Точнее, то, что от него осталось.\n" +
                    "– надгробная надпись на кладбище в Вызиме.";
        }
        else{
            VasiliskDescription += "Люди называют василисков королями зерриканских пустынь и очень часто " +
                    "путают их с кокатриксами. По поверьям, эти создания настолько ненавидят " +
                    "все живое, что само их дыхание несет смерть, а их взгляд обращает людей в камень. " +
                    "В сказках василиска убивают, выставляя перед их мордой зеркало. Чудовище якобы " +
                    "умирает от собственного смертоносного взгляда. Однако, как любят пошутить ведьмаки, " +
                    "единственный способ убить василиска с помощью зеркала - садануть им зверю прямо по башке.";
        }
        return  VasiliskDescription;
    }

    public String GenerateGryphonDescription(){
        String GryphonDescription = "Ваш слух, натренированный годами, улавливает лёгкий шелест " +
                "крыльев. С каждой секундой этот звук усиливается, похоже что-то летит прямо на вас. ";
        GryphonDescription += "Проблема заключается в том, что это нечто загородило всё небо над вами. ";
        if((RandomOf4()-1)/2 == 0){
            GryphonDescription += "«Грифоны любят помучать. Едят живьём, по кусочку».\n" +
                    "— Описание Грифона в «Гвинте»";
        }
        else{
            GryphonDescription += "Ну да. Полуорёл был, полукот. Совсем как у дворян на гербе," +
                    " только в когтях нёс не скипетр, а коровье стерво.\n" +
                    "— анонимный свидетель нападения грифона";
        }
        return GryphonDescription;
    }

    @Override
    public String GenerateDescription() {
        String FirstMessage = GenerateFirstSentence() + GenerateWeatherDescription() + GenerateThirdSentence() + " You see";

        String DesriptionLocationSentence = lstm_modelManager.GenerateLocationDescriptionForTheWitcher(FirstMessage);

        Log.d("My_log", DesriptionLocationSentence);

        return GenerateKey_ObjectDescription() + GenerateFirstSentence() + GenerateWeatherDescription()
                + GenerateThirdSentence() + DesriptionLocationSentence + GenerateNPCSentence() + GenerateNPCCharacters();
    }
}
