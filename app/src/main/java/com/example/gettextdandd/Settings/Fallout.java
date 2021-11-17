package com.example.gettextdandd.Settings;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Switch;

import androidx.core.content.res.TypedArrayUtils;

import com.example.gettextdandd.MainActivity;
import com.example.gettextdandd.R;
import com.example.gettextdandd.ml.ModelStapkovsky;
import com.example.gettextdandd.ml.ModelStapkovskyQant;
import com.example.gettextdandd.ml.ModelStapkovskyThirdbest;
import com.example.gettextdandd.ml.SmallThewitcherModelGood;
import com.example.gettextdandd.ml.SmallThewitcherModelSecondbest;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.ops.DequantizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Fallout implements SettingInterface{
    private String location, weather, biom;

    HashMap Dictionary = new HashMap<String, Integer>();
    HashMap DictionaryDecode = new HashMap<Integer, String>();

    Resources res = MainActivity.getInstance().getResources();

    public Fallout(String biom, String location, String weather){
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

//    public String GenerateCityNPC(){
//        return res.getString(R.string.CityNPCDescription);
//    }

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

//    public String GenerateSupermutantsDescription(){
//        return "Вы замечаете группу супермутантов, в ближнем " +
//                "бою они очень сильны, ноони глупее людей, поэтому их можно " +
//                "без проблем обхитрить. Главное не подпускать их близко. " +
//                "Я бы не советовал лишний раз вступать с ними в бой";
//    }

//    public String GenerateGulsDescription(){
//        return "Вы замечаете кучу мертвецов лежащих на улице. " +
//                "От них исходит лёгкий запах гниения, но он не настолько сильный, " +
//                "каким должен быть. Похоже здесь что-то не так.";
//    }

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

//    public String GenerateLegionCaesarsNPC(){
//        return "Вы вышли на главную площадь перед шатром цезаря, " +
//                "вы можете пойти к Цезарю и обсудить с ним кое-какие вопросы," +
//                " но вам придётся подождать. Цезарь - очень занятой человек. " +
//                "В ожидании вы можете пополнить припасы в арсенале легиона";
//    }

//    public String GenerateDeathClawDescription(){
//        return "Вы чувствуете как дрожит земля. Это может быть " +
//                "только коготь смерти, молитесь о том, чтобы он вас не заметил";
//    }

//    public String GenerateMoleRat(){
//        return "Внезапно из-под земли вырывается группа кротокрысов. " +
//                "Похоже они проголодались и хотят полакомиться вами.";
//    }

//    public String GenerateCaravan(){
//        return "Вам встретился караван торговцев. Вы можете пополнить припасы";
//    }

    @Override
    public String GenerateDescription() {
        String FirstMessage = "the witcher he was led in by a soldier in a hooded coat the conversation did not yield any significant results the miller was terrified he mumbled and stammered and his scars told the witcher more than he did the striga could open her jaws impressively wide and had extremely sharp";
//        String FirstMessage = GenerateFirstSentence() + GenerateWeatherDescription() + GenerateThirdSentence();

        try{
            GenerateDictionary("word_dict_Stapkovsky.json");
            try {
                ModelStapkovskyThirdbest model = ModelStapkovskyThirdbest.newInstance(MainActivity.getInstance());

                for(int i = 0; i<20; i++){

                    ArrayList<Integer> TokenizedSequence = tokenize(FirstMessage, Dictionary);

                    Log.d("My_log", TokenizedSequence.toString());

                    TokenizedSequence = padSequence(TokenizedSequence);

                    Log.d("My_log", TokenizedSequence.toString());

                    float[] floatArray = toFloatArray(TokenizedSequence);
                    ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[floatArray.length * 4]);
                    for(int j = 0; j< floatArray.length - 1; j++){
                        byteBuffer.putFloat(floatArray.length-j);
                    }
                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 10}, DataType.FLOAT32);


                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    ModelStapkovskyThirdbest.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    int indexOfWord = GetIndexMaxFloatFromArray(outputFeature0.getFloatArray());
                    String word = String.valueOf(DictionaryDecode.get(indexOfWord));

                    Log.d("My_Log", Arrays.toString(outputFeature0.getFloatArray()));
                    Log.d("Log_for_output_values", String.valueOf(outputFeature0.getFloatValue(2)));
                    Log.d("My_Log", String.valueOf(indexOfWord));
                    Log.d("My_Log", word);
                    FirstMessage += " " + word;

                }
                model.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            ArrayList<Integer> TokenizedSequence = tokenize(FirstMessage, Dictionary);
//            TokenizedSequence = padSequence(TokenizedSequence);
//            float[] inputs = new float[TokenizedSequence.size()];
//            for(int i = 0; i < TokenizedSequence.size(); i++){
//                inputs[i] = TokenizedSequence.get(i).floatValue();
//            }
//
//            Interpreter interpreter = new Interpreter(loadModelFile());
//
//            Map<Integer, Object> outputs = new HashMap<>();
//            interpreter.run(inputs , outputs);
//            Log.d("My_Log", String.valueOf(outputs));
            return null;

        }catch (JSONException e){
            e.printStackTrace();
        }

        return GenerateFirstSentence() + GenerateWeatherDescription() + GenerateThirdSentence() + GenerateNPCCharacters();
    }

    public int GetIndexMaxFloatFromArray(float[] array){
        int position = 0;
        float maxValue = -1;

        for(int i = 0; i < array.length; i++){
            if(array[i] > maxValue) {
                maxValue = array[i];
                position = i;
            }
        }
        return position;
    }

    private float[] toFloatArray(ArrayList<Integer> data){
        int i = 0;
        float[] array = new float[data.size()];
        for (int f: data){
            array[i++] = (float) f;
        }
        return array;
    }

    private MappedByteBuffer loadModelFile(){
        String MODEL_ASSETS_PATH = "model_Stapkovsky.tflite";
        AssetFileDescriptor assetFileDescriptor;
        try {
            assetFileDescriptor = MainActivity.getInstance().getAssets().openFd(MODEL_ASSETS_PATH);
            FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
            FileChannel fileChannel = fileInputStream.getChannel();
            long startoffset = assetFileDescriptor.getStartOffset();
            long declaredLength = assetFileDescriptor.getDeclaredLength();
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startoffset, declaredLength);
            return buffer;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String loadJSONFromAsset(String filename){
        String json = null;
        try {
            InputStream inputStream = MainActivity.getInstance().getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void GenerateDictionary(String filename) throws JSONException {
        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(loadJSONFromAsset(filename)));
        Iterator<String> iterator  = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Dictionary.put(key , jsonObject.get(key));
            DictionaryDecode.put(jsonObject.get(key), key);
        }
    }

    private ArrayList<Integer> tokenize (String message, HashMap<String, Integer> vocabData){
        String[] parts  = message.split(" ");
        ArrayList<Integer> tokenizedMessage = new ArrayList<>();
        for (String part: parts) {
            if (!part.trim().equals("")){
                int index = 0;
                if (vocabData.get(part) != null) {
                    index = vocabData.get(part);
                }
                tokenizedMessage.add(index);
            }
        }
        return tokenizedMessage;
    }

    private ArrayList<Integer> padSequence (ArrayList<Integer> sequence){
        int maxlen = 10;
        if (sequence.size() > maxlen) {
            return new ArrayList<>(sequence.subList(sequence.size() - maxlen, sequence.size()));
        }
        else if ( sequence.size() < maxlen ) {
            ArrayList<Integer> array =  new ArrayList<>();
            for (int i = 0; i < maxlen - sequence.size(); i++){
                array.add(0);
            }
            array.addAll(sequence);
            return array;
        }
        else{
            return sequence;
        }
    }
}
