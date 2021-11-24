package com.example.gettextdandd;


import android.util.Log;

import com.example.gettextdandd.ml.AnklavRus;
import com.example.gettextdandd.ml.SapkovskyEn;
import com.example.gettextdandd.ml.TolkinEn;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class LSTM_ModelManager {
    String NameOfDictionary;

    public String TranslatedMessage = "";

    HashMap Dictionary = new HashMap<String, Integer>();
    HashMap DictionaryDecode = new HashMap<Integer, String>();

    public LSTM_ModelManager(String NameOfDictionary){
        this.NameOfDictionary = NameOfDictionary;
    }



    public String GenerateLocationDescriptionForFallout(String Message){
        String Sentence = " Вы видите";
        try{
            GenerateDictionary(NameOfDictionary);
            try {
                AnklavRus model = AnklavRus.newInstance(MainActivity.getInstance());
                for(int i = 0; i<10; i++){
                    ArrayList<Integer> TokenizedSequence = tokenize(Message, Dictionary);
                    TokenizedSequence = padSequence(TokenizedSequence);

                    float[] floatArray = toFloatArray(TokenizedSequence);

                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 20}, DataType.FLOAT32);
                    inputFeature0.loadArray(floatArray);

                    // Runs model inference and gets result.
                    AnklavRus.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    int indexOfWord = GetIndexMaxFloatFromArray(outputFeature0.getFloatArray());
                    String word = String.valueOf(DictionaryDecode.get(indexOfWord));

                    Sentence += " " + word;
                    Message += " " + word;
                }
                model.close();
                Sentence += ". ";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return Sentence;
    }

    public String GenerateLocationDescriptionForLTR(String Message){
        translateFromToThread(Message, "ru", "en");
        String EnglishMessage = TranslatedMessage;

        String Sentence = " You see";
        try{
            GenerateDictionary(NameOfDictionary);
            try {
                Thread.sleep(500);
                TolkinEn model = TolkinEn.newInstance(MainActivity.getInstance());
                for(int i = 0; i<10; i++){
                    ArrayList<Integer> TokenizedSequence = tokenize(EnglishMessage, Dictionary);
                    TokenizedSequence = padSequence(TokenizedSequence);

                    float[] floatArray = toFloatArray(TokenizedSequence);

                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 20}, DataType.FLOAT32);
                    inputFeature0.loadArray(floatArray);

                    // Runs model inference and gets result.
                    TolkinEn.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    int indexOfWord = GetIndexMaxFloatFromArray(outputFeature0.getFloatArray());
                    String word = String.valueOf(DictionaryDecode.get(indexOfWord));

                    EnglishMessage += " " + word;
                    Sentence += " " + word;
                }
                model.close();
                Sentence += ". ";
                translateFromToThread(Sentence, "en", "ru");
                Thread.sleep(500);
                Sentence = TranslatedMessage;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return Sentence;
    }

    public String GenerateLocationDescriptionForTheWitcher(String Message){
        translateFromToThread(Message, "ru", "en");
        String EnglishMessage = TranslatedMessage;

        String Sentence = " You see ";
        try{
            GenerateDictionary(NameOfDictionary);
            try {
                Thread.sleep(500);
                SapkovskyEn model = SapkovskyEn.newInstance(MainActivity.getInstance());
                for(int i = 0; i<10; i++){
                    ArrayList<Integer> TokenizedSequence = tokenize(EnglishMessage, Dictionary);
                    TokenizedSequence = padSequence(TokenizedSequence);
                    float[] floatArray = toFloatArray(TokenizedSequence);

                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 20}, DataType.FLOAT32);
                    inputFeature0.loadArray(floatArray);

                    // Runs model inference and gets result.
                    SapkovskyEn.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    int indexOfWord = GetIndexMaxFloatFromArray(outputFeature0.getFloatArray());
                    String word = String.valueOf(DictionaryDecode.get(indexOfWord));

                    EnglishMessage += " " + word;
                    Sentence += " " + word;
                }
                model.close();
                Sentence += ". ";


            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        translateFromToThread(Sentence, "en", "ru");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Sentence = TranslatedMessage;
        return Sentence;
    }

    public void translateFromTo(String Message, String FromLanguage, String ToLanguage){
        String str = "";
        try {
            Log.d("My_Log", " I'm in!");
            String encode = URLEncoder.encode(Message, "UTF-8");
            StringBuilder sb = new StringBuilder();
            sb.append("https://translate.googleapis.com/translate_a/single?client=gtx&sl=");
            sb.append(FromLanguage);
            sb.append("&tl=");
            sb.append(ToLanguage);
            sb.append("&dt=t&q=");
            sb.append(encode);
            HttpResponse execute = new DefaultHttpClient().execute(new HttpGet(sb.toString()));
            StatusLine statusLine = execute.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                execute.getEntity().writeTo(byteArrayOutputStream);
                String byteArrayOutputStream2 = byteArrayOutputStream.toString();
                byteArrayOutputStream.close();
                JSONArray jsonArray = new JSONArray(byteArrayOutputStream2).getJSONArray(0);
                String str2 = str;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray jsonArray2 = jsonArray.getJSONArray(i);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str2);
                    sb2.append(jsonArray2.get(0).toString());
                    str2 = sb2.toString();
                }
                TranslatedMessage = str2;
            }
            execute.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        } catch (Exception e) {
            Log.e("translate_api", e.getMessage());
        }
    }

    public void translateFromToThread(String Message, String FromLanguage, String ToLanguage){

        Thread translate = new Thread(new Runnable() {
            @Override
            public void run() {
                translateFromTo(Message, FromLanguage, ToLanguage);
            }
        });
        translate.start();
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

    private String loadJSONFromAsset(String file){
        String json = null;
        try {
            InputStream inputStream = MainActivity.getInstance().getAssets().open(file);
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
        int maxlen = 20;
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
