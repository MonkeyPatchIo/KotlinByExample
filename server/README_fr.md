Kotlin par l'exemple - Serveur
===

Cette application est écrite en Kotlin, elle utilise SpringBoot 2.
Son objectif est de fournir l'API REST de résolution du [problème de transvasement](https://en.wikipedia.org/wiki/Water_pouring_puzzle).

Pour une requête HTTP comme ceci:

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

Pré-requis
---

* JDK 8 (Ne marche pas avec Java 9)
* un éditeur qui fonctionne bien avec Kotlin comme [IntelliJ CE](https://www.jetbrains.com/idea/download/)


Construction
---

Pour construire le serveur, faire:

```bash
./gradlew clean assemble
```

Vous pouvez ensuite le lancer avec `java -jar server/build/libs/server.jar`

Instructions
---

Si vous débutez en Kotlin, vous devriez au moins regarder 
[basic Syntax](https://kotlinlang.org/docs/reference/basic-syntax.html) et
[idioms](https://kotlinlang.org/docs/reference/idioms.html).

Vous devez implémenter les méthodes qui utilise `TODO("x.y")`, ou faire quelque chose la ou il y a `// TODO x.y`.

Pour vous guider, lancez les tests qui vont vous permettre de valider votre implémentation.
Pour lancer les tests dans votre terminal avec NodeJS : `./gradlew test`, mais il est plutôt recommandé de lancer les tests dans l'IDE en construisant un lanceur JUnit sur le projet *server* comme cela: ![JUnit launcher](doc/Test_JUnit.png)

Les tests sont écrits en [JUnit 5](http://junit.org/junit5/)

N'hesitez pas à regarder la [documentation de Kotlin](https://kotlinlang.org/docs/reference/), si vous en avez le besoin.

Cet exercice est constitué de 3 étapes :

* (1_Glass) Implémentation des 'extensions methods' de `Glass`
* (2_Solver) Implémentation du `TailRecursiveSolver`
* (3_Application) Implémentation de `ServerApplication` (Partie Spring)

Vous êtes libre de remplacer la dernière partie par votre micro-framework favorit: [SparkJava](http://sparkjava.com/), [Ktor](http://ktor.io/index.html), [Vert.x](http://vertx.io/) ...

Note: vous n'avez pas besoin de modifier les tests, mais vous pouvez les regarder pour mieux comprendre les besoins.

### (1_Glass) Implémentation des 'extensions methods' de `Glass`

Les implémentations doivent être faites avec les [extension de méthode de Kotlin](https://kotlinlang.org/docs/reference/extensions.html).

#### (1.1) `Glass::isEmpty`

Implémentez `fun Glass.isEmpty()` dans le fichier `src/main/kotlin/io/monkeypatch/talks/waterpouring/model/Glass+Ext.kt`.

Une implémentation lisible peut s'écrire en une seule expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !

#### (1.2) `Glass::isFull`

Implémentez `fun Glass.isFull()` dans le fichier  `src/main/kotlin/io/monkeypatch/talks/waterpouring/model/Glass+Ext.kt`.

Une implémentation lisible peut s'écrire en une seule expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !

#### (1.3) `Glass::remainingVolume`

Implémentez `fun Glass.remainingVolume()` dans le fichier `src/main/kotlin/io/monkeypatch/talks/waterpouring/model/Glass+Ext.kt`.

Pour un `Glass(capacity = 10, current = 8)` le volume restant est `2` (10 - 8).

Une implémentation lisible peut s'écrire en une seule expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !

#### (1.4) `Glass::empty` with `copy`

Implémentez `fun Glass.empty()` dans le fichier  `src/main/kotlin/io/monkeypatch/talks/waterpouring/model/Glass+Ext.kt`.

Utilisez la méthode `Glass::copy`. La classe `Glass` contient cette méthode car c'est une `data class`.
Voir [Copying](https://kotlinlang.org/docs/reference/data-classes.html#copying).

Une implémentation lisible peut s'écrire en une seule expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !

#### (1.5) `Glass::fill` with `copy`

Implémentez `fun Glass.fill()` dans le fichier  `src/main/kotlin/io/monkeypatch/talks/waterpouring/model/Glass+Ext.kt`.

Utilisez la méthode `Glass::copy`. La classe `Glass` contient cette méthode car c'est une `data class`.
Voir [Copying](https://kotlinlang.org/docs/reference/data-classes.html#copying).

Une implémentation lisible peut s'écrire en une seule expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !

#### (1.6) `Glass::minus` operator

Implémentez `fun Glass.minus(value: Int)` dans le fichier `src/main/kotlin/io/monkeypatch/talks/waterpouring/model/Glass+Ext.kt`.

Utilisez la méthode `Glass::copy`. La classe `Glass` contient cette méthode car c'est une `data class`.
Voir [Copying](https://kotlinlang.org/docs/reference/data-classes.html#copying).

Nous souhaitons pouvoir écrire ce code: `Glass(capacity = 12, current = 6) - 4`.
Voir [Operators](https://kotlinlang.org/docs/reference/java-interop.html#operators)

ATTENTION, si vous retirez plus que le verre contient, il devient vide, donc `(Glass(capacity = 12, current = 6) - 10) == Glass(capacity = 12, current = 0)`

Une implémentation lisible peut s'écrire en une seule expression sans `if-then-else` ni a `Math.max`.
Vous pouvez utiliser un `if-then-else` ou `Math.max`, mais du code plus élégant peut être écrit, faites une recherche de `coerce` dans la bibliothèque standard.

NOTES:
  - DO NOT change the `Glass.kt` file !

#### (1.7) `Glass::plus` operator

Implémentez `fun Glass.plus(value: Int)` dans le fichier `src/main/kotlin/io/monkeypatch/talks/waterpouring/model/Glass+Ext.kt`.

Utilisez la méthode `Glass::copy`. La classe `Glass` contient cette méthode car c'est une `data class`.
Voir [Copying](https://kotlinlang.org/docs/reference/data-classes.html#copying).

Nous souhaitons pouvoir écrire ce code: `Glass(capacity = 12, current = 6) + 4`.
Voir [Operators](https://kotlinlang.org/docs/reference/java-interop.html#operators)

ATTENTION, si vous ajoutez plus que le verre ne peut en contenir, il devient plein, donc `(Glass(capacity = 12, current = 6) + 10) == Glass(capacity = 12, current = 12)`

Une implémentation lisible peut s'écrire en une seule expression sans `if-then-else` ni `Math.min`.
Vous pouvez utiliser un `if-then-else` ou `Math.min`, mais du code plus élégant peut être écrit, faites une recherche de `coerce` dans la bibliothèque standard.

NOTES:
  - DO NOT change the `Glass.kt` file !

### (2_Solver) Implémentation du `TailRecursiveSolver`

Vous allez maintenant travailler sur l'implémentation du solveur.
Le `TailRecursiveSolver` est volontairement trés découpé pour avoir des tests unitaires simples.

Dans cette partie vous allez beaucoup utiliser les [Higher-Order Functions and Lambdas](https://kotlinlang.org/docs/reference/lambdas.html) et l'[API des collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/index.html) qui fournit des méthodes commme `filter`, `map`, `flatMap`, ...
et la classe [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html#pair) de la bibliothèque standard.

Remarquez aussi l'utilisation d'un [`typealias`](https://kotlinlang.org/docs/reference/type-aliases.html) pour racourcir le type `Pair<State, List<Move>>` en `StateWithHistory`.

#### (2.1) `TailRecursiveSolver::findSolution` with lambda and a safe call

Implémentez `fun findSolution(statesWithHistory: Collection<StateWithHistory>, expected: State): List<Move>?` dans le fichier  `src/main/kotlin/io/monkeypatch/talks/waterpouring/server/TailRecursiveSolver.kt`.
Le `?` est particulièrement important dans le type de retour.

Utilisez la bonne méthode de `kotlin.collections`, écrivez le prédicat sous la forme `{ ... }` (Lambda).
Cette méthode doit retourner un `StateWithHistory?`, vous devez donc utiliser un [safe call](https://kotlinlang.org/docs/reference/null-safety.html#safe-calls) pour avoir le résultat souhaité.

Une implémentation lisible peut s'écrire en une ou deux ligne.
Pour du code plus propre, vous pouvez utiliser une [déconstruction](https://kotlinlang.org/docs/reference/multi-declarations.html#destructuring-in-lambdas-since-11) sur la `Pair`.

#### (2.2) `State::availableMoves`

Implémentez `fun State.availableMoves(): Collection<Move>` dans le fichier `src/main/kotlin/io/monkeypatch/talks/waterpouring/model/State+Ext.kt`.

D'abort Implémentez `glassNotEmptyIndexes` et `glassNotFillIndexes` qui corresponds aux index des verres dans le `State`, des verres non vide, et non plein.
Rappelez vous des méthodes que vous avez implémenter: `Glass::isEmpty` et `Glass::isFull`.

Ensuite calculez la liste des `Empty` pour la variable `empties`, liste des `Fill` pour `fills` et liste des `Pour` pour `pours`.

Pour finir utilisez un opérateur pour faire la concaténation.

Une implémentation lisible peut s'écrire en environ vingt lignes.

Aide: vous pouvez utiliser ces méthodes : `mapIndexed`, `filter` ou `filterNot`, `map`, `flatMap` sur les listes.

#### (2.3) `State::process`

Implémentez `fun State.process(): State` dans le fichier `src/main/kotlin/io/monkeypatch/talks/waterpouring/model/State+Ext.kt`.

Vous devez créer un nouvel `State` (c'est à dire une `List<Glass>`) en traitant le `Move`.
Vous devez utiliser un [`when`](https://kotlinlang.org/docs/reference/control-flow.html#when-expression) pour agir en fonction du type de `Move`.
Regarder aussi le [smart cast](https://kotlinlang.org/docs/reference/typecasts.html#smart-casts).

En Kotlin `when`, `if-then-else` sont des expressions (i.e. retourne une valeur).

Rappelez vous des méthodes que vous avez implémenté: `Glass::empty`, `Glass::fill`, `Glass::remainingVolume`, `Glass::plus`, et `Glass::minus`.

Une implémentation lisible peut s'écrire en grosse dizaine de lignes.

Aide: utilisez un `mapIndexed`, suivit d'un `when` pour appliquer le `Move` (si nécessaire), le cas du `Pour` nécessite un autre `when` ou bien une expression `if-elseif-else`.

#### (2.4) `TailRecursiveSolver::nextStatesFromState`

Implémentez `fun nextStatesFromState(stateWithHistory: StateWithHistory): List<StateWithHistory>` dans le fichier  `src/main/kotlin/io/monkeypatch/talks/waterpouring/server/TailRecursiveSolver.kt`.

Rappelez vous des méthodes que vous avez implémenté: `State::availableMoves` et `State::process`.

Une implémentation lisible peut s'écrire en environ trois lignes.

#### (2.5) `TailRecursiveSolver::nextStatesFromCollection`

Implémentez `fun nextStatesFromCollection(statesWithHistory: Collection<StateWithHistory>): List<StateWithHistory>` dans le fichier `src/main/kotlin/io/monkeypatch/talks/waterpouring/server/TailRecursiveSolver.kt`.

Rappelez vous de la méthode que vous avez implémenté: `TailRecursiveSolver::nextStatesFromState`.

Une implémentation lisible peut s'écrire en une seule expression.

#### (2.6) `TailRecursiveSolver::allVisitedStates`

Implémentez `fun allVisitedStates(visitedStates: Set<State>, newlyStates: List<StateWithHistory>): Set<State>` dans le fichier  `src/main/kotlin/io/monkeypatch/talks/waterpouring/server/TailRecursiveSolver.kt`.

Une implémentation lisible peut s'écrire en une seule ligne.

#### (2.7) `TailRecursiveSolver::solve`

Implémentez `fun solve(from: State, to: State): List<Move>` dans le fichier `src/main/kotlin/io/monkeypatch/talks/waterpouring/server/TailRecursiveSolver.kt`.

Commencez par créer une fonction interne auxiliaire `fun solveAux(statesWithHistory: Collection<StateWithHistory>, visitedStates: Set<State>): List<Move>`, et utilisez cette fonction pour écrire l'instruction `return`.

Rappelez vous des méthodes que vous avez implémenté: `findSolution`, `nextStatesFromCollection`, `allVisitedStates`.

S'il n'y a pas de solution, vous allez avoir une boucle infinie, donc vérifiez avant le prochain appel de la récursivité que vous avez de nouveaux états, sinon levez une `IllegalStateException`.

Une implémentation lisible peut s'écrire en environ vingt lignes (avec quelques commentaires).

En plus vous pouvez marquer la fonction auxilliaire comme [tail-recursive](https://kotlinlang.org/docs/reference/functions.html#tail-recursive-functions).

Aides:

* Comme le paramètre `from` est un état déjà connu, vous pouvez donc le mettre dans le premier `visitedStates`.
* dans `solveAux` concentrez vous d'abord sur la terminaison de notre récursivité en utilisant `findSolution`.
* ensuite calculez les états suivant, en supprimant ceux qui ne sont pas nouveaux.
* enfin calculez le nouveau `visitedStates`.

### (3_Application) Implémentation de `ServerApplication` (Partie Spring)

Cette partie se concentre uniquement sur [SpringBoot v2](https://docs.spring.io/spring-boot/docs/2.0.0.M6/reference/htmlsingle/) en utilisant [reactive webflux](https://docs.spring.io/spring-framework/docs/5.0.0.M1/spring-framework-reference/html/web-reactive.html).

La version `GA` arrive pour la fin de l'année, mais la milestone actuelle est déjà très utilisable, voir [milestones page](https://github.com/spring-projects/spring-boot/milestones) pour suivre la roadmap.

SpringBoot 2 se base sur Spring 5, donc une autre bonne source d'information est [Spring 5 documentation](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/index.html), ainsi que la [partie Kotlin](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/languages.html#languages).
Le blog [Introducing Kotlin support in Spring Framework 5.0](https://spring.io/blog/2017/01/04/introducing-kotlin-support-in-spring-framework-5-0), les vidéo de Sebastien Deleuze à Devoxx Belgique: [Why Spring ❤ ️Kotlin](https://www.youtube.com/watch?v=kbMXAjWEft0)ou de Josh Long [Spring Tips: Bootiful Kotlin Redux](https://spring.io/blog/2017/11/08/spring-tips-bootiful-kotlin-redux) sont aussi très utile pour vite rentrer dans les nouveautés de Spring 5 et SpringBoot 2, en particulier sur les aspects Kotlin.
 
#### (3.1) `ServerApplication`

Corrigez le `TailRecursiveSolver` pour qu'il soit injectable dans notre `ServerApplication` dans le fichier `src/main/kotlin/io/monkeypatch/talks/waterpouring/server/TailRecursiveSolver.kt`.

Voir [l'injection de dépendance](https://docs.spring.io/spring-boot/docs/2.0.0.M6/reference/htmlsingle/#using-boot-spring-beans-and-dependency-injection) dans SpringBoot.

Nous voulons aussi récupérer la valeur de `timeoutInSeconds` depuis le fichier de configuration `application.properties` avec la clef `solver.timeout.in.seconds`.

Aide: voir [Spring projects in Kotlin](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/languages.html#kotlin-spring-projects-in-kotlin)

#### (3.2) `ServerApplication::routes`

Mettre à jour `ServerApplication::routes` dans le fichier `src/main/kotlin/io/monkeypatch/talks/waterpouring/server/ServerApplication.kt`.

Vous devez ajouter la route pour `POST /api/solve` et appeler la méthode `ServerApplication::solve` sur cette route.

Voir l'aide du [router DSL](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/languages.html#kotlin-web).

#### (3.3) `ServerApplication::solve`

Implémentez les méthodes `fun solve(request: ServerRequest): Mono<ServerResponse>` et `fun solveWithTimeout(input: Mono<Pair<State, State>>): Mono<List<Move>>` dans le fichier `src/main/kotlin/io/monkeypatch/talks/waterpouring/server/TailRecursiveSolver.kt`.

Dans un premier temps ne vous préoccupez pas du **timeout** dans `solveWithTimeout`.

Nous allons utiliser [Reactor](https://projectreactor.io/), une bibliothèque réactive très proche de [RxJava](https://github.com/ReactiveX/RxJava).
Un `Mono` est équivalent à un `Single` ou un `Observable` avec au plus un élément.

Dans le `solveWithTimeout` vous devez appeler votre solveur.

#### (3.4) `ServerApplication::exceptionResponseMapper`

Implémentez `fun exceptionResponseMapper(): ExceptionResponseMapper` dans le fichier `src/main/kotlin/io/monkeypatch/talks/waterpouring/server/TailRecursiveSolver.kt`.

Nous allons utiliser la classe `WebExceptionHandler` pour intercepter et traiter les cas d'erreurs : `IllegalStateException` et `TimeoutException`.
Nous devons lui fournir une  `ExceptionResponseMapper`. (voir `src/main/kotlin/io/monkeypatch/talks/waterpouring/server/ApplicationWebExceptionHandler.kt`)

Pour une `IllegalStateException` on veut retourner une **Bad Request** et pour `TimeoutException` une **Request Timeout**.

#### (3.5) `ServerApplication::solveWithTimeout`

Corrigez la méthode `fun solveWithTimeout(input: Mono<Pair<State, State>>): Mono<List<Move>>` dans le fichier `src/main/kotlin/io/monkeypatch/talks/waterpouring/server/TailRecursiveSolver.kt`.

Maintenant on va définir le timeout de la durée de `timeoutInSeconds` lors de la résolution du problème.

Aide: [Reactor: Which operator do I need?](https://projectreactor.io/docs/core/release/reference/docs/index.html#which-operator) pour choisir le bon opérateur.