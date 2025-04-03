# Projet Java IHM CY Tech 2025

Ceci est le repository du projet de la matière Java IHM CY Tch de l'année 2025.

# Sommaire

- [Projet Java IHM CY Tech 2025](#projet-java-ihm-cy-tech-2025)
- [Sommaire](#sommaire)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Architecture](#architecture)
- [Informations](#informations)

# Installation

Les logiciels nécéssaire sont:  
- javac v21.0.6  
- openjdk v21.0.6  
- Maven 3.8.7  

Recommendé:  
- JavaFX Scene Builder  

# Utilisation

Il suffit de lancer le fichier [run.sh](/run.sh). L'application se compilera et se lancera d'elle même.  

# Architecture

Le code source se trouve sous le nom de paquet "org.openjfx". [App.java](/src/main/java/org/openjfx/App.java) est le point d'entré de JavaFX, et [Main.java](/src/main/java/org/openjfx/src/Main.java) celui du projet.  

La structure se découpe en module, et chaque scene est associée à un fichier fxml du même nom dans [resources](/resources/). Il est donc fortement recommendé d'utiliser **JavaFX Scene Builder**.  

# Informations

Si jamais quelque chose casse avec FMXL, regardez [cet exemple](https://github.com/zonski/hello-javafx-maven-example/tree/master).  