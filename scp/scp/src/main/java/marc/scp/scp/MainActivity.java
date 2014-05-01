package marc.scp.scp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;
import android.view.View;

public class MainActivity extends ActionBarActivity
{
    public final static String USERNAME = "com.whomarc.scp.USERNAME";
    public final static String PASSWORD = "com.whomarc.scp.PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Called when the user clicks the connnect button
    public void connect(View view)
    {
        //this is a Context, because MainACtivity inherits from context
        Intent intent = new Intent(this, DisplayMessageActivity.class);

        //grab the strings out of the correct field
        EditText editText = (EditText) findViewById(R.id.passwordField);
        String password = editText.getText().toString();

        editText = (EditText) findViewById(R.id.usernameField);
        String username = editText.getText().toString();

        //add the correct data to the intent
        intent.putExtra(PASSWORD, password);
        intent.putExtra(USERNAME, username);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
