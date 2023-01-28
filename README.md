# ZadaniePretius

## Jak zbudować projekt?
1. Włączamy projekt w IDE (w moim przypadku InteliJ Idea)
2. Używamy terminala spod poziomu projektu (\src\main\java), wpisujemy: "javac FileOrganizer.java" (tworzy to FileOrganizer.class)
3. Następnie po stworzeniu pliku (FileOrganizer.class) uruchamiamy go poleceniem: java FileOrganizer
4. Te polecenia zbudują nam projekt, stworzą strukturę katalogów (Katalogi znajdują się pod scieżka: /src/main/java). 
5. Po uruchomieniu programu i stworzeniu struktury - można wrzucać do folderu HOME, pliki z rozszerzeniem: *jar oraz *xml, one wówczas będą 
przenoszone do folderów DEV oraz TEST, zgodnie z poleceniem.

# Ważne! 
W repozytorium są już utworzone pliki, więc żeby program poprawnie się uruchomił (java FileOrganizer) - należy najpierw usunąć strukturę folderów (HOME, TEST, DEV)
