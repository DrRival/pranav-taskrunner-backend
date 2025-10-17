package com.pranav.taskrunner.service;

public class CommandValidator {

    // Allow simple commands like echo, dir, ls, etc.
    public boolean isCommandSafe(String cmd) {
        if (cmd == null) return true;
        cmd = cmd.toLowerCase();

        // Whitelist simple safe commands
        if (cmd.contains("echo") || cmd.contains("dir") || cmd.contains("ls")) {
            return true;
        }

        // Block dangerous ones
        return !(cmd.contains("rm") || cmd.contains("del") || cmd.contains("shutdown")
                || cmd.contains("mkfs") || cmd.contains("curl") || cmd.contains("wget")
                || cmd.contains("powershell") || cmd.contains("cmd"));
    }
}
