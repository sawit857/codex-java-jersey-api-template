#!/usr/bin/env bash
set -euo pipefail

JAVA_VERSION_OUTPUT="$(java -version 2>&1 | head -n 1 || true)"
echo "Java: ${JAVA_VERSION_OUTPUT}"
echo "Maven: $(mvn -version | head -n 1)"

mvn clean verify

REPORT_DIR="target/surefire-reports"
if [[ ! -d "${REPORT_DIR}" ]]; then
  echo "ERROR: Surefire report directory not found; tests may not have executed." >&2
  exit 1
fi

TOTAL=$(grep -h "tests=\"" "${REPORT_DIR}"/TEST-*.xml 2>/dev/null \
  | sed -E 's/.*tests="([0-9]+)".*/\1/' \
  | awk '{sum += $1} END {print sum + 0}')

if [[ "${TOTAL}" -le 0 ]]; then
  echo "ERROR: Maven completed but no tests were recorded." >&2
  exit 1
fi

echo "Verification passed with ${TOTAL} executed tests."
