## Инструкция по сборке

1. Склонировать репозиторий.
2. Создать папку build в папке с репозиторием:

Linux:
```
mkdir build
```
Windows:
```
md build
```
3. Скомпилировать программу:
```
javac -d build FileParser.java
```
4. Перейти в папку build:
```
cd build
```
5. Создать jar файл:
```
jar --create --file file_parser.jar --main-class FileParser *
```

## Инструкция по запуску

В коммандной строке/терминале ввести:
```
java -jar file_parser.jar [ОЦПИИ]... [ФАЙЛЫ]...
```

## Используемая версия JDK:
openjdk 25.0.2 2026-01-20 LTS

OpenJDK Runtime Environment Temurin-25.0.2+10 (build 25.0.2+10-LTS)

OpenJDK 64-Bit Server VM Temurin-25.0.2+10 (build 25.0.2+10-LTS, mixed mode, sharing)
