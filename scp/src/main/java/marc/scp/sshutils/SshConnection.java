package marc.scp.sshutils;

import com.jcraft.jsch.*;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;
import jackpal.androidterm.emulatorview.TermSession;

import java.io.File;

import android.util.Log;

public class SshConnection  extends TermSession
{
    private SessionUserInfo sessionUserInfo;

    //jsch session
    private Session session;
    private Channel channel;
    boolean connected;

    private final String log = "SshConnection";

    public boolean isConnected()
    {
        return connected;
    }
    public SshConnection(SessionUserInfo user)
    {
        JSch jsch = new JSch();
        connected = false;
        channel = null;
        try
        {
            session = jsch.getSession(user.getUser(), user.getHost(), 22);
        }
        catch(Exception e)
        {
            System.out.println(e);
            session = null;
            //log error here
        }
        session.setUserInfo(user);
    }

    public void disableHostChecking()
    {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
    }

    public void setTerm(InputStream termIn, OutputStream termOut)
    {
        setTermIn(termIn);
        setTermOut(termOut);
    }

    public boolean connect()
    {
        boolean ret = true;
        try
        {
            if(session != null)
            {
                session.connect();
                connected = true;
            }
            Log.d(log, "SSH Connected");
        }
        catch(Exception  e)
        {
            Log.d(log, e.getMessage());
            ret = false;
            connected = false;
        }
        return ret;
    }

    public String executeCommand(String command)
    {
        String ret = null;
        if(!connected)
        {
            return ret;
        }
            try
            {
                //open channel ready to send input
                channel = session.openChannel("exec");
                ((ChannelExec)channel).setCommand(command);
                 channel.setInputStream(null);
                InputStream in = channel.getInputStream();
                OutputStream out = channel.getOutputStream();
                setTermIn(in);
                setTermOut(out);
                 channel.connect();

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                while ((line = br.readLine()) != null)
                {
                    stringBuilder.append(line + "\n");// append newline
                }

                //in.close();
                br.close();

                if (stringBuilder.length() > 0)
                {
                    ret = stringBuilder.toString();
                }
                else
                {
                    ret = "...\n";
                }
            }
            catch(Exception  e)
            {
                Log.d(log, e.getMessage());
                connected = false;
            }
            channel.disconnect();
        return ret;
    }

    boolean sendFile(File[] files, SftpProgressMonitor monitor)
    {
        boolean ret = false;
        if(!connected)
        {
            return ret;
        }
        try
        {
            //open channel ready to send input
            channel = session.openChannel("sftp");
            channel.setInputStream(null);

            channel.connect();
            ChannelSftp sftp = (ChannelSftp)channel;
            for (File file : files) {

                try
                {
                    sftp.put(file.getPath(), file.getName(), monitor, ChannelSftp.APPEND);
                }
                catch (SftpException e)
                {
                    e.printStackTrace();
                }
                ret = true;
            }
        }
        catch(Exception e)
        {

        }
        channel.disconnect();
        return ret;
    }

    @Override //called when data is processed from the input stream
    public void processInput(byte[] buffer, int offset, int count)
    {
        Log.d(log, "Input Stream " + Arrays.toString(buffer));
    }

 }

