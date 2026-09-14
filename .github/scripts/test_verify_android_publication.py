import hashlib
import importlib.util
import io
from pathlib import Path
import struct
import unittest
import zipfile


spec = importlib.util.spec_from_file_location("publication", Path(__file__).with_name("verify-android-publication.py"))
publication = importlib.util.module_from_spec(spec)
spec.loader.exec_module(publication)


class PublicationTests(unittest.TestCase):
    def test_checksum(self):
        content = b"test artifact"
        publication.verify_checksum(content, hashlib.sha1(content).hexdigest().encode())
        with self.assertRaises(ValueError):
            publication.verify_checksum(content, b"0" * 40)

    def make_aar(self, missing_abi=False, opaque_logo=False, square_logo=False):
        output = io.BytesIO()
        with zipfile.ZipFile(output, "w") as archive:
            for abi in publication.ABIS[:-1] if missing_abi else publication.ABIS:
                archive.writestr(f"jni/{abi}/libmapvina.so", b"test library")
            for name, width, height in (("icon", 108, 31), ("helmet", 32, 32)):
                if square_logo and name == "icon":
                    width, height = 48, 48
                header = b"\x89PNG\r\n\x1a\n" + struct.pack(">I", 13) + b"IHDR"
                header += struct.pack(">II", width, height) + bytes((8, 2 if opaque_logo else 6))
                archive.writestr(f"res/drawable-mdpi-v4/mapvina_logo_{name}.png", header)
        return output.getvalue()

    def test_aar_has_all_abis_and_transparent_logos(self):
        publication.verify_aar(self.make_aar())

    def test_missing_abi_rejected(self):
        with self.assertRaisesRegex(ValueError, "Missing native library"):
            publication.verify_aar(self.make_aar(missing_abi=True))

    def test_opaque_logo_rejected(self):
        with self.assertRaisesRegex(ValueError, "transparency"):
            publication.verify_aar(self.make_aar(opaque_logo=True))

    def test_legacy_square_logo_rejected(self):
        with self.assertRaisesRegex(ValueError, "dimensions"):
            publication.verify_aar(self.make_aar(square_logo=True))

    def test_pom(self):
        dependencies = "".join(
            f"<dependency><artifactId>{name}</artifactId><version>1.0.1</version></dependency>"
            for name in ("android-sdk-geojson", "android-sdk-turf", "mapvina-android-gestures")
        )
        pom = f'<project xmlns="http://maven.apache.org/POM/4.0.0"><groupId>io.github.mapvina</groupId><artifactId>android-sdk</artifactId><version>1.0.2</version><dependencies>{dependencies}</dependencies></project>'
        publication.verify_pom(pom, "android-sdk", "1.0.2")
        with self.assertRaises(ValueError):
            publication.verify_pom(pom.replace("1.0.1", "1.0.0"), "android-sdk", "1.0.2")


if __name__ == "__main__":
    unittest.main()
