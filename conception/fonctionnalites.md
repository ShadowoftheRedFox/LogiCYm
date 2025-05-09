```md
# Nom de la fonctionnalité
> **Description :**
> <br>**Etat :** "A faire" "Hypothèses" "Prêt" "Intégré"
### Hypothèses/Idées :
### Solution Choisie :
```

# Passer un signal dans le circuit
> **Description :** Quand le mode simulation est activé, comment on passe un état de porte en porte, pas à pas.
> <br>**Etat :** Hypothèses
### Hypothèses/Idées :
1. Transporter dans le signal le nombre de gate parcouru, ainsi, au pas numéro 3, tous les signaux devraient en être à leur 3 ème gate.
<br> **/!\\** Les gates qui reçoivent plusieurs signaux au même pas risque de se mettre à jour plusieurs fois. Il faudra prendre en compte le dernier changement à ce pas.

2. Faire un dictionnaire qui se vide après chaques pas, référançant les gates à mettre à jour (= regarde ses entrées et met à jour ses sorties)
```
Exemple : Dictionnaire en fonction du temp T

T=0 : {""idGate0"" : ref_Input_A, ""idGate1"" : ref_Input_B}
    ->  On commence à partir des gate input A et B.
    Ils regardent leur état/valeurs d'entrée (ici les deux sont à 1) et le propage à leurs outputs (les cables qui sont connecté).
    Ces outputs sont ajoutés au dictionnaire pour T=1.

T=1 : {""idCable0"" : ref_Cable_0, ""idCable1"" : ref_Cable_1}
    ->  Les valeurs de T=0 on été suprimés et remplacées
    Les éléments (ici les cables) vérifies leurs entrées/etat et mettent à jour leur sorties (ils passent la valeur au gate suivant)
    Les éléments ajoute les références de leurs sorties au Dictionnaire pour T=2.

T=2 : {""idGate2"" : ref_Gate_A}
    ->  Les deux cables faisaient référance au même gate (qui dans notre exemple est un gate AND)
    Celui-ci vérifie donc UNE SEULE FOIS ses entrées et modifie sa sortie
    On ajoute l'élément référencé par sa sortie au Dictionnaire pour T=3.

T=3 : c'est un cable, on référence sa sortie pour T=4.

T=4 : {""idGate0"" : ref_Output_A}
    ->  Le gate output vérifie ses entrées et change d'état(/sortie), ce qui permetra d'afficher le résultat.
```
### Solution Choisie :

# Connecter deux gate
> **Description :** Quels sont les conditions et besoin pour pouvoir connecter deux gates
> <br> **Etat :** Hypothèses
### Hypothèses/Idées :
* Il faut que les deux gates existes.
* Les ports doivent être précisés [strictement 1 input et 1 output].
* Les ports doivent avoir la même taille de bus.
* Si les ports sont déjà pris on déconnecte l'ancien câble et on branche le nouveau.
  * (si on est en ligne de commande on lui demande s'il veut remplacer par la nouvelle connexion)

```
Exemple d'utilisation :
1) On crée une instance de cable (id unique, ref gate amont, ref gate aval, taille de bus ?)
2) On reférence le cable depuis le port output (du gate en amont)
3) On reférence le cable depuis le port input (du gate en aval)
```

### Solution Choisie :

# Clock
> **Description :** Faire un gate spécial pouvant changer d'etat dans le temps suivant une fréquence
> <br>**Etat :** A faire
### Hypothèses/Idées :
### Solution Choisie :

# Detection de cycle
> **Description :** Detecter quand un signal repasse par un gate qu'il à déjà traversé. Est-ce grave ?
> <br>**Etat :** A faire
### Hypothèses/Idées :
hypothèse : ce n'est pas grave si le signal se stabilise (C'est à dire: il ne change pas de valeur à l'infini)
<br>Cependant, n'est-ce pas là le principe des clock ?

### Solution Choisie :
