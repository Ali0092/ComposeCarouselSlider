# ComposeCarouselLib
[![](https://jitpack.io/v/Ali0092/ComposeCarouselSlider.svg)](https://jitpack.io/#Ali0092/ComposeCarouselSlider)

A lightweight and customizable image carousel slider built with **Jetpack Compose**, designed for Android apps. This library provides smooth animations, scaling transitions, and dot indicators to showcase images in a modern and beautiful way.

---

## ✨ Features

* Smooth **scaling transition** between carousel pages
* Fully **customizable** dot indicators
* Clean and minimal design

---

## 📦 Installation

Add JitPack to your root `build.gradle`:

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Then, add the dependency in your module's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.Ali0092:ComposeCarouselSlider:v1.0.0'
}
```

---

## 🛠️ Usage

```kotlin
ComposeCarouselSlider(
    modifier = Modifier.fillMaxWidth(),
    height = 200.dp,
    sidePadding = 16.dp,
    pageSpacing = 8.dp,
    imageCornerRoundness = 12.dp,
    imagesList = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3
    ),
    nonSelectedDotColor = Color.Gray,
    selectedDotColor = Color.White
)
```

---

## ⚙️ Parameters

| Name                   | Type        | Description                                     |
| ---------------------- | ----------- | ----------------------------------------------- |
| `modifier`             | Modifier    | Layout modifier for the carousel                |
| `height`               | `Dp`        | Height of each carousel image card              |
| `sidePadding`          | `Dp`        | Padding around the horizontal pager             |
| `pageSpacing`          | `Dp`        | Space between each page (default: `8.dp`)       |
| `imageCornerRoundness` | `Dp`        | Corner radius for image cards (default: `8.dp`) |
| `imagesList`           | `List<Int>` | List of drawable resource IDs                   |
| `nonSelectedDotColor`  | `Color`     | Color of unselected dot indicators              |
| `selectedDotColor`     | `Color`     | Color of the selected dot indicator             |

---

## 📸 Preview

![ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/f4b902e3-abe7-456b-b21f-2f9b52a21f4d)


---

## 📄 License

This project is licensed under the [Apache License 2.0](https://github.com/Ali0092/ComposeCarouselSlider/blob/main/LICENSE).

---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

---

## 👤 Author

**Muhammad Ali**
📧 [aliatwork364@gmail.com](mailto:aliatwork364@gmail.com)
🔗 [LinkedIn](https://www.linkedin.com/in/muhammad-ali-a28422222/)
🐙 [GitHub](https://github.com/Ali0092/)
