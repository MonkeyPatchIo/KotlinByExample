Kotlin par l'exemple - Web
===

Cette application est écrite en Kotlin avec KotlinJS.
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

Pré-requis
---

* JDK 8 (Ne marche pas avec Java 9)
* NodeJS (LTS ou stable)
* un éditeur qui fonctionne bien avec Kotlin comme [IntelliJ CE](https://www.jetbrains.com/idea/download/)
* un navigateur moderne

Kotlin et le JavaScript
---

Pour commencer, vous pouvez regarder rapidement [Kotlin JavaScript Overview](https://kotlinlang.org/docs/reference/js-overview.html).

Cette application web est packagée sous forme de [module UMD](https://github.com/umdjs/umd).
Pour construire un fichier exécutable on utilise [Webpack](https://webpack.js.org/) pour agréger les toutes les dépendances.
(ce qui nécessite NodeJS)

Nous avons en effet une dépendance sur 'kotlin.js', qui correspond à l'implémentation de la [bibliothèque standard de Kotlin](https://kotlinlang.org/api/latest/jvm/stdlib/index.html), ainsi que des dépendances lié à l'utilisation de React ou pour les tests.
Dans la documentation de Kotlin certaines parties concernent uniquement la JVM ou le JavaScript, faites donc bien attention à sélectionner 'JS' dans la combo-box de la documentation pour se concentrer sur ce qui est utilisable dans cet exercice.

Pour réduire la taille de notre fichier JavaScript, on utilise le [plugin webpack Kotlin DCE](https://kotlinlang.org/docs/reference/javascript-dce.html). L'idée de ce plugin est de retirer les parties de code non utilisées du 'kotlin.js', DCE signifie 'Dead Code Elimination', mais on parle aussi de 'Tree Shaking'.

Enfin, tester notre code avec KotlinJS est un peu complexe, nous avons choisi d'utiliser [Mocha](https://mochajs.org/) au travers de [kotlin-mocha](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-mocha).

Instructions
---

Si vous débutez en Kotlin, vous devriez au moins regarder 
[basic Syntax](https://kotlinlang.org/docs/reference/basic-syntax.html) et
[idioms](https://kotlinlang.org/docs/reference/idioms.html).

Vous devez implémenter les méthodes qui utilise `TODO("x.y")`, ou faire quelque chose la ou il y a `// TODO x.y`.

Pour vous guider, lancez les tests qui vont vous permettre de valider votre implémentation.
Pour lancer les tests dans votre terminal avec NodeJS : `./gradlew test` ou `npm test`, mais il est plutôt recommandé de lancer les tests dans le navigateur en ouvrant le fichier `src/test/web/tests.html`.

N'hésitez pas à regarder la [documentation de Kotlin](https://kotlinlang.org/docs/reference/), si vous en avez le besoin.

Cet exercice est constitué de 4 étapes :

* (_1_GlassTests) Implémentation des 'extensions methods' de `Glass`
* (_2_Templates) Implémentation de templates HTML
* (_3_EventHandler) Traitement de certain événements
* (_4_Solve) Implémentation du solveur

Notes:
- vous n'avez pas besoin de modifier les tests, mais vous pouvez les regarder pour mieux comprendre les besoins.
Donc après la première compilation des tests avec `./gradlew test`, vous pouvez juste lancer la compilation des fichiers principaux avec `./gradlew assemble`.
- malheureusement certains tests n'ont pas été activés car ils ne fonctionnaient pas. Si vous avez une idée lumineuse pour corriger ces tests, n'hésitez pas à proposer une Pull Request.

### (_1_GlassTests) Implémentez les extensions de `Glass`

Vous devez enrichir les méthodes de la classe `Glass` avec des extensions, voir [Kotlin extension methods](https://kotlinlang.org/docs/reference/extensions.html).

#### (1.1) `Glass::remainingVolume`

Implémentez `fun Glass.remainingVolume()` dans le fichier `src/main/kotlin/model/Glass+Ext.kt`.

Pour un `Glass(capacity = 10, current = 8)` le volume restant doit être `2` (10 - 8).

Une implémentation lisible peut être fait en une seule expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !


#### (1.2) `Glass::empty` avec `copy`

Implémentez `fun Glass.empty()` dans le fichier `src/main/kotlin/model/Glass+Ext.kt`.

Utilisez la méthode `Glass::copy`. La class `Glass` contient cette méthode car c'est une `data class`.
Voir [Copying](https://kotlinlang.org/docs/reference/data-classes.html#copying).

Une implémentation lisible peut être fait en une seule expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !

#### (1.3) `Glass::fill` avec `copy`

Implémentez `fun Glass.fill()` dans le fichier `src/main/kotlin/model/Glass+Ext.kt`.

Utilisez la méthode `Glass::copy`. La class `Glass` contient cette méthode car c'est une `data class`.
Voir [Copying](https://kotlinlang.org/docs/reference/data-classes.html#copying).

Une implémentation lisible peut être fait en une expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !


#### (1.4) `Glass::fillPercent`

Implémentez `fun Glass.fillPercent()` dans le fichier `src/main/kotlin/model/Glass+Ext.kt`.

Pour un `Glass(capacity = 10, current = 8)`, le pourcentage de remplissage est de `80.0`.

Une implémentation lisible peut être fait en une expression.

NOTE: NE CHANGEZ PAS le fichier `Glass.kt` !

#### (1.5) `Glass::minus`

Implémentez `fun Glass.minus(value: Int)` dans le fichier `src/main/kotlin/model/Glass+Ext.kt`.

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

#### (1.6) `Glass::plus`

Implémentez `fun Glass.plus(value: Int)` dans le fichier `src/main/kotlin/model/Glass+Ext.kt`.

Utilisez la méthode `Glass::copy`. La class `Glass` contient cette méthode car c'est une `data class`.
Voir [Copying](https://kotlinlang.org/docs/reference/data-classes.html#copying).

Nous souhaitons pouvoir écrire ce code : `Glass(capacity = 12, current = 6) + 4`.
Voir [Operators](https://kotlinlang.org/docs/reference/java-interop.html#operators)

ATTENTION, si vous ajoutez une quantité supérieure à la contenance du verre, il devient rempli, donc `(Glass(capacity = 12, current = 6) + 10) == Glass(capacity = 12, current = 12)`

Une implémentation lisible peut être fait en une expression sans `if-then-else` ni `Math.min`.
L'utilisation du `if-then-else` ou de `Math.min` est acceptable, mais pour écrire du code plus malin, recherchez  `coerce` dans la bibliothèque standard.

NOTES:
  - NE CHANGEZ PAS le fichier `Glass.kt` !
  - l'__operator__ ne peux pas facilement être testé en web

### (_2_Templates) Implémentez des templates HTML

Ici vous avez besoin d'un complément d'information sur le fonctionnement de l'application.

Nous utilisons [kotlin-react](https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react) pour faire le rendu de l'application.
Donc écrire des 'templates' correspond en faite à écrit des composants React en Kotlin.

Deplus avons fait une implémentation trivial d'un [Redux](http://redux.js.org/docs/introduction/CoreConcepts.html) pour gérer l'état de l'application.
Cet état est défini dans le fichier `src/main/kotlin/model/UiState.kt`.
L'implémentation du redux est fait dans le fichier `src/main/kotlin/store/Store.kt` (vous n'avez pas besoin de modifier le ce fichier).
Les actions, le reducer, et l'effet de l'application sont dans le répertoire `src/main/kotlin/redux/` (vous n'avez pas besoin de modifier le ces fichiers).

Le fichier `src/main/kotlin/Main.kt` contient la méthode `main`, qui lance `bootstrapApplication` chargé d'afficher le `UiState` d'appeler le composant principale.

Au sein d'un composant `RComponent`, pour créer du HTML on utilise la bibliothèque [kotlinx-html](https://github.com/Kotlin/kotlinx.html) qui est un DSL pour du HTML.

La structure principale est déjà faite dans le composant `MainComponent` du fichier `src/main/kotlin/components/main.component.kt`, cela peut servir d'exemple pour l'implémentation des templates restants du répertoire `src/main/kotlin/components/`.

#### (2.1) Composant pour `Glass`

Implémentez la méthode `RBuilder.render()` dans le fichier `src/main/kotlin/components/glass.component.kt`.

Vous devez obtenir l'HTML suivant :

 ```html
 <div class="glass" style="..."><div>
 ```

 Avec le style suivant:
  - height = `<glass.capacity>rem`
  - background-image = `linear-gradient(to top, chocolate <glass.fillPercent()>%, transparent 0px)`

Aides:
- Vous n'avez pas à toucher le CSS, il est déjà fait dans le fichier `src/main/web/style.css`.
- Pour ajouter des styles en Kotlin, vous pouvez utiliser un bout de code qui ressemble à comme ceci:

```
attrs.jsStyle = js {
    color = "gold"
    backgroundColor = "red"
}
```

#### (2.2) Composant pour l'état final

Implémentez la méthode `RBuilder.render()` dans le fichier `src/main/kotlin/components/final-state.component.kt`.

Chaque verre de l'état doit être représenté par :

```html
<!-- pour chaque verre -->
  <div class="glass-container">
    <!-- utiliser le composant glass ici -->
    <input type="number" value="<the glass current>" min="0" max="<the class capacity>">
    <span>/</span>
    <span class="capacity"><!-- capacity --></span>
  </div>
<!-- fin -->
```

Aides:
  - Ne gérez pas les changements de valeur de l'input pour l'instant.
  - Certaines méthodes de `List` fournissent un index.

#### (2.3) Composant pour l'état initiale

Implémentez la méthode `RBuilder.render()` dans le fichier `src/main/kotlin/components/initial-state.component.kt`.

Chaque verre de l'état doit être représenté par :

```html
<!-- pour chaque verre -->
  <div class="glass-container">
    <!-- utiliser le composant glass ici -->
    <input type="number" value="<the glass current>" min="0" max="<the class capacity>">
    <span>/</span>
    <input type="number" value="<the glass capacity>" min="<the configuration min capacity value>" max="<the configuration max capacity value>">
  </div>
<!-- fin -->
```

Aide:
  - Ne gérez pas les changements de valeur de l'input pour l'instant.
  - Certaines méthodes de `List` fournissent un index.

### (_3_EventHandler) Implémentez le traitement des événements

Lorsqu'on utilise __Redux__, il faut un __reducer__ pour mettre à jour l'état de l'application, Pour déclencher le reducer on envoie une `Action` avec `Store::dispatch(Action)`.

L'idée ici est de transformer un événement DOM en `Action` puis d'envoyer cette action.
Nous avons créé une fonction `triggerAction` sur le `Store` pour écrire cette opération plus facilement :

```kotlin
button {
  onClickFunction = { event -> store.dispatch(ActionXXX)}
}
```

Nous allons définir les opérations utilisés dans le `MainProps` du fichier `src/main/kotlin/components/main.component.kt`.
Ensuite on créer une implémentation dans l'extension `RBuilder.mainContainer`, et bien sur enregistrer ces actions sur les évènements dans nos templates.

#### (3.1) supprimer un verre

Utiliser une lambda pour définir l'attribut `onRemoveGlass` dans la fonction `RBuilder.mainContainer` du fichier `src/main/kotlin/components/main.component.kt`.

Puis déclancher cette action lorsque l'on clique sur le bouton `.btn-remove` dans le `render()` du `MainComponent` du même fichier.

#### (3.2) ajouter un verre

Utiliser une lambda pour définir l'attribut `onAddGlass` dans la fonction `RBuilder.mainContainer` du fichier `src/main/kotlin/components/main.component.kt`.

Puis déclancher cette action lorsque l'on clique sur le bouton `.btn-add` dans le `render()` du `MainComponent` du même fichier.

Bonus: vous pouvez utiliser une valeur aléatoire pour la capacité et la valeur courante en utilisant la fonction définie dans `src/main/kotlin/helpers/math.helpers.kt` avec `(min..max).random()` en tenant compte de la configuration.

#### (3.3) changer la valeur courante dans l'état final

Utiliser une lambda pour définir l'attribut `onFinalCurrentChange` dans la fonction `RBuilder.mainContainer` du fichier `src/main/kotlin/components/main.component.kt`.

Puis déclancher cette action lorsque la valeur du champ change dans le `render()` du `FinalStateComponent` du fichier `src/main/kotlin/components/final-state.component.kt`.

NOTE: le test ne fonctionne pas pour cette partie

#### (3.4) changer la valeur courante dans l'état initial

Utiliser une lambda pour définir l'attribut `onInitialCurrentChange` dans la fonction `RBuilder.mainContainer` du fichier `src/main/kotlin/components/main.component.kt`.

Puis déclancher cette action lorsque la valeur du champ change dans le `render()` du `InitialStateComponent` du fichier `src/main/kotlin/components/initial-state.component.kt`.

NOTE: le test ne fonctionne pas pour cette partie

#### (3.5) changer la capacité dans l'état initial

Utiliser une lambda pour définir l'attribut `onInitialCapacityChange` dans la fonction `RBuilder.mainContainer` du fichier `src/main/kotlin/components/main.component.kt`.

Puis déclancher cette action lorsque la valeur du champ change dans le `render()` du `InitialStateComponent` du fichier `src/main/kotlin/components/initial-state.component.kt`.

NOTE: le test ne fonctionne pas pour cette partie

### (_4_Solve) Implémentez la résolution

C'est heure de finir le code, en implémentant les éléments manquant pour la résolution.
Pour cela il faut faire un appel sur le serveur, donc ce code doit être asynchrone.

Pour gérer ce type d'effet de bord, on utilise un `Effect`.
L'`Effect` est défini de cette façon : `typealias Effect = (Action<*>) -> Promise<Action<*>?>`.

Pour une `Action` l'effet peut retourner une `Promise<Action>` (La Promise permet du code asynchrone), ou bien une `Promise<null>` si cela ne doit pas déclencher une autre `Action`. Bien sûr cet appel serveur peut échouer, il faut donc prévoir une `Action` spécial pour les cas d'erreurs.

#### (4.1) déclancher la `SolveAction`

Utiliser une lambda pour définir l'attribut `onSolve` dans la fonction `RBuilder.mainContainer` du fichier `src/main/kotlin/components/main.component.kt`.

Puis déclencher cette action lorsque l'on clique sur le bouton `.btn-solve` dans le `render()` du `MainComponent` du même fichier.

#### (4.2) convertire un `SolveMove` en un `Move` avec un `when`

Implémentez `fun SolveMove.asMove()` dans le fichier `src/main/kotlin/redux/effects.kt`.

Le résultat JSON retourné par le serveur est du type `Array<SolveMove>`, nous devons convertir ces `SolveMove` en un `Move`.

Vous devez utiliser une [expression `when`](https://kotlinlang.org/docs/reference/control-flow.html#when-expression) pour gérer les divers types de `Move`.
Tous les attributs de `SolveMove` sont nullable, vous pouvez utiliser le `?:` pour lancer une exception s'il y a un résultat `null` inattendu.

#### (4.3) Implémentez l'`Effect`

Compléter le `val effect = solveEffect(...)` dans le fichier `src/main/kotlin/Main.kt` et la fonction `fun solveEffect(...)` du fichier `src/main/kotlin/redux/effects.kt` .

Lorsque le `solveEffect` a une `SolveAction`, il doit faire un appel serveur pour récupérer le résultat.
Si ce résultat est positif, on doit retourner une `SolveActionSuccess` avec la `List<Move>` correspondant à la solution.
Sinon, on retourne une `SolveActionError`.

On a extrait l'opération d'appel vers le serveur avec `typealias Solver = (String, Pair<State, State>) -> Promise<Array<SolveMove>>`. Ceci nous permet de facilement de fournir une implémentation bouchon dans les tests.

Vous devrez donc construire la bonne valeur de ce `solver` lors de la construction de l'effet dans le fichier `src/main/kotlin/Main.kt`.

Aide: utilisez une fonction définie dans le fichier `src/main/kotlin/helpers/http.helpers.kt`, cela permet d'utiliser l'API `fetch` pour faire l'appel vers le serveur.

#### (4.4) Implémentez `State::move` avec `when`

Implémentez `fun State.move(move: Move): State` dans le fichier `src/main/kotlin/model/State+Ext.kt`.

Pour chaque `Move` on veut afficher l'état correspondant, on doit donc implémenter cette méthode utilitaire.
Vous devez créer un nouveau `State` (soit une `List<Glass>`) en appliquant le `Move` a un état courant.

Vous devez utiliser un [`when`](https://kotlinlang.org/docs/reference/control-flow.html#when-expression) pour  déterminer le type de déplacement.
Regarder aussi le [smart cast](https://kotlinlang.org/docs/reference/typecasts.html#smart-casts).

En Kotlin `when`, `if-then-else` sont des expressions (i.e. retourne une valeur).

Vous avez précédemment implémenté les méthodes `Glass::empty`, `Glass::fill`, `Glass::remainingVolume`, `Glass::plus`, et `Glass::minus`.

Une implémentation lisible peut être fait en une grosse dizaine de lignes.

Aide: vous pouvez utiliser `mapIndexed`, puis un `when` pour appliquer chaque spécificité des déplacements (si nécessaire), le cas `Pour` nécessite un `when` supplémentaire ou bien une expression `if-elseif-else`.

#### (4.5) Implémentez `UiState::solutionList`

Implémentez `val solutionList: List<Pair<Move?, State>>` dans le fichier `src/main/kotlin/model/UiState.kt`.

Dans le `UiState`, définissez une propriété [lazy](https://kotlinlang.org/docs/reference/delegated-properties.html#lazy) `solutionList` avec :

- un premier élément qui est une [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html) de `null` et de l'état initial
- puis chaque `Pair` sont composés du déplacement suivant et de l'état résultant pour résoudre le problème.

Aide: vous pouvez utiliser la méthode `fold` de `List`

#### (4.6) Le composant pour la solution

Implémentez la méthode `RBuilder.render()` dans le fichier `src/main/kotlin/components/solution-state.component.kt`.

Pour chaque élément de la solution, vous devez obtenir l'HTML suivant :

```html
<li>
    <div class="move"><!-- move --></div>
    <div class="state">
        <!-- for every glass -->
        <div class="glass-container">
            <!-- glass template -->
            <span><!-- glass current -->/ <!-- glass capacity --></span>
        </div>
        <!-- end for -->
    </div>
</li>
```

Vous pouvez utiliser le template du verre et la méthode `Move::toDisplayString()`.
