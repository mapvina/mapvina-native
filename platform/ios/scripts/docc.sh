#!/bin/bash

# This script is a bit of a hack to generate DocC documentation until Bazel has support for it.
# https://github.com/bazelbuild/rules_apple/issues/2241
# To use this script, make sure the XCFramework is built with Bazel (see ios-ci.yml).
# Then to start a local preview, run:
# $ platform/ios/scripts/docc.sh preview
# You can also build the documentation locally
# $ platform/ios/scripts/docc.sh
# Then go to build/MapVina.doccarchive and run
# $ python3 -m http.server
# Go to http://localhost:8000/documentation/mapvina/

set -e
shopt -s extglob

cmd="convert"
if [[ "$1" == "preview" ]]; then
  cmd="preview"
fi

SDK_PATH=$(xcrun -sdk iphoneos --show-sdk-path)

build_dir=build

rm -rf "$build_dir"/symbol-graphs
rm -rf "$build_dir"/headers

mkdir -p "$build_dir"/symbol-graphs
mkdir -p "$build_dir"/headers

bazel build --//:renderer=metal //platform/darwin:generated_style_public_hdrs

# download resources from S3 when the public bucket is available

if aws s3api head-bucket --no-sign-request --bucket mapvina-native 2>/dev/null; then
  aws s3 sync --no-sign-request "s3://mapvina-native/ios-documentation-resources" "platform/ios/MapVina.docc/Resources"
else
  echo "DocC resources bucket is unavailable; continuing with checked-in resources."
fi

public_headers=$(bazel query 'kind("source file", deps(//platform:ios-sdk, 2))' --output location | grep ".h$" | sed -r 's#.*/([^:]+).*#\1#')
style_headers=$(bazel cquery --//:renderer=metal //platform/darwin:generated_style_public_hdrs --output=files)

cp $style_headers "$build_dir"/headers

filter_filenames() {
    local prefix="$1"
    local filenames="$2"
    local filtered_filenames=""

    for filename in $filenames; do
        local prefixed_filename="$prefix/$filename"

        if [ -f "$prefixed_filename" ]; then
            filtered_filenames="$filtered_filenames $prefixed_filename"
        fi
    done

    echo "$filtered_filenames"
}

ios_headers=$(filter_filenames "platform/ios/src" "$public_headers")
darwin_headers=$(filter_filenames "platform/darwin/src" "$public_headers")

clang_options=(
  --toolchain swift clang
  -extract-api
  --product-name=MapVina
  -isysroot "$SDK_PATH"
  -F "$SDK_PATH/System/Library/Frameworks"
  -I "$PWD"
  -I "$build_dir/headers"
  -I platform/darwin/src
  -x objective-c-header
)

headers=($ios_headers $darwin_headers build/headers/*.h)
output_name="combined.symbols.json"
xcrun "${clang_options[@]}" \
  -o "$build_dir/symbol-graphs/$output_name" \
  "${headers[@]}"

export DOCC_HTML_DIR=$(dirname $(xcrun --toolchain swift --find docc))/../share/docc/render
$(xcrun --find docc) "$cmd" platform/ios/MapVina.docc \
    --fallback-display-name "MapVina Native for iOS" \
    --fallback-bundle-identifier org.swift.MyProject \
    --fallback-bundle-version 0.0.1  \
    --additional-symbol-graph-dir "$build_dir"/symbol-graphs \
    --source-service github \
    --source-service-base-url https://github.io/github/mapvina/mapvina-native/blob/main \
    --checkout-path $(realpath .) \
    ${HOSTING_BASE_PATH:+--hosting-base-path "$HOSTING_BASE_PATH"} \
    --output-path "$build_dir"/MapVina.doccarchive

if [[ "$cmd" == "convert" ]]; then
  rm -rf build/docs
  $(xcrun --find docc) process-archive transform-for-static-hosting "$build_dir"/MapVina.doccarchive \
    ${HOSTING_BASE_PATH:+--hosting-base-path "$HOSTING_BASE_PATH"} \
    --output-path build/docs
fi
