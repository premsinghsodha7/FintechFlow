# FintechFlow

<p align="center"><img src="https://raw.githubusercontent.com/premsinghsodha7/FintechFlow/main/brand-logo.png" alt="FintechFlow logo" width="150"></p>

> A fictional digital banking app built with Kotlin and Jetpack Compose. It uses local sample data only; no real accounts or payments are connected.

FintechFlow is a portfolio app demonstrating a polished account overview and simple transfer flow, with UI state separated from the data source.
## App preview

![FintechFlow running on the Android emulator](https://raw.githubusercontent.com/premsinghsodha7/FintechFlow/main/screenshots/app-preview.jpg)

*Live screenshot captured from a Pixel 9 Pro emulator running Android 15 (API 35).*


## Features

- Home dashboard with available balance, quick actions, and recent transactions.
- Activity list with category filters for income, food, and shopping.
- Transfer form with amount validation and immediate demo balance updates.
- Cards screen with a freeze toggle, editable demo spend limit, theme picker, and safety guidance.
- Spending insights with income, expense, and category summaries.
- Three selectable color palettes: Harbor, Evergreen, and Glacier.
- Material 3 UI with bottom navigation and lifecycle-aware state collection.
- Repository and ViewModel boundaries that can later be backed by a real service.

Every name, card number, and amount is fictional and held in memory. Transfers reset when the app process restarts.

## Run

1. Install Android Studio and an Android SDK for API 35.
2. Open the repository in Android Studio and let Gradle sync.
3. Run the `app` configuration on an emulator or device (JDK 17).

From a terminal, use `./gradlew :app:installDebug` with a connected emulator/device. Run the unit tests with `./gradlew testDebugUnitTest`.

## Architecture

```text
app/src/main/kotlin/com/premsingh/fintechflow/
  data/       DemoFinanceRepository (in-memory implementation)
  domain/     Models, amount parsing/formatting, repository contract
  ui/         Compose screens and FinanceViewModel
```

The demo repository is the only data source. `FinanceViewModel` exposes immutable state to the Compose UI, and money input is parsed into minor units to avoid floating-point rounding.

## Build

- Kotlin 2.0.21, Jetpack Compose, Material 3
- Android Gradle Plugin 8.7.3, Gradle 8.9, JDK 17
- minSdk 24, targetSdk 35
- GitHub Actions runs unit tests and assembles the debug app on pushes and pull requests.

## Scope

FintechFlow is a UI and architecture learning project, not a bank or production payment app. Do not enter real personal, account, or payment information. No backend, authentication, or payment processor is included.

## License

MIT. See [LICENSE](LICENSE).
