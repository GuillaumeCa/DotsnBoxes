# Dots and Boxes

## Fonctionnement

### Démarrage du jeu

Pour démarrer le jeu, on exécute la classe Grid. La console nous donne ensuite le choix de jouer dans la console ou en mode graphique.


### Mode console

Avant de commencer à jouer, on demande au(x) joueur(s) de renseigner certains paramètres du jeu :

1. Taille de la grille (2-3-4-5) - *Nombre de carré par ligne ou colonne.*
2. Mode de jeu (1-2) - *Le mode de jeu 1 fait jouer deux joueurs. Le mode de jeu 2 fait jouer un joueur contre un joueur artificiel.*
3. Nombre d'handicap (0-..) - *Nombre de trait en pointillé placé aléatoirement sur la grille.*
4. **Mode de jeu 2 UNIQUEMENT** : Niveau de l'IA (1-2-3)
6. Joueur commençant à jouer en premier - *1 pour premier joueur, 2 pour 2e joueur.*
5. Nom du/des joueur(s)

Ensuite le jeu commence par le premier joueur renseigné. Le joueur joue lorsque son nom s'affiche sur la console et l'invite à jouer.

Pour jouer, le joueur doit renseigner **deux points**. L'ordre des points n'a pas d'importance du moment que l'entrée est valide et qu'il n'y a pas de trait déjà présent à l'emplacement spécifié. Pour renseigner ces deux points, le joueur doit les taper dans la console séparé par une virgule : ``1,2`` avant d'appuyer sur ↩︎ pour valider.

Lorsque le jeu est terminé, le(s) joueur(s) a/ont la possibilité de rejouer en tapant le nombre 1.

### Mode graphique
Avant de commencer à jouer, on demande au(x) joueur(s) de renseigner certains paramètres du jeu :

1. Mode - *Appuyez sur les boutons pour sélectionner le mode de jeu*
  - J1 vs J2 - *Deux joueurs jouent chacun leur tour*
  - J1 vs Ordi - *Un joueur joue contre une IA*
2. Taille - *idem mode console*
  - Appuyez sur le bouton + ou - pour augmenter ou diminuer la taille.
3. Handicap - *idem mode console*
  - Fonctionnement identique à **Taille**.
4. **J1 vs Ordi UNIQUEMENT** : Difficulté - *Niveau de l'IA*
  - Fonctionnement identique à **Taille**.
5. Nom J1/ Nom J2 :
  - Cliquez sur la zone de texte du nom du joueur pour la modifier.
  - Appuyez sur la touche ⌫ pour supprimer des lettres.
6. 1er Joueur - *Appuyez sur les boutons pour sélectionner le joueur démarrant la partie*
  - J1 - *J1 commence à jouer*
  - J2 ou Ordi - *J2 ou Ordi commence à jouer*

Ensuite le jeu commence par le 1er joueur renseigné. Pour créer des lignes ou en effacer (traits en gris), il suffit de passer la souris entre deux points, une ligne de la couleur du joueur apparait alors et le joueur n'a qu'a cliquer pour créer la ligne. C'est ensuite au tour du joueur suivant.

Lorsque la partie est terminé, le(s) joueur(s) peut/peuvent recommencer une nouvelle partie en cliquant sur le bouton central.

## Description des fonctionnalitées

### IA

#### Niveau 1

- Choisi un emplacement aléatoirement

#### Niveau 2
- Cherche à fermer un carré dont il reste un emplacement vide.
- Choisi un emplacement aléatoirement sinon

#### Niveau 3
- Evite de choisir les emplacements d'un carré possédant déjà deux côtés ou possédant 3 côtés dont un en pointillé.
- Cherche à fermer un carré dont il reste un emplacement vide sinon
- Choisi un emplacement aléatoirement sinon


### Mode console

#### Vérification des champs texte
- Sélection des paramètres - *Vérification des valeurs entrées* :
  - Sont des Nombres
  - Sont parmi les valeurs autorisées
    - Cas des handicap - *valeurs autorisées comprises entre 0 et le nombre de trait en pointillé que peut contenir une grille de la taille sélectionnée*.
- Coordonnés des lignes - *Vérifications* :
  - si les coordonnés sont entré sous la bonne forme
  - si les coordonnés correspondent à une ligne valide
  - si les coordonnés correspondent à une ligne non existante

##### Remarque
  On peut entrer aucune valeur pour certains champs lors de la sélection des paramètres du jeu. Des valeurs par défaut sont alors sélectionnées (les valeurs les plus faibles des paramètres).

### Mode Graphique

#### Hover

Lorsque un joueur passe la souris à l'emplacement d'un trait et qu'il peut modifier l'emplacement, un trait de la couleur du joueur (couleur du nom en haut de la fenêtre) s'affiche.

Lorsque l'on passe la souris sur un bouton et qu'il change de couleur, cela signifie que l'on peut cliquer dessus pour effectuer une action.

#### Sélection de valeurs numériques

Lorsque l'on choisi le nombre de traits en pointillé, le sélecteur se bloque automatiquement au nombre de trait maximum pour la taille de la grille sélectionnée. Si l'on diminue la taille de la grille et que le nombre de traits choisi initialement était supèrieur au nombre maximal de traits pour la taille actuelle, le nombre de traits en pointillé se règle automatiquement sur le nombre de traits maximum de la grille actuelle.

## Auteurs
- Guillaume Carré
- Oscar Bourat
