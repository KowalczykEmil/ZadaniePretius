import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.*;
import java.util.*;

public class FileOrganizer {
    private static final String HOME_DIR = "HOME";
    private static final String DEV_DIR = "DEV";
    private static final String TEST_DIR = "TEST";
    private static int count;


    public static void main(String[] args) {
        createDirs();  // Tworzy strukturę katalogów
        watchHomeDir(); //Nasłuchuje folder HOME, w którym umieszczamy pliki *.jar albo *.xml
        saveCount(); // Zapisuje liczbę przeniesionych plików w pliku count.txt
    }
    private static void createDirs() {                                                              /** Metoda statyczna, tworząca strukturę katalogów */
        try {
            Files.createDirectory(Paths.get(HOME_DIR));
            Files.createDirectory(Paths.get(DEV_DIR));
            Files.createDirectory(Paths.get(TEST_DIR));
        } catch (IOException e) {
            System.out.println("Podczas tworzenia katalogów wystąpił błąd" + e.getMessage());
        }
    }

    private static void watchHomeDir() {                                                                /** Metoda statyczna, nasłuchująca katalog HOME */
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path homeDir = Paths.get(HOME_DIR);
            homeDir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path file = homeDir.resolve((Path) event.context());
                    if (Files.isRegularFile(file)) {
                        moveFile(file);
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Podczas tworzenia katalogu HOME, wystąpił błąd  " + e.getMessage());
        }
    }

    private static void moveToDir(Path file, String dir) {                                                  /** Metoda statyczna, przenosząca plik do katalogu */
        try {
            Path destination = Paths.get(dir + "/" + file.getFileName());
            Files.move(file, destination, StandardCopyOption.REPLACE_EXISTING);
            count++;
            saveCount();
        } catch (IOException e) {
            System.out.println("Podczas przenoszenia pliku wystąpił błąd " + file.getFileName() + ": " + e.getMessage());
        }
    }

    private static void moveFile(Path file) {                                                               /** Metoda statyczna, przenosząca plik, wykorzystuje metodę moveToDir, która określa katalog" */
        String fileName = file.getFileName().toString();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        try {
            BasicFileAttributeView attributes = Files.getFileAttributeView(file, BasicFileAttributeView.class);
            BasicFileAttributes basicAttributes = attributes.readAttributes();
            FileTime creationTime = basicAttributes.creationTime();
            LocalDateTime creationDateTime = LocalDateTime.ofInstant(creationTime.toInstant(), ZoneId.systemDefault());
            int hour = creationDateTime.getHour();
            if (extension.equals("jar")) {
                if (hour % 2 == 0) {                    //Sprwadza, pierwszy podpunkt, czy godzina utworzenia jest parzysta, wówczas przenosimy do folderu DEV
                    moveToDir(file, DEV_DIR);
                } else {
                    moveToDir(file, TEST_DIR);          //Drugi podpunkt, jeśli nie jest parzysta, trafia do folderu TEST
                }
            } else if (extension.equals("xml")) {
                moveToDir(file, DEV_DIR);               //Trzeci podpunkt, plik z *.xml trafia do folderu DEV
            }
        } catch (IOException e) {
            System.out.println("Podczas przenoszenia pliku, wystąpił błąd " + fileName + ": " + e.getMessage());
        }
    }

    private static void saveCount() {                                                                   /** Zapisuję liczbę przenoszonych plików do pliku count.txt, który znajduje się w folderze HOME */
        try {
            Path countFile = Paths.get(HOME_DIR + "/count.txt");
            if (!Files.exists(countFile)) {
                Files.createFile(countFile);
            }
            String countString = Integer.toString(count);
            Files.write(countFile, countString.getBytes());
        } catch (IOException e) {
            System.out.println("Podczas zapisywania liczby przenoszonych plików wystapił błąd " + e.getMessage());
        }
    }
}
