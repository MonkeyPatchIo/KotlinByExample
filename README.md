Kotlin by Example
===

Requirements
---

### JDK 8
  
<!> build does not work with JDK 9 :'(

To install JDK, or switch version you can use [SDKMAN!](http://sdkman.io/)
  
### Your preferred IDE
 
[Android Studio](https://developer.android.com/studio/index.html) required for android exercises

[IntelliJ IDEA](https://www.jetbrains.com/idea/)

[Eclipse](http://www.eclipse.org/) is also supported, [see](https://kotlinlang.org/docs/tutorials/getting-started-eclipse.html)

<https://kotlinlang.org/docs/reference/faq.html#what-ides-support-kotlin>

### Specific requirement for Android part

[Android Studio](https://developer.android.com/studio/index.html) required for android exercises

Android SDK version `27`

You can configure the Android SDK directory by creating a `local.properties` like that:

```properties
sdk.dir=/path/to/Android/sdk
```

Water Pouring Puzzle
---

[Water Pouring Puzzle](https://en.wikipedia.org/wiki/Water_pouring_puzzle)

Build
---

Run in project root:

```bash
./gradlew clean assemble

```

First build require an Internet connection, and could be long (lots of downloads)

Test
---

Run in project root:

```bash
./gradlew test

```

Run
---

### Server

If the server has been built successfully you can start the server with

```bash
java -jar server/build/libs/server.jar
```

Then open your browser at <http://localhost:8080/>

Some solvable examples
---

+ 0/5, 0/3 to 4/5, 0/3
+ 0/8, 0/5 to 6/8, 0/5
+ 0/7, 0/5 to 4/7, 0/5
+ 0/9, 0/4 to 6/9, 0/4
+ 0/11, 0/3 to 7/11, 0/3
+ 0/11, 0/7) to 2/11, 0/7
+ 12/12, 0/8, 0/5 to 6/12, 6/8, 0/5
+ 0/24, 0/13, 0/11, 0/5 to 6/24, 6/13, 6/11, 0

REST API
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

Response if OK:

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

Or an error `4xx` if there is an issue, the body contains a textual description of issue.

Content
---

```bash
.
├── gradle: gradle wrapper directory
├── mobile: the android sub-project (see below)
├── server: the server sub-project (see below)
├── shared: the shared sub-project (see below)
├── shared-json: the shared-json sub-project (see below)
├── web: the web sub-project (see below) 
├── README.md: this file
├── build.gradle: gradle build
├── settings.gradle: gradle setting
├── gradle.properties: some gradle properties
├── gradlew: run the gradle wrapper on GNU/Linux or OSX
└── gradlew.bat: run the gradle wrapper on Windows
```

### shared

The shared code between over part.

Containing the definition of `Glass`, `State`.

### shared-json

The shared code for Jackson serialization/deserialization.
Used by mobile and server

### mobile (Android)

Home of the Android exercise

### server (Spring Boot 2.0)

Home of the SpringBoot exercise.

### Web (browser)

Home of the Web exercise.