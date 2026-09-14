# MapVina native map branding

The files in `source/` are the canonical MapVina Brand Kit v1.0 SVG assets used by
the Android and iOS map ornaments. Generated assets preserve the brand artwork
and padding while keeping the surrounding canvas fully transparent.

Regenerate checked-in Android and iOS assets from the repository root:

```sh
scripts/branding/generate-native-mapvina-logos.sh
```

The script requires `python3` and `rsvg-convert`.
