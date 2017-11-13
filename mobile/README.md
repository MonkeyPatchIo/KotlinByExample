Kotlin par l'exemple - Android
===

Cette application est écrite en Kotlin pour Android.
Son objectif est de fournir une interface web pour résoudre le [problème de transvasement](https://en.wikipedia.org/wiki/Water_pouring_puzzle).

Le serveur est déjà écrit, il expose l'API REST suivante:

```HTTP
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

Et doit répondre un JSON comme ceci :

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

> Pour lancer le seveur, il suffit de faire `java -jar repo/libs/server.jar`

Pré-requis
---

* JDK 8 (Ne marche pas avec Java 9)
* [Android Studio 3](https://developer.android.com/studio/index.html)
* Plugin Kotlin (Tools/Kotlin/Configure Kotlin Plugin Updates): Preview 1.2
* Android SDK 27 pour le build

Instructions
---

Si vous débutez en Kotlin, vous devriez au moins regarder 
[basic Syntax](https://kotlinlang.org/docs/reference/basic-syntax.html) et
[idioms](https://kotlinlang.org/docs/reference/idioms.html).

Vous devez implémenter les méthodes qui utilise `TODO("x.y")`, ou faire quelque chose la ou il y a `// TODO x.y`.

Pour vous guider, lancer les tests qui vont vous permettre de valider votre implémentation.
Pour lancer les tests dans votre terminal : `./gradlew test`, mais il est plutôt recommandé de lancer les tests depuis l'IDE.

N'hesitez pas à regarder la [documentation de Kotlin](https://kotlinlang.org/docs/reference/), si vous en avez le besoin.

Cet exercice est constitué de 3 étapes :

* (_1_Glass) Implémentation des 'extensions methods' de `Glass` 
* (_2_Solve) Appel du solver
* (_3_Android) Android

Note: vous n'avez pas besoin de modifier les tests, mais vous pouvez les regarder pour mieux comprendre les besoins.


### (_1_Glass) Implémentez les extensions de `Glass`

Vous devez enrichir les métodes de la classe `Glass` avec des extensions, voir [Kotlin extension methods](https://kotlinlang.org/docs/reference/extensions.html).

#### (1.1) `Glass::remainingVolume`

Implémentez `fun Glass.remainingVolume()` dans le fichier `Glass+Ext.kt`.

Pour un `Glass(capacity = 10, current = 8)` le volume restant doit être `2` (10 - 8).

Une implémentation lisible peut être fait en une expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !

#### (1.2) `Glass::filled`

Implémentez `fun Glass.filled()` dans le fichier `Glass+Ext.kt`.

Pour un `Glass(capacity = 10, current = 8)`, le pourcentage de remplissage est de `0.8`.

Une implémentation lisible peut être fait en une expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !

#### (1.3) `Glass::sized`

Implémentez `fun Glass.sized()` dans le fichier `Glass+Ext.kt`.

Pour les animations on veut calculer le pourcentage par rapport à la taille maximum.
Cette taille maximum est définit dans le singleton `Configuration`.

Pour un `Glass(capacity = Configuration.maxCapacity)`, le pourcentage est de `1.0`.

Une implémentation lisible peut être fait en une expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !

#### (1.4) `Glass::empty` with `copy`

Implémentez `fun Glass.empty()` dans le fichier  `src/main/kotlin/io/monkeypatch/talks/waterpouring/model/Glass+Ext.kt`.

Utilisez la méthode `Glass::copy`. La classe `Glass` contient cette méthode car c'est une `data class`.
Voir [Copying](https://kotlinlang.org/docs/reference/data-classes.html#copying).

Une implémentation lisible peut s'écrire en une seule expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !

#### (1.5) `Glass::fill` avec `copy`

Implémentez `fun Glass.fill()` dans le fichier `Glass+Ext.kt`.

Utilisez la méthode `Glass::copy`. La class `Glass` contient cette méthode car c'est une `data class`.
Voir [Copying](https://kotlinlang.org/docs/reference/data-classes.html#copying).

Une implémentation lisible peut être fait en une expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !

#### (1.6) `Glass::minus`

Implémentez `fun Glass.minus(value: Int)` dans le fichier `Glass+Ext.kt`.

Utilisez la méthode `Glass::copy`. La class `Glass` contient cette méthode car c'est une `data class`.
Voir [Copying](https://kotlinlang.org/docs/reference/data-classes.html#copying).

Nous souhaitons pouvoir écrire ce code : `Glass(capacity = 12, current = 6) - 4`.
Voir [Operators](https://kotlinlang.org/docs/reference/java-interop.html#operators)

ATTENTION, lorsqu'on retire une quantité supérieur au contenu du verre, il doit être vide, donc `(Glass(capacity = 12, current = 6) - 10) == Glass(capacity = 12, current = 0)`

Une implémentation lisible peut être fait en une expression sans `if-then-else` ni `Math.max`.
L'utilisation du `if-then-else` ou de `Math.max` est acceptable, mais pour écrire du code plus malin, recherchez  `coerce` dans la bibliothèque standard.

NOTES:
  - NE CHANGEZ PAS le fichier `Glass.kt` !
  - l'__operator__ ne peux pas facilement être testé en web

#### (1.7) `Glass::plus`

Implémentez `fun Glass.plus(value: Int)` dans le fichier `Glass+Ext.kt`.

Utilisez la méthode `Glass::copy`. La class `Glass` contient cette méthode car c'est une `data class`.
Voir [Copying](https://kotlinlang.org/docs/reference/data-classes.html#copying).

Nous souhaitons pouvoir écrire ce code : `Glass(capacity = 12, current = 6) + 4`.
Voir [Operators](https://kotlinlang.org/docs/reference/java-interop.html#operators)

ATTENTION, si vous ajouter une quantité supérieur à la contenance du verre, il devient rempli, donc `(Glass(capacity = 12, current = 6) + 10) == Glass(capacity = 12, current = 12)`

Une implémentation lisible peut être fait en une expression sans `if-then-else` ni `Math.min`.
L'utilisation du `if-then-else` ou de `Math.min` est acceptable, mais pour écrire du code plus malin, recherchez  `coerce` dans la bibliothèque standard.

NOTES:
  - NE CHANGEZ PAS le fichier `Glass.kt` !
  - l'__operator__ ne peux pas facilement être testé en web

### (_2_Solve) Appel du solver

On va maintenant implémenter quelques méthode pour la résolution du problème.

#### (2.1) Appel serveur

Compléter le fichier `SolverInterface.kt`.

Vous devez corriger la propriété [lazy](https://kotlinlang.org/docs/reference/delegated-properties.html#lazy) `private val solverInterface: SolverInterface`.

Vous devez utiliser la bibliothèque [Retrofit 2](http://square.github.io/retrofit/) pour créer une instance de `SolverInterface`.

#### (2.2) Implémentez `State::move` avec `when`

Implémentez `fun State.move(move: Move): State` dans le fichier `State+Ext.kt`.

Pour chaque `Move` on veut afficher l'état correspondant, on doit donc implémenter cette méthode utilitaire.
Vous devez créer un nouveau `State` (soit une `List<Glass>`) en appliquant le `Move` a un état courant.

Vous devez utiliser un [`when`](https://kotlinlang.org/docs/reference/control-flow.html#when-expression) pour  déterminer le type de déplacement.
Regardez également le [smart cast](https://kotlinlang.org/docs/reference/typecasts.html#smart-casts).

En Kotlin `when`, `if-then-else` sont des expressions (i.e. retourne une valeur).

Vous avez précédemment implémenté les méthodes `Glass::empty`, `Glass::fill`, `Glass::remainingVolume`, `Glass::plus`, et `Glass::minus`.

Une implémentation lisible peut être fait en une grosse dizaine de lignes.

Aide: vous pouvez utiliser `mapIndexed`, puis un `when` pour appliquer chaque spécificité des déplacements (si nécessaire), le cas `Pour` nécessite un `when` supplémentaire ou bien une expression `if-elseif-else`.

#### (2.3) Implémentez `List<Move>::solutionList`

Implémentez `List<Move>.toSolutionList(initialState: State): List<Pair<Move?, State>>` dans le fichier `State+Ext.kt`.

Nous allons construire une amimation pour afficher les étapes du résultat.
Pour cela il faut construire une `List<Pair<Move?, State>>`, voici les règles :

- un premier élément qui est une [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html) de `null` et de l'état initial
- puis chaque `Pair` sont composés du déplacement suivant et de l'état résultant pour résoudre le problème.

Aide: vous pouvez utiliser la méthode `fold` de `List`

### (_3_Android) Android

Ici plus de tests pour vous guider, mais vous devez finir les `TODO` restant