# mapvina-native: verification 04/10/2026

**Status: ANDROID ARTIFACT PASS; iOS RELEASE/BRANDING BLOCKED.**

Android public six variants `1.0.2`; iOS public `1.0.0`; source VERSION is not release proof.

Source baseline: `5ed6153406b1ad33c0cdcaab8b3c8ee04d335738` (`origin/main` plus the sample/doc changes in this PR).

Audit only: no SDK API/version changes, tags, publishing or main merge.

## Phạm vi chứng minh native

Sáu AAR public `1.0.2` có POM/AAR, checksum SHA-1 khớp và file chữ ký `.asc`.
Đây là kiểm tra **presence** chữ ký, không phải xác thực chữ ký GPG bằng trusted key.
Logo xxxhdpi trong cả sáu AAR có SHA-256
`5a9edbd0d555f788475950eac2bd6b2fcfab396f242b734984c7fd96537dd9ac`,
khớp resource native hiện tại. SVG master của Brand Kit khớp source branding.
Android demo public streets/night/satellite render tại TP.HCM sau khi đặt GPS
emulator đúng vùng; example streets cũng render. Logo ngang trong suốt đúng asset
public; tương phản trên dark/satellite còn hạn chế, không coi đây là nghiệm thu
accessibility hoặc snapshot/bốn vị trí.

iOS public ZIP `ios-v1.0.0` vẫn có logo cũ. Source `platform/ios/VERSION` là
`1.0.1` và source assets mới tồn tại, nhưng chưa có release/tag SPM `1.0.1`.
Không rebuild toàn bộ native trong đợt documentation audit; source build mới,
OpenGL/Vulkan runtime, static XCFramework, snapshot và device tests: NOT TESTED.

Workflow iOS stage `prerelease: true` trước khi CocoaPods/SPM hoàn tất. Nếu yêu cầu
release chỉ công khai sau cả hai kênh thành công, phải thiết kế đường tải có auth
cho draft hoặc gate tương đương; chỉ đổi draft flag sẽ làm URL tải public của SPM
không truy cập được. Không dispatch release trong PR docs này.

## Evidence and acceptance boundary

[Cross-repository artifact and runtime audit](https://github.com/mapvina/mapvina-document-android/blob/codex/docs-verification-20261004/docs/verification/2026-10-04/README.md).

PASS applies only to the named checks. BLOCKED is a reproduced failure; NOT TESTED is not a pass.

## Separate follow-up

[Reproduced blocker issue](https://github.com/mapvina/mapvina-native/issues/7). SDK/infra fixes are outside this PR.

[Verified release/tag commit identities](evidence/release-provenance.json). Tag association is not a full reproducible-build attestation.
