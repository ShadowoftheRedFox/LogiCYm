# Projet Java IHM CY Tech 2025: CYrcuits

Ceci est le repository du projet de la matière Java IHM CY Tch de l'année 2025.

# Sommaire

- [Projet Java IHM CY Tech 2025: CYrcuits](#projet-java-ihm-cy-tech-2025-cyrcuits)
- [Sommaire](#sommaire)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Architecture](#architecture)
- [Informations](#informations)
- [TODO](#todo)

# Installation

Les logiciels nécéssaire sont:  
- javac v21.0.6  
- openjdk v21.0.6  
- Maven 3.8.7  

Recommendé:  
- JavaFX Scene Builder  

# Utilisation

Le ficheir [clean.sh](./clean.sh) est utilisé pour compiler et lancer le projet à partir de 0.  

Le fichier [run.sh](/run.sh) ne compilera que les fichiers ayant changés et lancera l'application.  

# Architecture

Le code source se trouve sous le nom de paquet "cytech.pjava". [App.java](/src/main/java/org/openjfx/App.java) est le point d'entré de JavaFX, et [Main.java](/src/main/java/org/openjfx/src/Main.java) celui du projet.  

La structure se découpe en module, et chaque scene est associée à un fichier fxml du même nom dans [resources](/resources/). Il est donc fortement recommendé d'utiliser **JavaFX Scene Builder**.  

Un `SceneManager manager` est présent dans chaque controlleur, il permet de faire un changement de scène global de façon simplifié.
Chaque [controllers](./src/main/java/com/pjava/controllers/) va le demander à l'initialisation.  

Les noms des controlleurs sont <u>**Tâche**Contoller.java</u>, dans le [dossier des controllers](./src/main/java/com/pjava/controllers/), associé à un fichier fxml <u>**Tâche**.fxml</u>, dans le [dossier des fxml](./src/main/resources/fxml/), ainsi qu'à un fichier <u>**Tâche**.css</u>, dans le [dossier des styles](./src/main/resources/styles/).  

Les fichiers dans [src](./src/main/java/com/pjava/src/) servent de contenus interne (tel que le SceneManager précédemment cité).  

# Informations

Je suppose ici que tout les [controllers](./src/main/java/com/pjava/controllers/) fonctionnent en mode **fx:root**, d'où la possibilité d'utiliser le SceneManager sans trop de soucis.  

Le fichier [styles.css](./src/main/resources/styles/styles.css) est global à toute l'application, et est <span style="text-decoration-style: dotted;text-decoration-line: underline;" title="Pas encore implémenté">importé dans les autres fichiers css</span>.  

Les [fichiers fxml](./src/main/resources/fxml/) sont pour la plupart fait de façon semi automatique avec scène builder (voir [Installation](#installation)), d'où le manque, voir l'inexistance, de commentaires.  

Le fichier [run.sh](./run.sh) est présent originellement pour faciliter le lancement du projet, car la commande n'est pas forcément intuituve. Il permattra dans le futur de <span style="text-decoration-style: dotted;text-decoration-line: underline;" title="Pas encore implémenté, et même juste une idée">faire des vérifications avant le lancement de la compilation</span>.  

# TODO

- [ ] : Demander au prof ce que l'on doit faire pour le pas a pas (un délai lors de la propagation du signal, ou une erreur comme logisim)  
- [ ] : demander à quoi corresponde masse et l'alimentation spécifiquement