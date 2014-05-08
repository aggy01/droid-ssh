package marc.scp.scp;
import marc.scp.sshutils.*;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.MenuInflater;
import android.widget.TextView;

public class DisplayMessageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add buttons
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String username = intent.getStringExtra(MainActivity.USERNAME);
        String password = intent.getStringExtra(MainActivity.PASSWORD);
        String hostname = intent.getStringExtra(MainActivity.HOSTNAME);
        int port = Integer.parseInt(intent.getStringExtra(MainActivity.PORT));

        SessionUserInfo user = new SessionUserInfo(hostname, username, password, port);
        SshConnection connection = new SshConnection(user);
        connection.disableHostChecking();
        SshConnectTask task = new SshConnectTask(this, "ls");
        task.execute(connection);
    }

    public void result(String res)
    {
        //construct ssh object and try to connect
        //Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(res);

        // Set the text view as the activity layout
        setContentView(textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //when user clicks on action bar item
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            //openSaved();
            return true;
        }
        else if (id == R.id.action_saved)
        {
            //openSettings();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

}
