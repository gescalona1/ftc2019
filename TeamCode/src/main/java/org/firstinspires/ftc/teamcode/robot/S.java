package org.firstinspires.ftc.teamcode.robot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import com.qualcomm.ftccommon.CommandList;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.robocol.Command;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.baseopmodes.HardwareMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by gescalona on 2/5/19.
 */

public class S implements RecognitionListener {
    private Context context;

    public static SpeechRecognizer getSpeechRecognizer() {
        return speechRecognizer;
    }

    public static Intent getRecognizerIntent() {
        return recognizerIntent;
    }

    public  static SpeechRecognizer speechRecognizer;
    private static Intent recognizerIntent;
    private Telemetry telemetry;
    private static List<String> words;
    public S(Context context){
        this.context = context;

        if(SpeechRecognizer.isRecognitionAvailable(context)) {
            this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            speechRecognizer.setRecognitionListener(this);
            recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.ENGLISH);

            recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());

            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

            recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        }else throw new ExceptionWithContext("Cannot make speech recognizer, sorry :(");
    }

    public S(Context context, Telemetry telemetry){
        this(context);
        this.telemetry = telemetry;
    }

    private class SoundFake extends CommandList.CmdPlaySound {
        public SoundFake(long msPresentationTime, String hashString, SoundPlayer.PlaySoundParams params) {
            super(msPresentationTime, hashString, params);
        }

        @Override
        public String serialize(){
            speechRecognizer.startListening(recognizerIntent);
            return super.serialize();
        }
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }


    public static class TestCommand {
        public static final String Command = "CMD_LISTEN_TO_SOUND";
        public TestCommand() {

        }
        public String serialize()
        {
            return SimpleGson.getInstance().toJson(this);
        }
        public static TestCommand deserialize(String serialized) {
            speechRecognizer.startListening(recognizerIntent);
            return SimpleGson.getInstance().fromJson(serialized, TestCommand.class);
        }
    }
    public void doThis() {
        TestCommand cmdPlaySound = new TestCommand();
        Command command = new Command(TestCommand.Command, cmdPlaySound.serialize());
        NetworkConnectionHandler.getInstance().sendCommand(command);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        String message;
        switch (error) {

            case SpeechRecognizer.ERROR_AUDIO:

                message = "error audio";

                break;

            case SpeechRecognizer.ERROR_CLIENT:

                message = "error client";

                break;

            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:

                message = "error insufficient perms";

                break;

            case SpeechRecognizer.ERROR_NETWORK:

                message = "error network";

                break;

            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:

                message = "error network timeout";

                break;

            case SpeechRecognizer.ERROR_NO_MATCH:

                message = "error no match";

                break;

            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:

                message = "error recognizer busy";

                break;

            case SpeechRecognizer.ERROR_SERVER:

                message = "error server";

                break;

            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:

                message = "error speech timeout";

                break;

            default:

                message = "error understand";

                break;
        }
        if(telemetry != null) telemetry.addData("Error: ", message);
    }
    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if(telemetry != null) {
            telemetry.addData("matches: ", matches);
            telemetry.update();
        }
        words = matches;

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    public static List<String> getWords() {
        return words;
    }
}
