#!/usr/bin/env python3
from pathlib import Path
import json, os, time, urllib.request
repo=os.environ.get("GITHUB_REPOSITORY", "thrkingunknown/gboard-theme-patch")
branch=os.environ.get("METADATA_BRANCH", "main")
expected_version=os.environ.get("EXPECTED_VERSION")
url=f"https://raw.githubusercontent.com/{repo}/{branch}/patches-bundle.json"
request=urllib.request.Request(url, headers={"User-Agent":"gboard-theme-patch-ci"})
last=None
for attempt in range(1,11):
    try:
        with urllib.request.urlopen(request, timeout=20) as response:
            if response.status != 200: raise RuntimeError(f"HTTP {response.status}")
            payload=json.loads(response.read().decode("utf-8"))
        break
    except Exception as exc:
        last=exc
        if attempt==10: raise SystemExit(f"Published Morphe metadata unavailable at {url}: {exc}")
        time.sleep(5)
assert isinstance(payload,dict)
for key in ("created_at","description","download_url","signature_download_url","version"): assert key in payload, f"patches-bundle.json missing {key}"
if expected_version: assert payload["version"]==expected_version, f"Remote metadata version {payload['version']} != release version {expected_version}"
assert str(payload["download_url"]).endswith(f"/v{payload['version']}/patches-{payload['version']}.mpp")
print(f"Published Morphe metadata verified: {url} (v{payload['version']})")
