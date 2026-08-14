#!/usr/bin/env bash
set -euo pipefail

VERSION_LINE="$(java -version 2>&1 | head -n 1)"
echo "${VERSION_LINE}"

if [[ "${VERSION_LINE}" != *'"1.8.'* ]]; then
  echo "WARNING: This template targets JDK 8. Current java does not report version 1.8.x." >&2
  exit 2
fi
