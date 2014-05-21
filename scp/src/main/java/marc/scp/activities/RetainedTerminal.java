package marc.scp.activities;

import android.app.Fragment;
import android.os.Bundle;

import marc.scp.terminal.TerminalSession;
import marc.scp.terminal.TerminalView;

/**
 * Created by Marc on 5/19/2014.
 */
public class RetainedTerminal extends Fragment {

    // data object we want to retain
    private TerminalSession terminalSession;
    private TerminalView terminalview;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    //setters
    public void setTerminalSession(TerminalSession session)
    {
        terminalSession = session;
    }

    public void setTerminalView(TerminalView view)
    {
        terminalview = view;
    }

    //getters
    public TerminalSession getTerminalSession() {
        return terminalSession;
    }

    public TerminalView getTerminalView() {
        return terminalview;
    }
}