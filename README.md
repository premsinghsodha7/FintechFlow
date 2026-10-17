# FintechFlow

> A fictional digital banking app built with Kotlin and Jetpack Compose. No real accounts, payments, or personal data are used.

FintechFlow is a portfolio project for exploring clear financial UI, reusable Compose components, and practical Android app structure. The current starter includes a responsive account dashboard and sample activity backed by local, fictional data.

## Preview

The first screen presents a masked sample balance, quick actions, and recent activity. All amounts, names, and transactions are illustrative.

## Tech

- Kotlin
- Jetpack Compose and Material 3
- Android Gradle Plugin 8.7.3; Gradle 8.9; JDK 17
- minSdk 24, targetSdk 35

## Run

1. Install Android Studio and an Android SDK for API 35.
2. Open this repository in Android Studio.
3. Use JDK 17 for Gradle, sync, then run the `app` configuration on an emulator or device.

This starter does not include the Gradle wrapper yet. Android Studio can generate/use a compatible local Gradle installation.

## Project structure

```text
app/src/main/java/com/premsingh/fintechflow/
  MainActivity.kt  # dashboard UI and sample transaction model
```

The first iteration keeps the sample data local and the UI focused. Planned next steps: extract design tokens and UI components, add navigation and screen-level state, add unit/UI tests, and wire a mock API behind a repository interface.

## Safety and scope

FintechFlow is a UI and architecture learning project. It is not a bank, financial service, or production payment app. Never add real customer data, API credentials, or payment information.

## License

MIT. See [LICENSE](LICENSE).
