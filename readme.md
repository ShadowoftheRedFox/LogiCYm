# Projet Java IHM CY Tech 2025: CYrcuits

Ceci est le repository du projet de la matière Java IHM CY Tech de l'année 2025.

# Sommaire

- [Projet Java IHM CY Tech 2025: CYrcuits](#projet-java-ihm-cy-tech-2025-cyrcuits)
- [Sommaire](#sommaire)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Architecture](#architecture)
- [Informations](#informations)

# Installation

Les logiciels nécéssaires sont:  
- javac v21.0.6  
- openjdk v21.0.6  
- Maven 3.8.7  

Recommandé:  
- JavaFX Scene Builder  

# Utilisation

Le fichier [clean.sh](./clean.sh) est utilisé pour compiler et (re)lancer le projet à partir de 0.  

Le fichier [run.sh](/run.sh) ne compilera que les fichiers ayant changés et lancera l'application.  

Le fichier [javadoc.sh](/run.sh) va générer la documentation du projet dans [./target/reports/apidocs](./target/reports/apidocs/).  

# Architecture

Le code source se trouve sous le nom de paquet "com.pjava". [App.java](/src/main/java/org/openjfx/App.java) est le point d'entré de JavaFX.  

Les test unitaires se font automatiquement au lancement du projet, les tests étant dans [ici](./src/test/java/com/pjava/).  

La structure se découpe en module, et chaque scene est associée à un fichier fxml du même nom dans [resources](/resources/). Il est donc fortement recommendé d'utiliser **JavaFX Scene Builder**.  

Un `SceneManager manager` est présent dans chaque controlleur, il permet de faire un changement de scène global de façon simplifié.
Chaque [controllers](./src/main/java/com/pjava/controllers/) va le demander à l'initialisation.  

Les noms des controlleurs sont <u>**Tâche**Contoller.java</u>, dans le [dossier des controllers](./src/main/java/com/pjava/controllers/), associé à un fichier fxml <u>**Tâche**.fxml</u>, dans le [dossier des fxml](./src/main/resources/fxml/), ainsi qu'à un fichier <u>**Tâche**.css</u>, dans le [dossier des styles](./src/main/resources/styles/).  

Les fichiers dans [src](./src/main/java/com/pjava/src/) servent de contenus interne (tel que le SceneManager précédemment cité), et la logique des portes (dans [components](./src/main/java/com/pjava/src/components/)).  

# Informations

Nous supposons ici que tout les [controllers](./src/main/java/com/pjava/controllers/) fonctionnent en mode **fx:root**, d'où la possibilité d'utiliser le SceneManager sans trop de soucis.  

Le fichier [styles.css](./src/main/resources/styles/styles.css) est global à toute l'application, et est <span style="text-decoration-style: dotted;text-decoration-line: underline;" title="Pas encore implémenté">importé dans les autres fichiers css</span>.  

Les [fichiers fxml](./src/main/resources/fxml/) sont pour la plupart fait de façon semi automatique avec scène builder (voir [Installation](#installation)), d'où le manque, voir l'inexistance, de commentaires.  

Le fichier [run.sh](./run.sh) est présent originellement pour faciliter le lancement du projet, car la commande n'est pas forcément intuitive, voir [Architecture](#architecture).  
