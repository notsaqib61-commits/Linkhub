# LinkHub — Free/Premium public link publishing app

LinkHub is an Android WebView app + web app backed by Firebase Firestore. Published links are stored online, so people who install the app later can still see them.

## Features
- Free users: maximum **3 published links per rolling hour**.
- Premium users: **unlimited links**.
- Creator account: **CREATOR + blue tick**.
- Premium upgrade using a **Creator Code**.
- Premium blue tick option.
- Premium profile photo option.
- Publisher name shown on every link.
- Records the name/time of people who open a link; the publisher (and Creator) can open **Viewers**.
- Grey/white theme.
- Share-link and share-app actions.

## Firebase setup (required for global persistence)
1. Create a Firebase project.
2. Enable Authentication → Sign-in method → **Anonymous**.
3. Create Firestore Database.
4. Publish `firestore.rules`.
5. Add a Firebase **Web App** and copy its `apiKey`, `authDomain`, `projectId`, and `appId` into `app/src/main/assets/index.html` under `FIREBASE_CONFIG`.
6. In Firestore create `config/creator` with a string field `uid` containing your own anonymous-auth UID. That account receives the Creator badge.
7. For each Premium Creator Code, create `codes/<YOUR_CODE>` as a document. The document can be empty.
8. Put your APK/download page in `APP_SHARE_URL`.

### Important
The APK cannot contain a universal public database by itself. Firebase is the shared online backend that makes links persistent across different phones. Each installation signs in anonymously and gets its own user ID.

## Build the APK
This project includes `.github/workflows/build.yml` and can be built with GitHub Actions using Gradle 8.2 and Android API 34. Run the workflow named **Build APK**, then download the `LinkHub-apk` artifact.

For a production release, create a signed release key and publish the APK through a GitHub Release or another HTTPS download host, then put that URL into `APP_SHARE_URL`.
