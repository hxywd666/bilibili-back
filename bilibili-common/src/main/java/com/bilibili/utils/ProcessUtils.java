package com.bilibili.utils;

import com.bilibili.exception.FileErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessUtils {
    private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);

    private static final String osName = System.getProperty("os.name").toLowerCase();

    public static String executeCommand(String cmd, Boolean showLog) throws FileErrorException {
        if (StringTools.isEmpty(cmd)) {
            return null;
        }

        Runtime runtime = Runtime.getRuntime();
        Process process = null;

        try {
            // Determine OS
            if (osName.contains("win")) {
                process = Runtime.getRuntime().exec(cmd);
            } else {
                process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", cmd});
            }

            // Handle process streams
            PrintStream errorStream = new PrintStream(process.getErrorStream());
            PrintStream inputStream = new PrintStream(process.getInputStream());
            errorStream.start();
            inputStream.start();

            // Wait for process to complete
            process.waitFor();

            // Collect output
            String result = errorStream.stringBuffer.append(inputStream.stringBuffer + "\n").toString();
            if (showLog) {
                logger.info("Executed command: {}, result: {}", cmd, result);
            }

            return result;
        } catch (Exception e) {
            logger.error("Command execution failed: {}, error: {}", cmd, e.getMessage());
            throw new FileErrorException("Video conversion failed");
        } finally {
            if (process != null) {
                ProcessKiller ffmpegKiller = new ProcessKiller(process);
                runtime.addShutdownHook(ffmpegKiller);
            }
        }
    }

    private static class ProcessKiller extends Thread {
        private final Process process;

        public ProcessKiller(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            this.process.destroy();
        }
    }

    static class PrintStream extends Thread {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();

        public PrintStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try {
                if (inputStream == null) {
                    return;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
            } catch (Exception e) {
                logger.error("Error reading process stream: {}", e.getMessage());
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    logger.error("Error closing stream: {}", e.getMessage());
                }
            }
        }
    }
}