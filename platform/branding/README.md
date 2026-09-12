# MapVina native map branding

The files in `source/` are the canonical MapVina Brand Kit v1.0 SVG assets used by
the Android and iOS map ornaments. Generated badge assets use a 90% opaque white
background with a 4 dp/pt corner radius.

Regenerate checked-in Android and iOS assets from the repository root:

```sh
scripts/branding/generate-native-mapvina-logos.sh
```

The script requires `python3` and `rsvg-convert`.
