package com.example.ama.homechef;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



import com.ibm.watson.developer_cloud.android.library.camera.CameraHelper;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ImageClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RequestQueue MyRequestQueue;
    JSONArray ingredients = new JSONArray();
    private VisualRecognition vrClient;
    private CameraHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Visual Recognition client
        vrClient = new VisualRecognition(
                VisualRecognition.VERSION_DATE_2016_05_20,
                getString(R.string.api_key)
        );

        // Initialize camera helper
        helper = new CameraHelper(this);

    }

    public void takePicture(View view) {
        // More code here
        helper.dispatchTakePictureIntent();
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final Bitmap photo = helper.getBitmap(resultCode);
        final File photoFile = helper.getFile(resultCode);

        if(requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE) {
            // More code here
            ImageView preview = findViewById(R.id.preview);
            Matrix matrix = new Matrix();

            matrix.postRotate(90);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(photo,photo.getWidth(),photo.getHeight(),true);

            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
            preview.setImageBitmap(rotatedBitmap);
        }
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                VisualClassification response =
                        vrClient.classify(
                                new ClassifyImagesOptions.Builder()
                                        .images(photoFile)
                                        .build()
                        ).execute();

                // More code here
                ImageClassification classification =
                        response.getImages().get(0);

                VisualClassifier classifier =
                        classification.getClassifiers().get(0);

                final StringBuffer output = new StringBuffer();


                for(VisualClassifier.VisualClass object: classifier.getClasses()) {
                    if(object.getScore() > 0.7f)
                       /* output.append("<")
                                .append(object.getName())
                                .append("> ");*/
                        ingredients.put(object.getName());
                }
                output.append(ingredients.toString());

                String url = "http://yourdomain.com/path";
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //This code is executed if there is an error.
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                        return MyData;
                    }
                };

                MyRequestQueue.add(MyStringRequest);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView detectedObjects =
                                findViewById(R.id.detected_objects);
                        detectedObjects.setText(output);
                    }
                });
            }
        });
    }


}
