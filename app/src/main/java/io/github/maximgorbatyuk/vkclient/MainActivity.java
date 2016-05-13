package io.github.maximgorbatyuk.vkclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.List;

import io.github.maximgorbatyuk.vkclient.database.Database;
import io.github.maximgorbatyuk.vkclient.database.IExecuteResult;
import io.github.maximgorbatyuk.vkclient.help.Audio;
import io.github.maximgorbatyuk.vkclient.help.AudioAdapter;
import io.github.maximgorbatyuk.vkclient.help.Constants;
import io.github.maximgorbatyuk.vkclient.secure.SecureData;

public class MainActivity extends AppCompatActivity {

    private ListView            listView;
    private ArrayList<Audio>    RecordList;
    private Database            database;
    private Button              loginButton;
    private int                 Position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String[] fingers = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        //System.out.println("FingerPrint = " + Arrays.asList(fingers));

        loginButton = (Button) findViewById(R.id.loginVK);
        database = new Database(this);

        listView = (ListView) findViewById(R.id.FriendList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //

                startIntent(position);
                /*player.Play(position);
                Position = position;
                showNotification("Start playing");*/
            }
        });
        callRecordsInDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        // return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.loginAction:
                showNotification("login Action");
                break;
            case R.id.loginMyMusicAction:
                showNotification("My music");
                break;
            case R.id.loadPopularAction:
                showNotification("Popular Music");
                break;
            case R.id.loadLocalAction:
                showNotification("Get local music Action");
                break;
            case R.id.syncAction:
                showNotification("Synchronize");
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                //showNotification("Success: \n" + res.accessToken);
                VKRequest request = VKApi.audio().get();
                request.executeWithListener(requestListener);
            }

            @Override
            public void onError(VKError error) {
                showNotification("Error:\n" + error.errorMessage);
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void showNotification(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private VKRequest.VKRequestListener requestListener = new VKRequest.VKRequestListener() {
        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            super.attemptFailed(request, attemptNumber, totalAttempts);
        }

        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            //VKList<VKApiAudio> list = (VKList<VKApiAudio>) response.parsedModel;
            addListToLocalDatabase((VKList<VKApiAudio>) response.parsedModel);

            //loadListView(list);
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
            super.onProgress(progressType, bytesLoaded, bytesTotal);
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
        }
    };

    private void loadListView(List<Audio> list){
        try {
            // VKList<VKApiAudio> list = (VKList<VKApiAudio>) object;
            RecordList = new ArrayList<>(0);
            for (int i = 0; i < list.size(); i++){
                RecordList.add(new Audio(
                        list.get(i).id,
                        list.get(i).title,
                        list.get(i).artist,
                        list.get(i).duration,
                        list.get(i).url,
                        list.get(i).lyrics_id
                ));
            }
            AudioAdapter adapter = new AudioAdapter(this, RecordList);
            listView.setAdapter(adapter);
        } catch (Exception ex) {
            showNotification(ex.getMessage());
        }
    }


    private void startIntent(int position){
        Intent intent = new Intent(this, PlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.AUDIO_LIST, RecordList);
        intent.putExtra(Constants.AUDIO_LIST, bundle);
        intent.putExtra(Constants.POSITION, position);
        startActivity(intent);
    }

    private void addListToLocalDatabase(VKList<VKApiAudio> list){

        final Audio[] records = new Audio[list.size()];

        for (int i = 0; i < list.size(); i++){
            records[i] = new Audio( list.get(i) );
        }
        database.addRecord(records, new IExecuteResult() {
            @Override
            public void onExecute(int result) {
                showNotification("Added " + result + " records");
                loadRecordFromDatabase();
            }

            @Override
            public void onExecute(List<Audio> list) {

            }
        });
    }

    private void loadRecordFromDatabase() {
        database.getRecordList(new IExecuteResult() {
            @Override
            public void onExecute(int result) {

            }
            @Override
            public void onExecute(List<Audio> list) {
                loadListView(list);
            }
        });
    }

    private void callRecordsInDatabase(){
        database.getRecordList(new IExecuteResult() {
            @Override
            public void onExecute(int result) {

            }
            @Override
            public void onExecute(List<Audio> list) {
                if (list.size() > 0) {
                    loadRecordFromDatabase();
                    loginButton.setVisibility(View.INVISIBLE);
                }
                else
                    loginButton.setVisibility(View.VISIBLE);
                // getVkRecords();
            }
        });
    }

    public void getVkRecords(View view) {
        VKSdk.login(this, SecureData.scope);
        loginButton.setVisibility(View.INVISIBLE);
    }
}
