package com.github.bdes.dailylog;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyLogsPopupAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        JButton testButton = new JButton();
        final Project project = anActionEvent.getProject();
        String logs = null;
        try {
            Path normalizedPath = Paths.get(project.getBasePath()).normalize();
            logs = execShellGetCommits(normalizedPath.toString());
        } catch (NullPointerException e) { }


        if (logs != null && logs.equals("")) {
            logs = "!!!No commits for current day";
        }
        new SampleDialogWrapper(logs).showAndGet();
    }

    private String execShellGetCommits(String path) {
        String date = getCurrentDate();
        String logString = String.format("git log --after=\"%1$s 00:00\" --before=\"%1$s 23:59\" --pretty=\"format:%%s\"", date);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", String.format("cd %1$s && %2$s", path, logString));

        try {

            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            List<String> tmp = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                tmp.add(line + ";\n");
            }


            for (int i = tmp.size() - 1; i >= 0; i--) {
                output.append(tmp.get(i));
            }

            int exitVal = process.waitFor();
            if (exitVal == 0 && !output.toString().contains("fatal")) {
                return output.toString();
            } else {
                reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                return reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return formatter.format(date);
    }
}
