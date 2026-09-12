# Release MapVina iOS

We make iOS releases to GitHub (a downloadable XCFramework), the [Swift Package Index](https://swiftpackageindex.io/github/mapvina/mapvina-gl-native-distribution) and [CocoaPods](https://cocoapods.org/). Everyone with write access to the repository is able to make releases using the instructions below.

Also see the current [release policy](../release-policy.md).

## Making a release

1. Prepare a PR, see [this PR](https://github.io/github/mapvina/mapvina-native/pull/3193) as an example.

    - Update the [changelog](https://github.com/mapvina/mapvina-native/blob/main/platform/ios/CHANGELOG.md). The changelog should contain links to all relevant PRs for iOS since the last release. You can use the script below with a [GitHub access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens) with the `public_repo` scope. You will need to filter out PRs that do not relate to iOS.
      ```
      GITHUB_ACCESS_TOKEN=... node scripts/generate-changelog.mjs ios
      ```
      The heading in the changelog must match `## <VERSION>` exactly, or it will not be picked up. For example, for version 6.0.0:
      ```md
      ## 6.0.0
      ```
    - Update the `VERSION` file in `platform/ios/VERSION` with the version to be released. We use [semantic versioning](https://semver.org/), so any breaking changes require a major version bump. Use a minor version bump when functionality has been added in a backward compatible manner, and a patch version bump when the release contains only backward compatible bug fixes.

2. Once the PR is merged into `main`, `ios-ci.yml` detects the `VERSION` change and dispatches the release workflow for that `main` commit.

3. The workflow publishes a public GitHub prerelease first so CocoaPods and the Swift Package distribution can download the XCFramework. It runs `pod spec lint` before `pod trunk push`, dispatches and waits for the matching distribution workflow, verifies the distribution tag and manifest URL, and only then converts a full release from prerelease to final. If CocoaPods or Swift Package publication fails, the public prerelease remains in place and the finalization job does not run.

4. Required repository secrets are `COCOAPODS_TRUNK_TOKEN` and `MAPVINA_RELEASE_TOKEN`. The distribution token must be able to write `mapvina/mapvina-gl-native-distribution`.

## Pre-release

Run the `ios-ci` workflow. You can use the [GitHub CLI](https://cli.github.com/manual/gh_workflow_run):

```
gh workflow run ios-ci.yml -f release=pre --ref main
```

Or run the workflow from the Actions tab on GitHub.

The items under the `## main` heading in `platform/ios/CHANGELOG.md` will be used as changelog for the pre-release.
