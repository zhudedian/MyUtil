package com.zdd.myutil.media;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * create by zhudedian at 2018/6/2.
 */

public class VoiceHelper {
    private final static String TAG = "SpeechSendText";
    private static VoiceHelper mInstance;
    private Context mContext;
    private TextToSpeech mTextToSpeech;
    private boolean mIsIntialized = false;

    Map<String, SpeechFromTextCallback> mCallbacks = new HashMap<>();

    /**
     * Initalize our TextToSpeech engine, use a few tricks to get it to use a smaller file size
     * and be more easily recognized by the Alexa parser
     * @param context local/application level context
     */
    private VoiceHelper(Context context){
        mContext = context.getApplicationContext();
        mTextToSpeech = new TextToSpeech(mContext, mInitListener);
//        mTextToSpeech.setPitch(.8f);
//        mTextToSpeech.setSpeechRate(1.5f);
        mTextToSpeech.setLanguage(Locale.US);
//        mTextToSpeech.setPitch(1.5f);
//        mTextToSpeech.setSpeechRate(0.9f);
        mTextToSpeech.setOnUtteranceProgressListener(mUtteranceProgressListener);
    }

    /**
     * Get an instance of the VoiceHelper utility class, if it's currently null,
     * create a new instance
     * @param context
     * @return
     */
    public static VoiceHelper getInstance(Context context){
        if(mInstance == null){
            mInstance = new VoiceHelper(context);
        }

        return mInstance;
    }

    /**
     * Our TextToSpeech Init state changed listener
     */
    private TextToSpeech.OnInitListener mInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if(status == TextToSpeech.SUCCESS){
                mIsIntialized = true;
            }else{
                new IllegalStateException("Unable to initialize Text to Speech engine").printStackTrace();
            }
        }
    };

    /**
     * Our TextToSpeech UtteranceProgress state changed listener
     * We keep track of when we're done and pass back the byte[] raw audio of the recorded speech
     */
    private UtteranceProgressListener mUtteranceProgressListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String utteranceId) {
        }

        @Override
        public void onDone(String utteranceId) {
            //this allows us to keep track of multiple callbacks
            SpeechFromTextCallback callback = mCallbacks.get(utteranceId);
            if(callback != null){
                //get our cache file where we'll be storing the audio
                File cacheFile = getCacheFile(utteranceId);
                try {
                    byte[] data = readFileToByteArray(cacheFile);
                    callback.onSuccess(data);
                } catch (IOException e) {
                    e.printStackTrace();
                    //bubble up our error
                    callback.onError(e);
                }
                cacheFile.delete();
                //remove the utteranceId from our callback once we're done
                mCallbacks.remove(utteranceId);
            }
        }

        @Override
        public void onError(String utteranceId) {
            //add more information to our error
            this.onError(utteranceId, TextToSpeech.ERROR);
        }

        @Override
        public void onError(String utteranceId, int errorCode) {
            if(mCallbacks == null){
                return;
            }
            SpeechFromTextCallback callback = mCallbacks.get(utteranceId);
            if(callback != null){
                //if we have a callback, bubble up the error
                callback.onError(new Exception("Unable to process request, error code: "+errorCode));
            }
        }
    };

    /**
     * Create a new audio recording based on text passed in, update the callback with the changing states
     * @param text the text to render
     * @param callback
     */
    public void getSpeechFromText(String text, SpeechFromTextCallback callback){
        //create a new unique ID
        String utteranceId = createCodeVerifier();
        //add the callback to our list of callbacks
        mCallbacks.put(utteranceId, callback);

        //get our TextToSpeech engine
        TextToSpeech textToSpeech = getTextToSpeech();
        if (textToSpeech==null){
            return;
        }
        //set up our arguments
        HashMap<String, String> params = new HashMap<>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
//        Log.i(TAG, "ready textToSpeech.synthesizeToFile text="+text+",path="+getCacheFile(utteranceId).toString());
        //request an update from TTS

        int getSpeechFromTextState = textToSpeech.synthesizeToFile(text, params, getCacheFile(utteranceId).toString());
        Log.i(TAG, "getSpeechFromTextState="+getSpeechFromTextState);
    }

    /**
     * Get the TextToSpeech instance
     * @return our TextToSpeech instance, if it's initialized
     */
    private TextToSpeech getTextToSpeech(){
        if(!mIsIntialized){
            return null;
//            throw new IllegalStateException("Text to Speech engine is not initalized");
        }
        return mTextToSpeech;
    }
    public static byte[] readFileToByteArray(final File file) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return toByteArray(in); // Do NOT use file.length() - see IO-453
        } finally {
            closeQuietly(in);
        }
    }
    public static void closeQuietly(final InputStream input) {
        closeQuietly((Closeable) input);
    }
    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }
    public static FileInputStream openInputStream(final File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }
    public static byte[] toByteArray(final InputStream input) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }
    public static int copy(final InputStream input, final OutputStream output) throws IOException {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }
    public static long copyLarge(final InputStream input, final OutputStream output)
            throws IOException {
        return copy(input, output, 1024 * 4);
    }
    public static long copy(final InputStream input, final OutputStream output, final int bufferSize)
            throws IOException {
        return copyLarge(input, output, new byte[bufferSize]);
    }
    public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer)
            throws IOException {
        long count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    /**
     * Our cache file based on the unique id generated for the intent
     * @param utteranceId
     * @return
     */
    private File getCacheFile(String utteranceId){
        return new File(getCacheDir(), utteranceId+".wav");
    }

    //helper function to get the default cache dir for the app
    private File getCacheDir(){
        return mContext.getCacheDir();
    }

    /**
     * Create a new code verifier for our token exchanges
     * @return the new code verifier
     */
    public static String createCodeVerifier() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 128; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String verifier = sb.toString();
        return verifier;
    }
    /**
     * State-based callback for the VoiceHelper class
     */
    public interface SpeechFromTextCallback{
        void onSuccess(byte[] data);
        void onError(Exception e);
    }
}
