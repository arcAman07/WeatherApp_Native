package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//https://api.openweathermap.org/data/2.5/weather?q=Mumbai&appid=cbbf3324be040d15fb8a5e5e49da11da

public class MainActivity extends AppCompatActivity {
    TextView cityName;
    TextView displayWeather;
    TextView displayTemperature;
    String link = "";
    String weatherDescription;
    String temperature;
    String apiKey = "cbbf3324be040d15fb8a5e5e49da11da";
    public class DownloadWeather extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection;
            String result = "";
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char ch = (char) data;
                    result += ch;
                    data = reader.read();

                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "FAILED";
            }

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonPart;
            Log.i("Weather Json", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weather = jsonObject.getString("weather");
                String main = jsonObject.getString("main");
                JSONArray arr = new JSONArray(weather);
                for (int i = 0; i < arr.length(); i++) {
                    jsonPart = arr.getJSONObject(i);
                    weatherDescription = jsonPart.getString("description");


                }
                JSONObject temp = new JSONObject(main);
                temperature = temp.getString("temp");
                Log.i("Weather Description", weatherDescription);
                Log.i("Temperature", temperature);
                String descriptionText = "Description of weather- "+weatherDescription;
                double celsiusTemperature = Double.parseDouble(temperature)-273;
                String temperatureText = "Temperature in "+cityName.getText()+"- "+String.valueOf(Math.round(celsiusTemperature*10)/10)+"°C";
                displayWeather.setText(descriptionText);
                displayTemperature.setText(temperatureText);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Invalid City Name Entered!",Toast.LENGTH_SHORT).show();
            }

        }
    }
    public void tapWeather(View view) {
        Button button = (Button) findViewById(R.id.button4); // Randomly made the variable, no use currently , will use it later
        cityName = (TextView) findViewById(R.id.editCity);
        displayWeather = (TextView) findViewById(R.id.getWeather);
        displayTemperature = (TextView) findViewById(R.id.getTemperature);
        DownloadWeather task = new DownloadWeather();
        link = "https://api.openweathermap.org/data/2.5/weather?q="+cityName.getText()+"&appid="+apiKey;
        try {
            task.execute(link);
//            String descriptionText = "Description of the Weather- "+weatherDescription;
//            int celsiusTemperature = Integer.parseInt(temperature)-273;
//            String temperatureText = "Temperature in "+cityName+"- "+Integer.toString(celsiusTemperature)+"°C";
//            displayWeather.setText(descriptionText);
//            displayTemperature.setText(temperatureText);


        } catch(Exception e) {
            e.printStackTrace();
        }




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}