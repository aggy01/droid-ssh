package marc.scp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import marc.scp.constants.Constants;
import marc.scp.asyncDialogs.Dialogs;
import marc.scp.databaseutils.Database;
import marc.scp.databaseutils.FileSync;
import marc.scp.databaseutils.Preference;
import marc.scp.scp.R;

/**
 * Created by Marc on 5/15/14.
 */
public class AddFolderPair  extends Activity
{
    //singleton instances
    private Database dbInstance;
    private Dialogs dialogInstance;

    private ViewGroup contentView;
    private int selectedItemSpinner;
    private FileSync file;

    //hash of preferences and their id, used for adding folder pairs, so we don't have to query the database again
    //to find the preference object, the folder pair is linked to
    private HashMap<String, Integer> hash;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        contentView = (ViewGroup) getLayoutInflater().inflate(R.layout.add_folder_pair, null);
        dbInstance = Database.getInstance();
        dialogInstance = dialogInstance.getInstance();

        hash = new HashMap<String, Integer>();

        Button btn = (Button) contentView.findViewById(R.id.button_save);
        setupAddandEditButton(btn);

        Spinner spinner = (Spinner) contentView.findViewById(R.id.connection_list);
        populateSpinner(spinner);

        file = (FileSync)getIntent().getParcelableExtra(Constants.FILE_PARCEABLE);
        populateFields(file);

        setContentView(contentView);
        selectedItemSpinner = -1;
    }

    //helpers
    private void populateFields(FileSync file)
    {
        if(file != null)
        {
            EditText edit;
            edit = (EditText) contentView.findViewById(R.id.folderPairName);
            edit.setText(file.getName());
            edit = (EditText) contentView.findViewById(R.id.localFolderField);
            edit.setText(file.getLocalFolder());
            edit = (EditText) contentView.findViewById(R.id.remoteFolderField);
            edit.setText(file.getRemoteFolder());
        }
    }

    private void populateSpinner(Spinner spinner)
    {
        final List<Preference> preferencesList = Database.getInstance().getAllPreferences();
        if(preferencesList == null)
        {
            dialogInstance.getAlertDialog(this, "Error", "Please create a connection first", true);
            finish();
            return;
        }
        ArrayList<String> prefList = new ArrayList<String>();

        for (Preference pr: preferencesList)
        {
            if(pr != null)
            {
                prefList.add(pr.getName());
                hash.put(pr.getName(), pr.getId());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, prefList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String host = (String)parent.getItemAtPosition(pos);
                selectedItemSpinner = hash.get(host);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
                String host = (String)parent.getItemAtPosition(0);
                selectedItemSpinner = hash.get(host);
            }
        });
    }

    private FileSync errorCheckInput()
    {
        EditText edit;
        FileSync ret = null;
        Activity activity = this;

        edit = (EditText) contentView.findViewById(R.id.folderPairName);
        String name = edit.getText().toString();
        if(dialogInstance.toastIfEmpty(name, activity, "You did not enter a folder pair name."))
        {
            return ret;
        }

        edit = (EditText) contentView.findViewById(R.id.localFolderField);
        String localFolder = edit.getText().toString();
        if(dialogInstance.toastIfEmpty(localFolder, activity, "You did not enter a local folder."))
        {
            return ret;
        }
        File test = new File(localFolder);
        if(!test.exists())
        {
            dialogInstance.makeToast(activity, "Local folder does not exit");
            return ret;
        }
        edit = (EditText) contentView.findViewById(R.id.remoteFolderField);
        String remoteFolder = edit.getText().toString();
        if(dialogInstance.toastIfEmpty(remoteFolder, activity, "You did not enter a remote folder."))
        {
            return ret;
        }

        if(selectedItemSpinner == -1)
        {
            dialogInstance.makeToast(activity, "You did not select a connection to sync associate this folder sync with.");
            return ret;
        }
        ret = new FileSync(name, selectedItemSpinner, localFolder, remoteFolder);
        return ret;
    }

    //setup buttons
    private void setupAddandEditButton(Button btn)
    {
        final Activity activity = this;
        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                FileSync fileInput = errorCheckInput();
                if((file != null) && (fileInput != null))
                {
                    fileInput.setId(file.getId());
                    updateFile(fileInput);
                    finish();
                }
                else if(fileInput != null)
                {
                    addFile(fileInput);
                    finish();
                }

            }
        });
    }

    //database access
    private void addFile(FileSync fileSync)
    {
        dbInstance.addFileSync(fileSync);
    }

    private void updateFile(FileSync fileSync)
    {
        dbInstance.updateFile(fileSync);
    }

}
