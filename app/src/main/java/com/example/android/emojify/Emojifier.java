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

    private static final double SMILING_PROB_THRESHOLD = .15;
    private static final double EYE_OPEN_PROB_THRESHOLD = .5;

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
                whichEmoji(face);
            }
        }

        detector.release();
    }

    public static void whichEmoji(Face face) {
        float leftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
        float rightEyeOpenProbability = face.getIsRightEyeOpenProbability();
        float smilingProbability = face.getIsSmilingProbability();

        Log.d(TAG, "whichEmoji: Left eye open probability: " + leftEyeOpenProbability);
        Log.d(TAG, "whichEmoji: Right eye open probability: " + rightEyeOpenProbability);
        Log.d(TAG, "whichEmoji: Smiling probability: " + smilingProbability);

        boolean smiling = smilingProbability > SMILING_PROB_THRESHOLD;
        boolean rightEyeClosed = rightEyeOpenProbability < EYE_OPEN_PROB_THRESHOLD;
        boolean leftEyeClosed = leftEyeOpenProbability < EYE_OPEN_PROB_THRESHOLD;

        Emoji emoji;
        if(smiling) {
            if (leftEyeClosed && !rightEyeClosed) {
                emoji = Emoji.LEFT_WINK;
            }  else if(rightEyeClosed && !leftEyeClosed){
                emoji = Emoji.RIGHT_WINK;
            } else if (leftEyeClosed){
                emoji = Emoji.CLOSED_EYE_SMILE;
            } else {
                emoji = Emoji.SMILE;
            }
        } else {
            if (leftEyeClosed && !rightEyeClosed) {
                emoji = Emoji.LEFT_WINK_FROWN;
            }  else if(rightEyeClosed && !leftEyeClosed){
                emoji = Emoji.RIGHT_WINK_FROWN;
            } else if (leftEyeClosed){
                emoji = Emoji.CLOSED_EYE_FROWN;
            } else {
                emoji = Emoji.FROWN;
            }
        }

        Log.d(TAG, "whichEmoji: " + emoji.name());
    }

    private enum Emoji {
        SMILE,
        FROWN,
        LEFT_WINK,
        RIGHT_WINK,
        LEFT_WINK_FROWN,
        RIGHT_WINK_FROWN,
        CLOSED_EYE_SMILE,
        CLOSED_EYE_FROWN
    }
}
