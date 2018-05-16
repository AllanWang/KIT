# KIT

Kotlin Incremental Tools

A collection of small packages to make development a bit simpler.

[![](https://jitpack.io/v/ca.allanwang/kit.svg)](https://jitpack.io/#ca.allanwang/kit)

More notably:

| Model | Contents |
| --- | --- |
| [`:kotlin`](kotlin#readme) | Functions for kotlin without any dependencies |
| [`:logger`](logger#readme) | Class based logging tags |
| [`:props`](props#readme) | Simple lazy loaded properties holder |

Use it by adding

```gradle
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {

    // to include everything
    compile "ca.allanwang:kit:$KIT"
    
    // to include specific module
    compile "ca.allanwang.kit:logger:$KIT
}
```

Where `$KIT` can be any release, commit hash, or snapshot.
