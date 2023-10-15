package com.finsday.facedetection;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.finsday.facedetection.ml.EyeModel;
import com.finsday.facedetection.ml.EyebrowModel;
import com.finsday.facedetection.ml.ForeheadModel;
import com.finsday.facedetection.ml.JawModel;
import com.finsday.facedetection.ml.MouthModel;
import com.finsday.facedetection.ml.NoseModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class eye_model {
    private Context mContext;
    public eye_model(Context mContext) {
        this.mContext = mContext;
    }

    public void eyePrediction(Uri uri, TextView textView){
        int imageSize = 200;
        try {
            // models
            EyeModel model = EyeModel.newInstance(mContext);
            
            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{16, 3, 200, 200}, DataType.FLOAT32);

            ByteBuffer byteBuffer = getByteBuffer(uri, imageSize);

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            EyeModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            String[] classes = {"Big","Silt","Small"};

            extracted(textView, outputFeature0,classes,"eyes");

            // Releases model resources if no longer used.
            model.close();
        } catch (Exception e) {
            // TODO Handle the exception
        }
    }

    public void eyeBrowPrediction(Uri uri, TextView textView){
        int imageSize = 200;
        try {
            // models
            EyebrowModel model = EyebrowModel.newInstance(mContext);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{16, 3, 200, 200}, DataType.FLOAT32);


            ByteBuffer byteBuffer = getByteBuffer(uri, imageSize);

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            EyebrowModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            String[] classes = {"Arch","Circle","Straight"};

            extracted(textView, outputFeature0,classes,"eyebrows");

            // Releases model resources if no longer used.
            model.close();
        } catch (Exception e) {
            // TODO Handle the exception
        }
    }

    public void foreheadPrediction(Uri uri, TextView textView){
        int imageSize = 256;
        try {
            // models
            ForeheadModel model = ForeheadModel.newInstance(mContext);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.FLOAT32);

            Bitmap image = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }


            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ForeheadModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            String[] classes = {"Curved","Sloped","Straight"};

            extracted(textView, outputFeature0,classes,"forehead");

            // Releases model resources if no longer used.
            model.close();
        } catch (Exception e) {
            // TODO Handle the exception
        }
    }

    public void jawPrediction(Uri uri, TextView textView){
        int imageSize = 200;
        try {
            // models
            JawModel model = JawModel.newInstance(mContext);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{16, 3, 200, 200}, DataType.FLOAT32);
            ByteBuffer byteBuffer = getByteBuffer(uri, imageSize);

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            JawModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            String[] classes = {"Circle","Oval","Square","Triangle"};

            extracted(textView, outputFeature0,classes,"face");

            // Releases model resources if no longer used.
            model.close();
        } catch (Exception e) {
            // TODO Handle the exception
        }
    }

    public void mouthPrediction(Uri uri, TextView textView){
        int imageSize = 200;
        try {
            // models
            MouthModel model = MouthModel.newInstance(mContext);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{16, 3, 200, 200}, DataType.FLOAT32);
            ByteBuffer byteBuffer = getByteBuffer(uri, imageSize);

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            MouthModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            String[] classes = {"Medium","Small","Thick"};

            extracted(textView, outputFeature0,classes,"mouth");

            // Releases model resources if no longer used.
            model.close();
        } catch (Exception e) {
            // TODO Handle the exception
        }
    }

    public void nosePrediction(Uri uri, TextView textView){
        int imageSize = 200;
        try {
            // models
            NoseModel model = NoseModel.newInstance(mContext);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{16, 3, 200, 200}, DataType.FLOAT32);
            ByteBuffer byteBuffer = getByteBuffer(uri, imageSize);

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            NoseModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            String[] classes = {"Long","Small","Wide"};

            extracted(textView, outputFeature0,classes,"nose");

            // Releases model resources if no longer used.
            model.close();
        } catch (Exception e) {
            // TODO Handle the exception
        }
    }






    private void extracted(TextView textView, TensorBuffer outputFeature0,String[] classes,String className) {
        float[] confidences = outputFeature0.getFloatArray();
        // find the index of the class with the biggest confidence.
        int maxPos = 0;
        float maxConfidence = 0;
        for (int i = 0; i < confidences.length; i++) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i];
                maxPos = i;
            }
//            Toast.makeText(mContext, ""+confidences[i], Toast.LENGTH_SHORT).show();
        }


        String forText = classes[maxPos] +"_"+ confidences[maxPos];

        if (classes[maxPos] == classes[0]){
            forText = classes[maxPos] +" "+className+"!!";
        }else if (classes[maxPos] == classes[1]){
            forText = classes[maxPos] +" "+className+"!!";
        }else {
            forText = classes[maxPos] +" "+className+"!!";
        }

        textView.setText(forText);
    }

    @NonNull
    private ByteBuffer getByteBuffer(Uri uri, int imageSize) throws IOException {
        Bitmap image = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
        int dimension = Math.min(image.getWidth(), image.getHeight());
        image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
        image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3 * 16);
        byteBuffer.order(ByteOrder.nativeOrder());

        int[] intValues = new int[imageSize * imageSize];
        image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
        int pixel = 0;
        //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
        for(int i = 0; i < imageSize; i ++){
            for(int j = 0; j < imageSize; j++){
                int val = intValues[pixel++]; // RGB
                byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
            }
        }
        return byteBuffer;
    }
}
