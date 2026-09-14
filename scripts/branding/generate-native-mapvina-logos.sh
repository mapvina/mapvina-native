#!/usr/bin/env bash

set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
source_dir="$repo_root/platform/branding/source"
generated_dir="$repo_root/platform/branding/generated"
ios_assets="$repo_root/platform/ios/resources/Images.xcassets"
android_res="$repo_root/platform/android/MapVinaAndroid/src/main/res"

command -v python3 >/dev/null || { echo "python3 is required" >&2; exit 1; }
command -v rsvg-convert >/dev/null || { echo "rsvg-convert is required" >&2; exit 1; }

mkdir -p "$generated_dir"

python3 - "$source_dir" "$generated_dir" <<'PY'
import re
import sys
from pathlib import Path

source_dir = Path(sys.argv[1])
generated_dir = Path(sys.argv[2])


def svg_inner(path: Path) -> str:
    source = path.read_text(encoding="utf-8").strip()
    match = re.fullmatch(r"<svg\b[^>]*>(.*)</svg>", source, flags=re.DOTALL)
    if not match:
        raise ValueError(f"Unable to parse SVG root: {path}")
    return match.group(1)


def write_asset(
    source_name: str,
    output_name: str,
    source_width: float,
    source_height: float,
    content_width: float,
    content_height: float,
    badge_width: int,
    badge_height: int,
    offset_x: float,
    offset_y: float,
) -> None:
    inner = svg_inner(source_dir / source_name)
    scale_x = content_width / source_width
    scale_y = content_height / source_height
    output = (
        f'<svg xmlns="http://www.w3.org/2000/svg" width="{badge_width}" '
        f'height="{badge_height}" viewBox="0 0 {badge_width} {badge_height}">'
        f'<g transform="translate({offset_x} {offset_y}) scale({scale_x} {scale_y})">'
        f'{inner}</g></svg>\n'
    )
    (generated_dir / output_name).write_text(output, encoding="utf-8")


write_asset(
    "logo-horizontal-primary.svg",
    "mapvina-logo-horizontal-primary.svg",
    460,
    110,
    96,
    23,
    108,
    31,
    6,
    4,
)
write_asset(
    "icon-primary.svg",
    "mapvina-icon-primary.svg",
    220,
    220,
    24,
    24,
    32,
    32,
    4,
    4,
)
PY

write_imageset() {
  local directory="$1"
  local filename="$2"
  mkdir -p "$directory"
  cat > "$directory/Contents.json" <<JSON
{
  "images" : [
    {
      "filename" : "$filename",
      "idiom" : "universal"
    }
  ],
  "info" : {
    "author" : "xcode",
    "version" : 1
  },
  "properties" : {
    "preserves-vector-representation" : true
  }
}
JSON
}

write_imageset "$ios_assets/mapvina-logo-horizontal-primary.imageset" "mapvina-logo-horizontal-primary.svg"
write_imageset "$ios_assets/mapvina-icon-primary.imageset" "mapvina-icon-primary.svg"
write_imageset "$ios_assets/mapvina-logo-icon.imageset" "mapvina-logo-icon.svg"
write_imageset "$ios_assets/mapvina-logo-stroke-gray.imageset" "mapvina-logo-stroke-gray.svg"

cp "$generated_dir/mapvina-logo-horizontal-primary.svg" \
  "$ios_assets/mapvina-logo-horizontal-primary.imageset/mapvina-logo-horizontal-primary.svg"
cp "$generated_dir/mapvina-icon-primary.svg" \
  "$ios_assets/mapvina-icon-primary.imageset/mapvina-icon-primary.svg"
cp "$generated_dir/mapvina-icon-primary.svg" \
  "$ios_assets/mapvina-logo-icon.imageset/mapvina-logo-icon.svg"
cp "$generated_dir/mapvina-logo-horizontal-primary.svg" \
  "$ios_assets/mapvina-logo-stroke-gray.imageset/mapvina-logo-stroke-gray.svg"

rm -f "$ios_assets/mapvina-logo-stroke-gray.imageset"/mapvina_logo@*.png
rm -rf "$ios_assets/mapbox.imageset" "$ios_assets/mapbox_helmet.imageset"

rm -f "$android_res/drawable/mapvina_logo_icon.xml"

while read -r density width height; do
  output_dir="$android_res/drawable-$density"
  mkdir -p "$output_dir"
  rsvg-convert --width "$width" --height "$height" \
    "$generated_dir/mapvina-logo-horizontal-primary.svg" \
    > "$output_dir/mapvina_logo_icon.png"
done <<'SIZES'
mdpi 108 31
hdpi 162 47
xhdpi 216 62
xxhdpi 324 93
xxxhdpi 432 124
SIZES

while read -r density size; do
  output_dir="$android_res/drawable-$density"
  mkdir -p "$output_dir"
  rsvg-convert --width "$size" --height "$size" \
    "$generated_dir/mapvina-icon-primary.svg" \
    > "$output_dir/mapvina_logo_helmet.png"
done <<'SIZES'
mdpi 32
hdpi 48
xhdpi 64
xxhdpi 96
xxxhdpi 128
SIZES

find "$android_res" -type f -name '.!*mapvina_logo_helmet.png' -delete

echo "Generated MapVina Android and iOS map logo assets."
