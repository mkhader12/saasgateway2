package com.virtru.saas.workflow.utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class FileMonitorTest {

    @Test
    public void checkEventFireForNewFile() throws IOException, InterruptedException {

        final String[] receivedFile = new String[1];
        final String monitorPath = System.getProperty("user.home");
        String createdFileContent = "some text in the file" + Calendar.getInstance().getTime().getTime()+"\n";

        final String fileName = monitorPath + "/" + Calendar.getInstance().getTime().getTime();
        Runnable fileCreationRunnable = fileCreationRunnable(fileName, createdFileContent, receivedFile);

        startFileMonitoring(monitorPath, receivedFile, null);
        Thread th2 = new Thread(fileCreationRunnable, "FileCreationRunnable");
        th2.start();

        int i =0;
        while (receivedFile[0] == null) {
            Thread.sleep(2000);
            i++;
            if (i > 20) {
                fail("Unable to receive the created file");
                break;
            }
        }
        assertEquals(receivedFile[0], fileName);
        final Path receivedFilePath = Paths.get(receivedFile[0]);
        String receivedFileContent = new String(Files.readAllBytes(receivedFilePath));
        assertEquals(createdFileContent, receivedFileContent);
        Files.delete(receivedFilePath);
    }
    @Test
    public void checkEventFireForNewFileOnNewFolder() throws IOException, InterruptedException {

        final String[] receivedFile = new String[1];
        final String monitorPath = System.getProperty("user.home");
        String createdFileContent = "some text in the file" + Calendar.getInstance().getTime().getTime()+"\n";

        final String folderName = monitorPath + "/newfolder";
        final String fileName = folderName+"/" + Calendar.getInstance().getTime().getTime();
        Runnable fileCreationRunnable = fileCreationRunnable(fileName, createdFileContent, receivedFile);

        startFileMonitoring(monitorPath, receivedFile, folderName);
        Thread th2 = new Thread(fileCreationRunnable, "FileCreationRunnable");
        th2.start();

        int i =0;
        while (receivedFile[0] == null) {
            Thread.sleep(2000);
            i++;
            if (i > 20) {
                fail("Unable to receive the created file");
                break;
            }
        }
        final Path receivedFilePath = Paths.get(receivedFile[0]);
        assertEquals(receivedFile[0], fileName);
        String receivedFileContent = new String(Files.readAllBytes(receivedFilePath));
        assertEquals(createdFileContent, receivedFileContent);
        Files.delete(receivedFilePath);
        Files.delete(Paths.get(folderName));
    }

    private void startFileMonitoring(String monitorPath, String[] receivedFile, String newFolder)  {
        /*
        final FileCreateListener fileCreateListener = new FileCreateListener() {
            @Override
            public void receiveEvent(File newfile) {
                System.out.println("File =" + newfile.getAbsolutePath());
                receivedFile[0] = newfile.getAbsolutePath();
            }
        };

        AbstractFileMonitor fileMonitor = new AbstractFileMonitor(fileCreateListener){
            @Override
            public String getFilePath() {
                return monitorPath;
            }
        };

        if (newFolder != null) {
            Files.createDirectory(Paths.get(newFolder));
            fileMonitor.registerFolder(newFolder);
        }

        Runnable fileMonitorRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    fileMonitor.startMonitoring(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread th = new Thread(fileMonitorRunnable, "FileWatcherTest");
        th.start();
        */
    }

    private Runnable fileCreationRunnable(String fileName, String fileContent, String[] receivedFile) {
        return () -> {
                boolean fileCreated = false;
                while( receivedFile[0] == null) {
                    System.out.println("Waiting to receive notification about the created file");
                    try {
                        Thread.sleep(2000);
                        if (!fileCreated) {
                            createNewFileWithText(fileName, fileContent);
                            fileCreated = true;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            };
    }

    private void createNewFileWithText(String fileName, String text) throws IOException {
        File aNewFile = new File (fileName);
        BufferedWriter output = null;
        try {
            File file = new File(fileName);
            output = new BufferedWriter(new FileWriter(file));
            output.write(text);
            output.flush();
            System.out.println("Created File - " + fileName);
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( output != null ) {
                output.close();
            }
        }
    }

    public void checkEventFireForMultipleNewFiles() {

    }

}
