package com.virtru.saas.framework;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.virtru.saas.app.ApplicationContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.attribute.PosixFilePermission.GROUP_READ;
import static java.nio.file.attribute.PosixFilePermission.OTHERS_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_READ;


public abstract class FilePoller extends AbstractPoller {
    private static final Logger LOG = LogManager.getLogger(FilePoller.class);

    private WatchService fileWatcher;
    private Path monitorPath;

    public FilePoller(ApplicationContext applicationContext) throws IOException {
        super(applicationContext);
        String inputRequestPath = getFilePath();
        // Get the directory we want to watch, using the Paths singleton class
        monitorPath = Paths.get(inputRequestPath);
        if (monitorPath == null) {
            throw new UnsupportedOperationException("Directory " + inputRequestPath + " not found");
        }
        this.fileWatcher = getFileWatcher();
        registerSubFolders(fileWatcher, monitorPath);
    }

    private WatchService getFileWatcher() throws IOException {
        return FileSystems.getDefault().newWatchService();
    }

    @Override
    protected Object getPollingRequestObject() {
        WatchKey key = null;
        try {
            key = fileWatcher.take();
        } catch (InterruptedException e) {
            LOG.error(e);
        }
        if (key != null) {
            List<WatchEvent<?>> events = key.pollEvents();
            List<File> returnList = new ArrayList<>();

            // we have a polled event, now we traverse it and
            // receive all the states from it
            for (WatchEvent event : events) {

                LOG.info(String.format("Received %s event for file: %s\n",
                        event.kind(), event.context()));

                Path dir = (Path) key.watchable();
                Path fullPath = dir.resolve(event.context().toString());
                final File fileObj = new File(fullPath.toString());
                LOG.info("File Obj=" + fileObj.toString());
                if (!fileObj.isDirectory()) {
                    returnList.add(fileObj);
                } else {

                    try {
                        registerSubFolders(fileWatcher, fullPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            key.reset();
            return returnList;
        }
        return null;
    }

    public abstract String getFilePath();

    protected void registerSubFolders(WatchService watchService, final Path root) throws IOException {
        // register all subfolders
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                PosixFileAttributeView posixView = Files.getFileAttributeView(dir, PosixFileAttributeView.class);
                PosixFileAttributes posixAttr = posixView.readAttributes();

                Set<PosixFilePermission> perm = posixAttr.permissions();
                if (perm.contains(OWNER_READ) || perm.contains(GROUP_READ) || perm.contains(OTHERS_READ)) {
                    dir.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);
                    LOG.info("Registering Directory - " + dir.toString() + " - in the watch list");
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
 //   protected File getNextRequest() throws AppException {
//        File nextFile = getQueueItem();
//        if (nextFile != null) {
//            long newRequestSize = nextFile.length();
//            long maxSizeAllowed = getMaxSizeAllowed(getApplicationContext());
//            if (newRequestSize > maxSizeAllowed) {
//                String errorMsg = "Input request %s exceeded max allowed size %s";
//                throw new AppException(ErrorConstants.INPUT_REQUEST_EXCEEDED_MAX_ALLOWED_SIZE,
//                        String.format(errorMsg, newRequestSize, maxSizeAllowed));
//            } else if (getCurrentSize() + newRequestSize < maxSizeAllowed) {
//                incrementCurrentSize(newRequestSize);
//                return nextFile;
//            }
//        }
//        return null;
//    }

}
