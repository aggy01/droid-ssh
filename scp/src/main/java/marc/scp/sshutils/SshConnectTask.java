package marc.scp.sshutils;

import android.os.AsyncTask;

/**
 * Created by Marc on 5/1/14.
 */
public class SshConnectTask extends AsyncTask<SshConnection, Integer, Boolean>
{
    protected Boolean doInBackground(SshConnection... connection)
    {
        boolean ret = true;
        try
        {
            connection[0].connect();
            System.out.println(connection[0].executeCommand("ls"));
        }
        catch (Exception e)
        {
           /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("User friendly text explaining what went wrong.");
            AlertDialog alert = builder.create();
            alert.show();*/
            ret = false;
        }
        return ret;
    }

    protected void onProgressUpdate(Integer... progress)
    {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Boolean result)
    {
        //showDialog("Downloaded " + result + " bytes");
    }

}
