# Release MapVina Android

We make MapVina Android releases as downloadable assets on [GitHub](https://github.com/mapvina/mapvina-native/releases?q=android&expanded=true) and publish the six `io.github.mapvina:android-sdk` variants to [Maven Central](https://central.sonatype.com/artifact/io.github.mapvina/android-sdk/versions): default, default-debug, OpenGL, OpenGL-debug, Vulkan, and Vulkan-debug.

Also see the current [release policy](../release-policy.md).

## Making a release

To make an Android release, do the following:

1. Prepare a PR.

    - Update [`CHANGELOG.md`](https://github.com/mapvina/mapvina-native/blob/main/platform/android/CHANGELOG.md) in a PR. The changelog should contain links to all relevant PRs for Android since the last release. You can use the script below with a [GitHub access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens) with the `public_repo` scope. You will need to filter out PRs that do not relate to Android and categorize PRs as features or bugfixes.
        ```
        GITHUB_ACCESS_TOKEN=... node scripts/generate-changelog.mjs android
        ```
        The heading in the changelog must match `## <VERSION>` exactly, or it will not be picked up. For example, for version 9.6.0:
        ```md
        ## 9.6.0
        ```

    - Update `android/VERSION` with the new version.

2. Once the PR is merged into `main`, Android CI detects the `platform/android/VERSION` change and dispatches [`android-release.yml`](https://github.com/mapvina/mapvina-native/blob/main/.github/workflows/android-release.yml) for the tagged `main` commit.

3. The release remains a GitHub draft until all renderer/build-type assets are built and uploaded and the single Maven Central deployment containing all six publications has been validated and released. Missing publishing or signing secrets fail during preflight before a draft release is created.
