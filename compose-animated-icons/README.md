![JitPack](https://img.shields.io/jitpack/version/com.github.jnnkmsr/compose?style=for-the-badge)
![GitHub](https://img.shields.io/github/license/jnnkmsr/compose?style=for-the-badge)

# Animated Icons for Jetpack Compose

A library for [Jetpack Compose][compose] providing multi-state icon animations
using both [animated vector drawables (AVDs)][avd] and the [Compose animation
API][compose-animation].

<!-- TODO
## Usage

### Icon declaration

### Composables
-->

## Download

Add the [JitPack][jitpack] repository and the library dependency to your Gradle
build scripts.

### Kotlin DSL

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.jnnkmsr.compose:compose-animated-icons:<version>")
}
```

### Groovy DSL

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.jnnkmsr.compose:compose-animated-icons:<version>'
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
[avd]: https://d.android.com/jetpack/compose/animation/avd
[compose]: https://d.android.com/jetpack/compose
[compose-animation]: https://d.android.com/jetpack/compose/animation/composables-modifiers
[jitpack]: https://jitpack.io/
