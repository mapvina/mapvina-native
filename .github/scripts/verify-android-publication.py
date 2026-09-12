import argparse
import hashlib
import io
from pathlib import Path
import struct
import subprocess
import tempfile
import time
import urllib.error
import urllib.request
import xml.etree.ElementTree as ET
import zipfile


ARTIFACTS = tuple(
    "android-sdk" + suffix
    for suffix in ("", "-debug", "-opengl", "-opengl-debug", "-vulkan", "-vulkan-debug")
)
ABIS = ("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
MAVEN = "https://repo.maven.apache.org/maven2/io/github/mapvina"


def download(url):
    with urllib.request.urlopen(url, timeout=60) as response:
        return response.read()


def verify_checksum(content, checksum):
    expected = checksum.decode("ascii").strip().split()[0].lower()
    if hashlib.sha1(content).hexdigest() != expected:
        raise ValueError("Maven SHA-1 checksum mismatch")


def verify_pom(content, artifact, version):
    root = ET.fromstring(content)
    namespace = {"m": "http://maven.apache.org/POM/4.0.0"}
    for name, expected in (("groupId", "io.github.mapvina"), ("artifactId", artifact), ("version", version)):
        if root.findtext("m:" + name, namespaces=namespace) != expected:
            raise ValueError(f"Incorrect POM {name} for {artifact}")
    dependencies = {
        dependency.findtext("m:artifactId", namespaces=namespace): dependency.findtext("m:version", namespaces=namespace)
        for dependency in root.findall("m:dependencies/m:dependency", namespace)
    }
    for name in ("android-sdk-geojson", "android-sdk-turf", "mapvina-android-gestures"):
        if dependencies.get(name) != "1.0.1":
            raise ValueError(f"Incorrect {name} dependency in {artifact}")


def verify_aar(content):
    with zipfile.ZipFile(io.BytesIO(content)) as archive:
        entries = archive.namelist()
        for abi in ABIS:
            if f"jni/{abi}/libmapvina.so" not in entries:
                raise ValueError(f"Missing native library for {abi}")
        for name, expected in (("mapvina_logo_icon.png", (108, 31)), ("mapvina_logo_helmet.png", (32, 32))):
            matches = [entry for entry in entries if "/drawable-mdpi" in entry and entry.endswith("/" + name)]
            if len(matches) != 1:
                raise ValueError(f"Missing or ambiguous mdpi logo: {name}")
            image = archive.read(matches[0])
            if image[:8] != b"\x89PNG\r\n\x1a\n" or struct.unpack(">II", image[16:24]) != expected:
                raise ValueError(f"Incorrect logo dimensions: {name}")
            if image[25] not in (4, 6) and b"tRNS" not in image:
                raise ValueError(f"Logo lacks transparency: {name}")
        if any(".!" in entry and "mapvina_logo" in entry for entry in entries):
            raise ValueError("Legacy conflict-copy logo in AAR")


def verify_artifact(artifact, version, directory, gpg_home):
    stem = artifact + "-" + version
    base = f"{MAVEN}/{artifact}/{version}/"
    for suffix in (".pom", ".aar", "-sources.jar", "-javadoc.jar"):
        filename = stem + suffix
        content = download(base + filename)
        verify_checksum(content, download(base + filename + ".sha1"))
        signature = download(base + filename + ".asc")
        binary_path = directory / filename
        signature_path = directory / (filename + ".asc")
        binary_path.write_bytes(content)
        signature_path.write_bytes(signature)
        subprocess.run(
            ["gpg", "--batch", "--homedir", gpg_home, "--verify", str(signature_path), str(binary_path)],
            check=True, capture_output=True,
        )
        if suffix == ".pom":
            verify_pom(content, artifact, version)
        elif suffix == ".aar":
            verify_aar(content)
        else:
            with zipfile.ZipFile(io.BytesIO(content)) as archive:
                if not any(entry != "META-INF/MANIFEST.MF" and not entry.endswith("/") for entry in archive.namelist()):
                    raise ValueError(f"Empty documentation archive: {filename}")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("version")
    parser.add_argument("--gpg-home", required=True)
    parser.add_argument("--timeout", type=int, default=5400)
    args = parser.parse_args()
    deadline = time.monotonic() + args.timeout
    with tempfile.TemporaryDirectory(prefix="mapvina-maven-") as directory:
        for artifact in ARTIFACTS:
            while True:
                try:
                    verify_artifact(artifact, args.version, Path(directory), args.gpg_home)
                    print(f"VERIFIED {artifact}:{args.version}", flush=True)
                    break
                except (urllib.error.URLError, TimeoutError) as error:
                    if isinstance(error, urllib.error.HTTPError) and error.code not in (404, 429, 500, 502, 503, 504):
                        raise
                    if time.monotonic() >= deadline:
                        raise TimeoutError(f"Maven publication did not become available: {artifact}") from error
                    print(f"Waiting for Maven propagation: {artifact}", flush=True)
                    time.sleep(min(60, max(0, deadline - time.monotonic())))


if __name__ == "__main__":
    main()
