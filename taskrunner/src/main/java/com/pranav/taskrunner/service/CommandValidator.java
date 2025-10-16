package com.pranav.taskrunner.service;

public class CommandValidator {
    public boolean isCommandSafe(String cmd) {
        if (cmd == null) return false;
        String lowered = cmd.toLowerCase();
        String[] forbidden = { " rm ", "rm -", " sudo", "reboot", "shutdown", "mkfs", " chmod", " chown", " dd ", ">:","|","&&",";","/dev/" };
        for (String f : forbidden) {
            if (lowered.contains(f)) return false;
        }
        if (cmd.length() > 200) return false;
        return true;
    }
}
