![JitPack](https://img.shields.io/jitpack/version/com.github.jnnkmsr/compose?style=for-the-badge)
![GitHub](https://img.shields.io/github/license/jnnkmsr/compose?style=for-the-badge)
![GitHub top language](https://img.shields.io/github/languages/top/jnnkmsr/compose?style=for-the-badge)

# Jetpack Compose Extension Libraries

A collection of extension libraries for [Jetpack Compose][compose].

## Download

Add the [JitPack][jitpack] repository and the library dependency to your Gradle
build scripts.

### Kotlin DSL

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.jnnkmsr.compose:<library>:<version>")
}
```

### Groovy DSL

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.jnnkmsr.compose:<library>:<version>'
}
```

## License

```
Copyright 2023 Jannik Möser

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


<!-- External Links -->
[compose]: https://d.android.com/jetpack/compose
[jitpack]: https://jitpack.io/
