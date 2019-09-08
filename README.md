Kotlin by Example
===

Pré-requis
---

### JDK 8
  
<!> le build ne fonctione pas avec JDK 9 :'(

Pour installer le JDK , ou changer rapidement de version vous pouvez utilisez [SDKMAN!](http://sdkman.io/)
  
### Votre IDE préféré
 
[Android Studio](https://developer.android.com/studio/index.html) nécessaire pour l'exercice Android

[IntelliJ IDEA](https://www.jetbrains.com/idea/)

[Eclipse](http://www.eclipse.org/) est aussi supporté officielment, [voir](https://kotlinlang.org/docs/tutorials/getting-started-eclipse.html)

<https://kotlinlang.org/docs/reference/faq.html#what-ides-support-kotlin>

### Pré-requis pour la partie Android

[Android Studio](https://developer.android.com/studio/index.html) requis pour l'exercide Android, version > 3.0

Android SDK version `27`

Vous pouvez configurer le répertoire du SDK Android en créant un fichier `local.properties` comme ceci :

```properties
sdk.dir=/path/to/Android/sdk
```

Water Pouring Puzzle (Problème de transvasement)
---

[Water Pouring Puzzle](https://en.wikipedia.org/wiki/Water_pouring_puzzle)

Build
---

Lancer à la racine du projet :

```bash
./gradlew clean assemble

```

La première construction nécessite une connection Internet, et peut prendre pas mal de temps (beaucoup de téléchargement)

Tests
---

Lancer à la racine du projet :

```bash
./gradlew test

```

Lancement
---

### Server

Si le serveur à bien été construit, vous pouvez le lancer avec :

```bash
java -jar lib/build/libs/server.jar
```

Ensuite ouvrez votre navigateur sur <http://localhost:8080/>

Quelques exemples avec une solution
---

0/5, 0/3 vers 4/5, 0/3

0/8, 0/5 vers 6/8, 0/5

0/7, 0/5 vers 4/7, 0/5

0/9, 0/4 vers 6/9, 0/4

0/11, 0/3 vers 7/11, 0/3

0/11, 0/7 vers 2/11, 0/7

12/12, 0/8, 0/5 vers 6/12, 6/8, 0/5

0/24, 0/13, 0/11, 0/5 vers 6/24, 6/13, 6/11, 0

API REST
---

```
POST http://localhost:8080/api/solve
Content-Type: application/json

{
  "first": [
      { "capacity": 9, "current": 0},
      { "capacity": 4, "current": 0}
  ],
  "second": [
      { "capacity": 9, "current": 6},
      { "capacity": 4, "current": 0}
  ]
}
```

Si c'est OK, la réponse est:

```json
[
  { "type": "Fill", "index": 0 },
  { "type": "Pour", "from": 0, "to": 1 },
  { "type": "Empty", "index": 1 },
  { "type": "Pour", "from": 0, "to": 1 },
  { "type": "Empty", "index": 1 },
  { "type": "Pour", "from": 0, "to": 1 },
  { "type": "Fill", "index": 0 },
  { "type": "Pour", "from": 0, "to": 1 },
  { "type": "Empty", "index": 1 }
]
```

Ou une erreur `4xx` sera retourné en cas d'erreur, le corps de la réponse contiendra une description sommaire du problème.

Contenu
---

```bash
.
├── gradle: le répertoire du wrapper gralde
├── mobile: le sous projet android (voir ci-dessous)
├── server: le sous-project server (voir ci-dessous)
├── shared: le sous-project shared (voir ci-dessous)
├── shared-json: le sous-project shared-json (voir ci-dessous)
├── web: le sous-project web (voir ci-dessous)
├── README_fr.md: ce fichier
├── build.gradle: la configuration du build gradle
├── settings.gradle: la configuration de gradle
├── gradle.properties: la configuration des propriétés de gradle
├── gradlew: lance le wrapper gradle sur GNU/Linux ou OSX
└── gradlew.bat: lance le wrapper gradle sur Windows
```

### shared

Le code partagé entre les autres partie.

Contient la définition de `Glass`, `State`.

### shared-json

Le code partagé pour la serialization/deserialization de Jackson.
Ce code est utilisé par la partie mobile et la partie serveur.

### mobile (Android)

Racine de l'exercice Android.

### server (SpringBoot 2.0)

Racine de l'exercice SpringBoot.

### Web (browser)

Racine des exercices Android, Serveur, Web.
