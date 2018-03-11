Instructions
===

Cet atelier est constitué de quatres exercices :

* Serveur avec SpringBoot 2
* Android
* Web avec Kotlin-JS
* Basique (sans frameworks, ni bibliothèques)

Vous allez devoir choisir la partie qui vous intéresse, sachant que le début des exercices est quasiment le même.
Vous aurez le loisir de faire les autres exercices plus tard chez vous le désirez.
L'exercice 'Basique' est prévu pour ceux qui ont des soucis de récupération des dépendences. 
Dans le doute choisissez l'exercice serveur.


Pour gagner du temps il est fortement recommandé de préparer son environnement avant l'atelier.

1. récupérer le code avec `git clone http://github.com/MonkeyPatchIo/KotlinByExample`
2. lancer `./gradlew clean assemble test` ou `./gradlew.bat clean assemble test` pour récupérer le _wrapper_ gradle, avec les plugins prérequis.
3. vérifier que vous avez bien un IDE à jour qui supporte Kotlin, le cas échéant, installer un éditeur qui fonctionne bien avec Kotlin comme [IntelliJ CE](https://www.jetbrains.com/idea/download/)
4. vérifier que vous avez la dernière version du plugin pour Kotlin. 
  Pour cela allez dans `Tools | Kotlin | Configure Kotlin Plugin Updates`, ensuite cliquer sur `Check for updates now`, si besoin faites l'installation, puis redémarrer l'IDE.
5. vérifier que vous avec un JDK 8 (attention, il y a de fort risque que ça ne fonctionne pas avec une version 9 ou 10). Si besoin utilisez [SDKMAN!](http://sdkman.io/) pour récupérer le SDK.
6. si vous opter pour l'exercice Android, vérifier que vous avez la version 27 du SDK, et [Android Studio](https://developer.android.com/studio/index.html) en version 3+

La suite sera expliquée pendant l'atelier.
