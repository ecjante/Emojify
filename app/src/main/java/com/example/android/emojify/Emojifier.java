package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

/**
 * Created by enrico on 2/20/18.
 */

public class Emojifier {
    private static final String TAG = Emojifier.class.getSimpleName();

    public static void detectFaces(Context context, Bitmap bitmap) {
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        Frame frame = new Frame.Builder()
                .setBitmap(bitmap)
                .build();

        SparseArray<Face> faces = detector.detect(frame);

        Log.d(TAG, "detectFaces: Number of faces: " + faces.size());

        if (faces.size() == 0) {
            Toast.makeText(context, "No faces detected", Toast.LENGTH_LONG).show();
        } else {
            for (int i = 0; i < faces.size(); i++) {
                Face face = faces.valueAt(i);
                Log.d(TAG, "detectFaces: Face classification for face " + i);
                getClassifications(face);
            }
        }

        detector.release();
    }

    public static void getClassifications(Face face) {
        float leftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
        float rightEyeOpenProbability = face.getIsRightEyeOpenProbability();
        float smilingProbability = face.getIsSmilingProbability();

        Log.d(TAG, "getClassifications: Left eye open probability: " + leftEyeOpenProbability);
        Log.d(TAG, "getClassifications: Right eye open probability: " + rightEyeOpenProbability);
        Log.d(TAG, "getClassifications: Smiling probability: " + smilingProbability);
    }
}
